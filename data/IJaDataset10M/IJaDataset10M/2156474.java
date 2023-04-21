package expenser;

import java.awt.BorderLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author  Jacek
 */
public class Home extends javax.swing.JPanel {

    /** Creates new form Home */
    private UIManager.LookAndFeelInfo looks[];

    int look = 0;

    Core myCore = null;

    Connector myConnector = null;

    public Home() {
        initComponents();
        ClassLoader cldr = this.getClass().getClassLoader();
        java.net.URL imageURL = cldr.getResource("images/main2.jpg");
        ImageIcon myImage = new ImageIcon(imageURL);
        jLabel.setIcon(myImage);
        jLookFeel.setModel(new DefaultListModel());
        looks = UIManager.getInstalledLookAndFeels();
        for (int i = 0; i < looks.length; i++) {
            ((DefaultListModel) jLookFeel.getModel()).addElement(looks[i].getName());
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jHost = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLookFeel = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jConnect = new javax.swing.JButton();
        jLabConnect = new javax.swing.JLabel();
        jPass = new javax.swing.JPasswordField();
        jLabel = new javax.swing.JLabel();
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(590, 500));
        jLabel1.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11));
        jLabel1.setText("Połaczenie z serwerem MySQL");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, -1, -1));
        jHost.setText("jdbc:mysql://localhost:3306/dymczak");
        jHost.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHostActionPerformed(evt);
            }
        });
        add(jHost, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 230, -1));
        jLabel2.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 11));
        jLabel2.setText("adres:");
        add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 110, -1, 20));
        jUser.setText("root");
        add(jUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 100, -1));
        jLabel3.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 11));
        jLabel3.setText("login:");
        add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, -1, 20));
        jLabel4.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 11));
        jLabel4.setText("hasło:");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 170, -1, 20));
        jLabel5.setFont(new java.awt.Font("MS Reference Sans Serif", 1, 11));
        jLabel5.setText("Dostępne style wyświetlania");
        add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, -1, -1));
        jScrollPane1.setViewportView(jLookFeel);
        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 280, 230, 80));
        jButton2.setText("Zmień");
        jButton2.setBorderPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 370, 80, -1));
        jConnect.setText("Połącz");
        jConnect.setBorderPainted(false);
        jConnect.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jConnectActionPerformed(evt);
            }
        });
        add(jConnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 170, 80, -1));
        jLabConnect.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 11));
        jLabConnect.setForeground(new java.awt.Color(51, 51, 255));
        jLabConnect.setText("Podaj parametry serwera...");
        add(jLabConnect, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, -1));
        jPass.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 9));
        jPass.setText("jd1986");
        jPass.setEchoChar('●');
        add(jPass, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 172, 100, -1));
        jLabel.setBackground(new java.awt.Color(255, 255, 255));
        jLabel.setLabelFor(this);
        jLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel.setAlignmentY(0.0F);
        jLabel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelMouseClicked(evt);
            }
        });
        jLabel.addAncestorListener(new javax.swing.event.AncestorListener() {

            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }

            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                jLabelAncestorRemoved(evt);
            }
        });
        add(jLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, -50, 650, 480));
    }

    private void jLabelMouseClicked(java.awt.event.MouseEvent evt) {
    }

    private void jHostActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        changeTheLookAndFeel(jLookFeel.getSelectedIndex());
    }

    private void jLabelAncestorRemoved(javax.swing.event.AncestorEvent evt) {
    }

    private void jConnectActionPerformed(java.awt.event.ActionEvent evt) {
        if (jConnect.getText() == "Po��cz") {
            myConnector = new Connector();
            if (myConnector.Connect(jHost.getText(), jUser.getText(), jPass.getText()) == 1) {
                jLabConnect.setText("Nawi�zano po�aczenie z serwerem...");
                jConnect.setText("Roz��cz");
                jHost.setEnabled(false);
                jUser.setEnabled(false);
                jPass.setEnabled(false);
                myCore.conn = myConnector;
                myCore.Connected = 1;
                myCore.setTree();
            } else {
                jLabConnect.setText("Nie uda�o si� po��czy� z serwerem...");
                myCore.Connected = 0;
                myCore.setTree();
            }
        } else {
            jConnect.setText("Po��cz");
            jLabConnect.setText("Podaj parametry serwera...");
            jHost.setEnabled(true);
            jUser.setEnabled(true);
            jPass.setEnabled(true);
            myCore.Connected = 0;
            myCore.setTree();
        }
    }

    private void changeTheLookAndFeel(int value) {
        try {
            UIManager.setLookAndFeel(looks[value].getClassName());
            SwingUtilities.updateComponentTreeUI(this.getParent().getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jConnect;

    private javax.swing.JTextField jHost;

    private javax.swing.JLabel jLabConnect;

    private javax.swing.JLabel jLabel;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JList jLookFeel;

    private javax.swing.JPasswordField jPass;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextField jUser;
}
