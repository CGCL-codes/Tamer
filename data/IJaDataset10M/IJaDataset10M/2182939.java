package dplayer.queue.events;

import dplayer.queue.QueueManager;

/**
 * Event dispatcher.
 */
public class EventManager extends QueueManager {

    private static final EventManager sInstance = new EventManager();

    public static void addListener(final Class eventClass, final EventListener listener) {
        assert sInstance != null;
        sInstance.addQueueListener(eventClass, listener);
    }

    public static void removeListener(final Class eventClass, final EventListener listener) {
        assert sInstance != null;
        sInstance.removeQueueListener(eventClass, listener);
    }

    public static void send(final Event event) {
        assert sInstance != null;
        sInstance.enq(event);
    }

    public static void sendException(final String text, final Throwable exception) {
        send(new ExceptionEvent(text, exception));
    }

    public static void sendException(final String text) {
        sendException(text, null);
    }

    public static void sendException(final Throwable exception) {
        sendException(null, exception);
    }

    public static void sendGuiEvent(final GuiEvent event) {
        send(event);
    }

    public static boolean readAndDispatch() {
        assert sInstance != null;
        return sInstance.deqAndDispatch();
    }
}
