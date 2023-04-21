package net.sealisland.swing.dial;

public class NullDialRenderer extends DefaultDialRenderer {

    private static final NullDialRenderer instance = new NullDialRenderer();

    protected NullDialRenderer() {
    }

    protected void setValue(int value) {
        setText(" ");
    }

    public static NullDialRenderer getInstance() {
        return instance;
    }
}
