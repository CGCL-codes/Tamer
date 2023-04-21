package org.tm4j.tmapi.index.core;

import java.util.Collection;
import java.util.HashSet;
import org.tm4j.tmapi.core.TMAPITopicMapImpl;
import org.tm4j.tmapi.helpers.Wrapper;
import org.tm4j.tmapi.index.TMAPIIndexFlagsImpl;
import org.tm4j.topicmap.index.IndexException;
import org.tm4j.topicmap.index.basic.TopicTypesIndex;
import org.tmapi.core.HelperObjectConfigurationException;
import org.tmapi.core.Locator;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicMap;
import org.tmapi.index.IndexFlags;
import org.tmapi.index.TMAPIIndexException;
import org.tmapi.index.core.TopicsIndex;
import org.tmapi.core.TopicMapSystem;

/**
 * @author Kal
 * 
 * Describe TMAPITopicsIndexImpl here.
 */
public class TMAPITopicsIndexImpl implements TopicsIndex, TopicMapSystem.ConfigurableHelperObject {

    private TopicTypesIndex m_tti;

    private org.tm4j.topicmap.TopicMap m_tm;

    private TMAPITopicMapImpl m_tmapiTM;

    public void configure(TopicMap tm) throws HelperObjectConfigurationException {
        if (!(tm instanceof TMAPITopicMapImpl)) {
            throw new HelperObjectConfigurationException("Unable to create index on TMAPI topic map that is not managed by TM4J.");
        }
        m_tm = (org.tm4j.topicmap.TopicMap) ((TMAPITopicMapImpl) tm).getWrapped();
        m_tmapiTM = (TMAPITopicMapImpl) tm;
        try {
            m_tti = (TopicTypesIndex) m_tm.getIndexManager().getIndex(TopicTypesIndex.class);
        } catch (Exception e) {
            throw new HelperObjectConfigurationException("Unable to access TM4J TopicTypesIndex implementation.", e);
        }
    }

    public Collection getTopicTypes() {
        return Wrapper.wrap(m_tti.getTopicTypes(), m_tmapiTM);
    }

    public Collection getTopicsByType(Topic type) {
        return Wrapper.wrap(m_tti.getTopicsOfType(Wrapper.unwrap(type)), m_tmapiTM);
    }

    public Collection getTopicsByTypes(Topic[] types, boolean matchAll) {
        org.tm4j.topicmap.Topic[] rawTypes = new org.tm4j.topicmap.Topic[types.length];
        for (int i = 0; i < types.length; i++) {
            rawTypes[i] = Wrapper.unwrap(types[i]);
        }
        if (matchAll) {
            return Wrapper.wrap(m_tti.getTopicsOfTypes(rawTypes), m_tmapiTM);
        } else {
            HashSet ret = new HashSet();
            for (int i = 0; i < rawTypes.length; i++) {
                ret.addAll(m_tti.getTopicsOfType(rawTypes[i]));
            }
            return Wrapper.wrap(ret, m_tmapiTM);
        }
    }

    public Topic getTopicBySubjectLocator(Locator loc) {
        org.tm4j.net.Locator rawLoc = Wrapper.unwrap(loc);
        org.tm4j.topicmap.Topic ret = m_tm.getTopicBySubject(rawLoc);
        if (ret != null) {
            return Wrapper.wrap(ret, m_tmapiTM);
        } else {
            return null;
        }
    }

    public Topic getTopicBySubjectIdentifier(Locator loc) {
        org.tm4j.net.Locator rawLoc = Wrapper.unwrap(loc);
        org.tm4j.topicmap.Topic ret = m_tm.getTopicBySubjectIndicator(rawLoc);
        if (ret != null) {
            return Wrapper.wrap(ret, m_tmapiTM);
        } else {
            return null;
        }
    }

    public void open() throws TMAPIIndexException {
        try {
            if (!m_tti.isOpen()) {
                m_tti.open();
            }
        } catch (IndexException ex) {
            throw new TMAPIIndexException("Error opening underlying TM4J index.");
        }
    }

    public void close() throws TMAPIIndexException {
        try {
            if (m_tti != null) m_tti.close();
        } catch (IndexException ex) {
            throw new TMAPIIndexException("Error closing underlying TM4J index.");
        }
    }

    public boolean isOpen() throws TMAPIIndexException {
        return ((m_tti != null) && (m_tti.isOpen()));
    }

    public void reindex() throws TMAPIIndexException {
        if (m_tti == null) {
            open();
        }
    }

    public IndexFlags getFlags() throws TMAPIIndexException {
        return new TMAPIIndexFlagsImpl(true);
    }
}
