package com.tabuto.jsimlife.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.tabuto.jsimlife.JSimLife;
import com.tabuto.jsimlife.views.JSLMainView;

/**
 * Class extends AbstractAction to perform the following Action:
 * Stop the simulation drawing
 * 
 * @author tabuto83
 *
 * @see AbstractAction
 * @see JSLMainView
 */
public class PauseSimulationAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    JSLMainView root;

    /**
	 * Instantiate new StepSimulationAction
	 * 
	 * @param title String Action title
	 * @param mainview JSLMainView
	 * @see JSimLife
	 */
    public PauseSimulationAction(String title, JSLMainView mainview) {
        super(title);
        root = mainview;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        root.stopSimulation();
    }
}
