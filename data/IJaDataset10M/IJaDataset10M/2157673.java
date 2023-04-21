package org.pockit.view.lowfi;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Graphics;
import org.pockit.model.Setting;

/**
 * Test of the CustomItem
 * 
 * @author Claudius Coenen <amenthes@pock-it.org>
 * @deprecated not used at the moment.
 */
public class SettingItem extends CustomItem {

    private Setting setting;

    public boolean extended = false;

    public SettingItem(org.pockit.model.Setting setting) {
        super(null);
        this.setting = setting;
    }

    protected int getMinContentHeight() {
        return 40;
    }

    protected int getMinContentWidth() {
        return 96;
    }

    protected int getPrefContentHeight(int width) {
        if (extended) {
            return 120;
        } else {
            return getMinContentHeight();
        }
    }

    protected int getPrefContentWidth(int height) {
        return 240;
    }

    protected void paint(Graphics g, int width, int height) {
        g.setColor(0xffff00);
        g.drawRect(0, 0, width, height);
        g.setColor(0x000000);
        g.drawString(setting.getHumanname(), 0, 0, Graphics.LEFT | Graphics.TOP);
        if (extended) {
            g.setColor(0xcccccc);
            g.drawString(setting.getDescription(), 0, 20, Graphics.LEFT | Graphics.TOP);
        }
        if (setting.isDefault()) {
            g.setColor(0x00cc00);
        } else {
            g.setColor(0xcc0000);
        }
        g.drawString(setting.getValue(), width, height, Graphics.RIGHT | Graphics.BOTTOM);
    }
}
