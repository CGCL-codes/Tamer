package pl.edu.amu.wmi.ztg.grapher.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import pl.edu.amu.wmi.ztg.grapher.GrapherMessages;
import pl.edu.amu.wmi.ztg.grapher.io.GrapherFileFilter;

/**
 * 
 * @author Ireneusz Spinalski
 * 
 */
public class ExportAction extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = 4046258039024493129L;

    /**
     * Creates new instance of ExportAction.
     * 
     */
    public ExportAction() {
        super(GrapherMessages.getString("ExportAction.0"));
    }

    /**
     * Opens file choosing dialog for selecting file where graph is to be exported.
     * Allows choosing png,gif and jpg formats.
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
        JFileChooser fc = new JFileChooser();
        GrapherFileFilter pngFileFilter = new GrapherFileFilter("png");
        GrapherFileFilter jpgFileFilter = new GrapherFileFilter("jpg");
        GrapherFileFilter gifFileFilter = new GrapherFileFilter("gif");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addChoosableFileFilter(pngFileFilter);
        fc.addChoosableFileFilter(jpgFileFilter);
        fc.addChoosableFileFilter(gifFileFilter);
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
        }
    }

    ;
}
