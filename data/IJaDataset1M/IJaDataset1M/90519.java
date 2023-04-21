package mduel;

import java.awt.event.KeyEvent;

public class PKeyInfo {

    int left, right, up, down, action;

    boolean l_pressed = false, r_pressed = false, u_pressed = false, d_pressed = false, act_pressed = false;

    public PKeyInfo(int leftKey, int rightKey, int upKey, int downKey, int actionKey) {
        left = leftKey;
        right = rightKey;
        up = upKey;
        down = downKey;
        action = actionKey;
    }

    public PKeyInfo() {
        this(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_CONTROL);
    }

    public boolean setKeyStates(KeyEvent ke, boolean state) {
        if (ke.getKeyCode() == left) {
            l_pressed = state;
            return true;
        } else if (ke.getKeyCode() == right) {
            r_pressed = state;
            return true;
        } else if (ke.getKeyCode() == up) {
            u_pressed = state;
            return true;
        } else if (ke.getKeyCode() == down) {
            d_pressed = state;
            return true;
        } else if (ke.getKeyCode() == action) {
            act_pressed = state;
            return true;
        } else return false;
    }

    public boolean isAct_pressed() {
        return act_pressed;
    }

    public int getAction() {
        return action;
    }

    public boolean isD_pressed() {
        return d_pressed;
    }

    public int getDown() {
        return down;
    }

    public boolean isL_pressed() {
        return l_pressed;
    }

    public int getLeft() {
        return left;
    }

    public boolean isR_pressed() {
        return r_pressed;
    }

    public int getRight() {
        return right;
    }

    public boolean isU_pressed() {
        return u_pressed;
    }

    public int getUp() {
        return up;
    }

    public void setAct_pressed(boolean act_pressed) {
        this.act_pressed = act_pressed;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setD_pressed(boolean d_pressed) {
        this.d_pressed = d_pressed;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public void setL_pressed(boolean l_pressed) {
        this.l_pressed = l_pressed;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setR_pressed(boolean r_pressed) {
        this.r_pressed = r_pressed;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setU_pressed(boolean u_pressed) {
        this.u_pressed = u_pressed;
    }

    public void setUp(int up) {
        this.up = up;
    }
}
