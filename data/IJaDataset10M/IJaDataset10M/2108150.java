package ncclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Carl Berglund
 */
public class FileListener implements Runnable {

    private ServerSocket serverSocket;

    private GUI gui;

    private Socket fileSocket;

    private Socket textSocket;

    private Thread fileReciever;

    private BufferedWriter writer;

    private BufferedReader reader;

    /** Creates a new instance of FileListener */
    public FileListener(GUI gui, Socket textSocket) {
        this.gui = gui;
        this.textSocket = textSocket;
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(7331);
            for (; ; ) {
                fileSocket = serverSocket.accept();
                fileReciever = new Thread(new FileReciever(gui, fileSocket, textSocket));
                fileReciever.start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
