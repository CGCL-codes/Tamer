package saf.v3d.scene;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import javax.vecmath.Point3f;
import saf.v3d.picking.BoundingSphere;
import com.sun.opengl.util.j2d.TextRenderer;

/**
 * Encapsulates a text label.
 * 
 * @author Nick Collier
 */
public class Label {

    private String text;

    private String[] lines;

    private VSpatial parent;

    private Point3f pt = new Point3f();

    private Point3f offset = new Point3f();

    private Color color = Color.BLUE;

    private Position position = Position.CENTER;

    private boolean textChanged = true;

    private Rectangle2D[] bounds;

    private PositionCalculator posCalc = new Center();

    private float maxX, totalHeight;

    public Label(String text, VSpatial parent) {
        this(text, parent, Position.SOUTH);
    }

    /**
   * @param text
   * @param textRender
   */
    public Label(String text, VSpatial parent, Position position) {
        this.text = text;
        this.lines = text.split("\n");
        this.parent = parent;
        setPosition(position);
    }

    public void setOffset(float x, float y) {
        this.offset.set(x, y, 0);
    }

    public Point3f getOffset() {
        return offset;
    }

    public void setPosition(Position position) {
        if (position != this.position) {
            this.position = position;
            if (position == Position.CENTER) {
                posCalc = new Center();
            } else if (position == Position.NORTH) {
                posCalc = new North();
            } else if (position == Position.SOUTH) {
                posCalc = new South();
            } else if (position == Position.EAST) {
                posCalc = new East();
            } else if (position == Position.WEST) {
                posCalc = new West();
            } else if (position == Position.NORTH_EAST) {
                posCalc = new NorthEast();
            } else if (position == Position.NORTH_WEST) {
                posCalc = new NorthWest();
            } else if (position == Position.SOUTH_WEST) {
                posCalc = new SouthWest();
            } else if (position == Position.SOUTH_EAST) {
                posCalc = new SouthEast();
            }
        }
    }

    private void updateLocation(TextRenderer renderer) {
        BoundingSphere sphere = parent.getLocalBoundingSphere();
        pt.set(sphere.getCenterRef());
        parent.transform(pt);
        if (textChanged || bounds == null) {
            bounds = new Rectangle2D[lines.length];
            totalHeight = maxX = 0;
            for (int i = 0; i < lines.length; i++) {
                bounds[i] = renderer.getBounds(lines[i]);
                totalHeight += bounds[i].getHeight();
                maxX = (float) Math.max(maxX, bounds[i].getWidth());
            }
        }
        posCalc.calcPosition(pt, bounds, offset, sphere.getRadius() * parent.getLocalScale(), maxX, totalHeight);
    }

    /**
   * Notifies this label that the font used to draw has changed.
   */
    public void fontChanged() {
        bounds = null;
    }

    public void drawText(TextRenderer renderer) {
        updateLocation(renderer);
        renderer.setColor(color);
        int y = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            renderer.draw(line, (int) pt.x, (int) pt.y - y);
            if (i < bounds.length - 1) {
                y += (int) bounds[i + 1].getHeight() + 1;
            }
        }
    }

    public void setText(String text) {
        textChanged = !this.text.equals(text);
        this.text = text;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private interface PositionCalculator {

        void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height);
    }

    private static class Center implements PositionCalculator {

        @Override
        public void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height) {
            if (bounds.length == 1) {
                pt.x -= bounds[0].getWidth() / 2 + offset.x;
                pt.y -= bounds[0].getHeight() / 2 + offset.y;
            } else {
                pt.x -= width / 2 + offset.x;
                pt.y += (height - bounds[0].getHeight()) / 2 + offset.y;
            }
        }
    }

    private static class North implements PositionCalculator {

        @Override
        public void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height) {
            if (bounds.length == 1) {
                pt.x -= bounds[0].getWidth() / 2 + offset.x;
                pt.y += radius + offset.y;
            } else {
                pt.x -= width / 2 + offset.x;
                pt.y += radius + offset.y + height - (bounds[0].getHeight() / 2);
            }
        }
    }

    private static class South implements PositionCalculator {

        @Override
        public void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height) {
            if (bounds.length == 1) {
                pt.x -= bounds[0].getWidth() / 2 + offset.x;
                pt.y -= radius + bounds[0].getHeight() + offset.y;
            } else {
                pt.x -= width / 2 + offset.x;
                pt.y -= radius + bounds[0].getHeight() + offset.y;
            }
        }
    }

    private static class West implements PositionCalculator {

        @Override
        public void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height) {
            if (bounds.length == 1) {
                pt.x -= radius + offset.x + bounds[0].getWidth();
                pt.y -= bounds[0].getHeight() / 2 + offset.y;
            } else {
                pt.x -= radius + offset.x + width;
                pt.y += (height - bounds[0].getHeight()) / 2 + offset.y;
            }
        }
    }

    private static class East implements PositionCalculator {

        @Override
        public void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height) {
            if (bounds.length == 1) {
                pt.x += radius + offset.x;
                pt.y -= bounds[0].getHeight() / 2 + offset.y;
            } else {
                pt.x += radius + offset.x;
                pt.y += (height - bounds[0].getHeight()) / 2 + offset.y;
            }
        }
    }

    private static class NorthEast implements PositionCalculator {

        @Override
        public void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height) {
            if (bounds.length == 1) {
                pt.x += radius + offset.x;
                pt.y += radius + offset.y;
            } else {
                pt.x += radius + offset.x;
                pt.y += radius + offset.y + height - (bounds[0].getHeight() / 2);
            }
        }
    }

    private static class NorthWest implements PositionCalculator {

        @Override
        public void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height) {
            if (bounds.length == 1) {
                pt.x -= radius + offset.x + bounds[0].getWidth();
                pt.y += radius + offset.y;
            } else {
                pt.x -= radius + offset.x + width;
                pt.y += radius + offset.y + height - (bounds[0].getHeight() / 2);
            }
        }
    }

    private static class SouthWest implements PositionCalculator {

        @Override
        public void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height) {
            if (bounds.length == 1) {
                pt.x -= radius + offset.x + bounds[0].getWidth();
                pt.y -= radius + bounds[0].getHeight() + offset.y;
            } else {
                pt.x -= radius + offset.x + width;
                pt.y -= radius + bounds[0].getHeight() + offset.y;
            }
        }
    }

    private static class SouthEast implements PositionCalculator {

        @Override
        public void calcPosition(Point3f pt, Rectangle2D[] bounds, Point3f offset, float radius, float width, float height) {
            if (bounds.length == 1) {
                pt.x += radius + offset.x;
                pt.y -= radius + bounds[0].getHeight() + offset.y;
            } else {
                pt.x += radius + offset.x;
                pt.y -= radius + bounds[0].getHeight() + offset.y;
            }
        }
    }
}
