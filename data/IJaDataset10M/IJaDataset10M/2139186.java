package testers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class is designed to assist TestBinaryData in testing the functionality
 * of Buddy's binary access methods. The binary file access methods in this
 * class are identical to buddy's - to allow for better testing.
 * @author Team Triple Threat
 * @see <a href="../buddyLibrary/Buddy.html">buddyLibrary.Buddy</a>
 * @see <a href="../testers/TestBinaryData.html">testers.TestBinaryData</a>
 */
public class BinaryTester2 {

    public BinaryTester2() {
    }

    /**
     * Get all abjects stored in this buddy's binary data file and return them
     * as an ArrayList
     * 
     * @return An ArrayList containing the objects stored in this buddy's binary
     *         data file. These objects are of type Object, and must be cast to
     *         their correct types by the calling buddy. If the file is empty or
     *         does not exist then this function returns an empty ArrayList.
     */
    public ArrayList<Object> getDataContent() {
        ObjectInputStream o = getDataReader();
        ArrayList<Object> result = new ArrayList<Object>();
        if (o != null) {
            try {
                do {
                    result.add(o.readObject());
                } while (true);
            } catch (Exception e) {
            }
            try {
                o.close();
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * Get a File reference to the binary data file for this buddy.
     * @return A File that points to this buddy's binary data file
     */
    public File getDataFile() {
        String temp[] = this.getClass().getName().replace(".", " ").split(" ");
        String buddyName = temp[temp.length - 1];
        String filename = System.getProperty("user.dir");
        filename += "\\Data\\" + getUser() + "\\";
        filename += buddyName + ".dat";
        return new File(filename);
    }

    /**
     * Get an ObjectInputStream that points to this buddy's binary data file.
     * This allows for more control over reading than getDataContent()
     * @return An ObjectInputStream that points to this buddy's binary data file.
     *         If  file does not exist then it returns null
     */
    public ObjectInputStream getDataReader() {
        ObjectInputStream objStream;
        try {
            FileInputStream inStream = new FileInputStream(getDataFile());
            objStream = new ObjectInputStream(inStream);
        } catch (IOException e) {
            objStream = null;
        }
        return objStream;
    }

    /**
     * Get an ObjectOutputStream that points to this buddy's binary data file.
     * This allows the buddy to write to the binary file
     * @return An ObjectOutputStream that points to this buddy's binary data
     *         file. The file is created if it does not exist already. If the
     *         file cannot be created for some reason, then this function
     *         returns null.
     */
    public ObjectOutputStream getDataWriter() {
        ObjectOutputStream objStream;
        try {
            File f = getDataFile();
            f.getParentFile().mkdirs();
            FileOutputStream outStream = new FileOutputStream(f);
            objStream = new ObjectOutputStream(outStream);
        } catch (IOException e) {
            objStream = null;
        }
        return objStream;
    }

    public String getUser() {
        return "Bob";
    }

    /**
     * This function gets the name of the class it is inside. (for instance, a
     * class called Bob would cause this function to return "Bob")
     * @return A string representing the name of the class this function is
     *         inside
     */
    public String getBuddyName() {
        String temp[] = this.getClass().getName().replace(".", " ").split(" ");
        return temp[temp.length - 1];
    }
}
