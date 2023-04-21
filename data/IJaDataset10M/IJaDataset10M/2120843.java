package gleam.docservice.gate;

import gate.AnnotationSet;
import gate.Corpus;
import gate.DataStore;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageResource;
import gate.Resource;
import gate.TextualDocument;
import gate.corpora.DocumentContentImpl;
import gate.corpora.DocumentXmlUtils;
import gate.creole.ResourceInstantiationException;
import gate.event.CreoleEvent;
import gate.event.CreoleListener;
import gate.event.DatastoreEvent;
import gate.event.DatastoreListener;
import gate.persist.PersistenceException;
import gate.security.SecurityException;
import gate.security.SecurityInfo;
import gate.security.Session;
import gate.util.AbstractFeatureBearer;
import gate.util.InvalidOffsetException;
import gate.util.Out;
import gate.util.Strings;
import gleam.docservice.AnnotationSetHandle;
import gleam.docservice.CorpusInfo;
import gleam.docservice.DocServiceException;
import gleam.docservice.DocumentInfo;
import gleam.docservice.DocService;
import gleam.docservice.util.DocserviceUtil;
import gleam.util.adapters.MapWrapper;
import gleam.util.cxf.CXFClientUtils;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.xml.stream.XMLStreamException;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * This is an adapter for GLEAM document service data store to be used as a GATE
 * DataStore.
 * </p>
 */
public class DocServiceDataStore extends AbstractFeatureBearer implements DataStore, CreoleListener {

    /** Debug flag */
    private static final boolean DEBUG = false;

    private static final boolean PROFILE = true;

    private static final boolean DEBUG_DETAILS = false;

    private static final boolean ALLOW_CHUNKING = false;

    private static final boolean USE_COMPRESSION = true;

    public static final String SERIAL_CORPUS_CLASS_NAME = "gate.corpora.SerialCorpusImpl";

    public static final String DOCSERVICE_CORPUS_CLASS_NAME = "gleam.docservice.gate.DocServiceCorpusImpl";

    public static final String CORPUS_CLASS_NAME = "gate.corpora.CorpusImpl";

    public static final String DOCUMENT_CLASS_NAME = "gate.corpora.DocumentImpl";

    /** The name of the datastore */
    protected String name;

    private String storageUrl;

    private gleam.docservice.DocService sds;

    private transient Vector datastoreListeners;

    private Map allLocks;

    private Map allNotifiers;

    /**
   * Creates a proxy factory bean, configured to use MTOM
   * and with the concrete IAA classes added to its data
   * binding.
   * 
   * @return
   */
    private JaxWsProxyFactoryBean getFactoryBean() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("mtom-enabled", Boolean.TRUE);
        factory.setProperties(props);
        JAXBDataBinding db = new JAXBDataBinding();
        db.setExtraClass(new Class[] { gleam.docservice.iaa.AllWaysFMeasureDetail.class, gleam.docservice.iaa.AllWaysKappaDetail.class, gleam.docservice.iaa.PairwiseFMeasureDetail.class, gleam.docservice.iaa.PairwiseKappaDetail.class });
        factory.setDataBinding(db);
        factory.setServiceClass(DocService.class);
        return factory;
    }

    public DocServiceDataStore() {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".DocServiceDataStore()");
        this.allLocks = new HashMap();
        this.allNotifiers = new HashMap();
        Gate.getCreoleRegister().addCreoleListener(this);
        Runtime.getRuntime().addShutdownHook(new Thread() {

            public void run() {
                if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + "executing shutdown hook...");
                for (Iterator itr = allLocks.entrySet().iterator(); itr.hasNext(); ) {
                    Map.Entry e = (Map.Entry) itr.next();
                    removeDocumentLocks((String) e.getKey(), true);
                }
            }
        });
    }

    public LanguageResource adopt(LanguageResource lr, SecurityInfo secInfo) throws PersistenceException, SecurityException {
        long t = System.currentTimeMillis();
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".adopt()" + " lr.name=" + lr.getName() + " secInfo='" + secInfo + "'");
        if (lr.getDataStore() != null) {
            if (lr.getDataStore().equals(this)) {
                return lr;
            } else {
                throw new PersistenceException("Can't adopt a resource which is already in a different datastore");
            }
        }
        try {
            LanguageResource res;
            if (lr instanceof Document) {
                Document doc = (Document) lr;
                doc.setDataStore(this);
                fireResourceAdopted(new DatastoreEvent(this, DatastoreEvent.RESOURCE_ADOPTED, doc, null));
                res = doc;
            } else if (lr instanceof Corpus) {
                FeatureMap fm = Factory.newFeatureMap();
                fm.put("transientSource", lr);
                res = (LanguageResource) Factory.createResource(DOCSERVICE_CORPUS_CLASS_NAME, fm);
                res.setDataStore(this);
                fireResourceAdopted(new DatastoreEvent(this, DatastoreEvent.RESOURCE_ADOPTED, res, null));
                return res;
            } else {
                throw new PersistenceException("ADOPT Operation not supported for sych type of LR: " + lr.getClass().getName());
            }
            if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".adopt()" + " lr.name=" + lr.getName() + " Resource adopted in " + (System.currentTimeMillis() - t) + " ms");
            return res;
        } catch (gate.creole.ResourceInstantiationException e) {
            throw new PersistenceException(e);
        }
    }

    public boolean canReadLR(Object lrID) throws PersistenceException, SecurityException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".canReadLR()");
        return false;
    }

    public boolean canWriteLR(Object lrID) throws PersistenceException, SecurityException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".canWriteLR()");
        return false;
    }

    public void close() throws PersistenceException {
        boolean isThereAreErrors = false;
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".close()");
        if (this.allLocks.size() > 0) {
            String[] docIDs = new String[this.allLocks.size()];
            int i = 0;
            for (Iterator itr = this.allLocks.entrySet().iterator(); itr.hasNext(); ) {
                Map.Entry e = (Map.Entry) itr.next();
                docIDs[i++] = (String) e.getKey();
            }
            for (i = 0; i < docIDs.length; i++) {
                removeDocumentLocks(docIDs[i], true);
            }
        }
        Gate.getDataStoreRegister().remove(this);
        if (isThereAreErrors) {
            throw new PersistenceException("There were errors while closing DocService DataStore.");
        }
    }

    public void create() throws PersistenceException, UnsupportedOperationException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".create()");
        throw new PersistenceException("Unsupported operation.");
    }

    public void delete() throws PersistenceException, UnsupportedOperationException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".delete()");
        throw new PersistenceException("Unsupported operation.");
    }

    public void delete(String lrClassName, Object lrId) throws PersistenceException, SecurityException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".delete(String, Object), lrClassName='" + lrClassName + "' lrId='" + lrId + "'");
        try {
            if (DOCSERVICE_CORPUS_CLASS_NAME.equals(lrClassName)) {
                this.sds.deleteCorpus((String) lrId);
            } else if (DOCUMENT_CLASS_NAME.equals(lrClassName)) {
                this.sds.deleteDoc((String) lrId);
            }
            fireResourceDeleted(new DatastoreEvent(this, DatastoreEvent.RESOURCE_DELETED, null, (String) lrId));
        } catch (DocServiceException re) {
            throw new PersistenceException(re);
        }
    }

    public List findLrIds(List constraints, String lrType) throws PersistenceException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".findLrIds(List, String)");
        throw new PersistenceException("Unsupported operation.");
    }

    public List findLrIds(List constraints) throws PersistenceException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".findLrIds(List)");
        throw new PersistenceException("Unsupported operation.");
    }

    public String getComment() {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getComment()");
        return "WS based adapter for GLEAM Doc Service";
    }

    public String getIconName() {
        return "ds.gif";
    }

    public LanguageResource getLr(String lrClassName, final Object lrId) throws PersistenceException, SecurityException {
        long t = System.currentTimeMillis();
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getLr()" + " lrClassName='" + lrClassName + "'" + " lrId='" + lrId + "'");
        if (!(lrId instanceof String)) {
            throw new PersistenceException("" + this.getClass().getName() + " only supports String type of Language Resource IDs");
        }
        Map docLocks = new HashMap();
        Map docNotifiers = new HashMap();
        try {
            LanguageResource res;
            if (DOCSERVICE_CORPUS_CLASS_NAME.equals(lrClassName)) {
                if (this.sds.getCorpusInfo((String) lrId) == null) throw new PersistenceException("Cant get corpus '" + lrId + "' from datastore.");
                FeatureMap fm = Factory.newFeatureMap();
                fm.put(Corpus.CORPUS_NAME_PARAMETER_NAME, this.sds.getCorpusInfo((String) lrId).getCorpusName());
                res = (Corpus) Factory.createResource(DOCSERVICE_CORPUS_CLASS_NAME, fm);
                res.setLRPersistenceId((String) lrId);
                res.setDataStore(this);
                FeatureMap corpusFeatures = Factory.newFeatureMap();
                Map<String, String> unwrapped = MapWrapper.unwrap(this.sds.getCorpusFeatures((String) lrId));
                if (unwrapped != null) {
                    corpusFeatures.putAll(unwrapped);
                }
                res.setFeatures(corpusFeatures);
                ((DocServiceCorpusImpl) res).init(this.sds);
                res.setFeatures(corpusFeatures);
            } else if (DOCUMENT_CLASS_NAME.equals(lrClassName)) {
                long t1 = System.currentTimeMillis();
                String docContent = this.sds.getDocContent((String) lrId);
                if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".getLr(" + lrClassName + ", " + lrId + ") getDocContent() content.length=" + docContent.length() + " takes " + (System.currentTimeMillis() - t1) + " ms");
                long t2 = System.currentTimeMillis();
                FeatureMap fm = Factory.newFeatureMap();
                fm.put("stringContent", docContent);
                res = (gate.corpora.DocumentImpl) Factory.createResource(DOCUMENT_CLASS_NAME, fm, null, this.sds.getDocInfo((String) lrId).getDocumentName());
                if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".getLr(): Factory.createResource() takes " + (System.currentTimeMillis() - t2) + " ms");
                FeatureMap docFeatures = Factory.newFeatureMap();
                Map<String, String> unwrapped = MapWrapper.unwrap(this.sds.getDocumentFeatures((String) lrId));
                if (unwrapped != null) {
                    docFeatures.putAll(unwrapped);
                }
                res.setFeatures(docFeatures);
                long t4 = System.currentTimeMillis();
                String[] asNames = this.sds.listAnnotationSets((String) lrId);
                if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".getLr(): listAnnotationSets() takes " + (System.currentTimeMillis() - t4) + " ms");
                for (int i = 0; i < asNames.length; i++) {
                    if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getLr(): getting named annotation set '" + asNames[i] + "'...");
                    long t5 = System.currentTimeMillis();
                    AnnotationSetHandle ash = this.sds.getAnnotationSet((String) lrId, asNames[i], false);
                    if (ash.getTaskID() == null || ash.getTaskID().length() == 0) {
                        throw new PersistenceException("Invalid task ID for document's named annotation set '" + asNames[i] + "'. Task ID=" + ash.getTaskID() + ". Document ID=" + lrId);
                    }
                    byte[] s = ash.getData();
                    if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".getLr(): get-annotation-set '" + asNames[i] + "' xmldata.length=" + s.length + " takes " + (System.currentTimeMillis() - t5) + " ms");
                    DocserviceUtil.setAnnotationSet(((Document) res).getAnnotations(asNames[i]), s);
                    docLocks.put(asNames[i], ash.getTaskID());
                    Thread notifier = createTaskNotifier(this.sds, ash.getTaskID());
                    notifier.start();
                    docNotifiers.put(asNames[i], notifier);
                }
                res.setLRPersistenceId(lrId);
                res.setDataStore(this);
                this.allLocks.put(lrId, docLocks);
                this.allNotifiers.put(lrId, docNotifiers);
            } else {
                throw new PersistenceException("Language Resources of type '" + lrClassName + "' not supprted by DocServiceDataStore.");
            }
            if (PROFILE) System.out.println("PROFILE: -------------------- " + this.getClass().getName() + ".getLr(" + lrClassName + ", " + lrId + ") Resource loaded in " + (System.currentTimeMillis() - t) + " ms");
            return res;
        } catch (ResourceInstantiationException e) {
            removeNotifiers(docNotifiers, (String) lrId);
            releaseDocLocks(docLocks, (String) lrId);
            throw new PersistenceException(e);
        } catch (XMLStreamException e) {
            removeNotifiers(docNotifiers, (String) lrId);
            releaseDocLocks(docLocks, (String) lrId);
            throw new PersistenceException(e);
        } catch (Exception e) {
            removeNotifiers(docNotifiers, (String) lrId);
            releaseDocLocks(docLocks, (String) lrId);
            throw new PersistenceException(e);
        }
    }

    public List getLrIds(String lrType) throws PersistenceException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getLrIds()");
        try {
            if (DOCSERVICE_CORPUS_CLASS_NAME.equals(lrType)) {
                CorpusInfo[] ii = sds.listCorpora();
                List result = new ArrayList();
                if (ii == null || ii.length == 0) return result;
                for (int i = 0; i < ii.length; i++) {
                    result.add(ii[i].getCorpusID());
                }
                return result;
            } else if (DOCUMENT_CLASS_NAME.equals(lrType)) {
                DocumentInfo[] ii = sds.listDocs();
                List result = new ArrayList();
                if (ii == null || ii.length == 0) return result;
                for (int i = 0; i < ii.length; i++) {
                    result.add(ii[i].getDocumentID());
                }
                return result;
            } else {
                throw new PersistenceException("" + this.getClass().getName() + " doesn't support LR type'" + lrType + "'");
            }
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    /** Get the name of an LR from its ID. */
    public String getLrName(Object lrId) {
        int secondSeparator = ((String) lrId).lastIndexOf("___");
        lrId = ((String) lrId).substring(0, secondSeparator);
        int firstSeparator = ((String) lrId).lastIndexOf("___");
        return ((String) lrId).substring(0, firstSeparator);
    }

    public List getLrNames(String lrType) throws PersistenceException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getLrNames()");
        try {
            if (DOCSERVICE_CORPUS_CLASS_NAME.equals(lrType)) {
                CorpusInfo[] ii = sds.listCorpora();
                List result = new ArrayList();
                if (ii == null || ii.length == 0) return result;
                for (int i = 0; i < ii.length; i++) {
                    result.add(ii[i].getCorpusName());
                }
                return result;
            } else if (DOCUMENT_CLASS_NAME.equals(lrType)) {
                DocumentInfo[] ii = sds.listDocs();
                List result = new ArrayList();
                if (ii == null || ii.length == 0) return result;
                for (int i = 0; i < ii.length; i++) {
                    result.add(ii[i].getDocumentName());
                }
                return result;
            } else {
                throw new PersistenceException("" + this.getClass().getName() + " doesn't support LR type'" + lrType + "'");
            }
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public List getLrTypes() throws PersistenceException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getLrTypes()");
        List l = new java.util.ArrayList();
        l.add(DOCSERVICE_CORPUS_CLASS_NAME);
        l.add(DOCUMENT_CLASS_NAME);
        return l;
    }

    public SecurityInfo getSecurityInfo(LanguageResource lr) throws PersistenceException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getSecurityInfo()");
        return null;
    }

    public Session getSession(Session s) throws SecurityException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getSession()");
        return null;
    }

    public String getStorageUrl() {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getStorageUrl()");
        return this.storageUrl;
    }

    public boolean isAutoSaving() {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".isAutoSaving()");
        return true;
    }

    public boolean lockLr(LanguageResource lr) throws PersistenceException, SecurityException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".lockLr()");
        return false;
    }

    public void open() throws PersistenceException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".open()");
        try {
            ClassLoader oldContextCL = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(Gate.getClassLoader());
            JaxWsProxyFactoryBean factoryBean = getFactoryBean();
            factoryBean.setAddress(this.storageUrl);
            this.sds = (DocService) factoryBean.create();
            Thread.currentThread().setContextClassLoader(oldContextCL);
            if (!ALLOW_CHUNKING) {
                CXFClientUtils.setAllowChunking(this.sds, false);
            }
            if (USE_COMPRESSION) {
                CXFClientUtils.configureForCompression(this.sds);
            }
            if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".open() docServiceHandle=" + this.sds);
            if (DEBUG) {
                CorpusInfo[] corpInfos = sds.listCorpora();
                for (int i = 0; i < corpInfos.length; i++) {
                    System.out.println("DEBUG: " + this.getClass().getName() + ".open(): " + corpInfos[i]);
                }
            }
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public void setAutoSaving(boolean autoSaving) throws UnsupportedOperationException, PersistenceException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".setAutoSaving()");
    }

    public void setSecurityInfo(LanguageResource lr, SecurityInfo si) throws PersistenceException, SecurityException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".setSecurityInfo()");
    }

    public void setSession(Session s) throws SecurityException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".setSession()");
    }

    public void setStorageUrl(String storageUrl) throws PersistenceException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".setStorageUrl()" + " current storageUrl='" + this.storageUrl + "'" + " new storageUrl='" + storageUrl + "'");
        this.storageUrl = storageUrl;
    }

    public void sync(LanguageResource lr) throws PersistenceException, SecurityException {
        long t = System.currentTimeMillis();
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".sync()" + " lr.name=" + lr.getName() + " ID='" + lr.getLRPersistenceId() + "'");
        if (lr.getDataStore() == null || !lr.getDataStore().equals(this)) {
            throw new PersistenceException("LR " + lr.getName() + " has not been adopted by this DataStore");
        }
        try {
            if (lr instanceof Document) {
                saveDocument((Document) lr);
            } else if (lr instanceof Corpus) {
                if (!(lr instanceof DocServiceCorpusImpl)) throw new PersistenceException("Can't save a corpus which " + "is not of type DocServiceCorpusImpl!");
                saveCorpus((DocServiceCorpusImpl) lr);
            } else {
                throw new PersistenceException("SYNC Operation not supported for such type of LR: " + lr.getClass().getName());
            }
            fireResourceWritten(new DatastoreEvent(this, DatastoreEvent.RESOURCE_WRITTEN, lr, lr.getLRPersistenceId()));
            if (PROFILE) System.out.println("PROFILE: -------------------- " + this.getClass().getName() + ".sync()" + " lr.name=" + lr.getName() + " ID='" + lr.getLRPersistenceId() + "' Resource synchronized in " + (System.currentTimeMillis() - t) + " ms");
        } catch (Exception re) {
            re.printStackTrace();
            throw new PersistenceException(re);
        }
    }

    public void unlockLr(LanguageResource lr) throws PersistenceException, SecurityException {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".unlockLr()");
    }

    public String getName() {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".getName() return value='" + this.name + "'");
        return this.name;
    }

    public void setName(String name) {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".setName()" + " current name='" + this.name + "'" + " new name='" + name + "'");
        this.name = name;
    }

    public synchronized void removeDatastoreListener(DatastoreListener l) {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".removeDatastoreListener()");
        if (datastoreListeners != null && datastoreListeners.contains(l)) {
            Vector v = (Vector) datastoreListeners.clone();
            v.removeElement(l);
            datastoreListeners = v;
        }
    }

    public synchronized void addDatastoreListener(DatastoreListener l) {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".addDatastoreListener()");
        Vector v = datastoreListeners == null ? new Vector(2) : (Vector) datastoreListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            datastoreListeners = v;
        }
    }

    protected void fireResourceAdopted(DatastoreEvent e) {
        if (datastoreListeners != null) {
            Vector listeners = datastoreListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((DatastoreListener) listeners.elementAt(i)).resourceAdopted(e);
            }
        }
    }

    protected void fireResourceDeleted(DatastoreEvent e) {
        if (datastoreListeners != null) {
            Vector listeners = datastoreListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((DatastoreListener) listeners.elementAt(i)).resourceDeleted(e);
            }
        }
    }

    protected void fireResourceWritten(DatastoreEvent e) {
        if (datastoreListeners != null) {
            Vector listeners = datastoreListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((DatastoreListener) listeners.elementAt(i)).resourceWritten(e);
            }
        }
    }

    /** String representation */
    public String toString() {
        String nl = Strings.getNl();
        StringBuffer s = new StringBuffer("DocServiceDataStore: ");
        s.append("name: " + this.name);
        s.append("; storageURL: " + this.storageUrl);
        s.append(nl);
        return s.toString();
    }

    private void fixCRLF(Document doc) {
        long t = System.currentTimeMillis();
        int i = doc.getContent().toString().indexOf("\r\n");
        if (DEBUG && i != -1) {
            System.out.println("DEBUG: " + this.getClass().getName() + ".sync(): local doc content content contains on or more \"/r/n\" which need to be fixed");
        }
        if (DEBUG_DETAILS && i != -1) {
            System.out.println("DEBUG_DETAILS: " + this.getClass().getName() + ".sync(): local doc content: '" + doc.getContent().toString().replaceAll("\n", "/n\n").replaceAll("\r", "/r\r") + "'");
        }
        while (i != -1) {
            try {
                doc.edit(new Long(i), new Long(i + 1), new DocumentContentImpl(""));
            } catch (InvalidOffsetException e) {
                e.printStackTrace();
            }
            i = doc.getContent().toString().indexOf("\r\n");
        }
        if (DEBUG_DETAILS) {
            System.out.println("DEBUG_DETAILS: " + this.getClass().getName() + ".adopt: local doc fixed content: '" + doc.getContent().toString().replaceAll("\n", "/n\n").replaceAll("\r", "/r\r") + "'");
        }
        if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".fixCRLF()" + " docName=" + doc.getName() + " fixCRLF() takes " + (System.currentTimeMillis() - t) + " ms");
    }

    private void checkDocContent(Document doc) throws PersistenceException {
        long t = System.currentTimeMillis();
        if (doc.getDataStore() == null || !doc.getDataStore().equals(this)) {
            throw new PersistenceException("Document " + doc.getName() + " has not been adopted by this DataStore.");
        }
        if (doc.getLRPersistenceId() == null) {
            throw new PersistenceException("Document " + doc.getName() + " has not been saved this DataStore.");
        }
        String remoteDocText = this.sds.getDocContent((String) doc.getLRPersistenceId());
        if (!remoteDocText.equals(doc.getContent().toString())) {
            if (DEBUG) {
                System.out.println("DEBUG: " + this.getClass().getName() + ".checkDocContent(): local doc content: '" + doc.getContent().toString().replaceAll("\n", "/n\n").replaceAll("\r", "/r\r") + "'");
                System.out.println("DEBUG: " + this.getClass().getName() + ".checkDocContent(): remote doc content: '" + remoteDocText.replaceAll("\n", "/n\n").replaceAll("\r", "/r\r") + "'");
            }
            throw new PersistenceException("Can't synchronize document. Document texts are different at the server and client sides.");
        }
        if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".checkDocContent()" + " docName=" + doc.getName() + " checkDocContent() takes " + (System.currentTimeMillis() - t) + " ms");
    }

    private void saveDocument(Document doc) throws RemoteException {
        long t = System.currentTimeMillis();
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".saveDocument() called");
        Map locksForUpdate = new HashMap();
        Map locksForDelete = new HashMap();
        try {
            if (doc.getLRPersistenceId() == null) {
                fixCRLF(doc);
                String encoding = "UTF-8";
                if (doc instanceof TextualDocument) {
                    encoding = ((TextualDocument) doc).getEncoding();
                }
                String docPersId = this.sds.createDoc(doc.getName(), doc.getContent().toString().getBytes(encoding), encoding);
                doc.setLRPersistenceId(docPersId);
            }
            checkDocContent(doc);
            locksForDelete.putAll(getAllLocks((String) doc.getLRPersistenceId()));
            if (locksForDelete.containsKey(null)) {
                locksForUpdate.put(null, locksForDelete.get(null));
                locksForDelete.remove(null);
            } else {
                if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".saveDocument(): getting default annotation set in RW mode...");
                locksForUpdate.put(null, this.sds.getAnnotationSet((String) doc.getLRPersistenceId(), null, false).getTaskID());
            }
            if (doc.getNamedAnnotationSets() != null) {
                Set names = doc.getAnnotationSetNames();
                Iterator itr = names.iterator();
                while (itr.hasNext()) {
                    String asName = (String) itr.next();
                    if (locksForDelete.containsKey(asName)) {
                        locksForUpdate.put(asName, locksForDelete.get(asName));
                        locksForDelete.remove(asName);
                    } else {
                        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".saveDocument(): getting named annotation set '" + asName + "' in RW mode...");
                        locksForUpdate.put(asName, this.sds.getAnnotationSet((String) doc.getLRPersistenceId(), asName, false).getTaskID());
                    }
                }
            }
            Map docLocks = (Map) this.allLocks.get((String) doc.getLRPersistenceId());
            if (docLocks == null) {
                docLocks = new HashMap();
                this.allLocks.put((String) doc.getLRPersistenceId(), docLocks);
            }
            Map docNotifiers = (Map) this.allNotifiers.get((String) doc.getLRPersistenceId());
            if (docNotifiers == null) {
                docNotifiers = new HashMap();
                this.allNotifiers.put((String) doc.getLRPersistenceId(), docNotifiers);
            }
            Iterator itr = locksForUpdate.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry entry = (Map.Entry) itr.next();
                String asName = (String) entry.getKey();
                String taskId = (String) entry.getValue();
                AnnotationSet annSet = (asName == null) ? doc.getAnnotations() : doc.getAnnotations(asName);
                StringBuffer sb = new StringBuffer();
                DocumentXmlUtils.annotationSetToXml(annSet, sb);
                if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".saveDocument(): updating annotation set '" + asName + "' taskId='" + taskId + "'");
                this.sds.setAnnotationSet(sb.toString().getBytes("UTF-8"), taskId, true);
                if (!docLocks.containsKey(asName)) {
                    docLocks.put(asName, taskId);
                }
                if (!docNotifiers.containsKey(asName)) {
                    Thread notifier = createTaskNotifier(this.sds, taskId);
                    notifier.start();
                    docNotifiers.put(asName, notifier);
                }
                itr.remove();
            }
            itr = locksForDelete.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry entry = (Map.Entry) itr.next();
                String asName = (String) entry.getKey();
                String taskId = (String) entry.getValue();
                if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".saveDocument(): deleting annotation set '" + asName + "' taskId='" + taskId + "'");
                this.sds.deleteAnnotationSet(taskId);
                if (docLocks.containsKey(asName)) {
                    docLocks.remove(asName);
                }
                if (docNotifiers.containsKey(asName)) {
                    Thread notifier = (Thread) docNotifiers.get(taskId);
                    if (notifier != null) notifier.interrupt();
                    docNotifiers.remove(asName);
                }
                itr.remove();
            }
            HashMap m = new HashMap();
            m.putAll(doc.getFeatures());
            this.sds.setDocumentFeatures((String) doc.getLRPersistenceId(), MapWrapper.wrap(m));
            if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".saveDocument()" + " docName=" + doc.getName() + " saveDocument() takes " + (System.currentTimeMillis() - t) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
            Iterator itr = locksForUpdate.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry entry = (Map.Entry) itr.next();
                String asName = (String) entry.getKey();
                String taskId = (String) entry.getValue();
                if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".saveDocument(): releasing 'update' lock '" + taskId + "' for annotation set '" + asName + "'");
                this.sds.releaseLock((String) entry.getKey());
            }
            itr = locksForDelete.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry entry = (Map.Entry) itr.next();
                String asName = (String) entry.getKey();
                String taskId = (String) entry.getValue();
                if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".saveDocument(): releasing 'delete' lock '" + taskId + "' for annotation set '" + asName + "'");
                this.sds.releaseLock((String) entry.getKey());
            }
        }
    }

    private Map getAllLocks(String docId) throws DocServiceException, RemoteException {
        long t = System.currentTimeMillis();
        Map m = (Map) allLocks.get(docId);
        Map docLocks = (m == null) ? new HashMap() : Collections.unmodifiableMap(m);
        if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".getAllLocks()" + " docID='" + docId + " getAllLocks() takes " + (System.currentTimeMillis() - t) + " ms");
        return docLocks;
    }

    private void saveCorpus(DocServiceCorpusImpl corpus) throws DocServiceException, PersistenceException, SecurityException {
        long t = System.currentTimeMillis();
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".saveCorpus() called");
        if (corpus.getLRPersistenceId() == null) {
            if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".saveCorpus(): this corpus doesn't contain persistent ID, so creating new corpus in datastore...");
            String pId = this.sds.createCorpus(corpus.getName());
            corpus.setLRPersistenceId(pId);
        }
        List docsForAdd = new ArrayList();
        List docsForRemove = new ArrayList();
        DocumentInfo[] DIs = this.sds.listDocs((String) corpus.getLRPersistenceId());
        if (DIs != null) {
            for (int i = 0; i < DIs.length; i++) {
                docsForRemove.add(DIs[i].getDocumentID());
            }
        }
        for (int i = 0; i < corpus.size(); i++) {
            if (corpus.getDocumentPersistentID(i) != null) {
                if (docsForRemove.contains(corpus.getDocumentPersistentID(i))) {
                    docsForRemove.remove(corpus.getDocumentPersistentID(i));
                } else {
                    docsForAdd.add(corpus.getDocumentPersistentID(i));
                }
            }
        }
        for (int i = 0; i < corpus.size(); i++) {
            if ((!corpus.isDocumentLoaded(i)) && corpus.isPersistentDocument(i)) continue;
            if (DEBUG) Out.prln("Saving document at position " + i);
            if (DEBUG) Out.prln("Document in memory " + corpus.isDocumentLoaded(i));
            if (DEBUG) Out.prln("is persistent? " + corpus.isPersistentDocument(i));
            if (DEBUG) Out.prln("Document name at position" + corpus.getDocumentName(i));
            Document doc = (Document) corpus.get(i);
            if (doc.getLRPersistenceId() == null) {
                if (DEBUG) Out.prln("Document adopted" + doc.getName());
                doc = (Document) this.adopt(doc, null);
                this.sync(doc);
                if (DEBUG) Out.prln("Document sync-ed");
                corpus.setDocumentPersistentID(i, doc.getLRPersistenceId());
                if (DEBUG) Out.prln("new document ID " + doc.getLRPersistenceId());
                this.sds.addDocumentToCorpus((String) corpus.getLRPersistenceId(), (String) doc.getLRPersistenceId());
            } else {
                this.sync(doc);
            }
        }
        for (Iterator iter = docsForRemove.iterator(); iter.hasNext(); ) {
            this.sds.removeDocumentFromCorpus((String) corpus.getLRPersistenceId(), (String) iter.next());
        }
        for (Iterator iter = docsForAdd.iterator(); iter.hasNext(); ) {
            this.sds.addDocumentToCorpus((String) corpus.getLRPersistenceId(), (String) iter.next());
        }
        HashMap m = new HashMap();
        m.putAll(corpus.getFeatures());
        this.sds.setCorpusFeatures((String) corpus.getLRPersistenceId(), MapWrapper.wrap(m));
        if (PROFILE) System.out.println("PROFILE: " + this.getClass().getName() + ".saveCorpus()" + " corpusName=" + corpus.getName() + " saveCorpus() takes " + (System.currentTimeMillis() - t) + " ms");
    }

    private void removeNotifiers(Map docNotifiers, String docID) {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".removeNotifiers(): removing notifiers for docID=" + docID);
        if (docNotifiers == null) return;
        for (Iterator itr = docNotifiers.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry e2 = (Map.Entry) itr.next();
            Thread taskNotifier = (Thread) e2.getValue();
            itr.remove();
            if (taskNotifier != null) taskNotifier.interrupt();
        }
    }

    private void releaseDocLocks(Map docLocks, String docID) {
        if (DEBUG) System.out.println("DEBUG: " + this.getClass().getName() + ".releaseDocLocks(): releasing locks for docID=" + docID);
        if (docLocks == null) return;
        for (Iterator itr = docLocks.entrySet().iterator(); itr.hasNext(); ) {
            Map.Entry e2 = (Map.Entry) itr.next();
            String taskID = (String) e2.getValue();
            try {
                itr.remove();
                this.sds.releaseLock(taskID);
            } catch (Exception e) {
                System.out.println("Exception occured while releasing document lock. Doc ID=" + docID + ", taskID=" + taskID);
                e.printStackTrace();
            }
        }
    }

    private void removeDocumentLocks(String docID, boolean release) {
        Map m = (Map) this.allNotifiers.get(docID);
        if (m != null) {
            removeNotifiers(m, docID);
            this.allNotifiers.remove(docID);
        }
        m = (Map) this.allLocks.get(docID);
        if (m != null && release) {
            releaseDocLocks(m, docID);
            this.allLocks.remove(docID);
        }
    }

    private static synchronized Thread createTaskNotifier(final DocService sds, final String docTaskId) {
        return new Thread() {

            public void run() {
                while (!interrupted()) {
                    try {
                        sleep(30000);
                        sds.keepaliveLock(docTaskId);
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void datastoreClosed(CreoleEvent evt) {
    }

    public void datastoreCreated(CreoleEvent evt) {
    }

    public void datastoreOpened(CreoleEvent evt) {
    }

    public void resourceLoaded(CreoleEvent evt) {
    }

    public void resourceRenamed(Resource evt, String arg1, String arg2) {
    }

    public void resourceUnloaded(CreoleEvent evt) {
        if (evt.getResource() instanceof Document) {
            Document d = (Document) evt.getResource();
            if (d.getDataStore() == null || d.getDataStore() != this) return;
            if (d.getLRPersistenceId() != null) {
                removeDocumentLocks((String) d.getLRPersistenceId(), true);
            }
        }
    }
}
