package mediathek.filme;

import javax.swing.event.EventListenerList;

public class MediathekTimer implements Runnable {

    private final int WARTEZEIT = 1000;

    private EventListenerList listeners = new EventListenerList();

    public void addAdListener(TimerListener listener) {
        listeners.add(TimerListener.class, listener);
    }

    public void reset() {
        notifyReset();
    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                Thread.sleep(WARTEZEIT);
            } catch (InterruptedException e) {
            }
            notifyTakt();
        }
    }

    private void notifyTakt() {
        for (TimerListener l : listeners.getListeners(TimerListener.class)) {
            l.ping();
        }
    }

    private void notifyReset() {
        for (TimerListener l : listeners.getListeners(TimerListener.class)) {
            l.reset();
        }
    }
}
