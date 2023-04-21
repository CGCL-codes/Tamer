package jgcp.common.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @Date 31/05/2009
 * @author Jie Zhao (288654)
 * @version 1.0
 * @param <T>
 */
public class FileSender<T extends Serializable> extends Thread {

    private T object = null;

    private ServerSocket ss = null;

    private String filename = null;

    public FileSender(T t, String filename) {
        try {
            this.filename = filename;
            ss = new ServerSocket(0);
            System.out.println("File Sender started on port:" + ss.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        object = t;
    }

    public int getPort() {
        if (ss != null) {
            return ss.getLocalPort();
        }
        return 0;
    }

    @Override
    public void run() {
        try {
            Socket s = ss.accept();
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(object);
            oos.flush();
            byte[] buffer = new byte[65534];
            s.setSendBufferSize(65534);
            File file = new File(filename);
            if (!file.canRead() || !file.exists()) return;
            FileInputStream fis = new FileInputStream(file);
            try {
                while (true) {
                    int length = fis.read(buffer);
                    if (length == -1) break;
                    os.write(buffer, 0, length);
                }
            } finally {
                System.out.println("Send end.");
                os.flush();
                os.close();
                fis.close();
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
