package org.scribble.util;

import junit.framework.TestCase;

public class ScopeTest extends TestCase {

    public void testCopyConstructor1() {
        Scope scope = new Scope();
        scope.setLocatedRole("hello");
        Scope copy = new Scope(scope);
        if (copy.getLocatedRole() == null) {
            fail("Located role should not be null '" + copy.getLocatedRole() + "'");
        }
        if (copy.getLocatedRole().equals(scope.getLocatedRole()) == false) {
            fail("Located role should be '" + scope.getLocatedRole() + "', but was: " + copy.getLocatedRole());
        }
    }

    public void testCopyConstructor2() {
        Scope scope = new Scope();
        scope.setState("name1", "value1");
        Scope copy = new Scope(scope);
        if (copy.getState("name1") == null) {
            fail("No state found for 'name1'");
        }
        if (copy.getState("name1").equals(scope.getState("name1")) == false) {
            fail("State for 'name1' was '" + copy.getState("name1") + "', but was expecting: " + scope.getState("name1"));
        }
    }
}
