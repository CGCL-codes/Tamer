package framework.core.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import framework.core.FileServerInterface;
import framework.core.client.hashtools.Hash;
import framework.core.client.hashtools.Sharing;
import framework.core.client.transfer.TransferServer;
import framework.core.fileservice.FileServiceInterface;

public class ClientFile implements Runnable {

    private ClientConfig conf;

    private String host;

    private FileServerInterface f;

    private String casa;

    private InetAddress myip;

    public ClientFile() {
        try {
            conf = ClientConfig.getClientConfig();
            host = conf.host + ":" + conf.rmiport;
            f = (FileServerInterface) Naming.lookup("//" + host + "/FileServer");
            myip = InetAddress.getLocalHost();
        } catch (Exception e) {
        }
    }

    public void run() {
        try {
            PrintStream out = new PrintStream(new FileOutputStream("fileout.share"));
            Sharing s = new Sharing();
            s.setRecursive(true);
            s.setShareDir(conf.sharing);
            s.printOut(out);
            Hash.getInstance().kill();
        } catch (FileNotFoundException e) {
            System.out.println("Impossibile creare il file di output!!!");
        }
        try {
            String filename = new String();
            String filehash = new String();
            FileServiceInterface service = (FileServiceInterface) f.getInstance();
            BufferedReader in = new BufferedReader(new FileReader("fileout.share"));
            while (true) {
                filename = in.readLine();
                filehash = in.readLine();
                if (filename.equals("")) break;
                service.sendFile(conf.username, myip.getHostAddress(), filename, filehash);
            }
        } catch (RemoteException e) {
        } catch (Exception i) {
        }
        new Thread(new TransferServer()).start();
    }
}
