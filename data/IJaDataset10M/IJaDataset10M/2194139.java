package org.scrinch.gui;

public class AboutBox extends javax.swing.JDialog {

    /** Creates new form AboutBox */
    public AboutBox(java.awt.Frame parent, String message, String title) {
        super(parent, true);
        initComponents();
        this.setTitle(title);
        this.lbTxt.setText(message);
        this.setSize(700, 530);
        int x = parent.getX() + (int) ((parent.getWidth() - this.getWidth()) / 2.0);
        int y = parent.getY() + (int) ((parent.getHeight() - this.getHeight()) / 2.0);
        this.setLocation(x, y);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jXGlassBox1 = new org.jdesktop.swingx.JXGlassBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lbTxt = new javax.swing.JLabel();
        lbLogo = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        btOk = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jXGlassBox1.setDismissOnClick(false);
        jXGlassBox1.setLayout(new java.awt.BorderLayout());
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel4.setLayout(new java.awt.BorderLayout());
        lbTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTxt.setText("About text here");
        jPanel4.add(lbTxt, java.awt.BorderLayout.SOUTH);
        lbLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/scrinch/gui/logo.png")));
        jPanel4.add(lbLogo, java.awt.BorderLayout.CENTER);
        jPanel1.add(jPanel4, java.awt.BorderLayout.NORTH);
        jPanel3.setLayout(new java.awt.GridBagLayout());
        jTextPane1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        jTextPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextPane1.setContentType("text/html");
        jTextPane1.setEditable(false);
        jTextPane1.setText("<html> <body> <center><U>GNU General Public License</U></center><BR>Scrinch is a stand-alone Swing application that helps managing your projects based on Agile principles.<BR><BR> Copyright (C) 2007  Julien Piaser, Jerome Layat, Peter Fluckiger, Christian Lebaudy, Jean-Marc Borer Scrinch is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.<BR><BR>Scrinch is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.<BR><BR>See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program.  If not, see <a href=\"http://www.gnu.org/licenses\">GNU General Public License</a>.</body> </html>");
        jTextPane1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel3.add(jTextPane1, gridBagConstraints);
        jPanel2.setLayout(new java.awt.BorderLayout());
        btOk.setText("OK");
        btOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOkActionPerformed(evt);
            }
        });
        jPanel2.add(btOk, java.awt.BorderLayout.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel3.add(jPanel2, gridBagConstraints);
        jPanel1.add(jPanel3, java.awt.BorderLayout.SOUTH);
        jXGlassBox1.add(jPanel1, java.awt.BorderLayout.CENTER);
        getContentPane().add(jXGlassBox1, java.awt.BorderLayout.NORTH);
        pack();
    }

    private void btOkActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private javax.swing.JButton btOk;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JTextPane jTextPane1;

    private org.jdesktop.swingx.JXGlassBox jXGlassBox1;

    private javax.swing.JLabel lbLogo;

    private javax.swing.JLabel lbTxt;
}
