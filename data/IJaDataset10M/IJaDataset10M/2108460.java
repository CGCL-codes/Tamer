package jskat.gui.main.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import jskat.data.JSkatDataModel;
import jskat.gui.img.JSkatGraphicRepository;
import jskat.gui.main.LastTricksDialog;

/**
 * Action for showing the LastTrickDialog
 */
public class LastTricksDialogAction implements ActionListener {

    private Log log = LogFactory.getLog(LastTricksDialogAction.class);

    /**
	 * Creates a new instance of LastTricksDialogAction
	 * 
	 * @param dataModel
	 *            The JSkatDataModel that holds all data
	 * @param jskatBitmaps
	 *            The JSkatGraphicRepository that holds all images used by JSkat
	 * @param parent
	 *            The parent JFrame
	 */
    public LastTricksDialogAction(JSkatDataModel dataModel, JSkatGraphicRepository jskatBitmaps, JFrame parent) {
        lastTricksDialog = LastTricksDialog.createInstance(dataModel, jskatBitmaps, parent);
        log.debug("LastTricksDialogAction is ready.");
    }

    /**
	 * The action that should be performed
	 * 
	 * @param e
	 *            The Event that fires the action
	 */
    public void actionPerformed(ActionEvent e) {
        lastTricksDialog.setVisible(true);
    }

    private LastTricksDialog lastTricksDialog;
}
