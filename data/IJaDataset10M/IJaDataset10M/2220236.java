package ucalgary.ebe.ci.mice.impl.manymouse;

import ucalgary.ebe.ci.mice.events.impl.CIMouseEvent;

/**
 * @author hkolenda
 * 
 */
public class MousePointerData {

    private int x = 0;

    private int y = 0;

    private int buttonMask = 0;

    private boolean dirty = false;

    private int id = 0;

    public MousePointerData(int id) {
        this.id = id;
    }

    /**
     * @param button
     *            the button to add
     */
    public void addButton(int button) {
        if ((this.buttonMask & button) != button) {
            this.buttonMask |= button;
            setDirty();
        }
    }

    /**
     * @param button
     *            the button to add
     */
    public void removeButton(int button) {
        if ((this.buttonMask & button) == button) {
            this.buttonMask ^= button;
            setDirty();
        }
    }

    /**
     * @return the dirty
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * @param clears
     *            dirty falg
     */
    public void clearDirty() {
        this.dirty = false;
    }

    /**
     * @param stes
     *            dirty falg
     */
    public void setDirty() {
        this.dirty = true;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x
     *            the x to add
     */
    public void addX(int x) {
        this.x += x;
        if (this.x < 0) {
            this.x = 0;
        }
        setDirty();
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(int x) {
        if (this.x != x) {
            this.x = x;
            if (this.x < 0) {
                this.x = 0;
            }
            setDirty();
        }
    }

    /**
     * @return the y
     */
    public int getY() {
        return this.y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(int y) {
        if (this.y != y) {
            this.y = y;
            if (this.y < 0) {
                this.y = 0;
            }
            setDirty();
        }
    }

    /**
     * @param y
     *            the y to add
     */
    public void addY(int y) {
        this.y += y;
        if (this.y < 0) {
            this.y = 0;
        }
        setDirty();
    }

    public CIMouseEvent getMouseEvent() {
        clearDirty();
        return new CIMouseEvent(id, getX(), getY());
    }
}
