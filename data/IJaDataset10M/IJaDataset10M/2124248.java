package jp.ac.shizuoka.cs.panda.mmm.mr3.actions;

import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import jp.ac.shizuoka.cs.panda.mmm.mr3.*;

/**
 * @author takeshi morita
 *
 */
public class ImportJavaObject extends AbstractActionFile {

    private static final String PROJECT = "Project (Java Object)";

    public ImportJavaObject(MR3 mr3) {
        super(mr3, PROJECT);
    }

    private ObjectInputStream createInputStream(File file) throws FileNotFoundException, IOException {
        InputStream fi = new FileInputStream(file);
        fi = new GZIPInputStream(fi);
        return new ObjectInputStream(fi);
    }

    public void openProject(File file) {
        try {
            mr3.newProject();
            ObjectInputStream oi = createInputStream(file);
            Object obj = oi.readObject();
            if (obj instanceof ArrayList) {
                ArrayList list = (ArrayList) obj;
                int index = mr3.getGraphManager().loadState(list);
                mr3.getNSTableDialog().loadState((List) list.get(index));
            }
            oi.close();
            mr3.setTitle("MR^3 - " + file.getAbsolutePath());
            mr3.setCurrentProject(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        File file = getFile(true, "mr3");
        if (file == null) {
            return;
        }
        openProject(file);
    }
}
