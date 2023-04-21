package coopnetclient.frames.popupmenus;

import coopnetclient.Globals;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.text.JTextComponent;

public class ChatOutputPopupMenu extends TextComponentPopupMenu {

    protected JMenuItem clearHighlight;

    public ChatOutputPopupMenu(JTextComponent parent) {
        super(parent);
        clearHighlight = new JMenuItem("Clear Highlighting");
        clearHighlight.addActionListener(this);
        this.add(clearHighlight);
    }

    @Override
    public void show(Component invoker, int x, int y) {
        super.show(invoker, x, y);
        clearHighlight.setEnabled(Globals.getHighlightList().size() > 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == clearHighlight) {
            Globals.clearHighlights();
        }
    }
}
