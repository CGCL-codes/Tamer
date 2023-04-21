package org.tmapiutils.impexp.xtm;

import org.tmapi.core.*;

/**
 * @author  Stefan Lischke
 */
public class TMAPIResolver implements Resolver {

    private java.util.HashMap subjIndT = new java.util.HashMap();

    private java.util.HashMap topics = new java.util.HashMap();

    public Topic resolveOrCreateTopic(TopicMap tmap, String xmlid) {
        Topic topic;
        Topic o = (Topic) topics.get(xmlid);
        if (o == null) {
            topic = tmap.createTopic();
            Locator locxmlid = tmap.createLocator(tmap.getBaseLocator().getReference() + "#" + xmlid);
            try {
                topic.addSourceLocator(locxmlid);
            } catch (DuplicateSourceLocatorException dsl) {
            }
            topics.put(xmlid, topic);
        } else {
            topic = o;
        }
        return topic;
    }

    public Topic resolveOrCreateTopicBySubjectIndicator(TopicMap tmap, String subjInd) {
        Topic topic = (Topic) this.subjIndT.get(subjInd);
        if (topic == null) {
            topic = tmap.createTopic();
            Locator loc = tmap.createLocator(subjInd);
            topic.addSubjectIdentifier(loc);
            this.subjIndT.put(subjInd, topic);
        }
        return topic;
    }

    public Association createAssociation(TopicMap tmap, String xmlid) {
        return (tmap.createAssociation());
    }

    public AssociationRole createAssociationRole(Association a, Topic player, Topic role, String xmlid) {
        return (a.createAssociationRole(player, role));
    }

    public Occurrence createOccurrence(Topic t, String xmlid) {
        return (t.createOccurrence((String) null, null, null));
    }

    public TopicName createTopicName(Topic t, String xmlid) {
        return (t.createTopicName(null, null));
    }
}
