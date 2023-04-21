package communication.v4;

import java.io.*;
import java.net.*;

public class SinglethreadedSendingService {

    Socket mySocket;

    OutputStream oStream;

    ObjectOutput ooStream;

    public void send(String destination, int destinationPort, Object msg) {
        try {
            mySocket = new Socket(destination, destinationPort);
            oStream = mySocket.getOutputStream();
            ooStream = new ObjectOutputStream(oStream);
            ooStream.writeObject(msg);
            ooStream.close();
            oStream.close();
            mySocket.close();
        } catch (Exception e) {
            System.out.println("SingleThreadedSender: caught Exception, might be wrong Hostname/Port (" + e.getMessage() + ")");
        }
    }
}
