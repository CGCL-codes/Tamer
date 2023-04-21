package edu.lcmi.grouppac.gui;

import java.awt.event.InputEvent;
import javax.swing.KeyStroke;

/**
 * Just close the application.  Creation date: (08/04/2002 11:53:25)
 * 
 * @version $Revision: 1.5 $
 * @author <a href="mailto:padilha@das.ufsc.br">Ricardo Sangoi Padilha</a>, <a
 *         href="http://www.das.ufsc.br/">UFSC, Florian�polis, SC, Brazil</a>
 */
public class ExitAction extends AbstractGroupPacAction {

    /**
	 * Creates a new ExitAction object.
	 */
    public ExitAction() {
        super("Exit", null, 'x', KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK), "Exit from this application");
    }

    /**
	 * @see AbstractGroupPacAction#execute()
	 */
    public void execute() throws Exception {
        System.exit(0);
    }
}
