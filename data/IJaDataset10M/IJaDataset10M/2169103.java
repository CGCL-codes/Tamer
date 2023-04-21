package net.url404.umodulargui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * The GUI desktop toolbar.
 *
 * @author makela@url404.net
 */
public class GUIToolBar extends JToolBar {

    private GUIDesktop desktop;

    JLabel statusPane;

    /**
   * Construct
   *
   * @param desktop     Link to GUIDesktop
   * @param menuBar     Link to GUIMenuBar, gets actions from here
   */
    public GUIToolBar(GUIDesktop desktop, GUIMenuBar menuBar) {
        this.desktop = desktop;
        JButton button;
        button = add(menuBar.getAction("new"));
        button.setToolTipText(GUIProperties.TOOLTIP_TEXTS[GUIProperties.TOOLTIP_TEXT_NEW]);
        button = add(menuBar.getAction("open"));
        button.setToolTipText(GUIProperties.TOOLTIP_TEXTS[GUIProperties.TOOLTIP_TEXT_OPEN]);
        button = add(menuBar.getAction("save"));
        button.setToolTipText(GUIProperties.TOOLTIP_TEXTS[GUIProperties.TOOLTIP_TEXT_SAVE]);
        Box box = Box.createHorizontalBox();
        statusPane = new JLabel("------");
        statusPane.setFont(new Font("Lucida Console", Font.BOLD, 10));
        statusPane.setBorder(BorderFactory.createRaisedBevelBorder());
        box.add(Box.createHorizontalStrut(10));
        box.add(statusPane);
        box.add(Box.createHorizontalStrut(10));
        add(box);
        button = add(menuBar.getAction("rewind"));
        button.setToolTipText(GUIProperties.TOOLTIP_TEXTS[GUIProperties.TOOLTIP_TEXT_REWIND]);
        button = add(menuBar.getAction("play"));
        button.setToolTipText(GUIProperties.TOOLTIP_TEXTS[GUIProperties.TOOLTIP_TEXT_PLAY]);
        button = add(menuBar.getAction("stop"));
        button.setToolTipText(GUIProperties.TOOLTIP_TEXTS[GUIProperties.TOOLTIP_TEXT_STOP]);
    }

    /**
   * Change status
   *
   * @param status    New status
   */
    public void setStatus(int status) {
        switch(status) {
            case GUIProperties.PLAYBACK_STATUS_OK:
                statusPane.setText("--OK--");
                break;
            case GUIProperties.PLAYBACK_STATUS_BROKEN:
                statusPane.setText("BROKEN");
                break;
            case GUIProperties.PLAYBACK_STATUS_PLAYING:
                statusPane.setText("-PLAY-");
                break;
            default:
                statusPane.setText("------");
        }
    }
}
