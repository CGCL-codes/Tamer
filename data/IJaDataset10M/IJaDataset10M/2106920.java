package wow.ui;

import javax.microedition.lcdui.*;
import wow.*;

public class Spellbook extends WoWscreen {

    private WoWbutton m_buttons[];

    private int m_width;

    private int m_buttonY;

    private int m_buttonCount;

    public Spellbook() {
        m_width = 0;
        m_buttons = null;
        m_buttonCount = 0;
    }

    private void drawButton(Graphics g, int x, int y, WoWbutton action) {
        boolean active = true;
        if (action.image() != null) g.drawImage(action.image(), x, y, Graphics.HCENTER | Graphics.VCENTER);
        if (action.grayed() > 0) {
            int angle = (action.grayed() * 360) / 100;
            if (angle < 5) angle = 5;
            g.setColor(0x800000);
            g.fillArc(x - 14, y - 14, 28, 28, 90, angle);
            g.setColor(0xff0000);
            g.drawArc(x - 14, y - 14, 28, 28, 90, angle);
            active = false;
        } else if (!action.enabled()) {
            g.setColor(0xff0000);
            g.drawLine(x - 14, y - 14, x + 14, y + 14);
            g.drawLine(x - 14, y + 14, x + 14, y - 14);
            active = false;
        }
        if (active && (action.label() != null) && (action.label().length() != 0)) {
            if (WoWgame.self().detailed()) {
                Font f = g.getFont();
                int h = f.getHeight();
                g.setColor(0x202020);
                g.fillRoundRect(x - 15, y + 14 - h, f.stringWidth(action.label()), h, 4, 4);
            }
            g.setColor(WoWgame.self().shifted() ? 0x0080ff : 0x00ff00);
            g.drawString(action.label(), x - 15, y + 14, Graphics.LEFT | Graphics.BOTTOM);
        }
    }

    public void activate(boolean active) {
        System.err.println("Spellbook.activate() active=" + active);
        if (active) WoWgame.self().fireFgSound(WoWmobile.resource("/sounds/universal/TradeskillLearnRecipeLoop.mp3"));
    }

    public void sizeEvent(int width, int height) {
        System.err.println("Spellbook.sizeEvent() " + width + " x " + height);
        m_width = width;
        m_buttonY = height - 18;
        int n = (width - 2) / 34;
        m_buttons = new WoWbutton[2 * n];
        for (int i = 0; i < 2 * n; i++) {
            int idx = i % n;
            WoWaction action = WoWgame.self().getStorage(i);
            if (action == null) m_buttons[i] = null; else {
                m_buttons[i] = new WoWbutton(action, "/images/" + action.name() + ".png");
                m_buttons[i].setKey('1' + idx, null);
            }
        }
        m_buttonCount = n;
        WoWgame.self().setDebug("Spellbook buttons: " + n);
    }

    public boolean paintEvent(Graphics g) {
        g.setColor(0x808000);
        if (WoWgame.self().detailed()) {
            g.fillRoundRect(4, 4, 36, 36, 8, 8);
            if (WoWgame.self().playerIcon() != null) g.drawImage(WoWgame.self().playerIcon(), 22, 22, Graphics.HCENTER | Graphics.VCENTER);
            g.drawString(WoWgame.self().playerName(), 44, 2, Graphics.LEFT | Graphics.TOP);
            g.drawString(WoWgame.self().playerLevel(), 44, 40, Graphics.LEFT | Graphics.BOTTOM);
        }
        int offs = WoWgame.self().shifted() ? m_buttonCount : 0;
        for (int i = 0; i < m_buttonCount; i++) {
            WoWbutton act = m_buttons[i + offs];
            if (act != null) drawButton(g, 34 * i + 18, m_buttonY, act);
        }
        return false;
    }

    public boolean keyEvent(int key, int action, boolean pressed, boolean repeated) {
        System.err.println("Spellbook.keyEvent() key=" + key + " act=" + action + " press=" + pressed);
        if (!repeated && (action != 0) && (key < 0)) {
            switch(action) {
                case Canvas.FIRE:
                    if (pressed) WoWgame.self().showScreen("Player");
                    return true;
                case Canvas.LEFT:
                    WoWgame.self().action("rotate", pressed ? "left" : "none");
                    return true;
                case Canvas.RIGHT:
                    WoWgame.self().action("rotate", pressed ? "right" : "none");
                    return true;
                case Canvas.UP:
                    WoWgame.self().action("move", pressed ? "forward" : "none");
                    return true;
                case Canvas.DOWN:
                    WoWgame.self().action("move", pressed ? "backward" : "none");
                    return true;
            }
        }
        if (pressed && !repeated) {
            int offs = WoWgame.self().shifted() ? m_buttonCount : 0;
            for (int i = 0; i < m_buttonCount; i++) {
                WoWbutton act = m_buttons[i + offs];
                if ((act != null) && act.matches(key)) {
                    WoWgame.self().setDebug("Matched button " + act.label() + " action " + act.name());
                    act.setGrayed();
                    WoWgame.self().action(act.name());
                    return true;
                }
            }
            if (key == '0') {
                WoWgame.self().pushScreen("GameMenu");
                return true;
            }
        }
        return false;
    }
}
