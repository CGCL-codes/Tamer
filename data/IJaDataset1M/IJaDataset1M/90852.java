package bgpanalyzer.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author paco
 */
public class Write2File {

    /** Creates a new instance of Write2File */
    public Write2File() {
    }

    public void ToFile(String date, String name, ArrayList<String> degree, ArrayList<Integer> value) {
        this.createDateDir(date);
        File ff = this.CreateDir(date, name + ".txt");
        String header = "#Metric: " + name + "\n#Date: " + date + "\n" + "#As Number - value" + "\n#---------------";
        try {
            FileWriter fstream = new FileWriter(ff);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(header);
            Iterator it = degree.iterator();
            Iterator it2 = value.iterator();
            while (it.hasNext()) {
                String aux = (String) it.next();
                Integer aux2 = (Integer) it2.next();
                out.write("\n" + aux + " " + aux2.toString());
            }
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public File CreateDir(String date, String creationFile) {
        String userdir = System.getProperty("user.dir");
        String f = new String(userdir + File.separator + "Files" + File.separator + date + File.separator + creationFile);
        File file = new File(f);
        try {
            boolean success = file.createNewFile();
            if (success) {
            } else {
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return file;
    }

    public void createDateDir(String creationFile) {
        File f2 = new File("Files");
        File directorio = new File(f2, creationFile);
        directorio.mkdirs();
    }
}
