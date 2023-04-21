package junit;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class AASlowTest {

    @Test
    public void slow1() throws InterruptedException {
        Thread.sleep(800);
    }
}
