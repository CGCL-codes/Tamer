package remote.control;

import org.junit.Test;
import static org.junit.Assert.*;

public class MoteStatusTest {

    @Test
    public void string() {
        assertEquals(MoteStatus.RUNNING.toString(), "running");
    }
}
