package vydavky.windows.utils;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import vydavky.client.utils.ClientUtils;

/**
 * Editor jednoducheho textoveho ciselniku. Podporuje pridavanie a odoberanie
 * hodnot.
 *
 * @author vitasek
 */
public class EditorCiselniku extends JPanel {

    private static final long serialVersionUID = 1754121L;

    private class MyListModel implements ListModel {

        @Override
        public int getSize() {
            return EditorCiselniku.this.model.size();
        }

        @Override
        public Object getElementAt(int index) {
            return EditorCiselniku.this.model.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }

    private final List<String> model = new ArrayList<String>();

    public EditorCiselniku() {
        initComponents();
        list.setModel(new MyListModel());
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                buttonRemove.setEnabled(list.getSelectedValue() != null);
            }
        });
    }

    public List<String> getModel() {
        return model;
    }

    public void setModel(final List<String> newModel) {
        this.model.clear();
        this.model.addAll(newModel);
        list.setModel(new MyListModel());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        text = new javax.swing.JTextField();
        buttonAdd = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        scrollPane = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        setLayout(new java.awt.GridBagLayout());
        text.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                textKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(text, gridBagConstraints);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("vydavky/i18n");
        buttonAdd.setText(bundle.getString("PRIDAŤ"));
        buttonAdd.setEnabled(false);
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(buttonAdd, gridBagConstraints);
        buttonRemove.setText(bundle.getString("ODOBRAT"));
        buttonRemove.setEnabled(false);
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(buttonRemove, gridBagConstraints);
        list.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scrollPane.setViewportView(list);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(scrollPane, gridBagConstraints);
    }

    private void buttonAddActionPerformed(java.awt.event.ActionEvent evt) {
        if (!ClientUtils.isEmpty(text.getText())) {
            model.add(text.getText());
            list.setModel(new MyListModel());
            text.setText(null);
            buttonAdd.setEnabled(false);
        }
    }

    private void textKeyTyped(java.awt.event.KeyEvent evt) {
        buttonAdd.setEnabled(!ClientUtils.isEmpty(text.getText()));
    }

    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {
        if (list.getSelectedValue() != null) {
            model.remove(list.getSelectedValue());
            list.setModel(new MyListModel());
        }
    }

    private javax.swing.JButton buttonAdd;

    private javax.swing.JButton buttonRemove;

    private javax.swing.JList list;

    private javax.swing.JScrollPane scrollPane;

    private javax.swing.JTextField text;
}
