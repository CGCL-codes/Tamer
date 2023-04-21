package se.kristofer.karlsson.util.bunchpackager.backend.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RectanglePlacement {

    private List<Rectangle> rectangles = new ArrayList<Rectangle>();

    private Map<String, Rectangle> map = new HashMap<String, Rectangle>();

    ;

    private int width;

    private int height;

    private int pieceArea;

    private Boolean valid;

    public static boolean overlaps(Rectangle r1, Rectangle r2, int x1, int y1) {
        int x2 = r2.getX();
        if (x1 >= x2 + r2.getWidth()) return false;
        if (x2 >= x1 + r1.getWidth()) return false;
        int y2 = r2.getY();
        if (y1 >= y2 + r2.getHeight()) return false;
        if (y2 >= y1 + r1.getHeight()) return false;
        return true;
    }

    public static boolean overlaps(Rectangle r1, Rectangle r2) {
        return overlaps(r1, r2, r1.getX(), r1.getY());
    }

    public boolean canAdd(Rectangle r1, int col, int row) {
        for (Rectangle r2 : rectangles) {
            if (overlaps(r1, r2, col, row)) {
                return false;
            }
        }
        return true;
    }

    public void addRectangle(Rectangle rectangle) {
        rectangles.add(rectangle);
        map.put(rectangle.getIdentifier(), rectangle);
        width = Math.max(width, rectangle.getX() + rectangle.getWidth());
        height = Math.max(height, rectangle.getY() + rectangle.getHeight());
        pieceArea += rectangle.getWidth() * rectangle.getHeight();
        valid = null;
    }

    public int getArea() {
        return getWidth() * getHeight();
    }

    public int getPieceAreaSum() {
        return pieceArea;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isValid() {
        if (valid == null) {
            valid = checkIntersects();
        }
        return valid;
    }

    private Boolean checkIntersects() {
        int n = rectangles.size();
        for (int i = 0; i < n - 1; i++) {
            Rectangle r1 = rectangles.get(i);
            for (int j = i + 1; j < n; j++) {
                Rectangle r2 = rectangles.get(j);
                if (overlaps(r1, r2, r1.getX(), r1.getY())) {
                    System.out.println(i + ", " + j);
                    System.out.println(r1);
                    System.out.println(r2);
                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    public Rectangle findByIdentifier(String identifier) {
        return map.get(identifier);
    }
}
