package net.sf.xspecs.test.oomatron;

import junit.framework.TestCase;
import net.sf.xspecs.oomatron.ConstraintDescriber;

public class ConstraintsDescriberTest extends TestCase implements TestData {

    static final Object ALICE = objectIdentifiedAs("alice");

    static final Object BOB = objectIdentifiedAs("bob");

    private ConstraintDescriber collection;

    public void setUp() {
        collection = new ConstraintDescriber();
    }

    public void testDisplaysNothingWhenFirstCreated() {
        String description = descriptionOf(collection);
        assertEquals("should have an empty description", "", description);
    }

    public void testDisplaysAddedConstraintsInAlphabeticalOrder() {
        Object alice = objectIdentifiedAs("alice");
        Object bob = objectIdentifiedAs("bob");
        collection.add(alice, F_METHOD);
        collection.add(bob, G_METHOD);
        collection.add(alice, G_METHOD);
        collection.add(bob, F_METHOD);
        String description = descriptionOf(collection);
        Assert.assertIncludesInOrder("should contain constrained methods in alphabetical order", new String[] { "alice.f()", "alice.g()", "bob.f()", "bob.g()" }, description);
    }

    public void testDisplaysDuplicateConstraintsOnlyOnce() {
        collection.add(ALICE, F_METHOD);
        collection.add(ALICE, F_METHOD);
        String description = descriptionOf(collection);
        assertEquals("should contain alice.f() only once", "alice.f()", description.trim());
    }

    public void testDisplaysParametersWithTypesAndSynthesisedNames() {
        collection.add(ALICE, H_METHOD);
        String description = descriptionOf(collection);
        assertEquals("alice.h(int arg1, java.lang.String arg2, java.util.List arg3)", description.trim());
    }

    private String descriptionOf(final ConstraintDescriber collection) {
        return collection.describeTo(new StringBuffer()).toString();
    }

    private static Object objectIdentifiedAs(final String name) {
        return new Object() {

            public String toString() {
                return name;
            }
        };
    }
}
