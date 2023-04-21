package net.javacrumbs.fluentapi4;

import org.junit.Test;

public class MockTestNoInheritance {

    @Test
    public void testMock() {
        MockFactory mockFactory = new MockFactory() {

            protected void configure() {
                expect(value("a")).andExpect(header("header")).andRespond(withValue("b"));
            }

            ;
        };
        mockFactory.verify();
    }

    @Test
    public void testMockClosure() {
        MockFactory mockFactory = new MockFactory() {

            {
                expect(value("a")).andExpect(header("header")).andRespond(withValue("b"));
            }
        };
        mockFactory.verify();
    }
}
