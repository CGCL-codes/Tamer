package com.scottandjoe.texasholdem.networking;

import com.scottandjoe.texasholdem.resources.Utilities;
import java.net.ServerSocket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Scott DellaTorre
 * @author Joe Stein
 */
public abstract class NetworkUtilities {

    private static WriterThread writerThread;

    public static int getNextAvailablePort(int port) {
        try {
            ServerSocket s = new ServerSocket(port);
            s.close();
            return s.getLocalPort();
        } catch (Exception e) {
            return getNextAvailablePort(++port);
        }
    }

    private static synchronized void prepareWriterThread() {
        if (writerThread == null) {
            writerThread = new WriterThread();
            writerThread.start();
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    try {
                        writerThread.finishSendingMessages();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            });
        }
    }

    public static void sendMessage(EncryptedMessageWriter writer, Message message, boolean encrypted) {
        prepareWriterThread();
        writerThread.addMessage(writer, message, encrypted);
    }

    public static void sendMessageAndWait(EncryptedMessageWriter writer, Message message, boolean encrypted) throws InterruptedException {
        prepareWriterThread();
        writerThread.addMessageAndWait(writer, message, encrypted);
    }
}

class WriterThread extends Thread {

    private BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<Task>();

    public WriterThread() {
        super("Writer Thread");
        setDaemon(true);
    }

    void addMessage(EncryptedMessageWriter writer, Message message, boolean encrypted) {
        taskQueue.add(new Task(Thread.currentThread(), writer, message, encrypted));
    }

    void addMessageAndWait(EncryptedMessageWriter writer, Message message, boolean encrypted) throws InterruptedException {
        Task task = new Task(Thread.currentThread(), writer, message, encrypted);
        taskQueue.add(task);
        task.waitForCompletion();
    }

    void finishSendingMessages() throws InterruptedException {
        Task task = new Task((Object) null);
        taskQueue.add(task);
        task.waitForCompletion();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Task task = taskQueue.take();
                if (task.info[0] != null) {
                    ((EncryptedMessageWriter) task.info[1]).writeMessage((Message) task.info[2], (Boolean) task.info[3]);
                    Utilities.log((Thread) task.info[0], Utilities.LOG_MESSAGES_OUT, (Message) task.info[2]);
                }
                task.complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
