package net.sf.magicmap.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import net.sf.magicmap.client.controller.Controller;
import net.sf.magicmap.client.gui.utils.GUIBuilder;
import net.sf.magicmap.client.gui.utils.GUIConstants;
import net.sf.magicmap.client.gui.utils.GUIUtils;

/**
 * @author thuebner
 */
public class StatusPanel extends JPanel {

    /**
     * serial version id
     */
    private static final long serialVersionUID = -3867182133983144609L;

    private JLabel label;

    private JLabel visible;

    public StatusPanel() {
        super();
        this.setMinimumSize(new Dimension(-1, 22));
        this.setPreferredSize(new Dimension(-1, 22));
        this.setLayout(new BorderLayout(4, 6));
        this.setBorder(new SoftBevelBorder(BevelBorder.LOWERED));
        this.label = new JLabel();
        this.add(this.label, BorderLayout.WEST);
        this.visible = new JLabel();
        this.add(this.visible, BorderLayout.EAST);
        this.visible.setVisible(false);
    }

    public void setMessage(String msg) {
        this.label.setText(msg);
    }

    public void setInvisible(boolean invisible) {
        this.visible.setVisible(Controller.getInstance().isConnected() && Controller.getInstance().isMapLoaded());
        if (invisible) {
            this.visible.setText(GUIUtils.i18n("invisible"));
            this.visible.setIcon(GUIBuilder.getToolIcon(GUIConstants.ICON_INVISIBLE));
        } else {
            this.visible.setText(GUIUtils.i18n("visible"));
            this.visible.setIcon(GUIBuilder.getToolIcon(GUIConstants.ICON_VISIBLE));
        }
    }
}
