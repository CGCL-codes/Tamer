package org.tmapiutils.query.tolog.predicates.test;

import java.util.ArrayList;
import org.tmapiutils.query.tolog.parser.Variable;
import org.tmapiutils.query.tolog.predicates.BaseLocatorPredicate;
import org.tmapiutils.query.tolog.utils.VariableSet;
import org.tmapi.core.TopicMap;

/**
 *
 * @author Kal Ahmed (kal[at]techquila.com)
 * @author Lars Heuer (heuer[at]semagia.com)
 */
public class BaseLocatorPredicateTest extends PredicateTestBase {

    /**
     * @param name
     */
    public BaseLocatorPredicateTest(String name) {
        super(name);
    }

    public String getTestMap() {
        return "[test]";
    }

    public void createTestMap(TopicMap tm) {
    }

    public Class getPredicateClass() {
        return BaseLocatorPredicate.class;
    }

    public void testOpenMatch() throws Exception {
        ArrayList params = new ArrayList();
        params.add(new Variable("A"));
        m_predicate.setParameters(params);
        VariableSet out = m_predicate.matches(params, m_context);
        assertNotNull(out);
        assertEquals(1, out.getColumns().size());
        assertEquals(1, out.getRows().size());
        assertEquals(m_tm.getBaseLocator(), out.getRow(0).get(0));
    }

    public void testClosedMatch() throws Exception {
        ArrayList params = new ArrayList();
        params.add(new Variable("A"));
        m_predicate.setParameters(params);
        params.set(0, m_tm.getBaseLocator());
        VariableSet out = m_predicate.matches(params, m_context);
        assertNotNull(out);
        assertEquals(1, out.getColumns().size());
        assertEquals(1, out.getRows().size());
        assertEquals(m_tm.getBaseLocator(), out.getRow(0).get(0));
        params.set(0, m_tm.getBaseLocator().resolveRelative("foobar"));
        out = m_predicate.matches(params, m_context);
        assertNotNull(out);
        assertEquals(1, out.getColumns().size());
        assertEquals(0, out.getRows().size());
    }
}
