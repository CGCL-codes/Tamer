package test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import communication.*;

public class FileTester {

    public static void main(String[] args) {
        try {
            FileAcceptorConnection acceptor = new FileAcceptorConnection(4444);
            FileSenderConnection sender = new FileSenderConnection(InetAddress.getLocalHost(), 4444);
            acceptor.acceptFile("testFile");
            sender.sendFile("photos.txt");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
