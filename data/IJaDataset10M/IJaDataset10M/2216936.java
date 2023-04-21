package zen.scrabbledict.gui;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This is the "About" window.
 * @author  flszen
 */
public class About extends EscapeDialog {

    private static final long serialVersionUID = 1;

    /** Creates new form About
     * @param parent The parent frame.
     */
    public About(java.awt.Frame parent) {
        super(parent, true);
        getRootPane().putClientProperty("Window.style", "small");
        setEscapeActionListner(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        lblName = new javax.swing.JLabel();
        lblVersion = new javax.swing.JLabel();
        pnlCenterButton = new javax.swing.JPanel();
        btnWebsite = new javax.swing.JButton();
        lblGPL = new javax.swing.JLabel();
        lblMoreGPL = new javax.swing.JLabel();
        lblMoreMoreGPL = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        lblName.setFont(new java.awt.Font("Lucida Grande", 1, 15));
        lblName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("zen/scrabbledict/gui/Bundle");
        lblName.setText(bundle.getString("About.lblName.text"));
        lblVersion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblVersion.setText(bundle.getString("About.lblVersion.text"));
        btnWebsite.setText(bundle.getString("About.btnWebsite.text"));
        btnWebsite.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWebsiteActionPerformed(evt);
            }
        });
        pnlCenterButton.add(btnWebsite);
        lblGPL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGPL.setText(bundle.getString("About.lblGPL.text"));
        lblGPL.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblMoreGPL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMoreGPL.setText(bundle.getString("About.lblMoreGPL.text"));
        lblMoreMoreGPL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMoreMoreGPL.setText(bundle.getString("About.lblMoreMoreGPL.text"));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, pnlCenterButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, lblName, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE).add(lblVersion, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE).add(lblGPL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE).add(lblMoreMoreGPL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE).add(lblMoreGPL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(lblName).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(lblVersion).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(pnlCenterButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(lblGPL).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(lblMoreGPL).add(2, 2, 2).add(lblMoreMoreGPL).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void btnWebsiteActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            BrowserLauncher bl = new BrowserLauncher();
            bl.openURLinBrowser("http://www.sourceforge.net/projects/scrabbledict");
            return;
        } catch (BrowserLaunchingInitializingException ex) {
            Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedOperatingSystemException ex) {
            Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(this, "There was an error opening your web browser.\nhttp://www.sourceforge.net/projects/scrabbledict", "Browser Error", JOptionPane.ERROR_MESSAGE);
    }

    private javax.swing.JButton btnWebsite;

    private javax.swing.JLabel lblGPL;

    private javax.swing.JLabel lblMoreGPL;

    private javax.swing.JLabel lblMoreMoreGPL;

    private javax.swing.JLabel lblName;

    private javax.swing.JLabel lblVersion;

    private javax.swing.JPanel pnlCenterButton;
}
