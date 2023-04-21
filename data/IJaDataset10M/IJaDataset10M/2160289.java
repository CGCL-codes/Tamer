package org.tm4j.tmapi.core;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.tm4j.net.Locator;
import org.tm4j.tmapi.helpers.Wrapper;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.MergedTopicSubjectClashException;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Occurrence;
import org.tmapi.core.ModelConstraintException;
import org.tmapi.core.SubjectLocatorClashException;
import org.tmapi.core.TMAPIRuntimeException;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicName;

public class TMAPITopicImpl extends TMAPITopicMapObjectImpl implements org.tmapi.core.Topic {

    private org.tm4j.topicmap.Topic m_obj;

    public TMAPITopicImpl(org.tm4j.topicmap.Topic obj, TMAPITopicMapImpl tm) {
        super(obj, tm);
        m_obj = obj;
    }

    public org.tm4j.topicmap.Topic getWrapped() {
        return m_obj;
    }

    public Set getTopicNames() {
        return Wrapper.wrap(m_obj.getNames(), m_tm);
    }

    public TopicName createTopicName(String baseNameString, Topic type, Collection scope) {
        if (type != null) {
            throw new UnsupportedOperationException("TM4J does not support typed topic names.");
        } else {
            return createTopicName(baseNameString, scope);
        }
    }

    public TopicName createTopicName(String baseNameString, Collection scope) {
        checkUpdateAllowed();
        try {
            org.tm4j.topicmap.BaseName bn = m_obj.createName(null);
            bn.setData(baseNameString);
            if ((scope != null) && !scope.isEmpty()) {
                Wrapper.setScopeThemes(bn, scope);
            }
            return Wrapper.wrap(bn, m_tm);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void remove() {
        checkUpdateAllowed();
        try {
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        super.remove();
    }

    public Set getOccurrences() {
        return Wrapper.wrap(m_obj.getOccurrences(), m_tm);
    }

    public org.tmapi.core.Occurrence createOccurrence(org.tmapi.core.Locator resourceRef, org.tmapi.core.Topic type, Collection scope) {
        try {
            org.tm4j.topicmap.Occurrence occ = createOccurrence(type, scope);
            if (resourceRef != null) {
                occ.setDataLocator(Wrapper.unwrap(resourceRef));
            }
            return Wrapper.wrap(occ, m_tm);
        } catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    public org.tmapi.core.Occurrence createOccurrence(String resourceData, org.tmapi.core.Topic type, Collection scope) {
        try {
            org.tm4j.topicmap.Occurrence occ = createOccurrence(type, scope);
            if (resourceData != null) {
                occ.setData(resourceData);
            }
            return Wrapper.wrap(occ, m_tm);
        } catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    /**
     * @param type
     * @param scope
     * @return
     * @throws DuplicateObjectIDException
     * @throws PropertyVetoException
     */
    private org.tm4j.topicmap.Occurrence createOccurrence(org.tmapi.core.Topic type, Collection scope) throws DuplicateObjectIDException, PropertyVetoException {
        checkUpdateAllowed();
        org.tm4j.topicmap.Occurrence occ = m_obj.createOccurrence(null);
        if (type != null) {
            occ.setType(Wrapper.unwrap(type));
        }
        if ((scope != null) && !scope.isEmpty()) {
            Wrapper.setScopeThemes(occ, scope);
        }
        return occ;
    }

    public Set getSubjectLocators() {
        Locator rawLoc = m_obj.getSubject();
        return rawLoc == null ? Collections.EMPTY_SET : Collections.singleton(Wrapper.wrap(rawLoc, m_tm));
    }

    public void addSubjectLocator(org.tmapi.core.Locator loc) {
        checkUpdateAllowed();
        Locator currLoc = m_obj.getSubject();
        Locator rawLoc = Wrapper.unwrap(loc);
        if (currLoc != null) {
            if (currLoc.equals(rawLoc)) {
                return;
            } else {
                throw new ModelConstraintException(this, "Topics can only have one subject locator under XTM 1.0");
            }
        }
        try {
            m_obj.setSubject(Wrapper.unwrap(loc));
        } catch (PropertyVetoException e) {
            throw new TMAPIRuntimeException("Addition of subject locator " + loc.getReference() + " was vetoed.", e);
        }
    }

    public Set getSubjectIdentifiers() {
        return Wrapper.wrap(m_obj.getSubjectIndicators(), m_tm);
    }

    public void addSubjectIdentifier(org.tmapi.core.Locator loc) {
        checkUpdateAllowed();
        try {
            m_obj.addSubjectIndicator(Wrapper.unwrap(loc));
        } catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    public void removeSubjectIdentifier(org.tmapi.core.Locator _obj) {
        checkUpdateAllowed();
        org.tm4j.net.Locator obj = Wrapper.unwrap(_obj);
        ArrayList objlist = new ArrayList(m_obj.getSubjectIndicators());
        if (!objlist.remove(obj)) {
            return;
        }
        try {
            m_obj.setSubjectIndicators((org.tm4j.net.Locator[]) objlist.toArray(new org.tm4j.net.Locator[objlist.size()]));
        } catch (Exception ex) {
            throw new RuntimeException(ex.toString());
        }
    }

    public Set getTypes() {
        return Wrapper.wrap(m_obj.getTypes(), m_tm);
    }

    public void addType(org.tmapi.core.Topic type) {
        checkUpdateAllowed();
        try {
            m_obj.addType(Wrapper.unwrap(type));
        } catch (java.beans.PropertyVetoException ex) {
            throw Wrapper.wrap(ex);
        }
    }

    public void removeType(org.tmapi.core.Topic _obj) {
        checkUpdateAllowed();
        org.tm4j.topicmap.Topic obj = Wrapper.unwrap(_obj);
        ArrayList objlist = new ArrayList(m_obj.getTypes());
        if (!objlist.remove(obj)) {
            return;
        }
        try {
            m_obj.setTypes((org.tm4j.topicmap.Topic[]) objlist.toArray(new org.tm4j.topicmap.Topic[objlist.size()]));
        } catch (java.beans.PropertyVetoException ex) {
            throw Wrapper.wrap(ex);
        }
    }

    public Set getRolesPlayed() {
        Set ret = new HashSet();
        Iterator it = m_obj.getRolesPlayed().iterator();
        while (it.hasNext()) {
            Member m = (Member) it.next();
            ret.add(Wrapper.wrap(m, m_obj, m_tm));
        }
        return ret;
    }

    public void mergeIn(Topic t) {
        checkUpdateAllowed();
        org.tm4j.topicmap.Topic toMerge = Wrapper.unwrap(t);
        try {
            m_obj.addMergedTopic(toMerge);
        } catch (MergedTopicSubjectClashException e) {
            throw new SubjectLocatorClashException(this, t, "Cannot merge topics with different non-null subject locators.");
        }
    }

    public Set getReified() {
        HashSet ret = new HashSet();
        Iterator it = m_obj.getSubjectIndicators().iterator();
        while (it.hasNext()) {
            ret.add(m_obj.getTopicMap().getObjectBySourceLocator((org.tm4j.net.Locator) it.next()));
        }
        return Wrapper.wrap(ret, m_tm);
    }

    public void removeSubjectLocator(org.tmapi.core.Locator loc) {
        checkUpdateAllowed();
        if (m_obj.getSubject() == null) return;
        Locator rawLoc = Wrapper.unwrap(loc);
        if (rawLoc.equals(m_obj.getSubject())) {
            return;
        }
        try {
            m_obj.setSubject(null);
        } catch (PropertyVetoException e) {
            throw new TMAPIRuntimeException("Removal of subject locator was vetoed.", e);
        }
    }

    public Topic getReifier() {
        throw new UnsupportedOperationException("A topic cannot reify a topic");
    }
}
