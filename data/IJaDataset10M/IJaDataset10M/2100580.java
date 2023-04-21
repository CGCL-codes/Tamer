package view;

import controller.TransitionManager;
import java.awt.Dimension;
import javax.swing.JFrame;

public class MultiPage extends javax.swing.JPanel {

    public MultiPage() {
        initComponents();
    }

    private void initComponents() {
        lblTitle = new javax.swing.JLabel();
        lblTitle_2 = new javax.swing.JLabel();
        pnlTitleLine = new javax.swing.JPanel();
        pnlButton = new javax.swing.JPanel();
        btnHost = new javax.swing.JButton();
        joinGameButton = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        setBackground(new java.awt.Color(0, 0, 0));
        lblTitle.setFont(new java.awt.Font("Playbill", 0, 175));
        lblTitle.setForeground(new java.awt.Color(102, 255, 255));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTitle.setText("  DUCK  ");
        lblTitle_2.setFont(new java.awt.Font("Playbill", 0, 175));
        lblTitle_2.setForeground(new java.awt.Color(102, 255, 255));
        lblTitle_2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTitle_2.setText("  HUNT  ");
        pnlTitleLine.setBackground(new java.awt.Color(255, 204, 102));
        javax.swing.GroupLayout pnlTitleLineLayout = new javax.swing.GroupLayout(pnlTitleLine);
        pnlTitleLine.setLayout(pnlTitleLineLayout);
        pnlTitleLineLayout.setHorizontalGroup(pnlTitleLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 507, Short.MAX_VALUE));
        pnlTitleLineLayout.setVerticalGroup(pnlTitleLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 9, Short.MAX_VALUE));
        pnlButton.setOpaque(false);
        btnHost.setBackground(new java.awt.Color(0, 0, 0));
        btnHost.setFont(new java.awt.Font("Arial Black", 0, 36));
        btnHost.setForeground(new java.awt.Color(255, 204, 102));
        btnHost.setText("HOST GAME");
        btnHost.setBorderPainted(false);
        btnHost.setContentAreaFilled(false);
        btnHost.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHostActionPerformed(evt);
            }
        });
        joinGameButton.setBackground(new java.awt.Color(0, 0, 0));
        joinGameButton.setFont(new java.awt.Font("Arial Black", 0, 36));
        joinGameButton.setForeground(new java.awt.Color(255, 204, 102));
        joinGameButton.setText("JOIN GAME");
        joinGameButton.setBorderPainted(false);
        joinGameButton.setContentAreaFilled(false);
        joinGameButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                joinGameButtonActionPerformed(evt);
            }
        });
        btnBack.setBackground(new java.awt.Color(0, 0, 0));
        btnBack.setFont(new java.awt.Font("Arial Black", 0, 36));
        btnBack.setForeground(new java.awt.Color(255, 204, 102));
        btnBack.setText("BACK");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout pnlButtonLayout = new javax.swing.GroupLayout(pnlButton);
        pnlButton.setLayout(pnlButtonLayout);
        pnlButtonLayout.setHorizontalGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(btnHost, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE).addComponent(joinGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE).addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE));
        pnlButtonLayout.setVerticalGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(pnlButtonLayout.createSequentialGroup().addComponent(btnHost).addGap(10, 10, 10).addComponent(joinGameButton).addGap(10, 10, 10).addComponent(btnBack)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(pnlButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(lblTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(lblTitle_2, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE).addGap(17, 17, 17))).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(106, 106, 106).addComponent(pnlTitleLine, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(131, 131, 131)))));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(37, 37, 37).addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pnlTitleLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(lblTitle_2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(29, 29, 29).addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(31, 31, 31)));
    }

    private void btnHostActionPerformed(java.awt.event.ActionEvent evt) {
        TransitionManager.showMultiServerPage();
    }

    private void joinGameButtonActionPerformed(java.awt.event.ActionEvent evt) {
        TransitionManager.showMultiClientPage();
    }

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
        TransitionManager.showFirstPage();
    }

    private javax.swing.JButton btnBack;

    private javax.swing.JButton btnHost;

    private javax.swing.JButton joinGameButton;

    private javax.swing.JLabel lblTitle;

    private javax.swing.JLabel lblTitle_2;

    private javax.swing.JPanel pnlButton;

    private javax.swing.JPanel pnlTitleLine;
}
