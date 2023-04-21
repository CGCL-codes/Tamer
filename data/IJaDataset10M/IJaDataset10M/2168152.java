package applications.pim;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.nex.context.*;

/**
 * Title:        Nexist
 * Description:  Collaboratory testbed
 * Copyright:    Copyright (c) 2001 Jack Park
 * Company:      nex
 * @author       Jack Park
 * @version 1.0
 * @license  NexistLicense (based on Apache)
 */
public class NewTopicDialog extends JDialog {

    NEXClient adaptor = null;

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel jPanel1 = new JPanel();

    JButton okButton = new JButton();

    JButton cancelButton = new JButton();

    JPanel jPanel2 = new JPanel();

    JLabel jLabel1 = new JLabel();

    JTextField idField = new JTextField();

    JPanel jPanel3 = new JPanel();

    BorderLayout borderLayout2 = new BorderLayout();

    JPanel jPanel4 = new JPanel();

    FlowLayout flowLayout1 = new FlowLayout();

    JLabel jLabel2 = new JLabel();

    JTextField baseNameField = new JTextField();

    JPanel jPanel5 = new JPanel();

    BorderLayout borderLayout3 = new BorderLayout();

    JPanel jPanel6 = new JPanel();

    JLabel jLabel3 = new JLabel();

    JTextField psiField = new JTextField();

    public NewTopicDialog(NEXClient h) {
        this.adaptor = h;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        this.setTitle("New Topic");
        okButton.setBorder(BorderFactory.createRaisedBevelBorder());
        okButton.setText("    OK    ");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });
        jLabel1.setText("Topic ID");
        idField.setPreferredSize(new Dimension(200, 21));
        jPanel3.setLayout(borderLayout2);
        jPanel4.setLayout(flowLayout1);
        jLabel2.setText("Base Name");
        baseNameField.setPreferredSize(new Dimension(200, 21));
        jPanel5.setLayout(borderLayout3);
        jLabel3.setText("PSI URI");
        psiField.setPreferredSize(new Dimension(200, 21));
        this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
        jPanel1.add(okButton, null);
        jPanel1.add(cancelButton, null);
        this.getContentPane().add(jPanel2, BorderLayout.NORTH);
        jPanel2.add(jLabel1, null);
        jPanel2.add(idField, null);
        this.getContentPane().add(jPanel3, BorderLayout.CENTER);
        jPanel3.add(jPanel4, BorderLayout.NORTH);
        jPanel4.add(jLabel2, null);
        jPanel4.add(baseNameField, null);
        jPanel3.add(jPanel5, BorderLayout.CENTER);
        jPanel5.add(jPanel6, BorderLayout.NORTH);
        jPanel6.add(jLabel3, null);
        jPanel6.add(psiField, null);
    }

    public void showSelf() {
        idField.setText("");
        baseNameField.setText("");
        psiField.setText("");
        this.show();
    }

    void okButton_actionPerformed(ActionEvent e) {
        adaptor.newTopic(idField.getText(), baseNameField.getText(), psiField.getText(), null);
        this.setVisible(false);
    }

    void cancelButton_actionPerformed(ActionEvent e) {
        this.setVisible(false);
    }
}
