package gnu.hylafax.util;

import gnu.hylafax.Client;
import gnu.hylafax.HylaFAXClient;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author John Yeary <jyeary@javanetwork.net>
 * @version 1.0
 * 
 * <p>
 * This class was created to move the test out of the HylaFAXClient class.
 * </p>
 * <p>
 * TODO A better test framework needs to be put into place.
 * </p>
 */
public class HylaFAXClientTest extends HylaFAXClient {

    private static final Log log = LogFactory.getLog(Client.class);

    /** Creates a new instance of HylaFAXClientTest */
    public HylaFAXClientTest() {
    }

    /**
     * Run some basic tests.
     * 
     * @param Arguments
     *                an array of command-line-argument Strings
     */
    public static void main(String Arguments[]) {
        HylaFAXClient c = new HylaFAXClient();
        try {
            c.open("localhost");
            c.noop();
            c.setPassive(true);
            c.user("fax");
            c.type(TYPE_IMAGE);
            System.out.println("current directory is: " + c.pwd());
            c.cwd("docq");
            System.out.println("current directory is: " + c.pwd());
            c.cdup();
            System.out.println("current directory is: " + c.pwd());
            System.out.println("idle timer set to " + c.idle() + " seconds.");
            c.idle(1800);
            System.out.println("idle timer set to " + c.idle() + " seconds.");
            System.out.println("job format: " + c.jobfmt());
            c.jobfmt("%-4j");
            System.out.println("job format: " + c.jobfmt());
            c.stru(STRU_FILE);
            c.stru(STRU_TIFF);
            c.stru(STRU_FILE);
            {
                String filename = "test.ps";
                FileInputStream file = new FileInputStream(filename);
                String f = c.putTemporary(file);
                System.out.println("filename= " + f);
                long local_size, remote_size;
                local_size = (new RandomAccessFile(filename, "r").length());
                remote_size = c.size(f);
                System.out.println(filename + " local size is " + local_size);
                System.out.println(f + " remote size is " + remote_size);
                FileOutputStream out_file = new FileOutputStream(filename + ".retr");
                c.get(f, out_file);
                local_size = (new RandomAccessFile(filename + ".retr", "r").length());
                System.out.println(filename + ".retr size is " + local_size);
                FileOutputStream zip_file = new FileOutputStream(filename + ".gz");
                c.mode(MODE_ZLIB);
                c.get(f, zip_file);
                local_size = (new RandomAccessFile(filename + ".gz", "r").length());
                System.out.println(filename + ".gz size is " + local_size);
                c.mode(MODE_STREAM);
            }
            {
                Vector files;
                int counter;
                files = c.getList();
                for (counter = 0; counter < files.size(); counter++) {
                    System.out.println((String) files.elementAt(counter));
                }
                files = c.getList("/tmp");
                for (counter = 0; counter < files.size(); counter++) {
                    System.out.println((String) files.elementAt(counter));
                }
                c.mode(MODE_ZLIB);
                files = c.getList("/tmp");
                for (counter = 0; counter < files.size(); counter++) {
                    System.out.println((String) files.elementAt(counter));
                }
                c.mode(MODE_STREAM);
                try {
                    c.getList("/joey-joe-joe-jr.shabba-do");
                    System.out.println("ERROR: file not found was expected");
                } catch (FileNotFoundException fnfe) {
                    System.out.println("GOOD: file not found, as expected");
                }
                files = c.getList();
                for (counter = 0; counter < files.size(); counter++) {
                    System.out.println((String) files.elementAt(counter));
                }
            }
            {
                Vector files;
                int counter;
                files = c.getNameList("/tmp");
                for (counter = 0; counter < files.size(); counter++) {
                    System.out.println((String) files.elementAt(counter));
                }
                files = c.getNameList("/tmp");
                for (counter = 0; counter < files.size(); counter++) {
                    System.out.println((String) files.elementAt(counter));
                }
                c.mode(MODE_ZLIB);
                files = c.getNameList("/tmp");
                for (counter = 0; counter < files.size(); counter++) {
                    System.out.println((String) files.elementAt(counter));
                }
                c.mode(MODE_STREAM);
                files = c.getNameList();
                for (counter = 0; counter < files.size(); counter++) {
                    System.out.println((String) files.elementAt(counter));
                }
            }
            String system = c.syst();
            System.out.println("system type: " + system + ".");
            c.noop();
            {
                Vector status = c.stat();
                int counter;
                for (counter = 0; counter < status.size(); counter++) {
                    System.out.println(status.elementAt(counter));
                }
                status = c.stat("docq");
                for (counter = 0; counter < status.size(); counter++) {
                    System.out.println(status.elementAt(counter));
                }
                try {
                    status = c.stat("joey-joe-joe-junior-shabba-do");
                    for (counter = 0; counter < status.size(); counter++) {
                        System.out.println(status.elementAt(counter));
                    }
                } catch (FileNotFoundException fnfe) {
                    System.out.println("GOOD: file not found.  this is what we expected");
                }
            }
            c.noop();
            c.quit();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        System.out.println("main: end");
    }
}
