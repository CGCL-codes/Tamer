package classes;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Diogo
 */
public class UtilitySerial {

    /**
     * Save the content of a String into a new file
     *
     * @param content the String that will be saved
     * @param path the path of directory
     * @return true if successfully saved the archive
     */
    public static boolean saveFile(String content, String path) {
        try {
            FileWriter fw = new FileWriter(path);
            System.err.println("Writting in: " + path);
            fw.write(content);
            fw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
	 * saves a object in a path
	 * 
	 * @param object
	 * @param path
	 * @return
	 */
    public static boolean saveObject(Object object, String path) {
        try {
            File f = new File(path);
            ObjectOutputStream save = new ObjectOutputStream(new FileOutputStream(f));
            save.writeObject(object);
            save.flush();
            save.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String OpenWindowGetFile(Component parent, String openDirPath) {
        javax.swing.JFileChooser jFileChooser;
        jFileChooser = new javax.swing.JFileChooser();
        jFileChooser.setDialogTitle("Choose a file");
        jFileChooser.setFileFilter(new FileNameExtensionFilter("lua", "*"));
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (openDirPath != null) jFileChooser.setCurrentDirectory(new File(openDirPath));
        try {
            int result = jFileChooser.showOpenDialog(parent);
            if (result == JFileChooser.APPROVE_OPTION) {
                return jFileChooser.getSelectedFile().toString();
            }
        } catch (Exception e) {
            System.err.println("Error: could not open the file.");
        }
        return "";
    }

    /**
     * Reads a file archive and returns it as object
     * @param path the path to read
     * @return a read object
     * @throws FileNotFoundException
     */
    public static Object Read(String path) throws FileNotFoundException {
        Object outPut = null;
        try {
            FileInputStream readingFile = new FileInputStream(path);
            ObjectInputStream readObject = new ObjectInputStream(readingFile);
            outPut = (Object) readObject.readObject();
            readObject.close();
            readingFile.close();
        } catch (Exception e) {
            outPut = null;
        }
        return outPut;
    }

    /**
     * Reads a file path and returns its content as String
     *
     * @param path the path to read
     * @return the file content
     */
    public static String ReadString(String path) throws FileNotFoundException {
        StringBuilder output = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            for (String input = br.readLine(); input != null; input = br.readLine()) {
                output.append(input).append("\n");
            }
        } catch (Exception e) {
        }
        return output.toString();
    }
}
