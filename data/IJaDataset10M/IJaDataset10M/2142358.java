package org.eclipse.epsilon.fptc.simulation.includes.application;

import org.junit.BeforeClass;
import org.junit.Test;

public class ApplicationTriple extends ApplicationUnitTest {

    @BeforeClass
    public static void setup() throws Throwable {
        applicationUnitTest(Fixture.TRIPLE, "(fault foo) -> (fault bar, fault baz, fault qux)");
        model.setVariable("connection1", "Connection.all.at(0)");
        model.setVariable("connection2", "Connection.all.at(1)");
        model.setVariable("connection3", "Connection.all.at(2)");
    }

    @Test
    public void applicationCausedChange() {
        model.assertTrue("changed");
    }

    @Test
    public void connection1ShouldContainOneLiteral() {
        model.assertEquals(1, "connection1.literals.size()");
    }

    @Test
    public void connection1ShouldContainBar() {
        model.assertEquals("bar", "connection1.literals.first.type");
    }

    @Test
    public void connection2ShouldContainOneLiteral() {
        model.assertEquals(1, "connection2.literals.size()");
    }

    @Test
    public void connection2ShouldContainBaz() {
        model.assertEquals("baz", "connection2.literals.first.type");
    }

    @Test
    public void connection3ShouldContainOneLiteral() {
        model.assertEquals(1, "connection3.literals.size()");
    }

    @Test
    public void connection3ShouldContainQux() {
        model.assertEquals("qux", "connection3.literals.first.type");
    }

    @Test
    public void modelContainsNoExtraLiterals() {
        model.assertEquals(7, "Literal.all.size()");
    }
}
