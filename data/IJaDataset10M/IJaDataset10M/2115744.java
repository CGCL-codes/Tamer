package net.ontopia.topicmaps.cmdlineutils.statistics;

import java.util.*;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.impl.basic.*;

/**
 * 
 */
public class NoTypeCount {

    private Collection returntopics, returnoccurs, returnassocs;

    private TopicMapIF tm;

    public NoTypeCount(TopicMapIF tm) {
        this.tm = tm;
        returntopics = new ArrayList();
        returnoccurs = new ArrayList();
        returnassocs = new ArrayList();
    }

    /**
   * Traverses the topicmap and finds the topics, associations and
   * occurrences that has no types.
   */
    public void traverse() throws NullPointerException {
        Collection topics = tm.getTopics();
        Iterator it = topics.iterator();
        while (it.hasNext()) {
            TopicIF topic = (TopicIF) it.next();
            Collection topictypes = topic.getTypes();
            Collection occurrences = topic.getOccurrences();
            if (topictypes.size() == 0) {
                returntopics.add(topic);
            }
            Iterator itr = occurrences.iterator();
            while (itr.hasNext()) {
                OccurrenceIF o = (OccurrenceIF) itr.next();
                TopicIF t = o.getType();
                if (t == null) {
                    returnoccurs.add(o);
                }
            }
        }
        Collection assocs = tm.getAssociations();
        it = assocs.iterator();
        while (it.hasNext()) {
            AssociationIF a = (AssociationIF) it.next();
            TopicIF t = a.getType();
            if (t == null) {
                returnassocs.add(a);
            }
        }
    }

    /**
   * Returns a Collection of Topics which have no type.
   */
    public Collection getNoTypeTopics() {
        return returntopics;
    }

    /**
   * Returns a Collection of Occurrences which have no type.
   */
    public Collection getNoTypeOccurrences() {
        return returnoccurs;
    }

    /**
   * Returns a Collection of Associations which have no type.
   */
    public Collection getNoTypeAssociations() {
        return returnassocs;
    }
}
