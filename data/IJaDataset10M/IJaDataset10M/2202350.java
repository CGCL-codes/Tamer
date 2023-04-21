package org.tm4j.tmapi.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import org.tmapi.core.Locator;
import org.tmapi.core.TopicMapObject;

public abstract class TMAPITopicMapObjectStub implements org.tmapi.core.TopicMapObject {

    protected org.tmapi.core.TopicMapObject m_impl = null;

    private org.tm4j.topicmap.TopicMap m_implMap;

    private TMAPITopicMapImpl m_tm;

    protected Set m_locators = null;

    public TMAPITopicMapObjectStub(org.tm4j.topicmap.TopicMap implMap, TMAPITopicMapImpl tm) {
        m_implMap = implMap;
        m_tm = tm;
    }

    public org.tmapi.core.TopicMap getTopicMap() {
        if (m_impl != null) {
            return m_impl.getTopicMap();
        } else {
            return m_tm;
        }
    }

    public void addSourceLocator(org.tmapi.core.Locator loc) {
        if (m_impl != null) {
            m_impl.addSourceLocator(loc);
        } else {
            m_locators.add(loc);
        }
    }

    public Set getSourceLocators() {
        if (m_impl != null) {
            return m_impl.getSourceLocators();
        } else {
            return Collections.unmodifiableSet(m_locators);
        }
    }

    public void removeSourceLocator(org.tmapi.core.Locator loc) {
        if (m_impl != null) {
            m_impl.removeSourceLocator(loc);
        } else {
            m_locators.remove(loc);
        }
    }

    public void setImplementation(org.tmapi.core.TopicMapObject impl) {
        m_impl = impl;
        Iterator it = m_locators.iterator();
        while (it.hasNext()) {
            m_impl.addSourceLocator((Locator) it.next());
        }
    }

    public abstract org.tmapi.core.TopicMapObject getImplementationObject();

    public boolean equals(Object other) {
        if (m_impl != null) {
            if (other instanceof TMAPITopicMapObjectImpl) {
                return m_impl.getObjectId().equals(((TopicMapObject) other).getObjectId());
            } else if (other instanceof TMAPITopicMapObjectStub) {
                if (((TMAPITopicMapObjectStub) other).getImplementationObject() != null) {
                    return m_impl.getObjectId().equals(((TMAPITopicMapObjectStub) other).getImplementationObject().getObjectId());
                }
            }
        }
        return false;
    }

    public int hashCode() {
        if (m_impl != null) {
            return m_impl.getObjectId().hashCode();
        }
        return super.hashCode();
    }
}
