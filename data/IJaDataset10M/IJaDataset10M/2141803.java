package pepperdine.modules.bacibeansgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Edit", id = "pepperdine.modules.bacibeansgui.Cut")
@ActionRegistration(iconBase = "pepperdine/modules/bacibeansgui/cut.gif", displayName = "#CTL_Cut")
@ActionReferences({ @ActionReference(path = "Menu/Edit", position = 1200), @ActionReference(path = "Shortcuts", name = "D-X") })
@Messages("CTL_Cut=Cut")
public final class Cut implements ActionListener {

    public void actionPerformed(ActionEvent e) {
    }
}
