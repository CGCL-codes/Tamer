package atai.gui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import atai.questions.ATEntity;
import atai.questions.ATRole;
import atai.questions.ATView;

class ValueSelectionDialog extends JDialog {

    JList list;

    JTextField newValue = new JTextField(20);

    JLabel message = new JLabel();

    JComboBox typeSelector = null;

    Hashtable<String, String> values = new Hashtable<String, String>();

    private Vector<String> keys;

    public ValueSelectionDialog() {
        super((Frame) null, "Value selection", true);
        Box content = javax.swing.Box.createVerticalBox();
        list = new JList();
        JButton accept = new JButton("Accept");
        JButton cancel = new JButton("Cancel");
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.add(new JLabel("Choose one from the existing entities:"));
        content.add(p1);
        content.add(new JScrollPane(list));
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.add(new JLabel("Or write the name of a new one and its type:"));
        content.add(p2);
        content.add(newValue);
        typeSelector = new JComboBox();
        content.add(typeSelector);
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p3.add(message);
        content.add(p3);
        newValue.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent arg0) {
            }

            public void keyReleased(KeyEvent arg0) {
                if (newValue.getText() == null || newValue.getText().equals("")) {
                    list.setEnabled(true);
                    message.setText("");
                    pack();
                } else {
                    list.setEnabled(false);
                    message.setText("Delete the characters of the field to enable the list");
                    pack();
                }
            }

            public void keyTyped(KeyEvent arg0) {
            }
        });
        JPanel decision = new JPanel();
        decision.add(accept);
        decision.add(cancel);
        content.add(decision);
        this.getContentPane().add(content);
        accept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                list.clearSelection();
                setVisible(false);
            }
        });
    }

    public void setValues(Hashtable<String, String> values, Vector<String> types) {
        this.values = values;
        Vector<String> list = new Vector<String>();
        keys = new Vector<String>();
        for (String value : values.keySet()) {
            list.add(value + ":" + values.get(value));
            keys.add(value);
        }
        this.list.setListData(list.toArray());
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel(new Vector<String>(types));
        typeSelector.setModel(dcbm);
        this.pack();
        this.repaint();
    }

    public String getSelected() {
        if (newValue.getText() == null || newValue.getText().equals("")) {
            if (list.getSelectedValue() == null) return null; else return this.keys.get(list.getSelectedIndex());
        } else return newValue.getText();
    }

    public String getSelectedType() {
        if (newValue.getText() == null || newValue.getText().equals("")) return null; else return typeSelector.getSelectedItem().toString();
    }
}
