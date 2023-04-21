package com.radroid.rts.world;

import java.util.HashMap;
import java.util.Map;
import com.radroid.rts.position.Position3f;

class Pixmap2D {

    private int width;

    private int height;

    private Map<Integer, Position3f> positions = new HashMap<Integer, Position3f>();

    public Pixmap2D(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }

    public void setPosition(int x, int y, Position3f position) {
        int i = y * width + x;
        positions.put(i, position);
    }

    public Position3f getPosition(int x, int y) {
        int i = y * width + x;
        Position3f position = positions.get(i);
        if (position == null) {
            position = new Position3f();
            setPosition(x, y, position);
        }
        return position;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public float getPosition1f(int x, int y) {
        return getPosition(x, y).getZ();
    }

    public void setPixel(int x, int y, float alpha) {
        Position3f position = getPosition(x, y);
        position.setZ(alpha);
    }
}
