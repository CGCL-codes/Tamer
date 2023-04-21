package reconcile.weka.core.xml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This class enables one to change the UID of a serialized object and therefore
 * not losing the data stored in the binary format.
 * 
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1.1 $ 
 */
public class SerialUIDChanger {

    /**
    * checks whether KOML is present
    * 
    * @return returns <code>true</code> if KOML is present
    * @throws Exception if KOML is not present 
    */
    protected static boolean checkKOML() throws Exception {
        if (!KOML.isPresent()) throw new Exception("KOML is not present!"); else return true;
    }

    /**
    * checks whether the given filename ends with ".koml"
    * 
    * @param filename the filename to check
    * @return whether it is a KOML file or not
    * @see KOML#FILE_EXTENSION
    */
    public static boolean isKOML(String filename) {
        return filename.toLowerCase().endsWith(KOML.FILE_EXTENSION);
    }

    /**
    * loads a serialized object and returns it
    * 
    * @param binary the filename that points to the file containing the
    *        serialized object
    * @return the object from the file
    * @throws Exception if reading fails
    */
    protected static Object readBinary(String binary) throws Exception {
        FileInputStream fi;
        ObjectInputStream oi;
        Object o;
        fi = new FileInputStream(binary);
        oi = new ObjectInputStream(new BufferedInputStream(fi));
        o = oi.readObject();
        oi.close();
        return o;
    }

    /**
    * serializes the given object into the given file
    * 
    * @param binary the file to store the object in
    * @param o the object to serialize
    * @throws Exception if saving fails 
    */
    protected static void writeBinary(String binary, Object o) throws Exception {
        FileOutputStream fo;
        ObjectOutputStream oo;
        fo = new FileOutputStream(binary);
        oo = new ObjectOutputStream(new BufferedOutputStream(fo));
        oo.writeObject(o);
        oo.close();
    }

    /**
    * converts a binary file into a KOML XML file
    * 
    * @param binary the binary file to convert
    * @param koml where to store the XML output
    * @throws Exception if conversion fails
    */
    public static void binaryToKOML(String binary, String koml) throws Exception {
        Object o;
        checkKOML();
        o = readBinary(binary);
        if (o == null) throw new Exception("Failed to deserialize object from binary file '" + binary + "'!");
        KOML.write(koml, o);
    }

    /**
    * converts a KOML file into a binary one
    * 
    * @param koml the filename with the XML data
    * @param binary the name of the 
    */
    public static void komlToBinary(String koml, String binary) throws Exception {
        Object o;
        checkKOML();
        o = KOML.read(koml);
        if (o == null) throw new Exception("Failed to deserialize object from XML file '" + koml + "'!");
        writeBinary(binary, o);
    }

    /**
    * changes the oldUID into newUID from the given file (binary/KOML) into the
    * other one (binary/KOML). it basically does a replace in the XML, i.e. it
    * looks for " uid='oldUID'" and replaces it with " uid='newUID'".
    * 
    * @param oldUID the old UID to change
    * @param newUID the new UID to use
    * @param fromFile the original file with the old UID
    * @param toFile the new file where to store the modified UID
    * @throws Exception if conversion fails
    */
    public static void changeUID(long oldUID, long newUID, String fromFile, String toFile) throws Exception {
        String inputFile;
        String tempFile;
        File file;
        String content;
        String line;
        BufferedReader reader;
        BufferedWriter writer;
        if (!isKOML(fromFile)) {
            inputFile = fromFile + ".koml";
            binaryToKOML(fromFile, inputFile);
        } else {
            inputFile = fromFile;
        }
        reader = new BufferedReader(new FileReader(inputFile));
        content = "";
        while ((line = reader.readLine()) != null) {
            if (!content.equals("")) content += "\n";
            content += line;
        }
        reader.close();
        content = content.replaceAll(" uid='" + Long.toString(oldUID) + "'", " uid='" + Long.toString(newUID) + "'");
        tempFile = inputFile + ".temp";
        writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write(content);
        writer.flush();
        writer.close();
        if (!isKOML(toFile)) {
            komlToBinary(tempFile, toFile);
        } else {
            writer = new BufferedWriter(new FileWriter(toFile));
            writer.write(content);
            writer.flush();
            writer.close();
        }
        file = new File(tempFile);
        file.delete();
    }

    /**
    * exchanges an old UID for a new one. a file that doesn't end with ".koml"
    * is considered being binary.
    * takes four arguments: oldUID newUID oldFilename newFilename
    * 
    * @param args the command line parameters
    * @see KOML#FILE_EXTENSION
    */
    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println();
            System.out.println("Usage: " + SerialUIDChanger.class.getName() + " <oldUID> <newUID> <oldFilename> <newFilename>");
            System.out.println("       <oldFilename> and <newFilename> have to be different");
            System.out.println();
        } else {
            if (args[2].equals(args[3])) throw new Exception("Filenames have to be different!");
            changeUID(Long.parseLong(args[0]), Long.parseLong(args[1]), args[2], args[3]);
        }
    }
}
