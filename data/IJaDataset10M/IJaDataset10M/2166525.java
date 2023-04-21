package hokutonorogue.game;

import java.awt.*;
import com.golden.gamedev.object.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Alessio Carotenuto
 * @version 1.0
 */
public class SimpleChoice implements Choice {

    public static final SimpleChoice OK_CHOICE = new SimpleChoice("OK", "OK");

    public static final SimpleChoice CANCEL_CHOICE = new SimpleChoice("CANCEL", "CANCEL");

    public static final SimpleChoice YES_CHOICE = new SimpleChoice("YES", "YES");

    public static final SimpleChoice NO_CHOICE = new SimpleChoice("NO", "NO");

    public static final SimpleChoice NEXT_CHOICE = new SimpleChoice("<NEXT>", "<NEXT>");

    public static final SimpleChoice PREV_CHOICE = new SimpleChoice("<PREV>", "<PREV>");

    public static final SimpleChoice ALL_CHOICE = new SimpleChoice("<ALL>", "<ALL>");

    private Object userObject = null;

    private String label = null;

    private boolean enabled = true;

    private boolean positive = false;

    private int x = 0;

    private int y = 0;

    public SimpleChoice() {
    }

    public SimpleChoice(String label, Object userObject) {
        this.userObject = userObject;
        this.label = label;
    }

    public SimpleChoice(String label, Object userObject, boolean enabled) {
        this.userObject = userObject;
        this.label = label;
        this.enabled = enabled;
    }

    public Object getUserObject() {
        return userObject;
    }

    public String getLabel() {
        return label;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String toString() {
        return label;
    }

    public void render(Graphics2D g, GameFont font, HokutoGameObject hokutoGameObject, int y, int fontSize) {
        x = MainGame.PLAYFIELD_WIDTH / 2 - getLabel().length() * fontSize / 2;
        this.y = y;
        font.drawString(g, getLabel(), x, y);
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }
}
