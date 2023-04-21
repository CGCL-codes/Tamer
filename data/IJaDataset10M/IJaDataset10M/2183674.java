package src.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.backend.*;

public class ObjsChangeFrame extends JFrame {

    private Model model;

    private ObjsTableModel tableModel;

    private int levelIndex;

    public ObjsChangeFrame(Model model, JTable table, ObjsTableModel tableModel, int levelIndex) {
        this.model = model;
        this.levelIndex = levelIndex;
        this.tableModel = tableModel;
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        getContentPane().setLayout(null);
        jLabel1.setFont(new java.awt.Font("Dialog", Font.PLAIN, 15));
        jLabel1.setText("Change All");
        jLabel1.setBounds(new Rectangle(130, 14, 132, 29));
        jLabel4.setText("From:");
        jLabel4.setBounds(new Rectangle(15, 51, 103, 19));
        jLabel5.setText("To:");
        jLabel5.setBounds(new Rectangle(15, 120, 94, 18));
        jLabel7.setText("Subtype:");
        jLabel7.setBounds(new Rectangle(15, 146, 103, 19));
        btnCancel.setBounds(new Rectangle(83, 202, 80, 31));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new ObjsChangeFrame_btnCancel_actionAdapter(this));
        btnOK.setBounds(new Rectangle(173, 202, 80, 31));
        btnOK.setText("OK");
        btnOK.addActionListener(new ObjsChangeFrame_btnOK_actionAdapter(this));
        jLabel3.setText("Subtype:");
        jLabel3.setBounds(new Rectangle(178, 71, 103, 19));
        boxFromType.setBounds(new Rectangle(15, 96, 143, 18));
        boxFromType.addActionListener(new ObjsChangeFrame_boxFromType_actionAdapter(this));
        jLabel2.setBounds(new Rectangle(15, 71, 103, 19));
        boxFromSubtype.setBounds(new Rectangle(178, 96, 143, 18));
        boxToSubtype.setBounds(new Rectangle(15, 170, 143, 18));
        this.getContentPane().add(jLabel4);
        this.getContentPane().add(jLabel5);
        this.getContentPane().add(jLabel7);
        this.getContentPane().add(jLabel1);
        this.getContentPane().add(boxFromType);
        this.getContentPane().add(jLabel2);
        this.getContentPane().add(jLabel3);
        this.getContentPane().add(btnOK);
        this.getContentPane().add(btnCancel);
        this.getContentPane().add(boxFromSubtype);
        this.getContentPane().add(boxToSubtype);
        jLabel2.setText("Type:");
        for (int i = 0; i < StaticFunctions.OBJS_TYPES.length; i++) {
            boxFromType.addItem(StaticFunctions.OBJS_TYPES[i]);
        }
        fillSubtypeBoxes();
        setSize(350, 280);
        setTitle("Change all ...");
        setResizable(false);
        StaticFunctions.centerFrame(this);
        setVisible(true);
    }

    JLabel jLabel1 = new JLabel();

    JLabel jLabel2 = new JLabel();

    JLabel jLabel3 = new JLabel();

    JLabel jLabel4 = new JLabel();

    JComboBox boxFromType = new JComboBox();

    JLabel jLabel5 = new JLabel();

    JLabel jLabel7 = new JLabel();

    JButton btnCancel = new JButton();

    JButton btnOK = new JButton();

    JComboBox boxFromSubtype = new JComboBox();

    JComboBox boxToSubtype = new JComboBox();

    public void fillSubtypeBoxes() {
        String type = (String) boxFromType.getSelectedItem();
        if (type.equals("Monster")) {
            boxFromSubtype.removeAllItems();
            boxToSubtype.removeAllItems();
            for (int i = 0; i < StaticFunctions.OBJS_SUBTYPES[0].length; i++) {
                boxFromSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[0][i]);
                boxToSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[0][i]);
            }
        } else if (type.equals("Scenery")) {
            boxFromSubtype.removeAllItems();
            boxToSubtype.removeAllItems();
            int environment = model.getEnvironment(levelIndex);
            for (int i = 0; i < StaticFunctions.scenery.length; i++) {
                boxFromSubtype.addItem(StaticFunctions.scenery[i]);
                boxToSubtype.addItem(StaticFunctions.scenery[i]);
            }
        } else if (type.equals("Item")) {
            boxFromSubtype.removeAllItems();
            boxToSubtype.removeAllItems();
            for (int i = 0; i < StaticFunctions.OBJS_SUBTYPES[2].length; i++) {
                boxFromSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[2][i]);
                boxToSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[2][i]);
            }
        } else if (type.equals("Player")) {
            boxFromSubtype.removeAllItems();
            boxToSubtype.removeAllItems();
            for (int i = 0; i < StaticFunctions.OBJS_SUBTYPES[3].length; i++) {
                boxFromSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[3][i]);
                boxToSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[3][i]);
            }
        } else if (type.equals("Goal")) {
            boxFromSubtype.removeAllItems();
            boxToSubtype.removeAllItems();
            for (int i = 0; i < StaticFunctions.OBJS_SUBTYPES[4].length; i++) {
                boxFromSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[4][i]);
                boxToSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[4][i]);
            }
        } else if (type.equals("Sound")) {
            boxFromSubtype.removeAllItems();
            boxToSubtype.removeAllItems();
            for (int i = 0; i < StaticFunctions.OBJS_SUBTYPES[5].length; i++) {
                boxFromSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[5][i]);
                boxToSubtype.addItem(StaticFunctions.OBJS_SUBTYPES[5][i]);
            }
        }
    }

    public void btnCancel_actionPerformed(ActionEvent e) {
        model.removeChildFrame(this);
        this.dispose();
    }

    public void btnOK_actionPerformed(ActionEvent e) {
        try {
            for (int i = 0; i < tableModel.getData().size(); i++) {
                if (((String) tableModel.getValueAt(i, ObjsTableModel.OBJS_TYPE)).equals(boxFromType.getSelectedItem())) {
                    if (((String) tableModel.getValueAt(i, ObjsTableModel.OBJS_SUBTYPE)).equals(boxFromSubtype.getSelectedItem())) {
                        tableModel.setValueAt(boxToSubtype.getSelectedItem(), i, ObjsTableModel.OBJS_SUBTYPE);
                        tableModel.fireTableCellUpdated(i, ObjsTableModel.OBJS_SUBTYPE);
                    }
                }
            }
        } catch (Exception ex) {
        }
        model.removeChildFrame(this);
        this.dispose();
    }

    public void boxFromType_actionPerformed(ActionEvent e) {
        fillSubtypeBoxes();
    }
}

class ObjsChangeFrame_boxFromType_actionAdapter implements ActionListener {

    private ObjsChangeFrame adaptee;

    ObjsChangeFrame_boxFromType_actionAdapter(ObjsChangeFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.boxFromType_actionPerformed(e);
    }
}

class ObjsChangeFrame_btnOK_actionAdapter implements ActionListener {

    private ObjsChangeFrame adaptee;

    ObjsChangeFrame_btnOK_actionAdapter(ObjsChangeFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnOK_actionPerformed(e);
    }
}

class ObjsChangeFrame_btnCancel_actionAdapter implements ActionListener {

    private ObjsChangeFrame adaptee;

    ObjsChangeFrame_btnCancel_actionAdapter(ObjsChangeFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnCancel_actionPerformed(e);
    }
}
