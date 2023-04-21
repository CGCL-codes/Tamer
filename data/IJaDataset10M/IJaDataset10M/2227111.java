package tests;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import stories.AStoryWithPendingStepAndFailingStrategy;

public class AStoryWithPendingStepAndFailingStrategyTest {

    @Test
    public void testStory() {
        Result result = JUnitCore.runClasses(AStoryWithPendingStepAndFailingStrategy.class);
        assertThat(result.getFailureCount()).isEqualTo(1);
        assertThat(result.getFailures().get(0).getDescription().getDisplayName()).startsWith("Scenario: a scenario with pending step and FailingUponPendingStep strategy is visualized as failed");
    }
}
