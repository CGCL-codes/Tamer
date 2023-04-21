package com.elibera.ccs.panel.std;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import com.elibera.ccs.buttons.action.page.ButtonHelp;
import com.elibera.ccs.img.HelperBinary;
import com.elibera.ccs.img.ImageReader;
import com.elibera.ccs.panel.HelperPanel;
import com.elibera.ccs.parser.HelperContentPackage;
import com.elibera.ccs.parser.InterfaceDocContainer;
import com.elibera.ccs.res.Msg;

/**
 * @author meisi
 *
 */
public class PanelRootHeader extends JPanel {

    private JButton jButton = null;

    private InterfaceDocContainer container;

    public String helpKey;

    public String toolTip;

    private JPanel jPanel = null;

    private JScrollPane jScrollPaneContent;

    public JEditorPane htmlPane;

    /**
	 * This is the default constructor
	 */
    public PanelRootHeader(InterfaceDocContainer container, String helpKey, String toolTip) {
        super();
        this.init(container, helpKey, toolTip);
        initialize();
    }

    public void reset(InterfaceDocContainer container, String helpKey, String toolTip) {
        this.init(container, helpKey, toolTip);
        getJButton().setToolTipText(toolTip);
    }

    private void init(InterfaceDocContainer container, String helpKey, String toolTip) {
        this.container = container;
        this.helpKey = helpKey;
        if (toolTip == null) toolTip = Msg.getMsg("HELP_STD_TOOL_TIP");
        this.toolTip = toolTip;
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        HelperPanel.formatPanel(this);
        this.setLayout(new BorderLayout());
        this.add(getJPanel(), java.awt.BorderLayout.EAST);
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            HelperPanel.formatButton(jButton);
            try {
                BufferedImage img = ImageReader.openImageJar(container, HelperBinary.EDITOR_IMG_HELP);
                ImageIcon icon = new ImageIcon(img);
                jButton.setText("");
                jButton.setIcon(icon);
            } catch (Exception e) {
                e.printStackTrace();
            }
            jButton.setToolTipText(toolTip);
            jButton.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ButtonHelp.showHelpPopUp(helpKey, jPanel);
                }
            });
        }
        return jButton;
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            HelperPanel.formatPanel(jPanel);
            jPanel.add(getJButton());
        }
        return jPanel;
    }
}
