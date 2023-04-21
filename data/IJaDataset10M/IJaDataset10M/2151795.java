package org.sidora.strata.context;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Action wich shows ChronoProperties component.
 * @author Enric Tartera, Juan Manuel Gimeno, Roger Masgoret
 * @version 1.0
 */
public class ChronoPropertiesAction extends AbstractAction {

    public ChronoPropertiesAction() {
        super(NbBundle.getMessage(ChronoPropertiesAction.class, "CTL_ChronoPropertiesAction"));
    }

    public void actionPerformed(ActionEvent evt) {
        ChronoPropertiesTopComponent win = ChronoPropertiesTopComponent.findInstance();
        win.open();
        win.requestActive();
    }

    public void actionPerformed() {
        ChronoPropertiesTopComponent win = ChronoPropertiesTopComponent.findInstance();
        win.open();
        win.requestActive();
        win.fillFields();
    }

    public void cerrar() {
        ChronoPropertiesTopComponent win = ChronoPropertiesTopComponent.findInstance();
        win.close();
    }
}
