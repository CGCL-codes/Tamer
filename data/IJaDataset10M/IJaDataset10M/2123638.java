package games.midhedava.client.gui.wt;

import games.midhedava.client.MidhedavaUI;
import games.midhedava.client.gui.HelpDialog;
import games.midhedava.client.gui.styled.WoodStyle;
import games.midhedava.client.gui.styled.swing.StyledJButton;

/**
 *
 * @author  timothyb89
 */
public class GameButtonHelperContent extends javax.swing.JPanel {

    private static final long serialVersionUID = -1607102841664745919L;

    SettingsPanel sp;

    MidhedavaUI ui;

    /** Creates new form PurchaseHelperContent */
    public GameButtonHelperContent(SettingsPanel sp, GameButtonHelper gbh, MidhedavaUI ui) {
        initComponents();
        this.sp = sp;
        this.ui = ui;
    }

    private void initComponents() {
        WoodStyle style = new WoodStyle();
        gh = new StyledJButton(style);
        jButton1 = new StyledJButton(style);
        setLayout(null);
        setMaximumSize(new java.awt.Dimension(100, 150));
        setMinimumSize(new java.awt.Dimension(100, 150));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(100, 150));
        gh.setText("Game Help");
        gh.setOpaque(false);
        gh.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ghActionPerformed(evt);
            }
        });
        add(gh);
        gh.setBounds(0, 0, 150, 25);
        jButton1.setText("Purchase Helper");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1);
        jButton1.setBounds(0, 30, 150, 25);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (sp.buywindow.isVisible()) {
            sp.buywindow.setVisible(false);
        } else {
            sp.buywindow.setVisible(true);
        }
    }

    private void ghActionPerformed(java.awt.event.ActionEvent evt) {
        new HelpDialog().display();
    }

    private StyledJButton gh;

    private StyledJButton jButton1;
}
