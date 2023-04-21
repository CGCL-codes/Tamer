package odeoncinemaassignment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class LanguagePickFrame extends javax.swing.JFrame {

    public LanguagePickFrame() {
        initComponents();
    }

    public static final String DATE_FORMAT_NOW = "dd MMM yyyy hh:mm";

    @SuppressWarnings("unchecked")
    private void initComponents() {
        languageButtonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        odeonLogoLbl = new javax.swing.JLabel();
        flagsPanel = new javax.swing.JPanel();
        franceButton = new javax.swing.JToggleButton();
        irelandButton = new javax.swing.JToggleButton();
        spainButton = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        exitButtonFlagScreen = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 102, 255));
        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        odeonLogoLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/odeon_logo.png")));
        jPanel1.add(odeonLogoLbl);
        flagsPanel.setAutoscrolls(true);
        languageButtonGroup.add(franceButton);
        franceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/France-128.png")));
        franceButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 0, 0), 2));
        franceButton.setBorderPainted(false);
        franceButton.setContentAreaFilled(false);
        franceButton.setOpaque(true);
        franceButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                franceButtonMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                franceButtonMouseExited(evt);
            }
        });
        flagsPanel.add(franceButton);
        languageButtonGroup.add(irelandButton);
        irelandButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Ireland-128.png")));
        irelandButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 0, 0), 2));
        irelandButton.setBorderPainted(false);
        irelandButton.setContentAreaFilled(false);
        irelandButton.setOpaque(true);
        irelandButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                irelandButtonMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                irelandButtonMouseExited(evt);
            }
        });
        irelandButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irelandButtonActionPerformed(evt);
            }
        });
        flagsPanel.add(irelandButton);
        languageButtonGroup.add(spainButton);
        spainButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Spain-128.png")));
        spainButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 0, 0), 2));
        spainButton.setBorderPainted(false);
        spainButton.setContentAreaFilled(false);
        spainButton.setOpaque(true);
        spainButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                spainButtonMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                spainButtonMouseExited(evt);
            }
        });
        flagsPanel.add(spainButton);
        exitButtonFlagScreen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/house.png")));
        exitButtonFlagScreen.setText("Exit");
        exitButtonFlagScreen.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonFlagScreenActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap(386, Short.MAX_VALUE).addComponent(exitButtonFlagScreen).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(exitButtonFlagScreen)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGap(547, 547, 547).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()).addComponent(flagsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1024, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(flagsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 337, Short.MAX_VALUE).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        pack();
    }

    private void exitButtonFlagScreenActionPerformed(java.awt.event.ActionEvent evt) {
        utilities.exitApp();
    }

    private void irelandButtonActionPerformed(java.awt.event.ActionEvent evt) {
        homeScreenFrame newHomeScreenFrame = new homeScreenFrame();
        newHomeScreenFrame.setVisible(true);
        homeScreenFrame.currentTime.setText(now());
    }

    private void irelandButtonMouseEntered(java.awt.event.MouseEvent evt) {
        irelandButton.setBorderPainted(true);
    }

    private void irelandButtonMouseExited(java.awt.event.MouseEvent evt) {
        irelandButton.setBorderPainted(false);
    }

    private void franceButtonMouseEntered(java.awt.event.MouseEvent evt) {
        franceButton.setBorderPainted(true);
    }

    private void franceButtonMouseExited(java.awt.event.MouseEvent evt) {
        franceButton.setBorderPainted(false);
    }

    private void spainButtonMouseEntered(java.awt.event.MouseEvent evt) {
        spainButton.setBorderPainted(true);
    }

    private void spainButtonMouseExited(java.awt.event.MouseEvent evt) {
        spainButton.setBorderPainted(false);
    }

    public static String now() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LanguagePickFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LanguagePickFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LanguagePickFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LanguagePickFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new LanguagePickFrame().setVisible(true);
            }
        });
    }

    private javax.swing.JButton exitButtonFlagScreen;

    private javax.swing.JPanel flagsPanel;

    private javax.swing.JToggleButton franceButton;

    private javax.swing.JToggleButton irelandButton;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel3;

    private javax.swing.ButtonGroup languageButtonGroup;

    private javax.swing.JLabel odeonLogoLbl;

    private javax.swing.JToggleButton spainButton;
}
