package pepperdine.modules.bacibeansgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

@ActionID(category = "Edit", id = "pepperdine.modules.bacibeansgui.Undo")
@ActionRegistration(iconBase = "pepperdine/modules/bacibeansgui/undo.gif", displayName = "#CTL_Undo")
@ActionReferences({ @ActionReference(path = "Menu/Edit", position = 800), @ActionReference(path = "Shortcuts", name = "D-Z"), @ActionReference(path = "Toolbars/Clipboard", position = 0) })
@Messages("CTL_Undo=Undo")
public final class Undo implements ActionListener {

    public void actionPerformed(ActionEvent e) {
    }
}
