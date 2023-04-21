package org.objectwiz.spi.agent.jboss;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.security.Principal;
import org.objectwiz.metadata.BusinessMethod;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;
import javax.naming.InitialContext;
import javax.naming.Reference;
import javax.naming.spi.NamingManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectwiz.Agent;
import org.objectwiz.PersistenceUnit;
import javax.management.ObjectName;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.persistence.EntityManagerFactory;
import org.apache.commons.lang.StringUtils;
import org.jboss.Version;
import org.jboss.security.RealmMapping;
import org.objectwiz.MethodExecutionException;
import org.objectwiz.PersistenceException;
import org.objectwiz.metadata.BusinessBean;
import org.objectwiz.spi.JPAClassAnalyzer;
import org.objectwiz.spi.SpiFactory;

/**
 * JBoss agent.
 * 
 * Implementation of the {@link Agent} interface for JBoss application server.
 * Tested with versions 4.3, 5.0.
 *
 * All the discovery is performed using JNDI:
 * <ul>
 *   <li><b>persistence units</b>: all the bindings from the global JNDI context starting with
 * the prefix <code>persistence.unit(s):</code> are passed to {@link SpiFactory} for creating the
 * associated {@link RealPersistenceUnit}.</li>
 *   <li><b>business methods</b>: business methods are also discovered using JNDI. All the bindings
 * in the subcontext of the application associated to the persistence unit are checked. If the associated
 * object is a {@link EJBObject} or {@link EJBLocalObject} then the associated EJB class is sent for analysis to
 * {@link JPAClassAnalyzer} and all the detected methods are associated to the persistence unit.</li>
 * </ul>
 *
 * Useful extracts from JBoss documentation:
 * <ul>
 *   <li><quote>Persistence units are not available within global JNDI unless you explicitly configure them to do so.</quote>
 *   <a href="http://www.jboss.org/file-access/default/members/jbossejb3/freezone/docs/reference/1.0.7/html/entityconfig.html">(link)</a></li>
 * </ul>
 * 
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class JBossAgent extends Agent {

    private boolean jboss5;

    private String puPrefix;

    private static final Log logger = LogFactory.getLog(JBossAgent.class);

    public JBossAgent() {
        super("localhost:jboss");
        this.jboss5 = Version.getInstance().getMajor() >= 5;
        this.puPrefix = jboss5 ? "persistence.unit:" : "persistence.units:";
    }

    @Override
    public String getDescription() {
        return "JBoss " + (jboss5 ? "5.x" : "");
    }

    /**
     * To initialize the list of persistence units, this agent checks the
     * JNDI context for bindings starting with:
     * <ul>
     *   <li>For JBoss > 5.0: <code>persistence.unit:</code></li>
     *   <li>For JBoss < 5.0: <code>persistence.units:</code></li>
     * </ul>
     *
     * In the JNDI view of JBoss JMX Console, a matching binding looks like:
     * <code>persistence.unit:unitName=#objectwiz (class: org.hibernate.impl.SessionFactoryImpl)</code>
     *
     * The name of the persistence unit is retrieved from the property <code>unitName</code>
     * of the {@link ObjectName} associated to the binding.
     *
     * @return the list of discovered persistence units.
     * @throws Exception if there was a problem while browsing the JNDI bindings. This method
     * does not throw an exception if a {@link PersistenceUnit} that was detected could not be
     * initialized (but a warning is logged).
     */
    @Override
    protected List<PersistenceUnit> initPersistenceUnits() throws Exception {
        List<PersistenceUnit> units = new ArrayList();
        InitialContext ctx = new InitialContext();
        NamingEnumeration pairs = ctx.listBindings("");
        while (pairs.hasMoreElements()) {
            Binding pair = (Binding) pairs.nextElement();
            if (!pair.getName().startsWith(this.puPrefix)) continue;
            logger.info("------ Persistence unit detected: " + pair.getName());
            ObjectName name = new ObjectName(pair.getName());
            String fullUnitName = name.getKeyProperty("unitName");
            if (fullUnitName == null) throw new Exception("Could not resolve unit name: " + pair.getName());
            int p = fullUnitName.lastIndexOf("#");
            String unitName = (p >= 0) ? fullUnitName.substring(p + 1) : fullUnitName;
            String applicationName = name.getKeyProperty("ear");
            if (applicationName == null) applicationName = unitName;
            logger.debug("Unit name: " + unitName);
            try {
                boolean skip = false;
                for (PersistenceUnit unit : units) {
                    if (unit.getName().equals(unitName)) {
                        skip = true;
                    }
                }
                if (skip) continue;
                PersistenceUnit unit = resolveUnit(unitName, applicationName, pair, true);
                units.add(unit);
                logger.info("Unit successfully registered: " + unitName + " (" + unit.getPersistenceProviderDescription() + ")");
            } catch (Exception e) {
                logger.warn("Could not register persistence unit: " + unitName, e);
            }
        }
        return units;
    }

    /**
     * This recursive helper was designed to deal with sub-contexts.
     * It should be called on a {@link Binding} that supposedly contains a persistence unit.
     * It will look recursively for a binding associated to a {@link Reference} object and
     * iterate through the sub-{@link Context}s until one is found.
     *
     * When the reference is found, it will be resolved using {@link NamingManager} and converted
     * to a {@link PersistenceUnit} by using {@link SpiFactory.createPersistenceUnit(Agent,String,Object)}.
     * If the detection of the underlying persistence framework is successfull, this method will then
     * discover the {@link BusinessMethod} objects associated to the unit.
     *
     * @param unitName             The name of the unit
     * @param applicationName      The name of the application that contains the unit
     * @param binding              The {@link Binding} that is supposed to contain a reference that
     *                             we can use to build an {@link EntityManagerFactory}.
     * @param mandatory            Flag indicating if we shall fail if no valid reference is found within
     *                             the sub-contexts.
     * @return                     The {@link PersistenceUnit}.
     * @throws Exception           Possible reasons of failure:
     *                             - the reference was not found
     *                             - the persistence framework is not supported
     */
    private PersistenceUnit resolveUnit(String unitName, String applicationName, Binding binding, boolean mandatory) throws Exception {
        logger.debug("Introspecting binding: " + binding.getName());
        Object object = binding.getObject();
        if (object instanceof Reference) {
            Object impl = NamingManager.getObjectInstance((Reference) object, null, null, null);
            if (impl == null) throw new Exception("Unable to resolve reference: " + binding.getName());
            PersistenceUnit unit = SpiFactory.instance().createPersistenceUnit(this, unitName, impl);
            if (unit == null) throw new Exception("No valid proxy found for: " + impl.getClass());
            try {
                Collection<BusinessBean> beans = findBusinessBeans(unit, applicationName);
                logger.info("Business beans found: " + beans.size());
                for (BusinessBean bean : beans) {
                    unit.registerBusinessBean(bean);
                }
            } catch (Exception e) {
                logger.warn("There was a problem while detecting business beans for application: " + applicationName, e);
            }
            return unit;
        } else if (object instanceof Context) {
            logger.debug("Entering inner context: " + binding.getName());
            Context ctx = (Context) object;
            NamingEnumeration pairs = ctx.listBindings("");
            PersistenceUnit unit = null;
            while (pairs.hasMoreElements()) {
                Binding pair = (Binding) pairs.nextElement();
                unit = resolveUnit(unitName, applicationName, pair, false);
            }
            if (mandatory && unit == null) {
                throw new Exception("Could not find any matching object in sub-contexts");
            }
            return unit;
        }
        throw new Exception("Unsupported binding type: " + object.getClass());
    }

    /**
     * Helper for discovering the {@link BusinessBean}s associated to a given application
     * using JNDI.
     *
     * This method first detects the root JNDI binding associated to the application (normally the same
     * name as the application - or in case of an EAR application the name of the EAR with the suffix).
     *
     * Each sub-binding is analyzed recursively by {@link analyseBindingForBusinessBeans(String,String,Binding)}
     * which returns a list of business beans (local/remote EJBs). Method are then detected by reflection from
     * the interface of the beans using {@link JPAClassAnalyzer#discoverBusinessMethods(String,Class)}.    
     *
     * @param applicationName   The name of the application
     * @return                  A collection of {@link BusinessMethod}s, or an empty list if none was found.
     * @throws NamingException
     */
    private Collection<BusinessBean> findBusinessBeans(PersistenceUnit unit, String applicationName) throws NamingException {
        Collection<BusinessBean> beans = new ArrayList();
        logger.info("Discovering business beans for application: " + applicationName);
        if (applicationName == null) return beans;
        if (applicationName.endsWith(".ear")) {
            applicationName = applicationName.substring(0, applicationName.length() - 4);
            logger.debug("Application name ends with '.ear', striping to: " + applicationName);
        }
        InitialContext ctx = new InitialContext();
        Object applicationObject = ctx.lookup(applicationName);
        if (applicationObject == null) {
            logger.info("Application name not bound in JNDI, skipping discovery of business beans: " + applicationName);
            return beans;
        }
        beans = analyseObjectForBusinessBeans(unit, JPAClassAnalyzer.instance(), null, applicationName, applicationObject);
        return beans;
    }

    /**
     * Recursive helper for finding business beans within the JNDI context of an application.
     *
     * @param unit              The target unit that we want to manipulate using the business beans
     *                          that are discovered.
     * @param parentObjectName  The name of the parent object in JNDI (for recursive purposes).
     * @param objectName        The short name of the given object in JNDI (must not be null).
     * @param obj               The object to analyze (extracted from JNDI).
     * @return                  A collection - possibly empty but not NULL - of business beans.
     */
    protected Collection<BusinessBean> analyseObjectForBusinessBeans(PersistenceUnit unit, JPAClassAnalyzer analyzer, String parentObjectName, String objectName, Object obj) {
        Collection<BusinessBean> beans = new ArrayList();
        String fullBindingName = (parentObjectName == null ? "" : parentObjectName + "/") + objectName;
        if (parentObjectName == null) logger.info(StringUtils.repeat("-", 25));
        logger.info("Searching binding for business beans: " + fullBindingName);
        logger.debug("Binding '" + objectName + "': " + explainClass(obj.getClass()));
        if (obj instanceof Context) {
            try {
                NamingEnumeration<Binding> subBindings = ((Context) obj).listBindings("");
                while (subBindings.hasMoreElements()) {
                    Binding subBinding = subBindings.nextElement();
                    beans.addAll(analyseObjectForBusinessBeans(unit, analyzer, fullBindingName, subBinding.getName(), subBinding.getObject()));
                }
            } catch (NamingException e) {
                logger.warn("Error while retrieving sub-bindings: " + e.getExplanation());
            }
        } else if ((obj instanceof EJBObject) || (obj instanceof EJBLocalObject)) {
            Class intf = obj.getClass().getInterfaces()[0];
            beans.add(analyzer.createBusinessBean(unit, fullBindingName, fullBindingName, intf));
            logger.info("Found " + (obj instanceof EJBObject ? "remote" : "local") + " EJB: " + intf.getName());
        } else if (obj instanceof Reference) {
            Reference ref = (Reference) obj;
            RefAddr local = ref.get("Local Business Interface");
            RefAddr remote = ref.get("Remote Business Interface");
            RefAddr target = local != null ? local : (remote != null ? remote : null);
            if (target == null) {
                logger.debug("Reference does is not a local/remote bean, skipping: " + ref.getClassName());
            } else {
                logger.debug("Reference: " + ref.getClassName() + ". Local: " + (local != null) + ". Remote: " + (remote != null));
            }
        } else {
            logger.debug("Could not do anything with object [class=" + obj.getClass().getName() + "]. Name: " + objectName);
        }
        return beans;
    }

    @Override
    public File getDeployDir() throws FileNotFoundException {
        return resolveDir(System.getProperty("jboss.server.home.dir") + File.separator + "deploy");
    }

    @Override
    public File getDataDir() throws FileNotFoundException {
        return resolveDir(System.getProperty("jboss.server.data.dir"));
    }

    private File resolveDir(String dirFilename) throws FileNotFoundException {
        File dir = new File(dirFilename);
        try {
            if (!(dir.exists() && dir.isDirectory())) {
                throw new FileNotFoundException(dir.getCanonicalPath());
            }
        } catch (IOException e) {
            throw new FileNotFoundException(e.getMessage());
        }
        return dir;
    }

    @Override
    public Object getBean(BusinessBean bean) throws Exception {
        InitialContext ctx = new InitialContext();
        try {
            return ctx.lookup(bean.getBeanFullReference());
        } catch (NamingException e) {
            throw new Exception("Unable to resolve bean: " + bean.getBeanFullReference(), e);
        }
    }

    /**
     * To resolve the roles for a given user, this method looks up the
     * {@link RealMapping} object at: <code>java:comp/env/security/realmMapping</code>.
     * @param principal   The principal of the user
     * @return            The list of roles associated to this user.
     */
    @Override
    public Collection<String> getUserRoles(Principal principal) {
        try {
            InitialContext ctx = new InitialContext();
            RealmMapping rm = (RealmMapping) ctx.lookup("java:comp/env/security/realmMapping");
            Set roles = rm.getUserRoles(null);
            Collection<String> roleNames = new HashSet();
            for (Object role : roles) {
                roleNames.add(((Principal) role).getName());
            }
            return roleNames;
        } catch (Exception e) {
            logger.warn("Error while retrieving roles", e);
            return Collections.EMPTY_SET;
        }
    }

    private String explainClass(Class clazz) {
        StringBuilder buf = new StringBuilder();
        buf.append("  Class: " + clazz.getName() + "\n");
        buf.append("  Interfaces: ");
        for (int i = 0; i < clazz.getInterfaces().length; i++) {
            buf.append((i > 0 ? ", " : "") + clazz.getInterfaces()[i].getName());
        }
        buf.append("\n");
        buf.append("  Superclasses: ");
        Class superclass = clazz.getSuperclass();
        int i = 0;
        while (superclass != null) {
            if (i > 0) buf.append(", ");
            buf.append(superclass.getName());
            i++;
            superclass = superclass.getSuperclass();
        }
        buf.append("\n");
        return buf.toString();
    }
}
