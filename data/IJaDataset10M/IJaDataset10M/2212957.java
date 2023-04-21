package dialogs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import data.ExportMatrix;

/**
 * Export matrix routine.
 * 
 * @author Luca Petraglio
 * @author Michael Mattes
 */
public class ExportMatrixRoutine {

    private static ExportMatrixRoutine uniqueInstance;

    private static File f;

    private static int status = 1;

    /**
	 * A private Constructor prevents any other class from instantiating.
	 */
    private ExportMatrixRoutine() {
    }

    /**
	 *  Get the instance of class.
	 * 
	 *  @return uniqueInstance instance of class
	 */
    public static ExportMatrixRoutine getObject() {
        if (uniqueInstance == null) {
            uniqueInstance = new ExportMatrixRoutine();
        }
        return uniqueInstance;
    }

    /**
	 * Call the file chooser to save to a new file.
	 */
    public void export() {
        final FileChooser fileChooser = FileChooser.getInstance();
        status = fileChooser.save(new JToggleButton(), "File to Export the Matrix");
        if (status == 0) {
            f = fileChooser.getF();
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new FileWriter(f.getAbsolutePath()));
                final ExportMatrix em = new ExportMatrix();
                bw.write(em.getAscii());
                bw.close();
            } catch (final IOException err) {
                JOptionPane.showMessageDialog(null, "Error: " + err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            uniqueInstance = null;
        }
    }
}
