package org.tm4j.topicmap.unified;

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
import java.util.Set;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactory;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.DuplicateResourceLocatorException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapFactory;
import org.tm4j.topicmap.TopicMapObject;
import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapUtils;
import org.tm4j.topicmap.index.IndexException;
import org.tm4j.topicmap.index.IndexManager;
import org.tm4j.topicmap.index.IndexManagerException;
import org.tm4j.topicmap.unified.index.UnifiedIndexManager;
import org.tm4j.utils.CollectionChain;
import org.tm4j.utils.IteratorChain;
import org.tm4j.utils.MultiValuePropertyChangeEvent;
import org.tm4j.utils.MultiValuePropertyChangeListener;

/**
 * Implements a unified topic navigation structure over multiple
 * seperate TopicMap objects. The current implementation allows
 * seamless access to all topics using the standard TopicMap interface.
 * In addition, topics from different topic maps which should be merged
 * by their subject or subject indicators are represented as a single
 * structure using the UnifiedTopic class.
 * @see UnifiedTopic
 *
 * @author <a href="mailto:kal@techquila.com">Kal Ahmed</a>
 * @author <a href="harald-kuhn@web.de">Harald Kuhn</a> (getProvider)
 */
public class UnifiedTopicMap implements TopicMap, PropertyChangeListener {

    private List m_topicMaps;

    private String m_name;

    private Locator m_baseLocator;

    private UnifiedIndexManager m_indexManager;

    private VetoableChangeSupport m_vetoableChangeSupport;

    private PropertyChangeSupport m_changeSupport;

    private HashMap m_mvpListeners;

    private TopicMapUtils m_utils;

    private String m_id;

    private LocatorFactory m_locatorFactory;

    private boolean m_notifyEvents;

    private IDUtils m_idUtils;

    public UnifiedTopicMap(Locator baseLocator) {
        this(baseLocator, true);
    }

    public UnifiedTopicMap(Locator baseLocator, boolean notifyEvents) {
        m_topicMaps = new ArrayList();
        m_baseLocator = baseLocator;
        m_indexManager = new UnifiedIndexManager(this);
        m_vetoableChangeSupport = new VetoableChangeSupport(this);
        m_changeSupport = new PropertyChangeSupport(this);
        m_mvpListeners = new HashMap();
        m_utils = new org.tm4j.topicmap.memory.TopicMapUtilsImpl(this);
        m_locatorFactory = new org.tm4j.net.memory.LocatorFactoryImpl();
        m_idUtils = new IDUtils(m_topicMaps);
    }

    public TopicMapProvider getProvider() {
        throw new UnsupportedOperationException("_Unified Backend has no Provider");
    }

    public void addTopicMap(TopicMap tm) throws UnifiedTopicMapException {
        try {
            m_topicMaps.add(tm);
            m_indexManager.add(tm.getIndexManager());
            if (m_notifyEvents) {
                tm.addPropertyChangeListener(this);
            }
        } catch (IndexManagerException ex) {
            throw new UnifiedTopicMapException("Could not initialise topic map indexes.", ex);
        } catch (IndexException ex) {
            throw new UnifiedTopicMapException("Could not open all required topic map indexes.", ex);
        }
    }

    public TopicMap getTopicMap(String id) {
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            TopicMap tm = (TopicMap) it.next();
            if (tm.getBaseLocator().getAddress().equals(id)) {
                return tm;
            }
        }
        return null;
    }

    public Collection getTopicMaps() {
        return Collections.unmodifiableCollection(m_topicMaps);
    }

    public Collection getUnifiedTopics(Collection rawTopics) {
        HashSet ret = new HashSet();
        HashSet processedIDs = new HashSet();
        Iterator topics = rawTopics.iterator();
        while (topics.hasNext()) {
            Topic t = (Topic) topics.next();
            String uid = m_idUtils.getQualifiedID(t);
            if (!processedIDs.contains(uid)) {
                processedIDs.add(uid);
                UnifiedTopic ut = new UnifiedTopic(this, t);
                ret.add(ut);
                Iterator merged = getMergedTopics(ut).iterator();
                while (merged.hasNext()) {
                    processedIDs.add(m_idUtils.getQualifiedID((TopicMapObject) merged.next()));
                }
            }
        }
        return ret;
    }

    public Collection getMergedTopics(UnifiedTopic t) {
        TopicMapObject tmo = getObjectByID(t.getID());
        if ((tmo != null) && (tmo instanceof Topic)) {
            Topic baseTopic = (Topic) getObjectByID(t.getID());
            return getMergedTopics(baseTopic);
        } else {
            throw new IllegalArgumentException("Parameter " + t.getID() + " does not map to a Topic in an underlying topic map.");
        }
    }

    public Collection getMergedTopics(Topic t) {
        Collection ret = new HashSet();
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            TopicMap tm = (TopicMap) it.next();
            if (t.getSubject() != null) {
                Topic mt = tm.getTopicBySubject(t.getSubject());
                if ((mt != null) && (!((mt.getTopicMap().equals(t.getTopicMap())) && mt.equals(t)))) {
                    ret.add(mt);
                }
            }
            Iterator inds = t.getSubjectIndicators().iterator();
            while (inds.hasNext()) {
                Topic mt = tm.getTopicBySubjectIndicator((Locator) inds.next());
                if ((mt != null) && (!((mt.getTopicMap().equals(t.getTopicMap())) && mt.equals(t)))) {
                    ret.add(mt);
                }
            }
        }
        return ret;
    }

    public void addAddedTheme(Topic theme) {
        unsupported();
    }

    public void addExternalRef(Locator ref) {
        unsupported();
    }

    public void addMergeMap(Locator ref, Topic[] addThemes) {
        unsupported();
    }

    public Collection getAddedThemes() {
        unsupported();
        return null;
    }

    public Collection getAssociations() {
        Iterator it = m_topicMaps.iterator();
        CollectionChain ret = new CollectionChain();
        while (it.hasNext()) {
            ret.addCollection(((TopicMap) it.next()).getAssociations());
        }
        return ret;
    }

    public Iterator getAssociationsIterator() {
        IteratorChain ret = new IteratorChain();
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            ret.add(((TopicMap) it.next()).getAssociationsIterator());
        }
        return ret;
    }

    public Locator getBaseLocator() {
        return m_baseLocator;
    }

    public Set getExternalRefs() {
        HashSet ret = new HashSet();
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            ret.addAll(((TopicMap) it.next()).getExternalRefs());
        }
        return ret;
    }

    public TopicMapFactory getFactory() {
        unsupported();
        return null;
    }

    public IndexManager getIndexManager() {
        return m_indexManager;
    }

    public LocatorFactory getLocatorFactory() {
        return m_locatorFactory;
    }

    public Set getMergeMapAddedThemes(Locator mapLoc) {
        unsupported();
        return null;
    }

    public Collection getMergeMapLocators() {
        CollectionChain ret = new CollectionChain();
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            ret.addCollection(((TopicMap) it.next()).getMergeMapLocators());
        }
        return ret;
    }

    public boolean hasMergeMap(Locator loc) {
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            if (((TopicMap) it.next()).hasMergeMap(loc)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return m_name;
    }

    public TopicMapObject getObjectByID(String id) {
        try {
            int tmId = IDUtils.getTopicMapID(id);
            String objId = IDUtils.getObjectID(id);
            TopicMap tm = (TopicMap) m_topicMaps.get(tmId);
            if (tm != null) {
                return tm.getObjectByID(objId);
            } else {
                System.out.println("Cannot find topic map with id " + tmId);
                return null;
            }
        } catch (IllegalArgumentException ex) {
            Iterator it = m_topicMaps.iterator();
            while (it.hasNext()) {
                TopicMapObject rawTMO = ((TopicMap) it.next()).getObjectByID(id);
                if (rawTMO != null) {
                    if (rawTMO instanceof Topic) {
                        return new UnifiedTopic(this, (Topic) rawTMO);
                    } else {
                        return rawTMO;
                    }
                }
            }
            throw ex;
        }
    }

    /**
     * @deprecated
     */
    public TopicMapObject getObjectByResourceLocator(Locator loc) {
        return getObjectBySourceLocator(loc);
    }

    public TopicMapObject getObjectBySourceLocator(Locator loc) {
        TopicMapObject ret = null;
        Iterator it = m_topicMaps.iterator();
        while ((ret == null) && (it.hasNext())) {
            ret = ((TopicMap) it.next()).getObjectBySourceLocator(loc);
        }
        return ret;
    }

    public Collection getObjects() {
        CollectionChain ret = new CollectionChain();
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            TopicMap tm = (TopicMap) it.next();
            ret.addCollection(tm.getObjects());
        }
        return ret;
    }

    /**
     * Returns the raw topics in all of the underlying topic maps.
     * <b>NOTE</b> this method DOES NOT return instances of the
     * UnifiedTopic interface due to performance considerations.
     * To get the set of UnifiedTopics in all of the underlying topic maps,
     * call {@link #getUnifiedTopics()}
     */
    public Set getTopics() {
        return getUnifiedTopics();
    }

    public Iterator getTopicsIterator() {
        Iterator ret = getUnifiedTopics().iterator();
        return ret;
    }

    public Set getUnifiedTopics() {
        HashSet ret = new HashSet();
        HashSet processedIDs = new HashSet();
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            TopicMap tm = (TopicMap) it.next();
            Iterator topics = tm.getTopics().iterator();
            while (topics.hasNext()) {
                Topic t = (Topic) topics.next();
                String uid = m_idUtils.getQualifiedID(t);
                if (!processedIDs.contains(uid)) {
                    processedIDs.add(uid);
                    UnifiedTopic ut = new UnifiedTopic(this, t);
                    ret.add(ut);
                    Iterator merged = getMergedTopics(ut).iterator();
                    while (merged.hasNext()) {
                        processedIDs.add(m_idUtils.getQualifiedID((TopicMapObject) merged.next()));
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Returns a count of the total number of raw topics
     * in all underlying topic maps. This count may be
     * larger than the size of the Collection returned
     * by {@link #getUnifiedTopics()} due to cross-topic-map
     * merges.
     */
    public int getTopicCount() {
        int ret = 0;
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            ret += ((TopicMap) it.next()).getTopicCount();
        }
        return ret;
    }

    /**
     * Returns the UnifiedTopic which wraps the topic object
     * specified by the id string.
     */
    public Topic getTopicByID(String id) {
        TopicMapObject tmo = getObjectByID(id);
        if ((tmo != null) && (tmo instanceof Topic)) {
            return new UnifiedTopic(this, (Topic) tmo);
        }
        return null;
    }

    public Topic getTopicBySubject(Locator subject) {
        Topic ret = null;
        Iterator it = m_topicMaps.iterator();
        while ((ret == null) && (it.hasNext())) {
            ret = new UnifiedTopic(this, ((TopicMap) it.next()).getTopicBySubject(subject));
        }
        return ret;
    }

    public Topic getTopicBySubjectIndicator(Locator subjectIndicator) {
        System.out.println("getTopicBySubjectIndicator(" + subjectIndicator.getAddress() + ")");
        Topic ret = null;
        Iterator it = m_topicMaps.iterator();
        while ((ret == null) && (it.hasNext())) {
            Topic raw = ((TopicMap) it.next()).getTopicBySubjectIndicator(subjectIndicator);
            if (raw != null) {
                System.out.println("Got match: " + raw.getID());
                ret = new UnifiedTopic(this, raw);
            }
        }
        return ret;
    }

    public TopicMapUtils getUtils() {
        return m_utils;
    }

    public boolean hasMergeMap() {
        return false;
    }

    public void removeAssociation(Association assoc) {
        unsupported();
    }

    public void removeTopic(Topic t) {
        unsupported();
    }

    public Topic createTopic(String id) {
        unsupported();
        return null;
    }

    public Association createAssociation(String id) {
        unsupported();
        return null;
    }

    public Association createAssociation(String id, Locator resourceLocator, Topic type, Topic[] themes) throws DuplicateObjectIDException, DuplicateResourceLocatorException, PropertyVetoException {
        unsupported();
        return null;
    }

    public void removeExternalRef(Locator ref) {
        unsupported();
    }

    public void removeMergeMap(Locator mapLoc) {
        unsupported();
    }

    public void setBaseLocator(Locator base) {
        m_baseLocator = base;
    }

    public void setName(String name) {
        m_name = name;
    }

    private void unsupported() {
        throw new UnsupportedOperationException();
    }

    public void setID(String id) {
        m_id = id;
    }

    public String getID() {
        return m_id;
    }

    public boolean equalsByID(TopicMapObject o) {
        return getID().equals(o.getID());
    }

    /**
     * @deprecated use {@link #getSourceLocators() }
     */
    public Locator getResourceLocator() {
        return m_baseLocator;
    }

    public Set getSourceLocators() {
        HashSet ret = new HashSet();
        ret.add(m_baseLocator);
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            ret.addAll(((TopicMap) it.next()).getSourceLocators());
        }
        return Collections.unmodifiableSet(ret);
    }

    /**
     * @deprecated use {@link #addSourceLocator(Locator) }
     */
    public void setResourceLocator(Locator loc) {
        unsupported();
    }

    public void addSourceLocator(Locator loc) {
        unsupported();
    }

    public void removeSourceLocator(Locator loc) {
        unsupported();
    }

    public TopicMap getTopicMap() {
        return this;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        m_changeSupport.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String propName, PropertyChangeListener l) {
        m_changeSupport.addPropertyChangeListener(propName, l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        m_changeSupport.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        m_changeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public void firePropertyChange(String propName, Object oldVal, Object newVal) {
        m_changeSupport.firePropertyChange(propName, oldVal, newVal);
    }

    public void firePropertyChange(PropertyChangeEvent ev) {
        m_changeSupport.firePropertyChange(ev);
    }

    public void addMultiValuePropertyChangeListener(String propName, MultiValuePropertyChangeListener l) {
        Collection listeners = (Collection) m_mvpListeners.get(propName);
        if (listeners == null) {
            listeners = new ArrayList();
            m_mvpListeners.put(propName, listeners);
        }
        listeners.add(l);
    }

    public void addMultiValuePropertyChangeListener(MultiValuePropertyChangeListener l) {
        addMultiValuePropertyChangeListener("__all__", l);
    }

    public void removeMultiValuePropertyChangeListener(MultiValuePropertyChangeListener l) {
        if (m_mvpListeners == null) {
            return;
        }
        Iterator it = m_mvpListeners.keySet().iterator();
        while (it.hasNext()) {
            Collection listeners = (Collection) m_mvpListeners.get(it.next());
            listeners.remove(l);
        }
    }

    public void fireMultiValuePropertyChange(String propName, int operation, Object operand) {
        fireMultiValuePropertyChange(new MultiValuePropertyChangeEvent(this, propName, operation, operand));
    }

    public void fireMultiValuePropertyChange(MultiValuePropertyChangeEvent ev) {
        notify(ev.getPropertyName(), ev);
        notify("__all__", ev);
    }

    private void notify(String propName, MultiValuePropertyChangeEvent ev) {
        Collection listeners = (Collection) m_mvpListeners.get(propName);
        if (listeners != null) {
            Iterator it = listeners.iterator();
            while (it.hasNext()) {
                MultiValuePropertyChangeListener l = (MultiValuePropertyChangeListener) it.next();
                l.multiValuePropertyChange(ev);
            }
        }
    }

    public void addVetoableChangeListener(VetoableChangeListener l) {
        m_vetoableChangeSupport.addVetoableChangeListener(l);
    }

    public void removeVetoableChangeListener(VetoableChangeListener l) {
        m_vetoableChangeSupport.removeVetoableChangeListener(l);
    }

    public void fireVetoableChange(String propertyName, Object oldVal, Object newVal) throws PropertyVetoException {
        m_vetoableChangeSupport.fireVetoableChange(propertyName, oldVal, newVal);
    }

    public void fireVetoableChange(PropertyChangeEvent ev) throws PropertyVetoException {
        m_vetoableChangeSupport.fireVetoableChange(ev);
    }

    public void destroy() {
        unsupported();
    }

    public void propertyChange(PropertyChangeEvent ev) {
        if (ev.getPropertyName().equals("rolesPlayed")) {
            m_changeSupport.firePropertyChange(new PropertyChangeEvent(new UnifiedTopic(this, (Topic) ev.getSource()), ev.getPropertyName(), ev.getOldValue(), ev.getNewValue()));
        }
    }

    /**
	 * Returns the value of the specified property for
	 * one of the underlying topic maps. The choice
	 * of which topic map's property value is returned
	 * is completely arbitrary.
	 */
    public Boolean getProperty(String propertyName) {
        Iterator it = m_topicMaps.iterator();
        while (it.hasNext()) {
            Boolean ret = ((TopicMap) it.next()).getProperty(propertyName);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }

    String getQualifiedID(TopicMapObject tmo) {
        return m_idUtils.getQualifiedID(tmo);
    }
}
