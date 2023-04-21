package verinec.validation.gui;

import java.io.File;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import verinec.VerinecException;
import verinec.gui.VerinecStudio;

/**
 * A ScrollPane containing a ValidatorEdiotor panel
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class ScrollableEditor extends JScrollPane {

    private ValidatorEditorPanel vep;

    /** creates a schollpane containing a ValidatorEdiotor panel
	 * @param gui the validator gui 
	 * @param studio the verinec studio 
	 * @param file the file to be opened, can be null for a new, empty editor
	 * @param type document type (@see TYPE_SCHEMA) and (@see TYPE_LOGFILE)
	 * @throws VerinecException if the file could not be loaded (if file is not null)
	 */
    public ScrollableEditor(ValidationGui gui, VerinecStudio studio, File file, int type) throws VerinecException {
        super(new ValidatorEditorPanel(gui, studio, file, type));
        vep = (ValidatorEditorPanel) ((JViewport) getComponent(0)).getComponent(0);
    }

    /** Set's up the gui. This is done by calling the Validator Editor Panels setupGui (@see ValidatorEditorPanel#setupGUI(VerinecStudio)) 
	 * @param studio the verinec studio
	 * @throws VerinecException if the doucment can't be parsed
	 */
    public void setupGUI(VerinecStudio studio) throws VerinecException {
        vep.setupGUI(studio);
    }

    /** gets the file edited in this editor
	 * @return the file edited in this editor
	 */
    public File getFile() {
        return vep.getFile();
    }
}
