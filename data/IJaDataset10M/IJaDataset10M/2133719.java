package distributed;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This thread executes a command that arrived at the PeerCommunicator.  
 * 
 * <pre>
 * <b>Domain:</b>
 *      client: Socket             -- the socket from the PeerCommunicator used to communicate with the original sending PeerCommunicator.
 *      ois:    ObjectInputStream  -- the input stream on which we receive the command
 *      oos:    ObjectOutputStream -- the output stream on which we return any results (including exceptions).
 *      
 *      <b>Invariant</b>
 *      client &ne; null AND ois &ne; null AND oss &ne; null
 * </pre>
 * @author Scott Woodfield
 */
public class ServerThread extends Thread {

    /**
	 * The socket over which we communicate with the sending PeerCommunicator.
	 */
    private Socket client = null;

    /**
	 * The ObjectInputStream from which we read the command.
	 */
    private ObjectInputStream ois = null;

    /**
	 * The ObjectOutputStream over which we send the result of executing the command.
	 */
    private ObjectOutputStream oos = null;

    /**
	 * The ServerThread constructor.  It establishes the connection with the sending PeerCommunicator.
	 * 
	 * @param clientSocket the socket from the PeerCommunicator used to communicate with the PeerCommunicator.
	 * 
	 * @pre clientSocket &ne; null
	 * @post a connection is established with the process on the other end of the clientSocket.
	 */
    public ServerThread(Socket clientSocket) {
        client = clientSocket;
        try {
            ois = new ObjectInputStream(client.getInputStream());
            oos = new ObjectOutputStream(client.getOutputStream());
        } catch (Exception e1) {
            try {
                client.close();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            return;
        }
    }

    /**
	 * Reads the command and executes it.  If it is a synchronous command it returns a result.
	 * 
	 * @pre the object to be read from the ObjectInputStream is a command.
	 * @post The command is executed on the specified object or class (see Command description).  If the command is synchronous then the result returned from executing the command
	 * is returned to remote PeerCommunicator.
	 */
    public void run() {
        Command command = null;
        try {
            command = (Command) ois.readObject();
            if (command.isSynchronous()) {
                Object result = command.execute();
                oos.writeObject(result);
                oos.flush();
                oos.close();
                ois.close();
                client.close();
            } else {
                oos.close();
                ois.close();
                client.close();
                command.execute();
            }
        } catch (Exception e) {
        }
    }
}
