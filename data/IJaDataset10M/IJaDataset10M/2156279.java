package jstudio.gui;

import java.awt.event.ActionEvent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import jstudio.control.Controller;
import jstudio.gui.generic.ContextualMenu;
import jstudio.gui.generic.EntityManagerPanel;
import jstudio.model.Person;
import jstudio.util.Language;

@SuppressWarnings("serial")
public class PersonPopup extends ContextualMenu<Person> {

    public PersonPopup(EntityManagerPanel<Person> parent, Controller<Person> controller) {
        super(parent);
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == viewItem) {
            JDialog dialog = new PersonPanel(context, parent, false).createDialog(parent.getTopLevelAncestor());
            dialog.setVisible(true);
        } else if (o == editItem) {
            JDialog dialog = new PersonPanel(context, parent, true).createDialog(parent.getTopLevelAncestor());
            dialog.setVisible(true);
        } else if (o == deleteItem) {
            int ch = JOptionPane.showConfirmDialog(parent, Language.string("Are you sure you want to remove {0} {1}?", context.getName(), context.getLastname()), Language.string("Romove person?"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (ch == JOptionPane.YES_OPTION) {
                controller.delete(context);
                parent.refresh();
            }
        } else if (o == newItem) {
            JDialog dialog = new PersonPanel(new Person(0l), parent, true).createDialog(parent.getTopLevelAncestor());
            dialog.setVisible(true);
        }
    }
}
