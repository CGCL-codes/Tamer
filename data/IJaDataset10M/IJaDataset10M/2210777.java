package net.sf.lavalamp.device;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import java.util.ArrayList;
import java.util.List;
import net.sf.lavalamp.Pause;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BlinkTest {

    List<String> events = new ArrayList<String>();

    @Mock
    Pause pause;

    @Mock
    Device device;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBlink() throws InterruptedException {
        Blink blink = new Blink();
        blink.setPause(pause);
        blink.perform(events, device);
        assertThat(events.size(), is(equalTo(10)));
        verify(pause, times(10)).sleep(500);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfInterrupted() throws InterruptedException {
        Blink blink = new Blink();
        doThrow(new InterruptedException()).when(pause).sleep(500);
        blink.setPause(pause);
        blink.perform(events, device);
    }
}
