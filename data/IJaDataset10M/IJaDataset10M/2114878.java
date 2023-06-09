package way2sms;

import java.awt.Cursor;
import java.awt.Desktop;
import java.net.URI;
import javax.swing.UIManager;

/**
 *
 * @author Dinesh
 */
public class About extends javax.swing.JFrame {

    public static About ab = null;

    /** Creates new form About */
    public About() {
        initComponents();
        this.setAlwaysOnTop(Settings.istop);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        projlbl = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        versionlbl = new javax.swing.JLabel();
        setTitle("About");
        setResizable(false);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image_resources/way2smsclient_logo_200px.png")));
        projlbl.setFont(new java.awt.Font("Arial", 0, 11));
        projlbl.setText("<html><font color=\"#0000FF\"><u>https://sourceforge.net/projects/w2sc/</u></font></html> ");
        projlbl.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                projlblMouseClicked(evt);
            }
        });
        jLabel2.setFont(new java.awt.Font("Arial", 0, 11));
        jLabel2.setText("I am a tiny java tool, used to send SMS over");
        jLabel3.setFont(new java.awt.Font("Arial", 0, 11));
        jLabel3.setText("India using Way2sms website");
        versionlbl.setFont(new java.awt.Font("Arial", 1, 11));
        versionlbl.setText("Version 0.1");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(projlbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(22, 22, 22).addComponent(jLabel1)).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addComponent(jLabel2))).addGroup(layout.createSequentialGroup().addGap(84, 84, 84).addComponent(versionlbl))).addContainerGap(25, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(versionlbl).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(projlbl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        projlbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pack();
    }

    private void projlblMouseClicked(java.awt.event.MouseEvent evt) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI("http://sourceforge.net/projects/w2sc/"));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new About().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel projlbl;

    private javax.swing.JLabel versionlbl;
}
