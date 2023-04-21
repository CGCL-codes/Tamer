package communication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileAcceptorConnection {

    private ServerConnection serverSocket;

    public FileAcceptorConnection(int port) throws IOException {
        serverSocket = new ServerConnection(port);
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public void acceptFile(String fileName) {
        try {
            SocketConnection socket = serverSocket.accept();
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String fileReceived;
            fileReceived = in.readUTF();
            String fileExt = fileReceived.substring(fileReceived.indexOf("."), fileReceived.length());
            BufferedInputStream buffIn = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream buffOt = new BufferedOutputStream(new FileOutputStream(new File(fileName + fileExt)));
            byte[] b = new byte[4096];
            int bytesRead;
            while ((bytesRead = buffIn.read(b)) != -1) {
                buffOt.write(b, 0, bytesRead);
            }
            buffOt.flush();
            buffOt.close();
            buffIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
