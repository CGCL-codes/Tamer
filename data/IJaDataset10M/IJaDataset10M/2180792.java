package druid.util.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import javax.swing.ToolTipManager;
import org.dlib.gui.CustomLook;
import org.dlib.gui.IconLine;
import org.dlib.gui.TLabel;
import org.dlib.gui.TStatusBar;

public class StatusBar extends TStatusBar {

    public static final int NUM_ICONS = 4;

    private static final int NUM_SLOT = 3;

    private static final int SB_HEIGHT = 20;

    private static final int SLOT_MESSAGES = 0;

    private static final int SLOT_MEMORY = 1;

    private static final int SLOT_ICONS = 2;

    private static StatusBar statusBar;

    private IconLabel lblIcons = new IconLabel(this, NUM_ICONS);

    public StatusBar() {
        super(NUM_SLOT);
        setSlotExpansion(0, true);
        getLabel(SLOT_MESSAGES).setPreferredSize(new Dimension(120, SB_HEIGHT));
        getLabel(SLOT_MEMORY).setPreferredSize(new Dimension(120, SB_HEIGHT));
        getLabel(SLOT_MESSAGES).setHorizontalAlignment(TLabel.LEFT);
        setLabel(SLOT_ICONS, lblIcons);
    }

    public static StatusBar getInstance() {
        if (statusBar == null) statusBar = new StatusBar();
        return statusBar;
    }

    public void showImage(int slot, Image image, String toolTip) {
        lblIcons.showImage(slot, image, toolTip);
        lblIcons.repaint();
    }

    public void hideImage(int slot) {
        lblIcons.hideImage(slot);
        lblIcons.repaint();
    }

    public void displayMemory(final String message, final String toolTip, final Color col) {
        setText(SLOT_MEMORY, message);
        setTooltip(SLOT_MEMORY, toolTip);
        setColor(SLOT_MEMORY, col);
    }

    public void displayMessage(final String text) {
        getLabel(SLOT_MESSAGES).setText(text);
    }
}

class IconLabel extends TLabel {

    private int iconNum;

    private String toolTips[];

    private IconLine icons = new IconLine();

    public IconLabel(StatusBar sb, int iconNum) {
        super("");
        setBorder(sb);
        setFont(CustomLook.statusBarFont);
        this.iconNum = iconNum;
        for (int i = 0; i < iconNum; i++) icons.addImage(ImageFactory.NULL.getImage());
        setIcon(icons);
        toolTips = new String[iconNum];
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    public void showImage(int slot, Image image, String toolTip) {
        icons.setImage(slot, image);
        toolTips[slot] = toolTip;
    }

    public void hideImage(int slot) {
        icons.setImage(slot, ImageFactory.NULL.getImage());
        toolTips[slot] = null;
    }

    public String getToolTipText(MouseEvent e) {
        int meanWidth = getWidth() / iconNum;
        int slot = e.getX() / meanWidth;
        return toolTips[slot];
    }
}
