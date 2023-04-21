package org.vrforcad.controller.gui.dialog.material;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.media.j3d.Appearance;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * The main shape material dialog.
 * Dialog which allow to modify the material parameters. 
 * @version 1.6 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class MaterialDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JButton jButtonClose = null;

    private Appearance ap;

    public MaterialDialog(Appearance ap) {
        this.ap = ap;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(415, 530);
        this.setTitle("Material");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJContentPane());
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.pack();
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            JPanel jPanelMatApp = new JPanel();
            jPanelMatApp.setLayout(new BoxLayout(jPanelMatApp, BoxLayout.Y_AXIS));
            jPanelMatApp.setName("Material");
            jPanelMatApp.add(new AppearanceAdjust());
            jPanelMatApp.add(new MaterialAdjust(ap));
            jContentPane.add(jPanelMatApp, BorderLayout.PAGE_START);
            JPanel jPanelEnd = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            jPanelEnd.add(getJButtonMaterialApply());
            jContentPane.add(jPanelEnd, BorderLayout.PAGE_END);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jButtonMaterialApply	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonMaterialApply() {
        if (jButtonClose == null) {
            jButtonClose = new JButton();
            jButtonClose.setText("Close");
            jButtonClose.setPreferredSize(new Dimension(100, 25));
            jButtonClose.setMinimumSize(jButtonClose.getPreferredSize());
            jButtonClose.setMaximumSize(jButtonClose.getPreferredSize());
            jButtonClose.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return jButtonClose;
    }
}
