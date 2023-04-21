package com.yserbius.hex;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

/**
 * 
 * @author Yserbius
 * 
 *
 */
public class Hex {

    private Map<HexDir, Hex> map;

    private int x;

    private int y;

    private boolean isEmpty;

    private HexPaintListener onPaint;

    public Hex(int a, int b) {
        x = a;
        y = b;
        map = new HashMap<HexDir, Hex>();
        isEmpty = false;
        onPaint = new HexPaintListener() {

            @Override
            public void onPaint(Graphics g, Polygon hex) {
                g.drawPolygon(hex);
            }
        };
    }

    public Collection<Hex> getAdjacent() {
        return map.values();
    }

    public void setAdjacent(HexDir i, Hex hex) {
        map.put(i, hex);
    }

    public Hex get(HexDir i) {
        Hex hex;
        hex = map.get(i);
        return hex;
    }

    public boolean equals(Hex hex) {
        return x == hex.x && y == hex.y;
    }

    public int getx() {
        return x;
    }

    public int gety() {
        return y;
    }

    public void setEmpty(boolean setEmpty) {
        this.isEmpty = setEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setPaintListener(HexPaintListener listener) {
        onPaint = listener;
    }

    public HexPaintListener getPaintListener() {
        return onPaint;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
