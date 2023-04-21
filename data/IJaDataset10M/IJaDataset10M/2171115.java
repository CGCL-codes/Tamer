package net.sourceforge.jcoupling.peer.native2;

import net.sourceforge.jcoupling.bus.server.callout.IntegratorCallerback;
import net.sourceforge.jcoupling.bus.server.CommunicatorID;
import net.sourceforge.jcoupling.bus.dao.LockException;
import net.sourceforge.jcoupling.peer.property.RequestKey;
import net.sourceforge.jcoupling.peer.Message;
import net.sourceforge.jcoupling.peer.interaction.MessageID;
import net.sourceforge.jcoupling.peer.interaction.ResponseContainer;
import java.util.List;
import java.util.Collections;
import java.net.Socket;
import java.net.SocketException;
import java.io.*;

/**
 * @author Lachlan Aldred
 */
public class JCouplingIntegratorCallBack implements IntegratorCallerback {

    public void callbackReceiveReady(CommunicatorID communicatorID, RequestKey key, List<Message> messages) {
        String serverport = communicatorID.toString();
        String server = serverport.substring(0, serverport.indexOf(':'));
        Message[] msgs = messages.toArray(new Message[messages.size()]);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeChars("hello world");
            byte[] byteBuffer = baos.toByteArray();
            int servPort = Integer.parseInt(serverport.substring(serverport.indexOf(':')));
            Socket socket = null;
            socket = new Socket(server, servPort);
            System.out.println("Connected to server...sending echo string");
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(byteBuffer);
            int totalBytesRcvd = 0;
            int bytesRcvd;
            while (totalBytesRcvd < byteBuffer.length) {
                if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd)) == -1) throw new SocketException("Connection close prematurely");
                totalBytesRcvd += bytesRcvd;
            }
            System.out.println("Received: " + new String(byteBuffer));
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void callbackSent(List<MessageID> messages, List<ResponseContainer> responses) {
    }

    public boolean lockCommunicator(CommunicatorID communicatorID, RequestKey requestKey) throws LockException {
        return false;
    }

    public void unlockCommunicator(CommunicatorID communicatorID, RequestKey requestKey) {
    }
}
