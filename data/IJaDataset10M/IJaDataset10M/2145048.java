package zildo.platform.input;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import zildo.Zildo;
import zildo.fwk.input.KeyboardHandler;
import zildo.monde.util.Angle;
import zildo.monde.util.Point;

/**
 * @author Tchegito
 *
 */
public class AndroidKeyboardHandler implements KeyboardHandler {

    static final EnumMap<Keys, Integer> platformKeys = new EnumMap<Keys, Integer>(Keys.class);

    private static final int KEY_ESCAPE = 0x01;

    private static final int KEY_BACK = 0x0E;

    private static final int KEY_TAB = 0x0F;

    private static final int KEY_Q = 0x10;

    private static final int KEY_W = 0x11;

    private static final int KEY_E = 0x12;

    private static final int KEY_RETURN = 0x1C;

    public static final int KEY_LSHIFT = 0x2A;

    private static final int KEY_X = 0x2D;

    private static final int KEY_UP = 0xC8;

    private static final int KEY_LEFT = 0xCB;

    private static final int KEY_RIGHT = 0xCD;

    private static final int KEY_DOWN = 0xD0;

    static {
        platformKeys.put(Keys.BACK, KEY_BACK);
        platformKeys.put(Keys.ESCAPE, KEY_ESCAPE);
        platformKeys.put(Keys.TAB, KEY_TAB);
        platformKeys.put(Keys.Q, KEY_Q);
        platformKeys.put(Keys.W, KEY_W);
        platformKeys.put(Keys.E, KEY_E);
        platformKeys.put(Keys.RETURN, KEY_RETURN);
        platformKeys.put(Keys.LSHIFT, KEY_LSHIFT);
        platformKeys.put(Keys.X, KEY_X);
        platformKeys.put(Keys.UP, KEY_UP);
        platformKeys.put(Keys.LEFT, KEY_LEFT);
        platformKeys.put(Keys.RIGHT, KEY_RIGHT);
        platformKeys.put(Keys.DOWN, KEY_DOWN);
    }

    List<Point> polledTouchedPoints;

    TouchMovement tm;

    boolean resetBack;

    AndroidInputInfos infos;

    public AndroidKeyboardHandler() {
        polledTouchedPoints = new ArrayList<Point>();
        tm = new TouchMovement(polledTouchedPoints);
        infos = new AndroidInputInfos();
    }

    public void setAndroidInputInfos(AndroidInputInfos infos) {
        this.infos = infos;
    }

    static final int middleX = Zildo.viewPortX / 2;

    static final int middleY = Zildo.viewPortY / 2;

    public boolean isKeyDown(int p_code) {
        if (!polledTouchedPoints.isEmpty()) {
            for (Point p : polledTouchedPoints) {
                switch(p_code) {
                    case KEY_Q:
                        return p.x >= middleX && p.y < middleY;
                    case KEY_W:
                        return p.x >= middleX && p.y >= middleY;
                    case KEY_X:
                        Point zildoPos = infos.getZildoPos();
                        if (zildoPos != null) {
                        }
                        return (zildoPos != null && zildoPos.distance(p) < 16);
                }
            }
        }
        Angle direction = tm.getCurrent();
        if (direction != null) {
            switch(p_code) {
                case KEY_UP:
                    return Angle.isContained(direction, Angle.NORD);
                case KEY_DOWN:
                    return Angle.isContained(direction, Angle.SUD);
                case KEY_LEFT:
                    return Angle.isContained(direction, Angle.OUEST);
                case KEY_RIGHT:
                    return Angle.isContained(direction, Angle.EST);
            }
        }
        if (p_code == KEY_ESCAPE) {
            if (infos.backPressed) {
                resetBack = true;
            }
            return infos.backPressed;
        }
        return false;
    }

    Angle previous;

    public void poll() {
        if (resetBack) {
            infos.backPressed = false;
            resetBack = false;
        }
        polledTouchedPoints.clear();
        if (!infos.liveTouchedPoints.isEmpty()) {
            polledTouchedPoints.clear();
            polledTouchedPoints.addAll(infos.liveTouchedPoints);
        }
        tm.render();
        previous = tm.getCurrent();
    }

    /**
	 * @return true if a keyboard event was read, false otherwise
	 */
    public boolean next() {
        return false;
    }

    public boolean getEventKeyState() {
        return false;
    }

    public int getEventKey() {
        return 0;
    }

    public char getEventCharacter() {
        return 0;
    }

    public int getCode(Keys k) {
        return platformKeys.get(k);
    }
}
