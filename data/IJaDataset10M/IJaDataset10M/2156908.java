package de.shandschuh.jaolt.core.server;

public class LocalHostSocketListener {

    private static SocketListeningThread socketListeningThread;

    public static void startSocketListener() {
        if (socketListeningThread == null) {
            socketListeningThread = new SocketListeningThread();
            socketListeningThread.start();
        }
    }

    public static void stopSocketListener() {
        if (socketListeningThread != null) {
            socketListeningThread.terminate();
            socketListeningThread = null;
        }
    }

    public static boolean socketListenerRuns() {
        return socketListeningThread != null;
    }

    public static void main(String[] args) {
        startSocketListener();
    }
}
