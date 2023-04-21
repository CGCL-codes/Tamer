package org.deri.iris.utils;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.utils.TermMatchingAndSubstitution;

public class TestTermMatchingAndSubstitution extends TestCase {

    public void testConstantMatch() {
        String someString = "test";
        ITerm body = Factory.TERM.createString(someString);
        ITerm relation = Factory.TERM.createString(someString);
        Map<IVariable, ITerm> variableMap = new HashMap<IVariable, ITerm>();
        assertTrue(TermMatchingAndSubstitution.match(body, relation, variableMap));
        assertEquals(variableMap.size(), 0);
    }

    public void testConstantMisMatch() {
        ITerm body = Factory.TERM.createString("test");
        ITerm relation = Factory.TERM.createString("different");
        Map<IVariable, ITerm> variableMap = new HashMap<IVariable, ITerm>();
        assertFalse(TermMatchingAndSubstitution.match(body, relation, variableMap));
        assertEquals(variableMap.size(), 0);
    }

    public void testVariable() {
        String someString = "test";
        String variableName = "X";
        ITerm body = Factory.TERM.createVariable(variableName);
        ITerm relation = Factory.TERM.createString(someString);
        Map<IVariable, ITerm> variableMap = new HashMap<IVariable, ITerm>();
        assertTrue(TermMatchingAndSubstitution.match(body, relation, variableMap));
        assertEquals(variableMap.size(), 1);
        ITerm matchedTerm = variableMap.get(body);
        assertEquals(relation, matchedTerm);
    }

    public void testHeadSubstitution() {
        IVariable X = Factory.TERM.createVariable("X");
        IVariable Y = Factory.TERM.createVariable("Y");
        ITerm x = Factory.TERM.createString("x");
        ITerm y = Factory.TERM.createString("y");
        ITerm j = Factory.TERM.createConstruct("j", y);
        Map<IVariable, ITerm> variableMap = new HashMap<IVariable, ITerm>();
        variableMap.put(X, x);
        variableMap.put(Y, j);
        ITerm h = Factory.TERM.createConstruct("h", Y, X);
        ITerm f = Factory.TERM.createConstruct("f", X, h);
        ITerm g = Factory.TERM.createConstruct("g", f);
        ITuple headTuple = Factory.BASIC.createTuple(g);
        ITuple substitutedHead = TermMatchingAndSubstitution.substituteVariablesInToTuple(headTuple, variableMap);
        ITerm y2 = Factory.TERM.createString("y");
        ITerm j2 = Factory.TERM.createConstruct("j", y2);
        ITerm x2 = Factory.TERM.createString("x");
        ITerm h2 = Factory.TERM.createConstruct("h", j2, x2);
        ITerm f2 = Factory.TERM.createConstruct("f", x2, h2);
        ITerm g2 = Factory.TERM.createConstruct("g", f2);
        assertEquals(substitutedHead.iterator().next(), g2);
    }
}
