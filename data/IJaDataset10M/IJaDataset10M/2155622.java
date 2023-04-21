package org.fest.swing.timing;

import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;
import org.fest.swing.timing.Timeout;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Tests for <code>{@link Timeout}</code>.
 *
 * @author Yvonne Wang
 */
public class TimeoutTest {

    @Test
    public void shouldReturnDurationPassedWhenCreated() {
        Timeout timeout = Timeout.timeout(2000);
        assertThat(timeout.duration()).isEqualTo(2000);
    }

    @Test
    public void shouldReturnDurationWhenCreatedWithTimeUnit() {
        Timeout timeout = Timeout.timeout(3, TimeUnit.SECONDS);
        assertThat(timeout.duration()).isEqualTo(3000);
    }
}
