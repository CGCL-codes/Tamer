package smsalert.proxy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import javax.mail.Message;

public class ReaderTask implements Runnable {

    BlockingQueue<Message> queue = null;

    public ReaderTask(BlockingQueue<Message> queue) {
        this.queue = queue;
    }

    public void run() {
    }
}
