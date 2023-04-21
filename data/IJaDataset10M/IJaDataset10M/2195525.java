package net.sourceforge.kas.cViewer.actions.java;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JButton;
import net.sourceforge.kas.cTree.CType;
import net.sourceforge.kas.cTree.adapter.C_Changer;
import net.sourceforge.kas.cTree.cAlter.AlterHandler;
import net.sourceforge.kas.cViewer.java.JMathComponent;
import net.sourceforge.kas.cViewer.java.Messages;

@SuppressWarnings("serial")
public class ChooseAction extends javax.swing.AbstractAction {

    private JMathComponent comp;

    public ChooseAction(JMathComponent comp) {
        super(Messages.getString("CViewer.buttonChooser"));
        this.comp = comp;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (comp.getActiveC().getFirst() != null && !comp.getActiveC().getFirst().getCType().equals(CType.MATH)) {
            System.out.println(comp.getActiveC().getFirst().toString());
            this.showMenu(e);
            comp.modifyDocument();
        }
    }

    public void showMenu(final ActionEvent evt) {
        final HashMap<String, C_Changer> options = AlterHandler.getInstance().getOptions(comp.getActiveC());
        final JPopupMenu menu = new JPopupMenu();
        Font itemFont = new Font("SansSerif", Font.PLAIN, 16);
        JMenuItem item = new JMenuItem(comp.getActionByName(Messages.getString("CViewer.buttonAlter")));
        item.setFont(itemFont);
        for (final String s : options.keySet()) {
            item = new JMenuItem(comp.getActionByName(Messages.getString("CViewer.buttonAlter")));
            item.setFont(itemFont);
            item.setText(s);
            menu.add(item);
        }
        menu.show(comp, ((JButton) evt.getSource()).getX(), ((JButton) evt.getSource()).getY());
    }
}
