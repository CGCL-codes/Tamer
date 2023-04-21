package net.sf.jabref;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import net.sf.jabref.undo.UndoablePreambleChange;

public class PreambleEditor extends JDialog {

    BibtexDatabase base;

    BasePanel panel;

    JabRefFrame baseFrame;

    JabRefPreferences prefs;

    GridBagLayout gbl = new GridBagLayout();

    GridBagConstraints con = new GridBagConstraints();

    JLabel lab;

    Container conPane = getContentPane();

    JPanel pan = new JPanel();

    FieldEditor ed;

    public PreambleEditor(JabRefFrame baseFrame, BasePanel panel, BibtexDatabase base, JabRefPreferences prefs) {
        super(baseFrame);
        this.baseFrame = baseFrame;
        this.panel = panel;
        this.base = base;
        this.prefs = prefs;
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                closeAction.actionPerformed(null);
            }

            public void windowOpened(WindowEvent e) {
                ed.requestFocus();
            }
        });
        setFocusTraversalPolicy(new LayoutFocusTraversalPolicy() {

            protected boolean accept(Component c) {
                return (super.accept(c) && (c instanceof FieldEditor));
            }
        });
        int prefHeight = (int) (GUIGlobals.PE_HEIGHT * GUIGlobals.FORM_HEIGHT[prefs.getInt("entryTypeFormHeightFactor")]);
        setSize(GUIGlobals.FORM_WIDTH[prefs.getInt("entryTypeFormWidth")], prefHeight);
        pan.setLayout(gbl);
        con.fill = GridBagConstraints.BOTH;
        con.weighty = 1;
        con.insets = new Insets(10, 5, 10, 5);
        String content = base.getPreamble();
        ed = new FieldTextArea(Globals.lang("Preamble"), ((content != null) ? content : ""));
        setupJTextComponent((FieldTextArea) ed);
        gbl.setConstraints(ed.getLabel(), con);
        pan.add(ed.getLabel());
        con.weightx = 1;
        gbl.setConstraints(ed.getPane(), con);
        pan.add(ed.getPane());
        conPane.add(pan, BorderLayout.CENTER);
        setTitle(Globals.lang("Edit preamble"));
    }

    private void setupJTextComponent(javax.swing.text.JTextComponent ta) {
        ta.getInputMap().put(prefs.getKey("Close preamble editor"), "close");
        ta.getActionMap().put("close", closeAction);
        ta.getInputMap().put(prefs.getKey("Preamble editor, store changes"), "store");
        ta.getActionMap().put("store", storeFieldAction);
        ta.getInputMap().put(prefs.getKey("Close preamble editor"), "close");
        ta.getActionMap().put("close", closeAction);
        ta.getInputMap().put(prefs.getKey("Undo"), "undo");
        ta.getActionMap().put("undo", undoAction);
        ta.getInputMap().put(prefs.getKey("Redo"), "redo");
        ta.getActionMap().put("redo", redoAction);
        ta.addFocusListener(new FieldListener());
    }

    public void updatePreamble() {
        ed.setText(base.getPreamble());
    }

    class FieldListener extends FocusAdapter {

        public void focusLost(FocusEvent e) {
            if (!e.isTemporary()) storeFieldAction.actionPerformed(new ActionEvent(e.getSource(), 0, ""));
        }
    }

    StoreFieldAction storeFieldAction = new StoreFieldAction();

    class StoreFieldAction extends AbstractAction {

        public StoreFieldAction() {
            super("Store field value");
            putValue(SHORT_DESCRIPTION, "Store field value");
        }

        public void actionPerformed(ActionEvent e) {
            String toSet = null;
            boolean set;
            if (ed.getText().length() > 0) toSet = ed.getText();
            if (toSet == null) {
                if (base.getPreamble() == null) set = false; else set = true;
            } else {
                if ((base.getPreamble() != null) && toSet.equals(base.getPreamble())) set = false; else set = true;
            }
            if (set) {
                panel.undoManager.addEdit(new UndoablePreambleChange(base, panel, base.getPreamble(), toSet));
                base.setPreamble(toSet);
                if ((toSet != null) && (toSet.length() > 0)) {
                    ed.setLabelColor(GUIGlobals.entryEditorLabelColor);
                    ed.setValidBackgroundColor();
                } else {
                    ed.setLabelColor(GUIGlobals.nullFieldColor);
                    ed.setValidBackgroundColor();
                }
                if (ed.getTextComponent().hasFocus()) ed.setActiveBackgroundColor();
                panel.markBaseChanged();
            }
        }
    }

    UndoAction undoAction = new UndoAction();

    class UndoAction extends AbstractAction {

        public UndoAction() {
            super("Undo", GUIGlobals.getImage("undo"));
            putValue(SHORT_DESCRIPTION, "Undo");
        }

        public void actionPerformed(ActionEvent e) {
            try {
                panel.runCommand("undo");
            } catch (Throwable ex) {
            }
        }
    }

    RedoAction redoAction = new RedoAction();

    class RedoAction extends AbstractAction {

        public RedoAction() {
            super("Undo", GUIGlobals.getImage("redo"));
            putValue(SHORT_DESCRIPTION, "Redo");
        }

        public void actionPerformed(ActionEvent e) {
            try {
                panel.runCommand("redo");
            } catch (Throwable ex) {
            }
        }
    }

    CloseAction closeAction = new CloseAction();

    class CloseAction extends AbstractAction {

        public CloseAction() {
            super(Globals.lang("Close window"));
        }

        public void actionPerformed(ActionEvent e) {
            storeFieldAction.actionPerformed(null);
            panel.preambleEditorClosing();
            dispose();
        }
    }

    public FieldEditor getFieldEditor() {
        return ed;
    }

    public void storeCurrentEdit() {
        storeFieldAction.actionPerformed(null);
    }
}
