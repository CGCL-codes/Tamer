package org.visu.ui.wins;

import java.awt.*;
import javax.swing.*;
import org.visu.ui.media.*;
import com.borland.jbcl.layout.*;
import java.awt.event.*;

/**
 * @author Vladimir Stanciu
 * @version 1.0
 */
public class AboutDialog extends JDialog {

    JPanel panel1 = new JPanel();

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel jPanel1 = new JPanel();

    JPanel jPanel2 = new JPanel();

    JPanel jPanel3 = new JPanel();

    JPanel jPanel4 = new JPanel();

    JLabel jLabel1 = new JLabel();

    BorderLayout borderLayout2 = new BorderLayout();

    ImageIcon dbplusIcon = MediaFile.getIcon(MediaFile.DB_PLUS);

    JButton okButton = new JButton();

    JPanel jPanel5 = new JPanel();

    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();

    JPanel jPanel6 = new JPanel();

    JLabel jLabel2 = new JLabel();

    JLabel jLabel3 = new JLabel();

    JLabel jLabel4 = new JLabel();

    JLabel jLabel5 = new JLabel();

    JLabel jLabel6 = new JLabel();

    JLabel jLabel7 = new JLabel();

    public AboutDialog(Frame frame, String title) {
        super(frame, title, true);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setLocationRelativeTo(frame);
        getRootPane().setDefaultButton(okButton);
    }

    public AboutDialog() {
        this(null, "");
    }

    private void jbInit() throws Exception {
        panel1.setLayout(borderLayout1);
        jPanel1.setBackground(Color.white);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel1.setMinimumSize(new Dimension(38, 50));
        jPanel1.setPreferredSize(new Dimension(38, 50));
        jPanel1.setLayout(borderLayout2);
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setIcon(dbplusIcon);
        jLabel1.setText("VisualPostgres");
        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButton_actionPerformed(e);
            }
        });
        jPanel5.setLayout(verticalFlowLayout1);
        jLabel2.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel2.setForeground(Color.blue);
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("VisualPostgres");
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("version 0.0.1");
        jPanel6.setBorder(null);
        jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel4.setText("pre-alpha");
        jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel5.setText("Author: Vladimir Stanciu");
        jLabel6.setFont(new java.awt.Font("Dialog", 2, 11));
        jLabel6.setToolTipText("");
        jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel6.setText("This is FREE SOFTWARE released under the terms and conditions of");
        jLabel7.setFont(new java.awt.Font("Dialog", 2, 11));
        jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel7.setText("GNU GPL version 2.");
        getContentPane().add(panel1);
        panel1.add(jPanel1, BorderLayout.NORTH);
        jPanel1.add(jLabel1, BorderLayout.CENTER);
        panel1.add(jPanel2, BorderLayout.WEST);
        panel1.add(jPanel3, BorderLayout.EAST);
        panel1.add(jPanel4, BorderLayout.SOUTH);
        jPanel4.add(okButton, null);
        panel1.add(jPanel5, BorderLayout.CENTER);
        jPanel5.add(jPanel6, null);
        jPanel5.add(jLabel2, null);
        jPanel5.add(jLabel3, null);
        jPanel5.add(jLabel4, null);
        jPanel5.add(jLabel5, null);
        jPanel5.add(jLabel6, null);
        jPanel5.add(jLabel7, null);
    }

    void okButton_actionPerformed(ActionEvent e) {
        dispose();
    }
}
