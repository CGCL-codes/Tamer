package wow.ui;

import java.util.Hashtable;
import javax.microedition.lcdui.*;
import javax.microedition.m3g.Graphics3D;
import wow.*;

public class SystemInfo extends WoWscreen {

    private int m_width;

    public SystemInfo() {
        m_width = 0;
    }

    public void activate(boolean active) {
        WoWgame.self().showDebug("SystemInfo.activate() active=" + active);
    }

    public void sizeEvent(int width, int height) {
        WoWgame.self().showDebug("SystemInfo.sizeEvent() " + width + " x " + height);
        m_width = width;
    }

    public boolean paintEvent(Graphics g) {
        Runtime r = Runtime.getRuntime();
        Hashtable prop3d = Graphics3D.getProperties();
        int y = 4;
        g.setColor(0xffffff);
        g.drawString("System Information", m_width / 2, y, Graphics.HCENTER | Graphics.TOP);
        y += 18;
        g.setColor(0xffff00);
        g.drawString("System", 4, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.setColor(0xffffff);
        g.drawString("Platform: " + System.getProperty("microedition.platform"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Profile: " + System.getProperty("microedition.profiles"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Configuration: " + System.getProperty("microedition.configuration"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Encoding: " + System.getProperty("microedition.encoding"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Time zone: " + System.getProperty("microedition.timezone"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 18;
        g.setColor(0xffff00);
        g.drawString("Memory", 4, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.setColor(0xffffff);
        g.drawString("Total: " + r.totalMemory(), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Free: " + r.freeMemory(), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 18;
        g.setColor(0xffff00);
        g.drawString("3D Graphics", 4, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.setColor(0xffffff);
        g.drawString("Maximum lights: " + (Integer) prop3d.get("maxLights"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Texture size: " + (Integer) prop3d.get("maxTextureDimension"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Texture units: " + (Integer) prop3d.get("numTextureUnits"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Antialiasing: " + (Boolean) prop3d.get("supportAntialiasing"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("True Color: " + (Boolean) prop3d.get("supportTrueColor"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Dithering: " + (Boolean) prop3d.get("supportDithering"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Mipmapping: " + (Boolean) prop3d.get("supportMipmapping"), 12, y, Graphics.LEFT | Graphics.TOP);
        y += 14;
        g.drawString("Perspective Corr.: " + (Boolean) prop3d.get("supportPerspectiveCorrection"), 12, y, Graphics.LEFT | Graphics.TOP);
        return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
        WoWgame.self().showDebug("SystemInfo.keyEvent() key=" + key + " act=" + action + " press=" + pressed);
        if (!repeated && (action != 0) && (key < 0)) {
            switch(action) {
                case Canvas.FIRE:
                    if (pressed) {
                        if (WoWgame.self().shifted()) {
                            System.gc();
                            return true;
                        }
                        return WoWgame.self().popScreen() || WoWgame.self().jumpScreen("Player");
                    }
                    return true;
                case Canvas.UP:
                    return true;
                case Canvas.DOWN:
                    return true;
            }
        }
        if (pressed && !repeated) {
            switch(key) {
                case '0':
                    WoWgame.self().pushScreen("GameMenu");
                    return true;
            }
        }
        return false;
    }

    public boolean ptrEvent(int x, int y, boolean pressed, boolean dragged) {
        if (pressed && !dragged) {
            return WoWgame.self().popScreen() || WoWgame.self().jumpScreen("Player");
        }
        return false;
    }
}
