package gui.actions;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import gui.*;

/**
 * Set the zoom to one.
 * @author dlegland
 */
public class ZoomOneAction extends EuclideAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** Apache log4j Logger */
    private static Logger logger = Logger.getLogger("Euclide");

    public ZoomOneAction(EuclideGui gui, String name) {
        super(gui, name);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        logger.info("zoom to 1:1");
        EuclideDocView docView = gui.getCurrentView();
        if (docView == null) return;
        EuclideSheetView view = docView.getCurrentSheetView();
        if (view == null) return;
        view.setZoom(1);
        view.invalidate();
        gui.getCurrentFrame().validate();
        gui.getCurrentFrame().repaint();
    }
}
