package cx.ath.mancel01.dependencyshot.test.event;

import cx.ath.mancel01.dependencyshot.api.event.EventListener;
import java.util.concurrent.CountDownLatch;
import javax.inject.Singleton;

/**
 *
 * @author Mathieu ANCELIN
 */
@Singleton
public class MyListener implements EventListener<MyEvent> {

    private int calls = 0;

    private CountDownLatch latch = new CountDownLatch(EventTest.NBR);

    @Override
    public void onEvent(MyEvent evt) {
        calls++;
        latch.countDown();
    }

    public int getCalls() {
        return calls;
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
