package org.unitils.mock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.unitils.core.Unitils;
import org.unitils.mock.core.MockObject;
import org.unitils.mock.core.Scenario;
import org.unitils.mock.dummy.DummyObjectUtil;
import static org.unitils.mock.core.proxy.StackTraceUtils.getInvocationStackTrace;

/**
 * @author Filip Neven
 * @author Tim Ducheyne
 * @author Kenny Claes
 */
public class MockUnitils {

    private static Log logger = LogFactory.getLog(MockModule.class);

    public static void assertNoMoreInvocations() {
        MockObject.getCurrentScenario().assertNoMoreInvocations(getInvocationStackTrace(MockUnitils.class, false));
    }

    public static <T> T createDummy(Class<T> type) {
        return DummyObjectUtil.createDummy(type);
    }

    public static void logFullScenarioReport() {
        Scenario scenario = getScenario();
        if (scenario != null) {
            logger.info("\n\n" + scenario.createFullReport());
        }
    }

    public static void logObservedScenario() {
        Scenario scenario = getScenario();
        if (scenario != null) {
            logger.info("\n\nObserved scenario:\n\n" + scenario.createObservedInvocationsReport());
        }
    }

    public static void logDetailedObservedScenario() {
        Scenario scenario = getScenario();
        if (scenario != null) {
            logger.info("\n\nDetailed observed scenario:\n\n" + scenario.createDetailedObservedInvocationsReport());
        }
    }

    public static void logSuggestedAsserts() {
        Scenario scenario = getScenario();
        if (scenario != null) {
            logger.info("\n\nSuggested assert statements:\n\n" + scenario.createSuggestedAssertsReport());
        }
    }

    private static Scenario getScenario() {
        return MockObject.getCurrentScenario();
    }

    private static MockModule getMockModule() {
        return Unitils.getInstance().getModulesRepository().getModuleOfType(MockModule.class);
    }
}
