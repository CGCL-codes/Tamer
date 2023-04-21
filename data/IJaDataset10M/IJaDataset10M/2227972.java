package de.at12.project.dnotes;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import de.at12.util.EasyDebug;

public class RemoveDataSrc extends JDialog {

    JPanel removeDataSrc = new JPanel();

    BorderLayout borderLayout1 = new BorderLayout();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    RemoveDataSrcPane removeDataSrcPane;

    DNotesFrame frame;

    EasyDebug d = new EasyDebug(this.getClass(), DNotes.DEBUG);

    int y, x;

    int ID;

    javax.swing.JTabbedPane jTabbedPane1 = new javax.swing.JTabbedPane();

    javax.swing.JPanel jpButtons = new javax.swing.JPanel();

    javax.swing.JButton jbOk = new javax.swing.JButton();

    javax.swing.JButton jbCancel = new javax.swing.JButton();

    javax.swing.JComboBox cbDataSrc = new javax.swing.JComboBox();

    javax.swing.JTextField tfDataSrcURL = new javax.swing.JTextField();

    javax.swing.JLabel jLabelDataSrc = new javax.swing.JLabel();

    javax.swing.JLabel jLabelDataSrcURL = new javax.swing.JLabel();

    public RemoveDataSrc(DNotesFrame _frame, String title, boolean modal) {
        super(_frame, title, modal);
        ID = new Integer(DNotes.getProperty("dataSrc.count")).intValue();
        frame = _frame;
        removeDataSrcPane = new RemoveDataSrcPane(frame);
        try {
            jbInit();
            pack();
            setSize(new Dimension(390, 290));
            show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        jbOk.setText(DNotes.getPropertyGui("common.remove"));
        jbOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jbOk_actionPerformed(e);
            }
        });
        jbCancel.setText(DNotes.getPropertyGui("common.cancel"));
        jbCancel.setHorizontalAlignment(SwingConstants.RIGHT);
        jbCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jbCancel_actionPerformed(e);
            }
        });
        removeDataSrc.setLayout(borderLayout1);
        getContentPane().add(removeDataSrc);
        removeDataSrc.add(removeDataSrcPane, BorderLayout.CENTER);
        removeDataSrc.add(jpButtons, BorderLayout.SOUTH);
        jpButtons.add(jbOk, null);
        jpButtons.add(jbCancel, null);
    }

    void jbOk_actionPerformed(ActionEvent e) {
        removeDataSrcPane.save();
        dispose();
    }

    void jbCancel_actionPerformed(ActionEvent e) {
        dispose();
    }
}
