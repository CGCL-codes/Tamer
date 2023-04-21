package edu.arizona.arid.akshen;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import edu.arizona.arid.akshen.util.Http;

public class MessageServiceThread extends Thread {

    private MessageServiceDelegate delegate;

    private DataInputStream in;

    public MessageServiceThread(MessageServiceDelegate delegate, InputStream in) {
        this.delegate = delegate;
        this.in = new DataInputStream(in);
    }

    public void run() {
        while (true) {
            try {
                String response = Http.get("http://home.sumin.us/labs/recall/post.php");
                delegate.messageReceived(response);
            } catch (IOException e) {
                delegate.exceptionRaised(e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    delegate.exceptionRaised(ie);
                }
            }
        }
    }
}
