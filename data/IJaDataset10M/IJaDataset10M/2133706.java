package org.softmed.rest.editor.comps.utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;
import org.restlet.data.Response;
import org.softmed.rest.editor.EditorUtil;
import org.softmed.rest.editor.commons.SwingUtil;
import org.softmed.rest.editor.comps.SimpleEditor;
import org.softmed.swing.IconManager;

public class DeletePanel extends JPanel implements ActionListener {

    JButton delete = new JButton(IconManager.remove);

    JButton deleteAll = new JButton(IconManager.cancel);

    SimpleEditor editor;

    boolean deleted = false;

    public DeletePanel(SimpleEditor editor) {
        setButtonBorder(BorderFactory.createLineBorder(Color.GRAY));
        setBackground(Color.white);
        this.editor = editor;
        MigLayout layout = new MigLayout("left,insets 0,gap 0");
        setLayout(layout);
        SwingUtil.setSize(this, 60, 20);
        SwingUtil.setSize(delete, EditorUtil.buttonHeightAndWidth, EditorUtil.buttonHeightAndWidth);
        SwingUtil.setSize(deleteAll, EditorUtil.buttonHeightAndWidth, EditorUtil.buttonHeightAndWidth);
        add(deleteAll);
        add(delete);
        delete.setActionCommand("delete");
        deleteAll.setActionCommand("deleteAll");
        deleteAll.addActionListener(this);
        delete.addActionListener(this);
        delete.setToolTipText("Delete this instance only. All referenced objects in fields and lists remain intact.");
        deleteAll.setToolTipText("Delete this object and all children too. All referenced objects in fields and lists are deleted!");
    }

    private void setButtonBorder(Border border) {
        delete.setBorder(border);
        deleteAll.setBorder(border);
    }

    public JButton getDelete() {
        return delete;
    }

    public JButton getDeleteAll() {
        return deleteAll;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        deleted = false;
        try {
            Object entity = editor.getEntity();
            Method getter = entity.getClass().getMethod("getUri");
            String uri = (String) getter.invoke(entity, null);
            Object source = e.getSource();
            Response response = null;
            if (source == delete) {
                int duh = JOptionPane.showConfirmDialog(this, "WARNING\n" + "This will permantly delete the object from the database.\n" + "Are you sure you want to proceed?", "WARNING", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (duh == JOptionPane.NO_OPTION) return;
                EditorUtil.showUpdatePanel();
                response = editor.getClient().delete(uri);
                deleted = true;
            } else if (source == deleteAll) {
                int duh = JOptionPane.showConfirmDialog(this, "WARNING\n" + "This will permantly delete the object and all referenced objects from the database.\n" + "Are you sure you want to proceed?", "WARNING", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (duh == JOptionPane.NO_OPTION) return;
                EditorUtil.showUpdatePanel();
                response = editor.getClient().delete(uri + "?all");
                deleted = true;
            }
            EditorUtil.hideUpdatePanel();
            if (response.getStatus().getCode() != 200) JOptionPane.showMessageDialog(this, "Couldn't delete!!!", "Error", JOptionPane.ERROR_MESSAGE);
            Container parent = editor.getParent();
            Container grandParent = parent.getParent();
            if (grandParent instanceof JViewport) {
                grandParent = grandParent.getParent();
                JComponent scroll = (JComponent) grandParent;
                grandParent = grandParent.getParent();
                JTabbedPane tabPane = (JTabbedPane) grandParent;
                int index = tabPane.indexOfComponent(scroll);
                tabPane.remove(index);
            } else parent.remove(editor);
        } catch (Throwable t) {
            EditorUtil.hideUpdatePanel();
            t.printStackTrace();
            JOptionPane.showMessageDialog(this, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setActionListeners(ActionListener listener) {
        ActionListener[] listeners = delete.getActionListeners();
        for (ActionListener actionListener : listeners) {
            delete.removeActionListener(actionListener);
        }
        listeners = deleteAll.getActionListeners();
        for (ActionListener actionListener : listeners) {
            deleteAll.removeActionListener(actionListener);
        }
        delete.addActionListener(listener);
        deleteAll.addActionListener(listener);
        delete.addActionListener(this);
        deleteAll.addActionListener(this);
    }

    public void removeActionListeners() {
        ActionListener[] listeners = delete.getActionListeners();
        for (ActionListener actionListener : listeners) {
            delete.removeActionListener(actionListener);
        }
        listeners = deleteAll.getActionListeners();
        for (ActionListener actionListener : listeners) {
            deleteAll.removeActionListener(actionListener);
        }
        delete.addActionListener(this);
        deleteAll.addActionListener(this);
    }

    public boolean isDeleted() {
        return deleted;
    }
}
