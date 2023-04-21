package org.jtomtom.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.jtomtom.Application;

/**
 * @author Frédéric Combes
 *
 */
public class QuitterAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    public QuitterAction() {
        super(Application.getInstance().getMainTranslator().getString("org.jtomtom.main.button.quit.label"));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        System.exit(0);
    }
}
