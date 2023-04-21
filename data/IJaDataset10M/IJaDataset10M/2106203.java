package org.equivalence.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.equivalence.common.FileRepository;

/**
 * process a request made of the server by a client
 * each request can be opened in its own thread
 * @author gcconner
 *
 */
class UpdateRequest implements Runnable {

    private FileRepository fr;

    private Socket socket;

    private UpdateServer updateServer;

    public UpdateRequest(UpdateServer updateServer, Socket socket, FileRepository fr) {
        this.updateServer = updateServer;
        this.socket = socket;
        this.fr = fr;
    }

    public void run() {
        try {
            updateServer.serverEvent("Processing request from " + socket.getInetAddress() + "...\r\n");
            processRequest();
            updateServer.serverEvent("request processed " + socket.getInetAddress() + "\r\n");
        } catch (Exception e) {
            updateServer.serverEvent("Error processing request from " + socket.getInetAddress() + ": " + e.getMessage() + "\r\n");
        } finally {
            try {
                String address = socket.getInetAddress().toString();
                socket.close();
                updateServer.serverEvent("connection to " + address + " closed\r\n");
            } catch (IOException e) {
                updateServer.serverEvent("Error closing socket\r\n");
            }
        }
    }

    /**
	 * process the user request
	 * @throws Exception
	 */
    private void processRequest() throws Exception {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        ObjectInputStream objIn = new ObjectInputStream(in);
        FileRepository repos = (FileRepository) objIn.readObject();
        FileRepositoryDiff diff = new FileRepositoryDiff(fr, repos);
        FileSender transfer = new FileSender();
        for (String s : diff.getAdd()) {
            transfer.addFile(s);
        }
        for (String s : diff.getReplace()) {
            transfer.addFile(s);
        }
        for (String s : diff.getRemove()) {
            transfer.deleteFile(s);
        }
        updateServer.serverEvent("transferring data to " + socket.getInetAddress() + "...\r\n");
        transfer.sendFiles(out);
        updateServer.serverEvent("completed transfer to " + socket.getInetAddress() + "\r\n");
        objIn.close();
        in.close();
        out.close();
    }
}
