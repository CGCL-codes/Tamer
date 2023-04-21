package persdocmanager.action;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.ImageIcon;
import persdocmanager.Persdocmanager;
import persdocmanager.gui.base.DocumentRenderer;
import persdocmanager.util.DocumentUtilitys;

/**
 * @author Gerald Skokalski
 * 
 */
public class EditPageExternalAction extends BasicAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5914615712029671854L;

    private File mFile;

    public EditPageExternalAction(String pString, ImageIcon pImageIcon, File pPage) {
        super(pString, pImageIcon);
        mFile = pPage;
    }

    public void actionPerformed(ActionEvent pE) {
        try {
            DocumentUtilitys.editFileInExternalApplication(mFile);
        } catch (Exception e) {
            Persdocmanager.handleError(e.getMessage(), e);
        }
    }

    public SwingWorker getFileWatcher() {
        return null;
    }
}
