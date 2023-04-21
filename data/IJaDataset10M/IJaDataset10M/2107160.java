package infinitewisdom.model.save;

import infinitewisdom.model.GameWorld;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Default java binary serialization. 
 * @author Levente Zsiros
 *
 */
public class SaveLoadDefaultBinary implements SaveLoadIF {

    @Override
    public GameWorld load(String fileName) {
        FileInputStream fis;
        try {
            fis = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fis);
            GameWorld gw2 = (GameWorld) in.readObject();
            gw2.getFreshlyCreatedUnits().addAll(gw2.getUnits());
            return gw2;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(String fileName, GameWorld gameWorld) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(gameWorld);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
