package hoplugins.experienceViewer;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import javax.swing.JOptionPane;

public class ErrorLog {

    public ErrorLog() {
    }

    public static void write(Exception ex) {
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            ex.printStackTrace(printWriter);
            FileWriter fileWriter = new FileWriter("ExperienceViewer.error", true);
            fileWriter.write(new Date() + stringWriter.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception innerEx) {
            JOptionPane.showMessageDialog(null, "Can\264t write error file!", "ExperienceViewer", 0);
        }
    }
}
