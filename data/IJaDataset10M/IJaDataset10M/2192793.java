package org.tm4j.topicmap.utils;

import org.tm4j.net.Locator;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Topic;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Contains common utilities for working with Topics.
 * These utilities will work correctly with Topics from any
 * backend and the comparison methods
 * (e.g. {@link #areEquivalent(Topic, Topic)} will work
 * in comparing topics from different providers.
 */
public class TopicUtils {

    /**
     * Determines topic equivalency. Two topics are considered equal
     * if one or more of the following conditions hold
     * <ol>
     *   <li>The topics are from the same parent topic map and
     *       have already been merged.</li>
     *   <li>The resourceLocator of the topics are equal</li>
     *   <li>The resourceLocator of one of the topics is equal to on of
     *       the subjectIndicators of the other topic.</li>
     *   <li>The subject of the topics are equal</li>
     *   <li>The topics have one or more subjectIndicators in common</li>
     *   <li>The topics have one or more base names with the same
     *       base name string in equivalent scopes.</li>
     * </ol>
     */
    public static boolean areEquivalent(Topic topic1, Topic topic2) {
        if (topic1.getTopicMap().equals(topic2.getTopicMap())) {
            return topic1.getBaseTopic().equals(topic2.getBaseTopic());
        }
        HashSet srcLocs = new HashSet();
        Iterator t1locs = topic1.getSourceLocators().iterator();
        while (t1locs.hasNext()) {
            Locator l = (Locator) t1locs.next();
            srcLocs.add(l.getNotation() + ":" + l.getAddress());
        }
        t1locs = topic1.getSubjectIndicators().iterator();
        while (t1locs.hasNext()) {
            Locator l = (Locator) t1locs.next();
            srcLocs.add(l.getNotation() + ":" + l.getAddress());
        }
        Iterator t2locs = topic2.getSourceLocators().iterator();
        while (t2locs.hasNext()) {
            Locator l = (Locator) t2locs.next();
            String s = l.getNotation() + ":" + l.getAddress();
            if (srcLocs.contains(s)) {
                return true;
            }
        }
        t2locs = topic2.getSubjectIndicators().iterator();
        while (t2locs.hasNext()) {
            Locator l = (Locator) t2locs.next();
            String s = l.getNotation() + ":" + l.getAddress();
            if (srcLocs.contains(s)) {
                return true;
            }
        }
        if ((topic1.getSubject() != null) && (topic2.getSubject() != null) && (topic1.getSubject().equals(topic2.getSubject()))) {
            return true;
        }
        Iterator it1 = topic1.getSubjectIndicators().iterator();
        while (it1.hasNext()) {
            Locator l = (Locator) it1.next();
            if (topic2.getSubjectIndicators().contains(l)) {
                return true;
            }
        }
        it1 = topic2.getSubjectIndicators().iterator();
        while (it1.hasNext()) {
            Locator l = (Locator) it1.next();
            if (topic1.getSubjectIndicators().contains(l)) {
                return true;
            }
        }
        it1 = topic1.getNames().iterator();
        while (it1.hasNext()) {
            BaseName bn1 = (BaseName) it1.next();
            Iterator it2 = topic2.getNames().iterator();
            while (it2.hasNext()) {
                BaseName bn2 = (BaseName) it2.next();
                if ((bn1.getData() != null) && (bn2.getData() != null) && (bn1.getData().equals(bn2.getData()))) {
                    return ScopeUtils.areEquivalent(bn1.getScope(), bn2.getScope());
                }
            }
        }
        return false;
    }
}
