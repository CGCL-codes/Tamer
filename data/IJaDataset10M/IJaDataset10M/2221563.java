package org.tmapiutils.query.tolog.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import junit.framework.TestCase;
import org.tmapiutils.query.tolog.parser.Sorting;
import org.tmapiutils.query.tolog.parser.Variable;
import org.tmapi.core.TopicMapSystemFactory;
import org.tmapi.core.TopicMapSystem;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Topic;
import org.tmapi.core.TopicName;

/**
 *
 * @author Kal Ahmed (kal[at]techquila.com)
 * @author Lars Heuer (heuer[at]semagia.com)
 */
public class SortingTest extends TestCase {

    private TopicMapSystem _tmSystem;

    /**
     * Constructor for SortingTest.
     * @param arg0
     */
    public SortingTest(String arg0) throws Exception {
        super(arg0);
        _tmSystem = TopicMapSystemFactory.newInstance().newTopicMapSystem();
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SortingTest.class);
    }

    public void testSortIntegers() throws Exception {
        ArrayList cols = new ArrayList();
        cols.add(new Variable("A"));
        cols.add(new Variable("B"));
        ArrayList rows = new ArrayList();
        ArrayList row1 = new ArrayList();
        row1.add(new Integer(2));
        row1.add(new Integer(1));
        rows.add(row1);
        ArrayList row2 = new ArrayList();
        row2.add(new Integer(1));
        row2.add(new Integer(2));
        rows.add(row2);
        ArrayList row3 = new ArrayList();
        row3.add(new Integer(1));
        row3.add(new Integer(1));
        rows.add(row3);
        Sorting sorting = new Sorting();
        sorting.addSort(new Variable("A"));
        sorting.addSort(new Variable("B"));
        sorting.sort(cols, rows);
        assertEquals(row3, rows.get(0));
        assertEquals(row2, rows.get(1));
        assertEquals(row1, rows.get(2));
    }

    public void testSortTopics() throws Exception {
        TopicMap tm = _tmSystem.createTopicMap("http://tmapiutils.org/test/tolog1.0/test/SortTopics");
        Topic t1 = tm.createTopic();
        Topic t2 = tm.createTopic();
        Topic t3 = tm.createTopic();
        String[] objectIds = new String[3];
        objectIds[0] = t1.getObjectId();
        objectIds[1] = t2.getObjectId();
        objectIds[2] = t3.getObjectId();
        Arrays.sort(objectIds);
        HashMap topics = new HashMap();
        topics.put(t1.getObjectId(), t1);
        topics.put(t2.getObjectId(), t2);
        topics.put(t3.getObjectId(), t3);
        ArrayList cols = new ArrayList();
        cols.add(new Variable("A"));
        ArrayList rows = new ArrayList();
        ArrayList row1 = new ArrayList();
        row1.add(topics.get(objectIds[2]));
        rows.add(row1);
        ArrayList row2 = new ArrayList();
        row2.add(topics.get(objectIds[0]));
        rows.add(row2);
        ArrayList row3 = new ArrayList();
        row3.add(topics.get(objectIds[1]));
        rows.add(row3);
        Sorting sorting = new Sorting();
        sorting.addSort(new Variable("A"));
        sorting.sort(cols, rows);
        sorting.setSortAscending(true);
        assertEquals(row2, rows.get(0));
        assertEquals(row3, rows.get(1));
        assertEquals(row1, rows.get(2));
        sorting.setSortAscending(false);
        sorting.sort(cols, rows);
        assertEquals(row1, rows.get(0));
        assertEquals(row3, rows.get(1));
        assertEquals(row2, rows.get(2));
    }

    public void testSortNames() throws Exception {
        TopicMap tm = _tmSystem.createTopicMap("http://tmapiutils.org/test/tolog1.0/test/SortNames");
        Topic t1 = tm.createTopic();
        TopicName bn1 = t1.createTopicName("Topic C", null);
        Topic t2 = tm.createTopic();
        TopicName bn2 = t2.createTopicName("Topic B", null);
        Topic t3 = tm.createTopic();
        TopicName bn3 = t3.createTopicName("Topic A", null);
        ArrayList cols = new ArrayList();
        cols.add(new Variable("A"));
        ArrayList rows = new ArrayList();
        ArrayList row1 = new ArrayList();
        row1.add(bn3);
        rows.add(row1);
        ArrayList row2 = new ArrayList();
        row2.add(bn1);
        rows.add(row2);
        ArrayList row3 = new ArrayList();
        row3.add(bn2);
        rows.add(row3);
        Sorting sorting = new Sorting();
        sorting.addSort(new Variable("A"));
        sorting.sort(cols, rows);
        assertEquals(row1, rows.get(0));
        assertEquals(row3, rows.get(1));
        assertEquals(row2, rows.get(2));
    }
}
