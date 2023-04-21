package net.stickycode.mockwire.junit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import net.stickycode.mockwire.Autowirable;
import net.stickycode.mockwire.Controlled;
import net.stickycode.mockwire.Mockable;
import net.stickycode.mockwire.UnderTest;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockwireRunner.class)
public class RunnerTest {

    @Controlled
    Mockable mockable;

    @UnderTest
    Autowirable autowirable;

    @Test
    public void test() {
        assertThat(mockable).isNotNull();
        assertThat(autowirable).isNotNull();
    }
}
