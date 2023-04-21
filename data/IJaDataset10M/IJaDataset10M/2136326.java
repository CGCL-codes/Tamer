package org.tm4j.tolog.test;

import org.tm4j.tolog.QueryEvaluator;
import org.tm4j.tolog.TologResultsSet;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * @deprecated from 0.9.5 use the pacakge org.tm4j.tologx
 */
public class QETestInvalidIDs extends QETestBase {

    public QETestInvalidIDs(String name) {
        super(name);
    }

    public void testInvalidRolePlayerID() {
        try {
            TopicMap tm = getTopicMap("qetest1");
            QueryEvaluator qe = new QueryEvaluator(tm);
            TologResultsSet rs = qe.evaluate("select $C from instance-of($C, invalidtopicid) ?");
            assertEquals("Expected invalid query to return no results.", rs.getNumRows(), 0);
        } catch (RuntimeException ex) {
            assertTrue(ex.getMessage().endsWith("invalidtopicid"));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexpected exception: " + ex.toString());
        }
    }

    public void testLocatePredicateByID() {
        try {
            TopicMap tm = createTopicMap();
            Topic assocType = tm.createTopic("assoc");
            Topic rp1 = tm.createTopic("player1");
            Topic rp2 = tm.createTopic("player2");
            Topic role1 = tm.createTopic("role1");
            Topic role2 = tm.createTopic("role2");
            Association assoc = tm.createAssociation(null);
            assoc.setType(assocType);
            Member m1 = assoc.createMember(null);
            m1.setRoleSpec(role1);
            m1.addPlayer(rp1);
            Member m2 = assoc.createMember(null);
            m2.setRoleSpec(role2);
            m2.addPlayer(rp2);
            QueryEvaluator qe = new QueryEvaluator(tm);
            TologResultsSet rs = qe.evaluate("select $A, $B from assoc($A:role1, $B:role2) ?");
            assertEquals("Expected query to return a single row.", rs.getNumRows(), 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Unexepected exception: " + ex.toString());
        }
    }
}
