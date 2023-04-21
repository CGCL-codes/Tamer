package bomberman.server;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.FileOutputStream;

/**
 * Stores the persistent data of the Server. This thread is executed
 * when the VM stops. Due to security restrictions this is not done
 * on the Java WebStart edition of KC Bomberman.
 * @author Christian Lins (christian.lins@web.de)
 */
class ShutdownThread extends Thread {

    public static final String DATABASE_FILE = "database.po";

    public static final String HIGHSCORE_FILE = "highscore.po";

    private Database database = null;

    private Highscore highscore = null;

    public ShutdownThread(Database database, Highscore highscore) {
        this.database = database;
        this.highscore = highscore;
    }

    /**
   * Serializes the Database and the Highscore.
   */
    @Override
    public void run() {
        try {
            XStream xstream = new XStream(new DomDriver());
            xstream.toXML(this.database, new FileOutputStream(DATABASE_FILE));
            xstream.toXML(this.highscore, new FileOutputStream(HIGHSCORE_FILE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
