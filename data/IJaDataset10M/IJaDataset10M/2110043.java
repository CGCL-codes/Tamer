package net.sourceforge.purrpackage.test.instrument.a;

public class OldSchoolFooAJUnitTest extends junit.framework.TestCase {

    public void setUp() {
    }

    public void testSomething() {
        new FooATestNG().fromSameOldSchool();
    }

    public void tearDown() {
        new FooAJUnit().fromSameOldSchool();
    }
}
