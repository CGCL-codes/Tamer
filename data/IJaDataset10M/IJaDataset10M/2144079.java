package gui.actions;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;
import gui.*;

/**
 * @author dlegland
 */
public class SelectToolAction extends EuclideAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** Apache log4j Logger */
    private static Logger logger = Logger.getLogger("Euclide");

    /**
	 * @param app
	 * @param name
	 */
    public SelectToolAction(EuclideGui gui, String name) {
        super(gui, name);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        logger.info("select tool: " + getName());
        EuclideTool tool = gui.getTool(getName());
        gui.getCurrentFrame().setCurrentTool(tool);
    }
}
