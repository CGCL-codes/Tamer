package com.bluemarsh.jswat.action;

import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.util.SessionSettings;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.prefs.BackingStoreException;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

/**
 * Class OpenSessionAction allows the user to load a saved session.
 *
 * @author Marko van Dooren
 * @author Nathan Fiedler
 */
public class OpenSessionAction extends JSwatAction {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new OpenSessionAction object with the default action
     * command string of "openSession".
     */
    public OpenSessionAction() {
        super("openSession");
    }

    /**
     * Performs the open action. This presents the user with a list
     * of available session names and loads the chosen one.
     *
     * @param  event  action event
     */
    public void actionPerformed(ActionEvent event) {
        Frame topFrame = getFrame(event);
        Session session = getSession(event);
        DefaultListModel model = new DefaultListModel();
        String[] names = null;
        try {
            names = SessionSettings.getAvailableSettings();
        } catch (BackingStoreException bse) {
            session.getStatusLog().writeStackTrace(bse);
            return;
        }
        for (int ii = 0; ii < names.length; ii++) {
            model.addElement(names[ii]);
        }
        JList nameList = new JList(model);
        nameList.setVisibleRowCount(5);
        JTextField textField = new JTextField(30);
        Object[] dialogElements = { new JLabel(Bundle.getString("OpenSession.currentName") + ' ' + SessionSettings.currentSettings()), Bundle.getString("OpenSession.chooseFromList"), new JScrollPane(nameList), Bundle.getString("OpenSession.enterNewName"), textField };
        new ListToFieldMediator(nameList, textField);
        String newName = null;
        while (newName == null) {
            int response = JOptionPane.showOptionDialog(topFrame, dialogElements, Bundle.getString("OpenSession.dialogTitle"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (response != JOptionPane.OK_OPTION) {
                return;
            }
            newName = ((JTextField) dialogElements[4]).getText();
            if (newName == null || newName.length() == 0) {
                displayError(event, Bundle.getString("OpenSession.mustEnterName"));
                newName = null;
            }
        }
        SessionSettings.loadSettings(newName);
    }

    /**
     * Listens to the list selection changes and places the item name
     * in the text field.
     *
     * @author  Nathan Fiedler
     */
    protected class ListToFieldMediator implements ListSelectionListener {

        /** List from which selected names come. */
        private JList listComp;

        /** Text component to populate with list item name. */
        private JTextComponent textComp;

        /**
         * Constructs an instance of this class.
         *
         * @param  listComp  list component.
         * @param  textComp  text component.
         */
        public ListToFieldMediator(JList listComp, JTextComponent textComp) {
            this.listComp = listComp;
            this.textComp = textComp;
            listComp.addListSelectionListener(this);
        }

        /**
         * Called whenever the value of the selection changes.
         *
         * @param  e  the event that characterizes the change.
         */
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                String name = (String) listComp.getSelectedValue();
                if (name != null) {
                    textComp.setText(name);
                }
            }
        }
    }
}
