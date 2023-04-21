package de.fu_berlin.inf.dpp.videosharing.activities;

/**
 * @author s-lau
 */
public class MouseVideoActivity extends VideoActivity {

    private static final long serialVersionUID = -7701446680571577001L;

    int x = -1;

    int y = -1;

    int width = -1;

    int height = -1;

    public MouseVideoActivity(int x, int y, int width, int height, Type type) {
        super(type);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getWidth(int screenWidth) {
        if (width < 0) return -1;
        return (int) (((double) x / width) * screenWidth);
    }

    public int getHeight(int screenHeight) {
        if (height < 0) return -1;
        return (int) (((double) y / height) * screenHeight);
    }

    @Override
    public String toString() {
        String position = " ";
        if (width > 0) position = " @" + x + "x" + y + " (" + width + "x" + height + ") ";
        return type.name() + position;
    }
}
