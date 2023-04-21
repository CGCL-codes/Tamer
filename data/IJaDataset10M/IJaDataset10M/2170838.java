package gate.util.persistence;

import gate.util.persistence.*;
import java.io.*;
import com.thoughtworks.xstream.XStream;

/**
 * <p>Handy command-line utility that loads a saved application state in the
 * old serialized-object format and resaves it in XML format.  Note that this
 * utility does not load the application into GATE and resave it, but merely
 * converts the persistent representation from one format to another.  If you
 * have an old-style saved state that will not load (because, for example, it
 * refers to a plugin that is not available) you can convert it to XML format
 * with this tool and then hand-edit the resulting XML to fix it.</p>
 *
 * <p>Usage: java -classpath &lt;gate.jar and lib/*.jar&gt;
 * gate.util.persistence.UpdateSavedApp &lt;oldFormatFile&gt;
 * &lt;newFormatFile&gt;</p>
 */
public class UpdateSavedApp {

    public static void main(String[] argv) throws Exception {
        if (argv.length < 2) {
            System.err.println("Usage:");
            System.err.println("  UpdateSavedApp <oldFile> <newFile>");
            System.exit(1);
        }
        File oldFile = new File(argv[0]);
        File newFile = new File(argv[1]);
        if (newFile.exists()) {
            System.err.println(newFile + " already exists.");
            System.err.println("Please move it out of the way.  This tool will " + "not overwrite an existing file,");
            System.err.println("in particular the new file must not be the same as " + "the old.");
            System.exit(1);
        }
        FileInputStream fis = new FileInputStream(oldFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object oldUrlList = ois.readObject();
        Object obj = ois.readObject();
        ois.close();
        GateApplication persistApp = new GateApplication();
        persistApp.urlList = oldUrlList;
        persistApp.application = obj;
        XStream xs = new XStream();
        FileWriter fw = new FileWriter(newFile);
        BufferedWriter bw = new BufferedWriter(fw);
        xs.toXML(persistApp, bw);
        bw.close();
    }
}
