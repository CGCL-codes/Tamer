package picto.draw;

import java.util.EventObject;

/**
 *
 * @author davedes
 */
public class DrawEvent extends EventObject {

    public static final int ID_PRESS = 10;

    public static final int ID_DRAG = 20;

    public static final int ID_RELEASE = 30;

    public static final int TYPE_CURSOR = 1;

    public static final int TYPE_STYLUS = 2;

    public static final int TYPE_ERASER = 4;

    public static final int NOBUTTON = 0;

    public static final int BUTTON_LEFT = 1;

    public static final int BUTTON_MIDDLE = 2;

    public static final int BUTTON_RIGHT = 3;

    private int id;

    private int button;

    private int x;

    private int y;

    private float pressure;

    private int type;

    private int modifiers;

    /** Creates a new instance of DrawEvent */
    public DrawEvent(Object source, int id, int button, int x, int y, float pressure, int type, int modifiers) {
        super(source);
        this.id = id;
        this.button = button;
        this.x = x;
        this.y = y;
        this.pressure = pressure;
        this.type = type;
        this.modifiers = modifiers;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }
}
