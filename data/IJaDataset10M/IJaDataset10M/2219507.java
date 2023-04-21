package scratchcomp;

import scratchcomp.util.Helper;
import scratchcomp.sys.GlobalConfig;
import scratchcomp.skin.*;
import java.io.File;
import java.awt.Canvas;
import java.awt.Window;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

public class UComponent extends Canvas implements Style, FocusListener {

    public Skin skin;

    public int style = Style.PLAIN;

    public boolean focus = false;

    public static GlobalConfig globalConfig;

    public UComponent() {
        if (globalConfig == null) globalConfig = Helper.getGlobalConfig();
        this.addFocusListener(this);
    }

    public synchronized void setStyle(int style) {
        this.style = style;
        if (style == Style.SKIN) {
            try {
                skin = SkinIO.loadSkin(Helper.checkPath("scratchcomp") + "skin" + File.separatorChar + "skins" + File.separatorChar + "default.skin");
            } catch (Exception e) {
                System.err.println("error loading skin ... " + e.getMessage());
                this.style = Style.PLAIN;
                style = Style.PLAIN;
            }
        }
        repaint();
    }

    public int getStyle() {
        return style;
    }

    public void setSkin(Skin s) {
        skin = s;
        repaint();
    }

    public Window getOwnerWindow() {
        Component component = this.getParent();
        while ((component != null) && !(component instanceof Window)) {
            if (component instanceof UPanel) component = ((UPanel) component).getOwnerWindow(); else component = component.getParent();
        }
        return ((Window) component);
    }

    public void focusGained(FocusEvent fe) {
        focus = true;
    }

    public void focusLost(FocusEvent fe) {
        focus = false;
    }

    public Color getForeground() {
        Color ret;
        if ((ret = super.getForeground()) == null) return new Color(0, 0, 0); else return ret;
    }

    public Color getBackground() {
        Color ret;
        if ((ret = super.getBackground()) == null) return new Color(255, 255, 255); else return ret;
    }

    protected void processKeyEvent(KeyEvent ke) {
        if (ke.getKeyCode() == '	') {
            ke.consume();
            return;
        }
        super.processKeyEvent(ke);
    }

    public boolean isFocusTraversable() {
        return true;
    }
}
