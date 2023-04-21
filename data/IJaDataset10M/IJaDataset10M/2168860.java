package org.tm4j.topicmap.hibernate;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.type.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactory;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.DuplicateResourceLocatorException;
import org.tm4j.topicmap.IntegrityViolationException;
import org.tm4j.topicmap.ProviderTransaction;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapFactory;
import org.tm4j.topicmap.TopicMapObject;
import org.tm4j.topicmap.TopicMapRuntimeException;
import org.tm4j.topicmap.TopicMapUtils;
import org.tm4j.topicmap.index.DuplicateIndexNameException;
import org.tm4j.topicmap.index.IndexManager;
import org.tm4j.topicmap.index.IndexManagerException;
import org.tm4j.utils.MultiValuePropertyChangeEvent;
import org.tm4j.utils.MultiValuePropertyChangeListener;
import uk.co.jezuk.mango.iterators.TransformingIterator;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TopicMapImpl extends PersistenceWrapper implements TopicMap {

    public static final String OPT_EVENT_NOTIF = "event.notification";

    TopicMapFactory m_factory;

    TopicMapUtilsImpl m_utils;

    String m_id;

    IndexManager m_ixMgr;

    private PropertyChangeSupport m_changeSupport;

    private VetoableChangeSupport m_vetoableChangeSupport;

    private Log m_log = LogFactory.getLog("org.tm4j.topicmap.backend.hibernate");

    private WrapFunction m_wrapper;

    private Map m_options;

    TopicMapImpl(TopicMapProviderImpl provider, TopicMapDataObject dataObject) {
        super(provider, null, dataObject);
        m_factory = new TopicMapFactoryImpl(provider.getLocatorFactory(), this, provider);
        m_utils = null;
        m_changeSupport = new PropertyChangeSupport(this);
        m_vetoableChangeSupport = new VetoableChangeSupport(this);
        m_wrapper = new WrapFunction(this, provider);
        m_pid = dataObject.getId();
    }

    public WrapFunction getWrapper() {
        return m_wrapper;
    }

    public Class getCls() {
        return TopicMapDataObject.class;
    }

    public TopicMapFactory getFactory() {
        return m_factory;
    }

    /**
     * Adds an added theme to the topic map.
     */
    public void addAddedTheme(Topic theme) {
        if (m_provider.isTransactionOpen()) {
            addAddedTheme(m_provider.getOpenTransaction(), theme);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                addAddedTheme(txn, theme);
                txn.commit();
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void addAddedTheme(ProviderTransaction txn, Topic theme) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        TopicDataObject tdo = (TopicDataObject) load(txn, (TopicImpl) theme);
        tmdo.getAddedThemes().add(tdo);
        save(txn, tmdo);
    }

    public Topic createTopic(String id) throws DuplicateObjectIDException, PropertyVetoException {
        return ((TopicMapFactoryImpl) getFactory()).createTopic(id);
    }

    public Association createAssociation(String id) throws DuplicateObjectIDException, PropertyVetoException {
        return ((TopicMapFactoryImpl) getFactory()).createAssociation(id);
    }

    public Association createAssociation(String id, Locator resourceLoc, Topic type, Topic[] themes) throws DuplicateObjectIDException, DuplicateResourceLocatorException, PropertyVetoException {
        return ((TopicMapFactoryImpl) getFactory()).createAssociation(id, resourceLoc, type, themes);
    }

    /**
     * Get the Topics in this topic map
     * @return An unmodifiable Set of all the topics in the topic map.
     */
    public Set getTopics() {
        if (m_provider.isTransactionOpen()) {
            return getTopics(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Set ret = getTopics(txn);
            txn.rollback();
            return ret;
        }
    }

    private Set getTopics(ProviderTransaction txn) {
        TopicMapDataObject tdo = (TopicMapDataObject) load(txn);
        List rawTopics = find(txn, "select topic from topic in class org.tm4j.topicmap.hibernate.TopicDataObject where topic.topicMap = ?", tdo, Hibernate.entity(TopicMapDataObject.class));
        Set ret = Collections.unmodifiableSet((Set) wrapCollection(rawTopics, new HashSet()));
        return ret;
    }

    public Iterator getTopicsIterator() {
        if (m_provider.isTransactionOpen()) {
            return getTopicsIterator(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Iterator ret = getTopicsIterator(txn);
            txn.rollback();
            return ret;
        }
    }

    private Iterator getTopicsIterator(ProviderTransaction txn) {
        TopicMapDataObject tdo = (TopicMapDataObject) load(txn);
        Iterator rawIt = iterate(txn, "select topic from topic in class org.tm4j.topicmap.hibernate.TopicDataObject where topic.topicMap = ?", tdo, Hibernate.entity(TopicMapDataObject.class));
        return new TransformingIterator(rawIt, getWrapper());
    }

    /**
     * Get a count of the number of topics in the topic map
     * @return The number of topics in the topic map
     */
    public int getTopicCount() {
        int ret = 0;
        if (m_provider.isTransactionOpen()) {
            ret = getTopicCount(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            ret = getTopicCount(txn);
            txn.rollback();
        }
        return ret;
    }

    public int getTopicCount(ProviderTransaction txn) {
        TopicMapDataObject tdo = (TopicMapDataObject) load(txn);
        try {
            return ((Integer) iterate(txn, "select count(topic) from topic in class org.tm4j.topicmap.hibernate.TopicDataObject where topic.topicMap = ?", tdo, Hibernate.entity(TopicMapDataObject.class)).next()).intValue();
        } catch (Exception ex) {
            throw new TopicMapRuntimeException("Storage query exception.", ex);
        }
    }

    /**
     * Gets the topic with the specified unique identifier
     * @param id The identifier of the topic to be retrieved.
     * @return The topic or null if no match is found
     */
    public Topic getTopicByID(String id) {
        if (m_provider.isTransactionOpen()) {
            return getTopicByID(m_provider.getOpenTransaction(), id);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Topic ret = getTopicByID(txn, id);
            txn.abort();
            return ret;
        }
    }

    private Topic getTopicByID(ProviderTransaction txn, String id) {
        if (m_log.isDebugEnabled()) {
            m_log.debug("BEGIN: getTopicByID(" + id + ")");
        }
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Topic ret = getTopicByID(txn, tmdo, id);
        return ret;
    }

    Topic getTopicByID(ProviderTransaction txn, TopicMapDataObject tmdo, String id) {
        List ret = find(txn, "from t in class org.tm4j.topicmap.hibernate.TopicDataObject where " + "  t.objectId = ? and t.topicMap = ?", new Object[] { id, tmdo }, new Type[] { Hibernate.STRING, Hibernate.entity(TopicMapDataObject.class) });
        if (ret.isEmpty()) {
            return null;
        }
        if (ret.size() > 1) {
            throw new TopicMapRuntimeException("Unexpected storage query result. Found multiple topics with id " + id);
        }
        if (m_log.isDebugEnabled()) {
            m_log.debug("END: getTopicByID()");
        }
        return (Topic) wrap(ret.iterator().next());
    }

    /**
     * Gets all the objects in the topic map.
     * @return A Collection of all of the TopicMapObjects in the topic map.
     */
    public Collection getObjects() {
        if (m_provider.isTransactionOpen()) {
            return getObjects(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Collection ret = getObjects(txn);
            txn.rollback();
            return ret;
        }
    }

    private Collection getObjects(ProviderTransaction txn) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Collection objects = find(txn, "from tmo in class org.tm4j.topicmap.hibernate.TopicMapObjectDataObject where " + "  tmo.topicMap = ?", tmdo, Hibernate.entity(TopicMapDataObject.class));
        return Collections.unmodifiableCollection(wrapCollection(objects, new ArrayList()));
    }

    /**
     * Gets the object with the specified unique identifier.
     * @param id The unique identifier of the object to be retrieved.
     * @return The topic map object which has the specified identifier or null if no match is found.
     */
    public TopicMapObject getObjectByID(String id) {
        if (m_provider.isTransactionOpen()) {
            return getObjectByID(m_provider.getOpenTransaction(), id);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            TopicMapObject ret = getObjectByID(txn, id);
            txn.rollback();
            return ret;
        }
    }

    private TopicMapObject getObjectByID(ProviderTransaction txn, String id) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        TopicMapObject ret = getObjectByID(txn, tmdo, id);
        return ret;
    }

    TopicMapObject getObjectByID(ProviderTransaction txn, TopicMapDataObject tmdo, String id) {
        Collection ret = null;
        if (id == null) {
            return null;
        }
        if (id.length() == 32) {
            ret = find(txn, "from t in class org.tm4j.topicmap.hibernate.TopicMapObjectDataObject where " + "  t.id = ? or (t.objectId = ? and t.topicMap = ?)", new Object[] { id, id, tmdo }, new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.entity(TopicMapDataObject.class) });
        } else {
            ret = find(txn, "from t in class org.tm4j.topicmap.hibernate.TopicMapObjectDataObject where " + "  t.objectId = ? and t.topicMap = ?", new Object[] { id, tmdo }, new Type[] { Hibernate.STRING, Hibernate.entity(TopicMapDataObject.class) });
        }
        if ((ret == null) || (ret.isEmpty())) {
            return null;
        }
        if (ret.size() > 1) {
            throw new TopicMapRuntimeException("Unexpected storage query result. Found multiple topicmap objects with id " + id);
        }
        return (TopicMapObject) wrap(ret.iterator().next());
    }

    /**
     * Gets the TopicMapObject which was generated from the specified resource.
     * @param locator The Locator of the resource to look for.
     * @return The topic map object which was created to represent the resource identified
     *         by <code>locator</code>
     * @deprecated from 0.9.0 use {@link #getObjectBySourceLocator(Locator)}
     */
    public TopicMapObject getObjectByResourceLocator(Locator resourceLocator) {
        return getObjectBySourceLocator(resourceLocator);
    }

    public TopicMapObject getObjectBySourceLocator(Locator srcLoc) {
        if (m_provider.isTransactionOpen()) {
            return getObjectBySourceLocator(m_provider.getOpenTransaction(), srcLoc);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            TopicMapObject ret = null;
            try {
                ret = getObjectBySourceLocator(txn, srcLoc);
            } catch (Exception ex) {
                txn.rollback();
                throw new TopicMapRuntimeException("STORAGE EXCEPTION", ex);
            }
            txn.rollback();
            return ret;
        }
    }

    private TopicMapObject getObjectBySourceLocator(ProviderTransaction txn, Locator resourceLocator) {
        if (m_log.isDebugEnabled()) {
            m_log.debug("BEGIN: getObjectBySourceLocator()");
        }
        TopicMapObject ret = null;
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        ret = getObjectByResourceLocator(txn, tmdo, resourceLocator);
        if (m_log.isDebugEnabled()) {
            m_log.debug("END: getObjectBySourceLocator()");
        }
        return ret;
    }

    TopicMapObject getObjectByResourceLocator(ProviderTransaction txn, TopicMapDataObject tmdo, LocatorDataObject ldo) {
        if (m_log.isDebugEnabled()) {
            m_log.debug("  Querying by locator object: " + ldo.getId());
        }
        Collection ret = find(txn, "select tmo from tmo in class org.tm4j.topicmap.hibernate.TopicMapObjectDataObject, " + "  loc in tmo.sourceLocators.elements where " + "  loc  = ? and tmo.topicMap = ?", new Object[] { ldo, tmdo }, new Type[] { Hibernate.entity(LocatorDataObject.class), Hibernate.entity(TopicMapDataObject.class) });
        if (ret.isEmpty()) {
            ret = find(txn, "select tm from tm in class org.tm4j.topicmap.hibernate.TopicMapDataObject," + "  loc in tm.sourceLocators.elements where " + "  loc = ? and tm = ?", new Object[] { ldo, tmdo }, new Type[] { Hibernate.entity(LocatorDataObject.class), Hibernate.entity(TopicMapDataObject.class) });
        }
        if (m_log.isDebugEnabled()) {
            m_log.debug("  Query by resource locator found: " + ret.size() + " matches.");
        }
        if (ret.isEmpty()) {
            return null;
        } else {
            return (TopicMapObject) wrap(ret.iterator().next());
        }
    }

    TopicMapObject getObjectByResourceLocator(ProviderTransaction txn, TopicMapDataObject tmdo, Locator loc) {
        Collection ret = find(txn, "select tmo from tmo in class org.tm4j.topicmap.hibernate.TopicMapObjectDataObject, " + "  loc in tmo.sourceLocators.elements where " + "  loc.address = ? and loc.notation = ? and tmo.topicMap = ?", new Object[] { loc.getAddress(), loc.getNotation(), tmdo }, new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.entity(TopicMapDataObject.class) });
        if (ret.isEmpty()) {
            ret = find(txn, "select tm from tm in class org.tm4j.topicmap.hibernate.TopicMapDataObject," + "  loc in tm.sourceLocators.elements where " + "  loc.address = ? and loc.notation = ? and tm = ?", new Object[] { loc.getAddress(), loc.getNotation(), tmdo }, new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.entity(TopicMapDataObject.class) });
        }
        if (ret.isEmpty()) {
            return null;
        } else {
            return (TopicMapObject) wrap(ret.iterator().next());
        }
    }

    /**
     * Get the topic which is bound to the specified subject
     * @param subjectLocator The locator of the subject to which the topic is bound
     * @return The topic bound to the specified subject or null if no match is found.
     */
    public Topic getTopicBySubject(Locator subjectLocator) {
        if (m_provider.isTransactionOpen()) {
            return getTopicBySubject(m_provider.getOpenTransaction(), subjectLocator);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Topic ret = getTopicBySubject(txn, subjectLocator);
            txn.rollback();
            return ret;
        }
    }

    private Topic getTopicBySubject(ProviderTransaction txn, Locator subjectLocator) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Topic ret = null;
        ret = getTopicBySubject(txn, tmdo, subjectLocator);
        return ret;
    }

    Topic getTopicBySubject(ProviderTransaction txn, TopicMapDataObject tmdo, Locator subjectLocator) {
        Collection ret = null;
        ret = find(txn, "from t in class org.tm4j.topicmap.hibernate.TopicDataObject where " + "  t.subject.address = ? and t.subject.notation = ? and t.topicMap = ?", new Object[] { subjectLocator.getAddress(), subjectLocator.getNotation(), tmdo }, new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.entity(TopicMapDataObject.class) });
        if (ret.isEmpty()) {
            return null;
        } else {
            if (ret.size() > 1) {
                m_log.warn("getTopicBySubject(): Query returned multiple topics. All returned topics should be merged. Returning a single arbitrary topic from the results set.");
            }
            return (Topic) wrap(ret.iterator().next());
        }
    }

    /**
     * Get the topic which is bound to the subject indicated by the specified resource.
     * @param subjectIndicatorLocator The locator of a resource which indicates the subject of the topic.
     * @return The topic bound to the specified subject or null if no match is found.
     */
    public Topic getTopicBySubjectIndicator(Locator subjectIndicatorLocator) {
        if (m_provider.isTransactionOpen()) {
            return getTopicBySubjectIndicator(m_provider.getOpenTransaction(), subjectIndicatorLocator);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Topic ret = getTopicBySubjectIndicator(txn, subjectIndicatorLocator);
            txn.rollback();
            return ret;
        }
    }

    private Topic getTopicBySubjectIndicator(ProviderTransaction txn, Locator subjectIndicatorLocator) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Topic ret = getTopicBySubjectIndicator(txn, tmdo, subjectIndicatorLocator);
        return ret;
    }

    Topic getTopicBySubjectIndicator(ProviderTransaction txn, TopicMapDataObject tmdo, Locator subjectIndicatorLocator) {
        Collection ret;
        ret = find(txn, "select t from t in class org.tm4j.topicmap.hibernate.TopicDataObject, " + "     si in t.subjectIndicators.elements where " + "  si.address  = ? and si.notation = ? and t.topicMap = ?", new Object[] { subjectIndicatorLocator.getAddress(), subjectIndicatorLocator.getNotation(), tmdo }, new Type[] { Hibernate.STRING, Hibernate.STRING, Hibernate.entity(TopicMapDataObject.class) });
        if (ret.isEmpty()) {
            return null;
        } else {
            if (ret.size() > 1) {
                m_log.warn("getTopicBySubjectIndicator(): Query returned multiple topics. All returned topics should be merged. Returning a single arbitrary topic from the results set.");
            }
            return (Topic) wrap(ret.iterator().next());
        }
    }

    /**
     * Get a list of the Topic objects defining the added themes of the topic map.
     * @return An unmodifiable Collection of the Topic objects defining the added themes of the
     * topic map.
     * @see Topic
     */
    public Collection getAddedThemes() {
        if (m_provider.isTransactionOpen()) {
            return getAddedThemes(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Collection ret = getAddedThemes(txn);
            txn.rollback();
            return ret;
        }
    }

    private Collection getAddedThemes(ProviderTransaction txn) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Collection ret = Collections.unmodifiableCollection(wrapCollection(tmdo.getAddedThemes(), new ArrayList()));
        return ret;
    }

    /**
     * Get a list of the Associations contained in the topic map.
     * @return An unmodifiable list of the associations contained in the topic map.
     * @see Association
     */
    public Collection getAssociations() {
        if (m_provider.isTransactionOpen()) {
            return getAssociations(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Collection ret = getAssociations(txn);
            txn.rollback();
            return ret;
        }
    }

    private Collection getAssociations(ProviderTransaction txn) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        List rawAssocs = find(txn, "select assoc from assoc in class org.tm4j.topicmap.hibernate.AssociationDataObject where assoc.topicMap = ?", tmdo, Hibernate.entity(TopicMapDataObject.class));
        Set ret = Collections.unmodifiableSet((Set) wrapCollection(rawAssocs, new HashSet()));
        return ret;
    }

    public Iterator getAssociationsIterator() {
        if (m_provider.isTransactionOpen()) {
            return getAssociationsIterator(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Iterator ret = getAssociationsIterator(txn);
            txn.rollback();
            return ret;
        }
    }

    private Iterator getAssociationsIterator(ProviderTransaction txn) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Iterator rawIter = iterate(txn, "select assoc from assoc in class org.tm4j.topicmap.hibernate.AssociationDataObject where assoc.topicMap = ?", tmdo, Hibernate.entity(TopicMapDataObject.class));
        return new TransformingIterator(rawIter, getWrapper());
    }

    /**
     * getName
     * Get the name of the topic map.
     */
    public String getName() {
        return null;
    }

    /**
     * setName
     * Set the name of the topic map.
     * @param name The name to be assigned to the topic map.
     */
    public void setName(String name) {
        return;
    }

    /**
     * Get the concrete implementation of the TopicMapUtils interface
     * which partners with the concrete implementation of this interface.
     */
    public TopicMapUtils getUtils() {
        if (m_utils == null) {
            m_utils = new TopicMapUtilsImpl(m_provider, this);
        }
        return m_utils;
    }

    /**
     * Get the concrete implementation of the LocatorFactory interface which
     * partners with the concrete implementation of this interface.
     * The returned factory will produce Locator objects which may be used with TopicMapObjects
     * in this topic map.
     */
    public LocatorFactory getLocatorFactory() {
        return m_provider.getLocatorFactory();
    }

    /**
     * Returns the Locator for the address of the
     * source which generated the topic map.
     * URI references withing the topic map will be resolved
     * relative to this locator.
     * @return The base resource locator of the topic map.
     */
    public Locator getBaseLocator() {
        if (m_provider.isTransactionOpen()) {
            return getBaseLocator(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Locator ret = getBaseLocator(txn);
            txn.rollback();
            return ret;
        }
    }

    private Locator getBaseLocator(ProviderTransaction txn) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        return (Locator) wrap(tmdo.getBaseLocator());
    }

    /**
     * Sets the Locator of the source which generated this
     * topic map.
     * @param base The base resource locator of the topic map.
     * @see #getBaseLocator()
     */
    public void setBaseLocator(Locator base) {
        if (m_provider.isTransactionOpen()) {
            setBaseLocator(m_provider.getOpenTransaction(), base);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                setBaseLocator(txn, base);
                txn.commit();
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void setBaseLocator(ProviderTransaction txn, Locator base) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        if (base != null) {
            if (!(base instanceof URILocatorImpl)) {
                base = ((LocatorFactoryImpl) getLocatorFactory()).createPersistentLocator(base);
            }
            LocatorDataObject ldo = (LocatorDataObject) load(txn, (URILocatorImpl) base);
            tmdo.setBaseLocator(ldo);
        } else {
            tmdo.setBaseLocator(null);
        }
        save(txn, tmdo);
    }

    /**
     * Returns an unmodifiable Map of the unresolved mergeMap
     * directives contained within this topic map.
     * The map key is the Locator of the external topic map to be merged
     * The map value is a Scope object which contains the additional themes
     * to be applied to the external map when it is merged.
     */
    public Collection getMergeMapLocators() {
        if (m_provider.isTransactionOpen()) {
            return getMergeMapLocators(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Collection ret = getMergeMapLocators(txn);
            txn.rollback();
            return ret;
        }
    }

    private Collection getMergeMapLocators(ProviderTransaction txn) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Collection ret = new ArrayList();
        List results = find(txn, "select mm.locator from tm in class org.tm4j.topicmap.hibernate.TopicMapDataObject, mm in tm.mergeMaps.elements where tm = ?", tmdo, Hibernate.entity(TopicMapDataObject.class));
        wrapCollection(results, ret);
        return ret;
    }

    /**
     * Adds a new mergeMap directive to this topic map.
     * @param mapLocator The Locator of the external topic map to be merged
     * @param addedThemes A Scope object containing the themes to be added to
     *                    the topic map when it is merged.
     */
    public void addMergeMap(Locator mapLocator, Topic[] addedThemes) {
        if (m_provider.isTransactionOpen()) {
            addMergeMap(m_provider.getOpenTransaction(), mapLocator, addedThemes);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                addMergeMap(txn, mapLocator, addedThemes);
                txn.commit();
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void addMergeMap(ProviderTransaction txn, Locator mapLocator, Topic[] addedThemes) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        if (!(mapLocator instanceof URILocatorImpl)) {
            mapLocator = ((LocatorFactoryImpl) getLocatorFactory()).createPersistentLocator(mapLocator);
        }
        LocatorDataObject ldo = (LocatorDataObject) load(txn, (URILocatorImpl) mapLocator);
        MergeMap mergeMap = null;
        Collection mergeMaps = tmdo.getMergeMaps();
        if (mergeMaps == null) {
            mergeMaps = new ArrayList();
            tmdo.setMergeMaps(mergeMaps);
        } else {
            Iterator it = mergeMaps.iterator();
            while (it.hasNext()) {
                MergeMap mm = (MergeMap) it.next();
                if (mm.getLocator().equals(ldo)) {
                    mergeMap = mm;
                    break;
                }
            }
        }
        if (mergeMap == null) {
            mergeMap = new MergeMap();
            mergeMap.setLocator(ldo);
            persist(txn, mergeMap);
            mergeMaps.add(mergeMap);
            tmdo.setMergeMaps(mergeMaps);
        }
        Set mms = mergeMap.getScope();
        if (mms == null) {
            mms = new HashSet();
            mergeMap.setScope(mms);
        }
        if (addedThemes != null) {
            for (int i = 0; i < addedThemes.length; i++) {
                mms.add((TopicDataObject) load(txn, (TopicImpl) addedThemes[i]));
            }
        }
        save(txn, tmdo);
    }

    /**
     * Determine whether or not the specified resource is listed in an
     * unresolved mergeMap directive for this topic map.
     * @param mapLocator The Locator of the external topic map to test for
     * @return True if the specified external resource is referenced from an
     *  unprocessed mergeMap directive, false otherwise.
     */
    public boolean hasMergeMap(Locator mapLocator) {
        if (m_provider.isTransactionOpen()) {
            return hasMergeMap(m_provider.getOpenTransaction(), mapLocator);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            boolean ret = hasMergeMap(txn, mapLocator);
            txn.rollback();
            return ret;
        }
    }

    private boolean hasMergeMap(ProviderTransaction txn, Locator mapLocator) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        boolean ret = hasMergeMap(txn, tmdo, mapLocator);
        return ret;
    }

    boolean hasMergeMap(ProviderTransaction txn, TopicMapDataObject tmdo, Locator mapLocator) {
        List results = find(txn, "select m from " + "  t in class org.tm4j.topicmap.hibernate.TopicMapDataObject," + "  m in t.mergeMaps.elements where t = ? and m.locator.address = ? and m.locator.notation = ?", new Object[] { tmdo, mapLocator.getAddress(), mapLocator.getNotation() }, new Type[] { Hibernate.entity(TopicMapDataObject.class), Hibernate.STRING, Hibernate.STRING });
        return !(results.isEmpty());
    }

    /**
     * Returns the collection of themes to be added to the external
     * topic map resource when it is merged with this map.
     * @param mapLocator The Locator of the external topic map to
     *          be merged.
     * @return If <code>mapLocator</code> specifies the locator of
     *    an external topic map to be merged into this topic map,
     *    a Scope object containing the themes to be added to the
     *    external topic map will be returned. Otherwise, null is returned.
     */
    public Set getMergeMapAddedThemes(Locator mapLocator) {
        if (m_provider.isTransactionOpen()) {
            return getMergeMapAddedThemes(m_provider.getOpenTransaction(), mapLocator);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Set ret = getMergeMapAddedThemes(txn, mapLocator);
            txn.rollback();
            return ret;
        }
    }

    private Set getMergeMapAddedThemes(ProviderTransaction txn, Locator mapLocator) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Set ret = getMergeMapAddedThemes(txn, tmdo, mapLocator);
        return ret;
    }

    Set getMergeMapAddedThemes(ProviderTransaction txn, TopicMapDataObject tmdo, Locator mapLocator) {
        List results = find(txn, "select elements(m.scope) from " + "  org.tm4j.topicmap.hibernate.TopicMapDataObject as tm," + "  m in elements(tm.mergeMaps)" + "  where tm = ? and m.locator.address = ? and m.locator.notation = ?", new Object[] { tmdo, mapLocator.getAddress(), mapLocator.getNotation() }, new Type[] { Hibernate.entity(TopicMapDataObject.class), Hibernate.STRING, Hibernate.STRING });
        if (results.isEmpty()) {
            return new HashSet();
        } else {
            Set ret = (Set) wrapCollection(results, new HashSet());
            return ret;
        }
    }

    /**
     * Removes an unresolved mergeMap directive from this topic map.
     * @param mapLocator The Locator of the external topic map reference to
     *                   be removed.
     */
    public void removeMergeMap(Locator mapLocator) {
        if (m_provider.isTransactionOpen()) {
            removeMergeMap(m_provider.getOpenTransaction(), mapLocator);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                removeMergeMap(txn, mapLocator);
                txn.commit();
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void removeMergeMap(ProviderTransaction txn, Locator mapLocator) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        removeMergeMap(txn, tmdo, mapLocator);
        save(txn, tmdo);
    }

    void removeMergeMap(ProviderTransaction txn, TopicMapDataObject tmdo, Locator mapLocator) {
        List results = find(txn, "select m from " + "  t in class org.tm4j.topicmap.hibernate.TopicMapDataObject," + "  m in t.mergeMaps.elements where t = ? and m.locator.address = ? and m.locator.notation = ?", new Object[] { tmdo, mapLocator.getAddress(), mapLocator.getNotation() }, new Type[] { Hibernate.entity(TopicMapDataObject.class), Hibernate.STRING, Hibernate.STRING });
        if (!results.isEmpty()) {
            delete(txn, results.iterator().next());
        }
    }

    /**
     * Adds an external reference from this topic map to another topic map.
     * An external reference represents a <topicRef> element which addresses
     * a Topic which is not contained within the resource addressed
     * by the baseLocator property of this topic map.
     *
     * @param topicRef <p>The external locator to be added. If the referenced resource
     *                 is already contained as an external reference or as a merge map,
     *                 it will not be added. For the purposes of this check, the
     *                 fragment and query parts of a URILocator are ignored, and
     *                 only the reference to the complete document will be stored</p>
     *                 <p>For example if the reference is to
     *                 http://www.tm4j.org/example.xtm#topic1, the locator which
     *                 is tested for and stored will be http://www.tm4j.org/example.xtm
     *                 This means that multiple references to different topics
     *                 in the same external topic map will only ever create one
     *                 external reference.</p>
     *                 <p>For other locator notations the complete address is used
     *                 for this test.</p>
     */
    public void addExternalRef(Locator topicRef) {
        if (m_provider.isTransactionOpen()) {
            addExternalRef(m_provider.getOpenTransaction(), topicRef);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                addExternalRef(txn, topicRef);
                txn.commit();
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void addExternalRef(ProviderTransaction txn, Locator topicRef) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        if (!(topicRef instanceof URILocatorImpl)) {
            topicRef = ((LocatorFactoryImpl) getLocatorFactory()).createPersistentLocator(topicRef);
        }
        LocatorDataObject ldo = (LocatorDataObject) load(txn, (URILocatorImpl) topicRef);
        tmdo.getExternalRefs().add(ldo);
        save(txn, tmdo);
    }

    /**
     * Returns all of the unresolved external references from this topic map.
     */
    public Set getExternalRefs() {
        if (m_provider.isTransactionOpen()) {
            return getExternalRefs(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Set ret = getExternalRefs(txn);
            txn.rollback();
            return ret;
        }
    }

    private Set getExternalRefs(ProviderTransaction txn) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Set ret = Collections.unmodifiableSet((Set) wrapCollection(tmdo.getExternalRefs(), new HashSet()));
        return ret;
    }

    /**
     * Removes a resource from the list of external references of this topic map.
     * Although this method is provided, applications wishing to maintain full
     * XTM 1.0 Conformance should never call this method directly.
     * Instead an external reference should be removed by calling
     * {@link TopicMapProvider#mergeTopicMap(TopicMap, Locator, Scope)}
     */
    public void removeExternalRef(Locator externalReference) {
        if (m_provider.isTransactionOpen()) {
            removeExternalRef(m_provider.getOpenTransaction(), externalReference);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                removeExternalRef(txn, externalReference);
                txn.commit();
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void removeExternalRef(ProviderTransaction txn, Locator externalReference) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        URILocatorImpl l = (URILocatorImpl) externalReference;
        Collection refs = tmdo.getExternalRefs();
        if (refs != null) {
            Iterator it = refs.iterator();
            while (it.hasNext()) {
                LocatorDataObject tmp = (LocatorDataObject) it.next();
                if (tmp.getId().equals(l.getPersistenceId())) {
                    refs.remove(tmp);
                    break;
                }
            }
            save(txn, tmdo);
        }
        return;
    }

    /**
     * Returns the IndexManager object which handles access
     * to all of the indexes for this topic map.
     */
    public IndexManager getIndexManager() {
        if (m_ixMgr == null) {
            m_ixMgr = new org.tm4j.topicmap.memory.index.MemoryIndexManagerImpl(this);
            try {
                m_ixMgr.registerIndexProvider(new org.tm4j.topicmap.hibernate.index.HibernateBasicIndexProvider(m_provider));
            } catch (DuplicateIndexNameException ex) {
                throw new TopicMapRuntimeException("INTERNAL ERROR: Duplicate Index Name encountered while intialising index for topic map " + this.toString() + " - " + ex.toString());
            } catch (IndexManagerException ex) {
                throw new TopicMapRuntimeException("INTERNAL ERROR: Error from IndexManager while initialising indices for topic map " + this.toString() + " - " + ex.toString());
            }
        }
        return m_ixMgr;
    }

    /**
      * Sets the object ID of the TopicMapObject.
      * The object ID is an internal identifier assigned by the
      * application to a topic map object.
      * ID values must be unique for each object in the same topic map.
      * Implementations are free to assign ID values in any way they see fit.
      *
      * @param    id    the new internal object ID.
      * @throws DuplicateObjectIDException If an attempt is made to
      *            set the object's ID to a value already assigned to
      *            an object in the same TopicMap.
      * @throws DuplicateResourceLocatorException If the resource locator of the
      *           object being updated clashes with the resource locator of
      *           an existing object.
      */
    public void setID(String id) throws DuplicateObjectIDException, DuplicateResourceLocatorException {
    }

    /**
     * Returns the object ID of the TopicMapObject.
     * Note that implementations are free to assign ID values in any way they see fit.
     * To get the Locator of the XTM element which is represented by this object,
     * use {@link #getResourceLocator()}.
     *
     * @return    the internal object ID.
     * @see       #setID
     */
    public String getID() {
        if (m_provider.isTransactionOpen()) {
            return getID(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            String ret = getID(txn);
            txn.rollback();
            return ret;
        }
    }

    private String getID(ProviderTransaction txn) {
        TopicMapDataObject tdo = (TopicMapDataObject) load(txn);
        return tdo.getId();
    }

    /**
     * Returns wether the ID of this TopicMapObject equals to the ID of the TopicMapObject provided.
     *
     * This method is to allow for implementations which do not have a notion of explicit ID strings or
     * where such a creation of ID strings is expensive, where IDs are not strings or where otherwise undue. 
     *
     * The implementation can safely assume that the TopicMapObject provided is from the same TopicMap. 
     */
    public boolean equalsByID(TopicMapObject o) {
        return getID().equals(o.getID());
    }

    /**
     * Returns the Locator of the XML or other resource which
     * caused the creation of this topic map object.
     *
     * @return    The creating Locator.
     * @deprecated from 0.9.0 use {@link #getResourceLocators()} instead
     */
    public Locator getResourceLocator() {
        Collection srcLocs = getSourceLocators();
        if (!srcLocs.isEmpty()) {
            return (Locator) srcLocs.iterator().next();
        } else {
            return null;
        }
    }

    public Set getSourceLocators() {
        if (m_provider.isTransactionOpen()) {
            return Collections.unmodifiableSet(getSourceLocators(m_provider.getOpenTransaction()));
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            Set ret = getSourceLocators(txn);
            txn.rollback();
            return Collections.unmodifiableSet(ret);
        }
    }

    private Set getSourceLocators(ProviderTransaction txn) {
        TopicMapDataObject tdo = (TopicMapDataObject) load(txn);
        Set resLocs = tdo.getSourceLocators();
        return (Set) wrapCollection(resLocs, new HashSet());
    }

    /**
     * Sets the Locator of the XML or other resource which
     * caused the creation of this topic map object. Typically,
     * this will be the Locator of an XTM element.
     *
     * @param    loc    the creating Locator.
     * @throws DuplicateResourceLocatorException if <code>loc</code> is already
     *         assigned as a resource locator to some other topic map object.
     * @deprecated from 0.9.0 use {@link #addResourceLocator(Locator)} instead
     */
    public void setResourceLocator(Locator loc) throws DuplicateResourceLocatorException {
        addSourceLocator(loc);
    }

    public void addSourceLocator(Locator loc) throws DuplicateResourceLocatorException {
        if (m_provider.isTransactionOpen()) {
            addSourceLocator(m_provider.getOpenTransaction(), loc);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                addSourceLocator(txn, loc);
                txn.commit();
            } catch (DuplicateResourceLocatorException ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw ex;
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void addSourceLocator(ProviderTransaction txn, Locator loc) throws DuplicateResourceLocatorException {
        URILocatorImpl persistentLoc = null;
        if (loc instanceof URILocatorImpl) {
            persistentLoc = (URILocatorImpl) loc;
        } else {
            persistentLoc = ((LocatorFactoryImpl) getLocatorFactory()).createPersistentLocator(loc);
        }
        TopicMapDataObject tdo = (TopicMapDataObject) load(txn);
        Set resLocs = tdo.getSourceLocators();
        if (resLocs == null) {
            resLocs = new HashSet();
        }
        resLocs.add((LocatorDataObject) load(txn, persistentLoc));
        tdo.setSourceLocators(resLocs);
        save(txn, tdo);
    }

    public void removeSourceLocator(Locator loc) {
        if (m_provider.isTransactionOpen()) {
            removeSourceLocator(m_provider.getOpenTransaction(), loc);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                removeSourceLocator(txn, loc);
                txn.commit();
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void removeSourceLocator(ProviderTransaction txn, Locator loc) {
        URILocatorImpl persistentLoc = null;
        if (loc instanceof URILocatorImpl) {
            persistentLoc = (URILocatorImpl) loc;
        } else {
            persistentLoc = ((LocatorFactoryImpl) getLocatorFactory()).createPersistentLocator(loc);
        }
        TopicMapDataObject tdo = (TopicMapDataObject) load(txn);
        Set resLocs = tdo.getSourceLocators();
        if (resLocs == null) {
            resLocs = new HashSet();
        }
        resLocs.remove((LocatorDataObject) load(txn, persistentLoc));
        tdo.setSourceLocators(resLocs);
    }

    /**
     * Returns a handle to the topic map which this object
     * is part of.
     *
     * @return    This object's parent topic map.
     */
    public TopicMap getTopicMap() {
        return this;
    }

    /**
    * Adds a Property Change Listener. The listener is registered for only one property.
    *
    * @param    propertyName    The property whose changes the listener monitors.
    * @param    listener        The new listener to be registered. It only listens
    *                           for changes in the property specified by <code>propertyName</code>.
    */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        m_provider.addPropertyChangeListener(this, propertyName, listener);
    }

    /**
    * Adds a Property Change Listener. The listener is registered for all properties.
    *
    * @param    listener        The new listener to be registered. It listens
    *                           for changes in all available properties.
    */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        m_provider.addPropertyChangeListener(this, listener);
    }

    /**
    * Removes a Property Change Listener.
    *
    * @param    listener        The listener to be removed.
    */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        m_provider.removePropertyChangeListener(this, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        m_provider.removePropertyChangeListener(this, propertyName, listener);
    }

    /**
    * Notifies a Property Change Listener of a property change.
    * Note that if <code>oldVal</code> equals <code>newVal</code>,
    * no property change event is actually fired.
    *
    * @param    propertyName    The property for which a property change is
    *                           to be fired. The Property Change Listener
    *                           responsible for handling changes to this
    *                           particular property is selected automatically.
    * @param    oldVal          The property's original value.
    * @param    newVal          The property's new value.
    */
    public void firePropertyChange(String propertyName, Object oldVal, Object newVal) {
        m_provider.firePropertyChange(this, new PropertyChangeEvent(this, propertyName, oldVal, newVal));
    }

    /**
    * Notifies the Property Change Listeners of a property change.
    *
    * @param    propertyChange    The PropertyChangeEvent to be fired.
    *                           The Property Change Listener is
    *                           responsible for handling changes to this
    *                           particular property is selected automatically.
    */
    public void firePropertyChange(PropertyChangeEvent propertyChange) {
        m_provider.firePropertyChange(this, propertyChange);
    }

    /**
    * Adds a MultiValue Property Change Listener. The listener is registered for only one property.
    *
    * @param    propertyName    The property whose changes the listener monitors.
    * @param    listener        The new listener to be registered. It only listens
    *                           for changes in the property specified by <code>propertyName</code>.
    * @deprecated From TM4J 0.8.0 only the ProperyChangeListener interface will be notified.
    */
    public void addMultiValuePropertyChangeListener(String propertyName, MultiValuePropertyChangeListener listener) {
        if (m_log.isDebugEnabled()) {
            m_log.debug("addMultiValuePropertyChangeListener(" + propertyName + ", " + listener.toString() + ")");
        }
        m_provider.addMultiValuePropertyChangeListener(this, propertyName, listener);
    }

    /**
    * Adds a MultiValue Property Change Listener. The listener is registered for all properties.
    *
    * @param    listener        The new listener to be registered. It listens
    *                           for changes in all available properties.
    * @deprecated From TM4J 0.8.0 only the ProperyChangeListener interface will be notified.
    */
    public void addMultiValuePropertyChangeListener(MultiValuePropertyChangeListener listener) {
        if (m_log.isDebugEnabled()) {
            m_log.debug("addMultiValuePropertyChangeListener(" + listener.toString() + ")");
        }
        m_provider.addMultiValuePropertyChangeListener(this, listener);
    }

    /**
    * Removes a MultiValue Property Change Listener.
    *
    * @param    listener        The listener to be removed.
    * @deprecated From TM4J 0.8.0 only the ProperyChangeListener interface will be notified.
    */
    public void removeMultiValuePropertyChangeListener(MultiValuePropertyChangeListener listener) {
        if (m_log.isDebugEnabled()) {
            m_log.debug("removeMultiValuePropertyChangeListener(" + listener.toString() + ")");
        }
        m_provider.removeMultiValuePropertyChangeListener(this, listener);
    }

    /**
     * Notifies the MultiValue Property Change Listeners of a property change to this object
     * @param   propertyName   The name of the property modified.
     * @param   operation      The type of modification to the property. Allowed values are
     *                         specified in the class
     *                         {@link org.tm4j.utils.MultiValuePropertyChangeEvent}
     * @param   operand        The object or Collection of objects which were involved
     *                         in the modification. For OP_CLEAR events, no operand is
     *                         required. For OP_ADD_SINGLE or OP_REMOVE_SINGLE events,
     *                         the operand must be the object added or removed.
     *                         For OP_ADD_SET and OP_REMOVE_SET events, the operand
     *                         must be a Collection of all the objects added or removed.
     *                         For OP_SET events, the operand must be a collection of the
     *                         new values assigned to the property.
     */
    public void fireMultiValuePropertyChange(String propertyName, int operation, Object operand) {
        fireMultiValuePropertyChange(new MultiValuePropertyChangeEvent(this, propertyName, operation, operand));
    }

    /**
    * Notifies the MultiValue Property Change Listeners of a property change.
    *
    * @param    propertyChange    The MultiValuePropertyChangeEvent to be fired.
    *                           The MultiValue Property Change Listener is
    *                           responsible for handling changes to this
    *                           particular property is selected automatically.
    */
    public void fireMultiValuePropertyChange(MultiValuePropertyChangeEvent propertyChange) {
        if (m_log.isDebugEnabled()) {
            m_log.debug("fireMultiValuePropertyChange(" + propertyChange.getPropertyName() + ")");
        }
        m_provider.fireMultiValuePropertyChange(this, propertyChange);
    }

    public void addVetoableChangeListener(VetoableChangeListener listener) {
        m_provider.addVetoableChangeListener(this, listener);
    }

    public void removeVetoableChangeListener(VetoableChangeListener listener) {
        m_provider.removeVetoableChangeListener(this, listener);
    }

    public void fireVetoableChange(String propertyName, Object oldValue, Object newValue) throws PropertyVetoException {
        m_provider.fireVetoableChange(this, new PropertyChangeEvent(this, propertyName, oldValue, newValue));
    }

    /**
     * Permanently removes this object from the topic map. Depending upon the backend
     * implementation, this method may also free up the memory or other storage space
     * used by the object.
     *
     * An object may only be destroyed after all references to it from other objects
     * have been removed. If the object detects that there is an existing reference
     * still held to it, then an IntegrityViolationException is thrown
     *
     * @throws IntegrityViolationException If this object is still referenced from
     *             one or more other topic map objects.
     */
    public void destroy() throws IntegrityViolationException {
        if (m_provider.isTransactionOpen()) {
            destroy(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                destroy(txn);
                txn.commit();
            } catch (IntegrityViolationException ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw ex;
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void destroy(ProviderTransaction txn) throws IntegrityViolationException {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Iterator it = iterate(txn, "select distinct o from o in class org.tm4j.topicmap.hibernate.OccurrenceDataObject where o.topicMap = ?", tmdo, Hibernate.entity(TopicMapDataObject.class));
        while (it.hasNext()) {
            OccurrenceDataObject odo = (OccurrenceDataObject) it.next();
            delete(txn, odo);
        }
        it = iterate(txn, "select distinct n from n in class org.tm4j.topicmap.hibernate.BaseNameDataObject where n.topicMap = ?", tmdo, Hibernate.entity(TopicMapDataObject.class));
        while (it.hasNext()) {
            BaseNameDataObject bndo = (BaseNameDataObject) it.next();
            delete(txn, bndo);
        }
        it = iterate(txn, "select distinct m from m in class org.tm4j.topicmap.hibernate.MemberDataObject where m.topicMap = ?", tmdo, Hibernate.entity(TopicMapDataObject.class));
        while (it.hasNext()) {
            delete(txn, (MemberDataObject) it.next());
        }
        it = iterate(txn, "select distinct a from a in class org.tm4j.topicmap.hibernate.AssociationDataObject where a.topicMap = ?", tmdo, Hibernate.entity(TopicMapDataObject.class));
        while (it.hasNext()) {
            AssociationDataObject ado = (AssociationDataObject) it.next();
            delete(txn, ado);
        }
        it = iterate(txn, "select distinct t from t in class org.tm4j.topicmap.hibernate.TopicDataObject where t.topicMap = ?", tmdo, Hibernate.entity(TopicMapDataObject.class));
        while (it.hasNext()) {
            TopicDataObject tdo = (TopicDataObject) it.next();
            delete(txn, tdo);
        }
        delete(txn, tmdo);
    }

    /**
     * Updates the internal cache of option values. This method
     * should be called by client applications that have reason to
     * suspect that the option value specified in the database
     * have been modified.
     */
    public void updateOptions() {
        if (m_provider.isTransactionOpen()) {
            updateOptions(m_provider.getOpenTransaction());
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            updateOptions(txn);
            txn.rollback();
        }
    }

    private void updateOptions(ProviderTransaction txn) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        if (tmdo.getOptions() == null) {
            m_options = new HashMap();
        } else {
            m_options = new HashMap(tmdo.getOptions());
        }
    }

    public String getStringOption(String key) {
        if (m_options == null) {
            updateOptions();
        }
        return (String) m_options.get(key);
    }

    public boolean getBooleanOption(String key) {
        Boolean b = new Boolean(getStringOption(key));
        return b.booleanValue();
    }

    /**
     * Sets the specified option value in the persistent store and
     * updates the internal cache from the database. This method
     * always flushes the update through to the database so it
     * may be picked up by other clients on the same database.
     * @param key the key of the option to be updated.
     * @param value the value of the option to be updated.
     */
    public void setOption(String key, String value) {
        if (m_provider.isTransactionOpen()) {
            setOption(m_provider.getOpenTransaction(), key, value);
        } else {
            ProviderTransaction txn = m_provider.openTransaction();
            try {
                setOption(txn, key, value);
                txn.commit();
            } catch (Exception ex) {
                if (txn.isOpen()) {
                    txn.rollback();
                }
                throw new TopicMapRuntimeException(ex);
            }
        }
    }

    private void setOption(ProviderTransaction txn, String key, String value) {
        TopicMapDataObject tmdo = (TopicMapDataObject) load(txn);
        Map opts = tmdo.getOptions();
        if (opts == null) {
            opts = new HashMap();
        }
        opts.put(key, value);
        tmdo.setOptions(opts);
        save(txn, tmdo);
        updateOptions(txn);
    }

    /**
     * Sets a boolean-valued option in the persisten store and
     * updates the internal cache from the database.
      * @param key the key of the option to be updated.
     * @param value the value of the option to be updated.
     */
    public void setOption(String key, boolean value) {
        setOption(key, String.valueOf(value));
    }

    public Boolean getProperty(String propertyName) {
        String propVal = getStringOption(propertyName);
        if (propVal == null) {
            return null;
        }
        return Boolean.valueOf(propVal);
    }
}
