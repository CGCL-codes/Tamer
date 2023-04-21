package de.enough.polish.ui.texteffects;

import de.enough.polish.android.lcdui.Font;
import de.enough.polish.android.lcdui.Graphics;
import de.enough.polish.ui.ClippingRegion;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.Style;
import de.enough.polish.ui.TextEffect;

/**
 * <p>Cycles through text so that only a single line or word is being shown.</p>
 *
 * <p>Copyright Enough Software 2008</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class CyclingTextEffect extends TextEffect {

    private static final long DEFAULT_INTERVAL = 1 * 1000;

    private String lastText;

    private String[] textRows;

    private int currentRow;

    private long interval = DEFAULT_INTERVAL;

    private long lastSwitchTime;

    private Font textFont;

    /**
	 * Creates a new cycling text effect.
	 */
    public CyclingTextEffect() {
    }

    public void animate(Item parent, long currentTime, ClippingRegion repaintRegion) {
        super.animate(parent, currentTime, repaintRegion);
        boolean addRepaintRegion = false;
        if (this.lastSwitchTime == 0) {
            this.lastSwitchTime = currentTime;
            addRepaintRegion = true;
        } else if (currentTime - this.lastSwitchTime > this.interval) {
            int index = this.currentRow + 1;
            if (index >= this.textRows.length) {
                index = 0;
            }
            this.currentRow = index;
            this.lastSwitchTime = currentTime;
            addRepaintRegion = true;
        }
        if (addRepaintRegion) {
            parent.addRepaintArea(repaintRegion);
        }
    }

    public void showNotify() {
        super.showNotify();
        this.lastSwitchTime = 0;
    }

    public String[] wrap(String text, int textColor, Font font, int firstLineWidth, int lineWidth) {
        if (text != this.lastText) {
            this.lastText = text;
            this.textRows = super.wrap(text, textColor, font, firstLineWidth, lineWidth);
        }
        this.textFont = font;
        if (this.textRows.length > 0) {
            return new String[] { this.textRows[0] };
        }
        return new String[] { "" };
    }

    public void drawStrings(String[] textLines, int textColor, int x, int y, int leftBorder, int rightBorder, int lineHeight, int maxWidth, int layout, Graphics g) {
        if ((layout & Item.LAYOUT_CENTER) == Item.LAYOUT_CENTER) {
            x = leftBorder + (rightBorder - leftBorder) / 2;
        } else if ((layout & Item.LAYOUT_RIGHT) == Item.LAYOUT_RIGHT) {
            x = rightBorder;
        }
        if ((layout & Item.LAYOUT_BOTTOM) == Item.LAYOUT_BOTTOM) {
            if ((layout & Item.LAYOUT_VCENTER) == Item.LAYOUT_VCENTER) {
                y -= this.textFont.getBaselinePosition();
            } else {
                y -= this.textFont.getHeight();
            }
        }
        int anchor = this.style.getAnchorHorizontal();
        String line = this.textRows[this.currentRow];
        g.drawString(line, x, y, Graphics.TOP | anchor);
    }

    public void drawString(String text, int textColor, int x, int y, int anchor, Graphics g) {
    }

    public void setStyle(Style style) {
        super.setStyle(style);
    }
}
