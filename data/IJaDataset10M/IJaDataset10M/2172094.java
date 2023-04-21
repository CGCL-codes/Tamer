package org.unitils.mock.report.impl;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.unitils.mock.Mock;
import org.unitils.mock.core.MockObject;
import static org.unitils.mock.core.MockObject.getCurrentScenario;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the usage of test fields in mock invocations. The names of the fields should be shown in the report (same as for large value).
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class FieldNamesObservedInvocationsReportTest {

    private ObservedInvocationsReport observedInvocationsReport;

    private Mock<TestInterface> testMock;

    private List<String> myTestField = new ArrayList<String>();

    @Before
    public void initialize() {
        observedInvocationsReport = new ObservedInvocationsReport(this);
        testMock = new MockObject<TestInterface>("testMock", TestInterface.class, this);
    }

    @Test
    public void fieldOfTestObjectAsReturnedValue() {
        testMock.returns(myTestField).testMethod(null);
        testMock.getMock().testMethod(null);
        String result = observedInvocationsReport.createReport(getCurrentScenario().getObservedInvocations());
        assertTrue(result.contains("myTestField"));
    }

    @Test
    public void fieldOfTestObjectAsReturnedArgument() {
        testMock.returns(null).testMethod(myTestField);
        testMock.getMock().testMethod(myTestField);
        String result = observedInvocationsReport.createReport(getCurrentScenario().getObservedInvocations());
        assertTrue(result.contains("myTestField"));
    }

    public static interface TestInterface {

        public Object testMethod(Object value);
    }
}
