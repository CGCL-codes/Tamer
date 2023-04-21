package emulator.GUI.display;

public interface VicDisplay {

    public static final int MAX_SCREEN_WIDTH = 208;

    public static final int MAX_SCREEN_HEIGHT = 260;

    public abstract void putPixel(int x, int y, int color);

    public abstract void putPixel(int x, int y, int color, int x_width);

    public abstract void showScreen();
}
