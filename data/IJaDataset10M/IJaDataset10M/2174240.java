package org.sourceforge.kga.simpleGui.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.sourceforge.kga.KitchenGardenAid;
import org.sourceforge.kga.simpleGui.Gui;
import org.sourceforge.kga.translation.Translation;

public class About extends KgaAction {

    private static final long serialVersionUID = 1L;

    public About(Gui gui) {
        super(gui, "about");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextArea ta = new JTextArea();
        ta.append("Kitchen garden aid\n");
        ta.append("Version " + KitchenGardenAid.VERSION + "\n");
        ta.append("Released under GNU GPL v3\n");
        ta.append("Contact: christian1195@gmail.com");
        ta.setBorder(BorderFactory.createEtchedBorder());
        ta.setPreferredSize(new Dimension(400, 100));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        JDialog tutorial = new JOptionPane(ta).createDialog(Translation.getPreferred().translate("about"));
        tutorial.setVisible(true);
    }
}
