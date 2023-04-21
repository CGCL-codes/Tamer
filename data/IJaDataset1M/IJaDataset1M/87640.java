package br.gov.component.demoiselle.report.builder;

import static org.junit.Assert.*;
import org.junit.Test;

public class ReportBuilderFactoryTest {

    @Test
    public void testConstructor() {
        assertNotNull(new ReportBuilderFactory());
    }

    @Test
    public void testCreate() {
        IReportBuilder rb = ReportBuilderFactory.create();
        assertNotNull(rb);
        assertTrue(rb instanceof ReportBuilder);
    }

    @Test
    public void testInstantiationExceptionCreate() {
        try {
            ReportBuilderFactory.create(MockReportBuilder.class);
            fail("testCreateError");
        } catch (Exception e) {
            assertEquals("InstantiationException for class [br.gov.component.demoiselle.report.builder.ReportBuilderFactoryTest$MockReportBuilder]", e.getMessage());
        }
    }

    private class MockReportBuilder extends ReportBuilder {
    }
}
