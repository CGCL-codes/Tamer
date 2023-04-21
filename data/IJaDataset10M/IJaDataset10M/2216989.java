package pepperdine.modules.bacibeansgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Edit", id = "pepperdine.modules.bacibeansgui.Paste")
@ActionRegistration(iconBase = "pepperdine/modules/bacibeansgui/paste.gif", displayName = "#CTL_Paste")
@ActionReferences({ @ActionReference(path = "Menu/Edit", position = 1200), @ActionReference(path = "Shortcuts", name = "D-V") })
@Messages("CTL_Paste=Paste")
public final class Paste implements ActionListener {

    public void actionPerformed(ActionEvent e) {
    }
}
