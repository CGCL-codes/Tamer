package framework.core.client.transfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import framework.core.client.ClientConfig;
import framework.core.client.hashtools.Hash;
import framework.core.client.hashtools.Sharing;

public class TransferServer implements Runnable {

    private ServerSocket ssock;

    private Socket s;

    private ClientConfig cfg;

    public TransferServer() {
        this.s = null;
        this.ssock = null;
        try {
            cfg = ClientConfig.getClientConfig();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        System.err.println("\n:: TransferServer: Creazione ServerSocket");
        System.err.println(":: < Press [Enter] to Continue... >");
        try {
            ssock = new ServerSocket(Integer.parseInt(cfg.port));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        while (true) {
            try {
                s = ssock.accept();
            } catch (IOException e) {
                System.out.println("Connessione Fallita");
                System.exit(2);
            }
            System.out.println("Connessione da: " + s);
            DataInputStream is = null;
            PrintStream os = null;
            try {
                BufferedInputStream ib = new BufferedInputStream(s.getInputStream());
                is = new DataInputStream(ib);
                BufferedOutputStream ob = new BufferedOutputStream(s.getOutputStream());
                os = new PrintStream(ob, false);
                String fn = new String(is.readLine());
                System.out.println("File Richiesto: " + fn);
                File name = new File(cfg.sharing[0] + "/" + fn);
                if (!name.exists()) {
                    os.println("File non trovato");
                    os.flush();
                    os.close();
                    is.close();
                    s.close();
                } else {
                    DataInputStream Is = new DataInputStream(new FileInputStream(cfg.sharing[0] + "/" + fn));
                    String r = null;
                    while ((r = Is.readLine()) != null) {
                        os.println(r);
                    }
                    os.flush();
                    os.close();
                    is.close();
                    s.close();
                }
                os.flush();
                os.close();
                is.close();
                s.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
