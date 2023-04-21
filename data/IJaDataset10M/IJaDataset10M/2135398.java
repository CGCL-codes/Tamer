package photobook.network;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import photobook.data.model.Photo;

public class TestFrame extends JFrame {

    private MulticastSender myServer = null;

    private MulticastListenner myClient = null;

    private TcpListener myListener = null;

    private TcpSender mySender = null;

    public TestFrame() {
        myClient = new MulticastListenner();
        Thread t2 = new Thread(myClient);
        t2.start();
        myServer = new MulticastSender(new HelloMessage("127.0.0.1", "flo"));
        Thread t1 = new Thread(myServer);
        t1.start();
    }

    public void showMessage() {
    }

    public static void main(String[] args) {
        TestFrame frame = new TestFrame();
        System.out.println("DEBUG: start");
    }
}
