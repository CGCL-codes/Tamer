package ossobook.client.io.file;

import java.io.File;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;

/**
 * @author ali
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public abstract class AbstraktesDatenEinlesen {

    protected JDesktopPane desktop;

    protected File source;

    public AbstraktesDatenEinlesen(JDesktopPane desktop) {
        this.desktop = desktop;
        JFileChooser fc = new JFileChooser();
        desktop.add(fc);
        int returnVal = fc.showOpenDialog(desktop);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            source = fc.getSelectedFile();
        }
        readData();
    }

    public AbstraktesDatenEinlesen(JDesktopPane desktop, File f) {
        this.desktop = desktop;
        this.source = f;
        readData();
    }

    /**
	 *  
	 */
    abstract void readData();
}
