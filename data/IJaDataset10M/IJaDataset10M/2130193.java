package no.eirikb.bomberman.shared;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import no.eirikb.bomberman.shared.img.ImageDB;

/**
 *
 * @author eirikb
 */
public class DB implements Serializable {

    private ImageDB idb;

    public ImageDB getIdb() {
        return idb;
    }

    public void setIdb(ImageDB idb) {
        this.idb = idb;
    }

    public static DB load() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("db"));
            DB db = (DB) in.readObject();
            in.close();
            return db;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
