package asti.util.event.dispatchers;

import java.util.Timer;
import java.util.TimerTask;
import asti.util.event.IEventDispatcher;
import asti.util.event.IEventListener;
import asti.util.interfaces.IDelayed;

/**
 * 
 * @author Asti
 */
public class TimerDispatcher<T extends IDelayed> implements IEventDispatcher<T> {

    private Timer _timer;

    public TimerDispatcher() {
        _timer = new Timer();
    }

    @Override
    public <E extends T> void dispatch(final E event, final IEventListener<? super E> listener) throws Exception {
        final Exception[] exceptions = new Exception[1];
        _timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    listener.onEventFired(event);
                } catch (Exception e) {
                    exceptions[0] = e;
                }
            }
        }, event.getDelay());
        if (null != exceptions[0]) {
            throw exceptions[0];
        }
    }
}
