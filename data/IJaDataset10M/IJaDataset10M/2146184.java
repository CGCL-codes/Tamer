package net.sf.evemsp.ui.items;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import net.sf.evemsp.EveMobileSkillPlanner;

/**
 * Show information about skills and the time required until the next level
 * 
 * @author Jaabaa
 */
public class SkillTimesItem extends CustomItem {

    EveMobileSkillPlanner esme;

    private boolean traversing = false;

    private String skillName;

    private String duration;

    /**
	 * Constructor
	 * 
	 * @param esme
	 * @param skillName
	 * @param duration
	 */
    public SkillTimesItem(EveMobileSkillPlanner esme, String skillName, String duration) {
        super(null);
        this.esme = esme;
        this.skillName = skillName;
        this.duration = duration;
    }

    protected int getMinContentHeight() {
        Font f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        return 2 * f.getHeight();
    }

    protected int getMinContentWidth() {
        return Display.getDisplay(esme).getCurrent().getWidth();
    }

    protected int getPrefContentHeight(int width) {
        return getMinContentHeight();
    }

    public int getPreferredWidth() {
        return Display.getDisplay(esme).getCurrent().getWidth();
    }

    protected int getPrefContentWidth(int height) {
        return getMinContentWidth();
    }

    protected boolean traverse(int dir, int viewportWidth, int viewportHeight, int[] visRect_inout) {
        if (traversing) {
        } else {
            traversing = true;
        }
        repaint();
        return false;
    }

    protected void traverseOut() {
        traversing = false;
        repaint();
    }

    protected void paint(Graphics g, int w, int h) {
        Display display = Display.getDisplay(esme);
        Font f = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        g.setFont(f);
        int bg = display.getColor(traversing ? Display.COLOR_BACKGROUND : Display.COLOR_HIGHLIGHTED_BACKGROUND);
        int fg = display.getColor(traversing ? Display.COLOR_FOREGROUND : Display.COLOR_HIGHLIGHTED_FOREGROUND);
        g.setColor(bg);
        g.fillRect(0, 0, w, h);
        g.setColor(fg);
        int y = 0;
        g.drawString(skillName, 0, y, Graphics.TOP | Graphics.LEFT);
        y += f.getHeight();
        g.drawString(duration, w, y, Graphics.TOP | Graphics.RIGHT);
    }
}
