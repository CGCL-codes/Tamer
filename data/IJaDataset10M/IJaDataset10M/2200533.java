package ti.plato.eclipse.log.analysis.u;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

/**
 * Eclipse log utilities
 *
 * @author alex.k@ti.com
 */
public class EclipseLogUtil {

    private EclipseLogUtil() {
    }

    private static IMemento getMemento() {
        FileInputStream input = null;
        try {
            File stateFile = new File("e:\\users\\alex\\ws\\ti.plato.eclipse.log.analysis\\memento.xml");
            if (!stateFile.exists()) {
                try {
                    stateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            input = new FileInputStream(stateFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(input, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            return XMLMemento.createReadRoot(reader);
        } catch (WorkbenchException e) {
            e.printStackTrace();
        }
        return null;
    }
}
