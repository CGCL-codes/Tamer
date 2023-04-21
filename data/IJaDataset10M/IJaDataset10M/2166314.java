package beans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * @author Silvio Donnini
 */
public class BeanFactory {

    private static XStream xstream = new XStream(new DomDriver());

    public static Object load(String filename) {
        try {
            StringBuffer fileData = new StringBuffer(1000);
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }
            reader.close();
            return xstream.fromXML(fileData.toString());
        } catch (NullPointerException e) {
            Logger.getLogger("application").warning("Error file: file not found");
        } catch (FileNotFoundException e) {
            Logger.getLogger("application").warning("Error file: file not found");
        } catch (IOException e) {
            Logger.getLogger("application").warning("General Error reading file");
        }
        return null;
    }

    public static void save(Object object, String filename) {
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF8"));
            out.write(xstream.toXML(object));
            out.close();
        } catch (IOException fnfe) {
            Logger.getLogger("application").warning("File not found");
        }
        Logger.getLogger("application").info("File saved");
    }
}
