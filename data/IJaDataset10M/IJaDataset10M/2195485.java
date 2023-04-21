package org.tmapiutils.query.tolog.predicates.test;

import java.util.ArrayList;
import java.util.Iterator;
import org.tmapiutils.query.tolog.parser.Variable;
import org.tmapiutils.query.tolog.predicates.ValueLikePredicate;
import org.tmapiutils.query.tolog.utils.VariableSet;
import org.tmapi.core.Locator;
import org.tmapi.core.TopicMapObject;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Topic;
import org.tmapi.core.Occurrence;
import org.tmapi.core.TopicName;

/**
 *
 * @author Kal Ahmed (kal[at]techquila.com)
 * @author Lars Heuer (heuer[at]semagia.com)
 */
public class ValueLikePredicateTest extends PredicateTestBase {

    /**
     * @param name
     */
    public ValueLikePredicateTest(String name) {
        super(name);
    }

    public String getTestMap() {
        return "[test = \"test\"]\n {test, description, [[This is a test.]]}\n{test, description, [[ Another occurrence ]]}";
    }

    public void createTestMap(TopicMap tm) {
        Topic test = tm.createTopic();
        Topic description = tm.createTopic();
        test.createTopicName("test", null);
        test.createOccurrence("This is a test.", description, null);
        test.createOccurrence("Another occurrence", description, null);
        registerObject("test", test);
        registerObject("description", description);
    }

    public Class getPredicateClass() {
        return ValueLikePredicate.class;
    }

    public void testOpenStringMatch() throws Exception {
        ArrayList params = new ArrayList();
        Variable a = new Variable("A");
        Variable b = new Variable("B");
        params.add(a);
        params.add(b);
        m_predicate.setParameters(params);
        params.set(1, "test");
        VariableSet out = m_predicate.matches(params, m_context);
        assertNotNull(out);
        assertEquals(2, out.getRows().size());
        Iterator it = out.getColumn(a).iterator();
        while (it.hasNext()) {
            TopicMapObject tmo = (TopicMapObject) it.next();
            assertTrue((tmo instanceof TopicName) || (tmo instanceof Occurrence));
        }
    }

    public void testClosedMatch() throws Exception {
        ArrayList params = new ArrayList();
        Variable a = new Variable("A");
        Variable b = new Variable("B");
        params.add(a);
        params.add(b);
        m_predicate.setParameters(params);
        Iterator it = ((Topic) getObjectById("test")).getOccurrences().iterator();
        while (it.hasNext()) {
            Occurrence o = (Occurrence) it.next();
            params.set(0, o);
            params.set(1, "test");
            VariableSet out = m_predicate.matches(params, m_context);
            assertNotNull(out);
            if (o.getValue().equals("This is a test.")) {
                assertEquals(1, out.getRows().size());
                assertEquals(o, out.getRow(0).get(0));
            } else {
                assertEquals(0, out.getRows().size());
            }
        }
    }
}
