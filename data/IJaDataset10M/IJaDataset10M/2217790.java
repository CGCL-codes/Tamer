package com.ragstorooks.testrr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class RunnerTest {

    private static final String FAILED_THE_MOD_6_TEST = "Failed the mod 6 test";

    private Runner runner;

    private MyRPScenario scenario1;

    private MyRPScenario scenario2;

    private MyRPScenario scenario3;

    private Map<ScenarioBase, Integer> scenarioWeightings;

    private ScheduledExecutorService executorService;

    private Collection<String> successes;

    private Collection<String> failures;

    private class MyRPScenario extends ScenarioBase {

        private int runCount = 0;

        @Override
        public void run(String scenarioId) {
            runCount++;
            if ((runCount % 6) == 0) {
                failures.add(scenarioId);
                getScenarioListener().scenarioFailure(scenarioId, FAILED_THE_MOD_6_TEST);
            } else {
                successes.add(scenarioId);
                getScenarioListener().scenarioSuccess(scenarioId);
            }
        }

        public int getRunCount() {
            return runCount;
        }
    }

    @Before
    public void before() {
        scenario1 = new MyRPScenario();
        scenario2 = new MyRPScenario();
        scenario3 = new MyRPScenario();
        successes = new ArrayList<String>();
        failures = new ArrayList<String>();
        scenarioWeightings = new HashMap<ScenarioBase, Integer>();
        scenarioWeightings.put(scenario1, 1);
        scenarioWeightings.put(scenario2, 1);
        scenarioWeightings.put(scenario3, 1);
        executorService = mock(ScheduledExecutorService.class);
        doAnswer(new Answer<Object>() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Runnable) invocation.getArguments()[0]).run();
                return null;
            }
        }).when(executorService).execute(isA(Runnable.class));
        runner = new Runner();
        runner.setNumberOfConcurrentStarts(1);
        runner.setNumberOfRuns(100);
        runner.setScenarioWeightings(scenarioWeightings);
        runner.setScheduledExecutorService(executorService);
    }

    @Test
    public void testNumberOfRuns() throws Exception {
        runner.run();
        assertEquals(100, scenario1.getRunCount() + scenario2.getRunCount() + scenario3.getRunCount());
    }

    @Test
    public void testWeightingsEqual() throws Exception {
        runner.run();
        assertEquals(100, scenario1.getRunCount() + scenario2.getRunCount() + scenario3.getRunCount());
        assertTrue(isWeightingApproximatelyOk(100, scenario1.getRunCount(), 33.3));
        assertTrue(isWeightingApproximatelyOk(100, scenario2.getRunCount(), 33.3));
        assertTrue(isWeightingApproximatelyOk(100, scenario3.getRunCount(), 33.3));
    }

    @Test
    public void testWeightingsUnequal() throws Exception {
        scenarioWeightings.put(scenario1, 10);
        scenarioWeightings.put(scenario2, 6);
        runner.run();
        assertEquals(100, scenario1.getRunCount() + scenario2.getRunCount() + scenario3.getRunCount());
        assertTrue(isWeightingApproximatelyOk(100, scenario1.getRunCount(), 59));
        assertTrue(isWeightingApproximatelyOk(100, scenario2.getRunCount(), 35));
        assertTrue(isWeightingApproximatelyOk(100, scenario3.getRunCount(), 6));
    }

    @Test
    public void testScenariosAreRunUsingExecutorService() throws Exception {
        executorService = mock(ScheduledExecutorService.class);
        runner.setScheduledExecutorService(executorService);
        runner.run();
        verify(executorService, times(100)).execute(isA(Runnable.class));
    }

    @Test
    public void testScenarioSuccesses() throws Exception {
        runner.run();
        assertTrue(Math.abs(82.3 - runner.getSuccessRate()) < 10);
        for (String scenarioId : runner.getScenarioSuccesses().keySet()) {
            assertTrue(String.format("Unexpected scenario %s was succesful", scenarioId), successes.contains(scenarioId));
            successes.remove(scenarioId);
        }
        assertEquals(0, successes.size());
    }

    @Test
    public void testScenarioFailures() throws Exception {
        runner.run();
        assertTrue(Math.abs(82.3 - runner.getSuccessRate()) < 10);
        for (String scenarioId : runner.getScenarioFailures().keySet()) {
            assertTrue(String.format("Unexpected scenario %s failed", scenarioId), failures.contains(scenarioId));
            failures.remove(scenarioId);
        }
        assertEquals(0, failures.size());
    }

    @Test
    public void testFailureMessage() throws Exception {
        runner.run();
        for (ScenarioResult result : runner.getScenarioFailures().values()) {
            assertEquals(FAILED_THE_MOD_6_TEST, result.getMessage());
        }
    }

    @Test
    public void testCoolDownPeriod() throws Exception {
        runner.setNumberOfRuns(0);
        runner.setCoolDownPeriod(520);
        long startTime = System.currentTimeMillis();
        runner.run();
        assertTrue(System.currentTimeMillis() - startTime >= 500);
    }

    @Test
    public void testTotalTimeTaken() throws Exception {
        runner.setCoolDownPeriod(0);
        runner.run();
        assertTrue(runner.getTotalRunTimeMilliSeconds() > 0);
    }

    @Test
    public void testUseDeterministicNumberOfRuns() throws Exception {
        runner.setNumberOfRuns(30);
        runner.setUseDeterministicNumberOfRuns(true);
        runner.run();
        assertEquals(30, scenario1.getRunCount() + scenario2.getRunCount() + scenario3.getRunCount());
        assertEquals(10, scenario1.getRunCount());
        assertEquals(10, scenario2.getRunCount());
        assertEquals(10, scenario3.getRunCount());
    }

    private boolean isWeightingApproximatelyOk(int totalRuns, int scenarioRuns, double weighting) {
        double trueWeighting = 100.0 * scenarioRuns / totalRuns;
        return Math.abs(trueWeighting - weighting) <= 10;
    }
}
