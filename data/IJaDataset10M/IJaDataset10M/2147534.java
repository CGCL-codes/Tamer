package org.kwantu.app.context;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.kwantu.app.CurrentSessionProvider;
import org.kwantu.app.KwantuJarClassLoader;
import org.kwantu.app.ListItem;
import org.kwantu.app.WicketApplicationController;
import org.kwantu.app.model.KwantuApplication;
import org.kwantu.m2.KwantuFaultException;
import org.kwantu.m2.model.*;
import org.kwantu.m2.model.ui.KwantuPanel;
import org.kwantu.m2.xpath.XPathUtil;
import org.kwantu.persistence.PersistentObject;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author chris
 */
public class KwantuApplicationContext extends ApplicationContext {

    private static final Log LOG = LogFactory.getLog(KwantuApplicationContext.class);

    private KwantuApplication kwantuApplication;

    private KwantuJarClassLoader classLoader;

    private KwantuModel kwantuModel;

    private KwantuModel effectiveModel;

    private ThreadLocal<KwantuApplicationContext.GlobalInstancesSet> globalInstances = new ThreadLocal<KwantuApplicationContext.GlobalInstancesSet>();

    private WicketApplicationController controller;

    private ArrayList<KwantuUpdate> cachedUpdateList = new ArrayList<KwantuUpdate>();

    private Properties dbProperties;

    public KwantuApplicationContext(WicketApplicationController controller, Properties dbProperties, KwantuApplication kwantuApplication, CurrentSessionProvider currentSessionProvider) {
        super(currentSessionProvider, kwantuApplication.getName());
        this.controller = controller;
        this.kwantuApplication = kwantuApplication;
        this.dbProperties = dbProperties;
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        this.classLoader = new KwantuJarClassLoader(controller, kwantuApplication.getArtifactId(), kwantuApplication.getVersion(), currentClassLoader);
        initializeModel();
        this.classLoader.setJavaArtifactId(kwantuModel.deriveJavaPackageName());
        Thread.currentThread().setContextClassLoader(this.classLoader);
        initializeHibernateSessionFactory();
        Thread.currentThread().setContextClassLoader(currentClassLoader);
    }

    @Override
    protected void configureHibernate(AnnotationConfiguration conf) {
        conf.setProperties(dbProperties);
        conf.setProperty("hibernate.connection.url", getKwantuApplication().getDatabaseURL());
        conf.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "managed");
        for (Class c : getKwantuClassesAsJavaClasses()) {
            LOG.info("   Adding " + c.getName() + " to the list of classes to be mapped.");
            conf.addAnnotatedClass(c);
        }
    }

    private void initializeModel() {
        LOG.info("Fetching kwantuModel.xml.");
        InputStream inputStream = classLoader.getResourceAsStream("kwantuModel.xml");
        if (inputStream == null) {
            LOG.error("Unable to get resource kwantuModel.xml in jar.");
            throw new KwantuFaultException("File not found when loading kwantuModel.xml from jar. " + "Probably the jar file with the application model wasn't found. " + "KwantuApplication=" + kwantuApplication);
        }
        kwantuModel = new KwantuModel();
        try {
            kwantuModel.deserializeFromXML(new InputStreamReader(inputStream));
        } catch (IOException e) {
            LOG.error("Unable to deserialize Model metadata", e);
            throw new KwantuFaultException("File error loading kwantuModel.xml from jar", e);
        }
        effectiveModel = kwantuModel.composeEffectiveModel();
        LOG.info("Deserialized model " + effectiveModel.getName());
        loadComponentMap();
    }

    public KwantuModel getEffectiveModel() {
        return effectiveModel;
    }

    public Set<Class> getKwantuClassesAsJavaClasses() {
        HashSet<Class> classes = new HashSet<Class>();
        for (KwantuClass c : getKwantuBOM().getKwantuClasses()) {
            classes.add(classLoader.getJavaClass(c));
        }
        return classes;
    }

    @Override
    public Set<KwantuPanel> getUiPanels() {
        return effectiveModel.getUiPanels();
    }

    public KwantuBusinessObjectModel getKwantuBOM() {
        return effectiveModel.getKwantuBusinessObjectModel();
    }

    /**
     * Find all classes which are not owned by any other class.
     */
    public HashSet<KwantuClass> getTopLevelKwantuClasses() {
        return getKwantuBOM().getTopLevelKwantuClasses();
    }

    public PersistentObject newKwantuInstance(KwantuClass kwantuClass) {
        Class appClass = classLoader.getJavaClass(kwantuClass);
        PersistentObject o = null;
        try {
            o = (PersistentObject) appClass.newInstance();
        } catch (InstantiationException ie) {
            LOG.error("Couldn't instantiate an object of type " + appClass.getName());
        } catch (IllegalAccessException ia) {
            LOG.error("Couldn't access class " + appClass.getName());
        }
        return o;
    }

    public KwantuApplication getKwantuApplication() {
        return kwantuApplication;
    }

    private class GlobalInstancesSet extends HashSet<PersistentObject> {

        public GlobalInstancesSet() {
            super();
            for (KwantuClass c : getTopLevelKwantuClasses()) {
                loadOrCreateGlobalInstances(c);
            }
        }

        private void loadOrCreateGlobalInstances(KwantuClass kwantuClass) {
            List<PersistentObject> l = getDbSession().createQuery("from " + kwantuClass.getName()).list();
            if (l.size() == 0) {
                LOG.info("Creating global instance for " + kwantuClass.getName());
                PersistentObject o = newKwantuInstance(kwantuClass);
                save(o);
                updateCachedPersistentObjects();
                try {
                    invoke(o, "init", new Object[] {}, false, false);
                } catch (Throwable t) {
                    LOG.info("Exception thrown by init() of " + kwantuClass.getName() + " singleton. We ignore it.", t);
                }
                add(o);
            } else {
                for (PersistentObject po : l) {
                    LOG.debug("Adding global instance " + po.toString() + " of " + kwantuClass.getName());
                    add(po);
                }
            }
        }
    }

    public Set<PersistentObject> getGlobalInstances() {
        if (globalInstances.get() == null) {
            globalInstances.set(new KwantuApplicationContext.GlobalInstancesSet());
        }
        return globalInstances.get();
    }

    @Override
    public void detach() {
        super.detach();
        globalInstances.set(null);
    }

    public ArrayList<ListItem> getGlobalInstanceList() {
        ArrayList<ListItem> as = new ArrayList<ListItem>();
        for (PersistentObject po : getGlobalInstances()) {
            as.add(new ListItem(po, po.getClass().getSimpleName()));
        }
        return as;
    }

    /** Cache the saves until the method is complete then validate the
     *  objects before saving.
     * @param objects
     */
    @Override
    public void save(PersistentObject... objects) {
        for (PersistentObject o : objects) {
            cachedUpdateList.add(new KwantuUpdate(o).save());
        }
    }

    public void updateCachedPersistentObjects() {
        Session session = getDbSession();
        session.getTransaction().begin();
        for (KwantuUpdate update : cachedUpdateList) {
            if (update.isSave()) {
                LOG.info("Saving " + update.getObject());
                session.saveOrUpdate(update.getObject());
            } else if (update.isDelete()) {
                LOG.info("Deleting " + update.getObject());
                delete(update.getObject());
            }
        }
        session.flush();
        session.getTransaction().commit();
    }

    private boolean validate(KwantuUpdate update) {
        PersistentObject o = update.getObject();
        String className = o.getClass().getSimpleName();
        KwantuClass kc = effectiveModel.getKwantuBusinessObjectModel().getKwantuClass(className);
        for (KwantuValidation kv : kc.getKwantuValidations()) {
            Boolean success = (Boolean) getFromContext(kv.getBooleanXPathExpression(), o);
            if (!success) {
                LOG.info("Validation failed! '" + kv.getBooleanXPathExpression() + "'.");
                controller.getFeedbacker().clear();
                controller.getFeedbacker().error(substituteXPaths(o, kv.getErrorMessage()));
                return false;
            }
        }
        return true;
    }

    public void invoke(Object context, String methodName, Object[] args, boolean cacheResult, boolean validate) {
        cachedUpdateList.clear();
        LOG.info("before invoking " + methodName + " and caching updates");
        super.invoke(context, methodName, args, cacheResult);
        if (controller.getFeedbacker().hasError()) {
            return;
        }
        if (validate) {
            for (KwantuUpdate update : cachedUpdateList) {
                if (update.isDelete()) {
                    continue;
                }
                if (!validate(update)) {
                    return;
                }
            }
        }
        LOG.info("after invoking " + methodName + " now updating from cache.");
        updateCachedPersistentObjects();
    }

    @Override
    public void invoke(Object context, String methodName, Object[] args, boolean cacheResult) {
        invoke(context, methodName, args, cacheResult, true);
    }

    /** Parse string replacing xpath expressions enclosed in {} with the values they
     * refer to in the current context (recursive substitution not supported).
     * @param context
     * @param s a string containing embedded xpath expressions ep "Week number {weekNo} is greater than 53."
     * @return
     */
    private String substituteXPaths(Object context, String s) {
        return XPathUtil.substituteXPaths(s, context, getRootRenderingContext());
    }

    /** load instance data from xml (for testing).
     * Use Xstreams id reference mode so that we
     * can set up references between objects.
     *
     * This doesn't work currently because hibernate generates a
     * error when we attempt to save the object graph(s)
     * org.hibernate.StaleStateException: Batch update returned unexpected row count from update: 0 actual row count: 0 expected: 1
     *
     * @param xmlReader the source of the xml.
     */
    public void instantiateFromXmlUsingXstream(Reader xmlReader) {
        XStream xstream = new XStream();
        xstream.setMode(XStream.ID_REFERENCES);
        xstream.alias("dataset", HashSet.class);
        HashSet<PersistentObject> globals = (HashSet<PersistentObject>) xstream.fromXML(xmlReader);
        getDbSession().getTransaction().begin();
        for (PersistentObject global : globals) {
            boolean validClass = false;
            String className = global.getClass().getName().replaceFirst("^.*\\.", "");
            for (KwantuClass kwantuClass : getTopLevelKwantuClasses()) {
                if (className.equals(kwantuClass.getName())) {
                    validClass = true;
                    break;
                }
            }
            if (!validClass) {
                throw new KwantuFaultException("Cannot create an instance of " + className + " because it is not a top-level class. " + "(ie items of this class must refer to another class which is not specified)");
            }
            if (global.getClass().getName().endsWith("Organisation")) {
                LOG.info("Saving unserialized instance of " + global.getClass().getName());
                getDbSession().update(global);
                invoke(global, "setController", new Object[] { controller }, false);
            }
        }
        getDbSession().flush();
        getDbSession().getTransaction().commit();
    }

    public void serializeAll(String filename) {
        serializeAllToDbUnit(filename);
    }

    public void serializeAllToDbUnit(String filename) {
        LOG.info("Serializing application data in dbunit format to " + filename);
        try {
            Connection jdbcConnection = getDbSession().connection();
            IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
            connection.getConfig().setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
            IDataSet fullDataSet = connection.createDataSet();
            FlatXmlDataSet.write(fullDataSet, new FileOutputStream(filename));
        } catch (IOException e) {
            throw new KwantuFaultException("Unable to serialise application data to file " + filename, e);
        } catch (DatabaseUnitException ex) {
            throw new KwantuFaultException("Exception when initializing database", ex);
        } catch (SQLException ex) {
            throw new KwantuFaultException("Exception when initializing database", ex);
        }
    }

    public void serializeAllToXStream(String filename) {
        LOG.info("Serializing application data in xstream format to " + filename);
        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.setMode(XStream.ID_REFERENCES);
            xstream.toXML(globalInstances, new FileWriter(filename));
        } catch (IOException e) {
            throw new KwantuFaultException("Unable to serialise application data.", e);
        }
    }

    /**
     * Use JXPath to access attributes (and more, traverse references) by reflection.
     * @param xpath The name of the attribute, (or the path to the attribute).
     * @param parentContext  The object possessing the attribute (or the first element of the path).
     * @return
     */
    public Object getFromContext(String xpath, Object parentContext) {
        return controller.getFromContext(xpath, parentContext);
    }

    public void setInContext(String xpath, Object contextObject, Object value) {
        controller.setInContext(xpath, contextObject, value);
    }

    /** we override this, because we want to recursively delete.
     *
     */
    @Override
    public void delete(PersistentObject o) {
        LOG.info("About to delete  " + o + ".");
        getDbSession().getTransaction().begin();
        recursiveDelete(o);
        getDbSession().getTransaction().commit();
    }

    private void recursiveDelete(PersistentObject o) {
        LOG.info("Deleting  " + o + ".");
        String className = o.getClass().getSimpleName();
        KwantuClass kwantuClass = getKwantuBOM().getKwantuClass(className);
        for (KwantuRelationship rel : kwantuClass.getKwantuRelationships()) {
            Object relObject = getFromContext(rel.getName(), o);
            if (relObject == null) {
                continue;
            }
            if (rel.isOwner() || rel.getRelationshipToKwantuClass().isAManyToManyLink()) {
                if (rel.getCardinality() == KwantuRelationship.Cardinality.MANY) {
                    for (Object obj : ((Set) relObject).toArray()) {
                        LOG.info("Recursively deleting owned object " + obj + ".");
                        recursiveDelete((PersistentObject) obj);
                    }
                } else {
                    PersistentObject ownedObject = (PersistentObject) getFromContext(rel.getName(), relObject);
                    if (ownedObject != null) {
                        LOG.info("Recursively deleting owned object " + ownedObject + ".");
                        recursiveDelete(ownedObject);
                    }
                }
            } else {
                KwantuRelationship inverseRel = rel.getInverseKwantuRelationship();
                if (rel.getCardinality() == KwantuRelationship.Cardinality.MANY) {
                    Set collection = (Set) getFromContext(rel.getName(), o);
                    for (Object relatedObj : collection) {
                        setInContext(inverseRel.getName(), relatedObj, null);
                    }
                    collection.clear();
                } else if (inverseRel.getCardinality() == KwantuRelationship.Cardinality.MANY) {
                    Set collection = (Set) getFromContext(inverseRel.getName(), relObject);
                    collection.remove(o);
                    setInContext(rel.getName(), o, null);
                } else {
                    setInContext(inverseRel.getName(), relObject, null);
                    setInContext(rel.getName(), o, null);
                }
            }
        }
        getDbSession().delete(o);
    }

    @Override
    public void initializeRootRenderingContext(HashMap<String, Object> map) {
        for (ListItem item : getGlobalInstanceList()) {
            map.put(item.getItem().getClass().getSimpleName(), item.getItem());
        }
    }

    @Override
    public String getApplicationKey() {
        return getKwantuApplication().getName();
    }

    class KwantuUpdate {

        private PersistentObject object;

        private boolean save = false;

        private boolean delete = false;

        KwantuUpdate(PersistentObject o) {
            object = o;
        }

        public PersistentObject getObject() {
            return object;
        }

        public KwantuUpdate save() {
            save = true;
            delete = false;
            return this;
        }

        public KwantuUpdate delete() {
            delete = true;
            save = false;
            return this;
        }

        public boolean isSave() {
            return save;
        }

        public boolean isDelete() {
            return delete;
        }
    }
}
