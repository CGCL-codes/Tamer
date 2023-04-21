package data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author 5pHiNxX
 */
public class ServerHandler {

    String fileSeparator = System.getProperty("file.separator");

    public Servers loadServers() {
        ObjectInputStream objIn = null;
        Servers server = null;
        try {
            File theFile = new File("config" + this.fileSeparator + "servers.fe");
            objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(theFile)));
            server = (Servers) objIn.readObject();
        } catch (ClassNotFoundException ex) {
            if (server == null) {
                server = Servers.getInstance();
            }
        } catch (IOException ex) {
            if (server == null) {
                server = Servers.getInstance();
            }
        } finally {
            try {
                if (objIn != null) {
                    objIn.close();
                }
            } catch (IOException ex) {
                if (server == null) {
                    server = Servers.getInstance();
                }
            }
        }
        return server;
    }

    public boolean saveServers(Servers theServers) {
        ObjectOutputStream objOut = null;
        try {
            File theFile = new File("config" + this.fileSeparator + "servers.fe");
            File theDir = new File(theFile.getAbsolutePath().substring(0, theFile.getAbsolutePath().length() - theFile.getName().length()));
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            if (!theFile.exists()) {
                theFile.createNewFile();
            }
            objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(theFile)));
            objOut.writeObject(theServers);
            return true;
        } catch (IOException ex) {
            return false;
        } finally {
            try {
                if (objOut != null) {
                    objOut.close();
                }
            } catch (IOException ex) {
                return false;
            }
        }
    }
}
