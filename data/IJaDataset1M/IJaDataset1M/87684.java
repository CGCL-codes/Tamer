package core;

public class Key {

    public static final Key LEFT = new Key(37);

    public static final Key RIGHT = new Key(33);

    public static final Key UP = new Key(34);

    public static final Key DOWN = new Key(35);

    private int keyCode;

    private boolean state = false;

    public Key(int UnicodeKeyCode) {
        this.keyCode = UnicodeKeyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }
}
