package barsuift.simLife.time;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.fest.assertions.Assertions.assertThat;

public class FpsCounterTest {

    private FpsCounter counter;

    @BeforeMethod
    protected void setUp() {
        counter = new FpsCounter();
    }

    @AfterMethod
    protected void tearDown() {
        counter = null;
    }

    @Test
    public void tick1() {
        checkInitialValues();
        counter.tick();
        checkInitialValues();
    }

    private void checkInitialValues() {
        assertThat(counter.getExecTime()).isEqualTo(1f);
        assertThat(counter.getAvgExecTime()).isEqualTo(1f);
        assertThat(counter.getFps()).isEqualTo(1);
        assertThat(counter.getAvgFps()).isEqualTo(1);
    }

    @Test
    public void tick10() {
        checkInitialValues();
        tenTicks();
        assertThat(counter.getExecTime()).as("10 ticks should not take more than 10 seconds").isLessThan(10000);
        assertThat(counter.getExecTime()).as("The exec time must always be positive").isGreaterThan(0);
        assertThat(counter.getAvgExecTime()).as("The average time should not have been recomputed").isEqualTo(1f);
        assertThat(counter.getFps()).isGreaterThan(0);
        assertThat(counter.getAvgFps()).as("The average fsp should not have been recomputed").isEqualTo(1);
    }

    @Test
    public void tick100() {
        checkInitialValues();
        for (int i = 0; i < 10; i++) {
            tenTicks();
        }
        assertThat(counter.getExecTime()).as("100 ticks should not take more than 10 seconds").isLessThan(10000);
        assertThat(counter.getExecTime()).as("The exec time must always be positive").isGreaterThan(0);
        assertThat(counter.getAvgExecTime()).as("100 ticks should not take more than 10 seconds").isLessThan(10000);
        assertThat(counter.getAvgExecTime()).as("The exec time must always be positive").isGreaterThan(0);
        assertThat(counter.getFps()).isGreaterThan(0);
        assertThat(counter.getAvgFps()).isGreaterThan(0);
    }

    private void tenTicks() {
        for (int i = 0; i < 10; i++) {
            counter.tick();
        }
    }
}
