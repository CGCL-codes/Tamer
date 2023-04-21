package org.shapelogic.imageprocessing;

import static org.shapelogic.util.Constants.DOWN;
import static org.shapelogic.util.Constants.LEFT;
import static org.shapelogic.util.Constants.RIGHT;
import static org.shapelogic.util.Constants.UP;
import org.shapelogic.color.ColorFactory;
import org.shapelogic.color.IColorDistanceWithImage;
import org.shapelogic.imageutil.SLImage;
import org.shapelogic.polygon.CPointInt;
import org.shapelogic.polygon.Polygon;

/** Edge Tracer. <br />
 * 
 * The first version is based on Wand from ImageJ 1.38.<br />
 * 
 * It traces with a 2 x 2 square that put the top left pixels inside the 
 * particle and the bottom right outside.<br />
 * 
 * Might be replaced with a version that has all the pixels inside.<br />
 * 
 * @author Sami Badawi
 *
 */
public class EdgeTracerWand implements IEdgeTracer {

    static final int UP_OR_DOWN = -2, LEFT_OR_RIGHT = -3, NA = -1;

    private IColorDistanceWithImage _colorDistanceWithImage;

    private int width, height;

    private double _maxDistance;

    private boolean _traceCloseToColor;

    /** Constructs a Wand object from an ImageProcessor. */
    public EdgeTracerWand(SLImage image, int referenceColor, double maxDistance, boolean traceCloseToColor) {
        _colorDistanceWithImage = ColorFactory.makeColorDistanceWithImage(image);
        _colorDistanceWithImage.setReferenceColor(referenceColor);
        _maxDistance = maxDistance;
        _traceCloseToColor = traceCloseToColor;
        width = image.getWidth();
        height = image.getHeight();
    }

    /** Use XOR to either handle colors close to reference color or far away. */
    private boolean inside(int x, int y) {
        if (x < 0 || y < 0) return false;
        if (width <= x || height <= y) return false;
        return _traceCloseToColor ^ (_maxDistance < _colorDistanceWithImage.distanceToReferenceColor(x, y));
    }

    boolean isLine(int xs, int ys) {
        int r = 5;
        int xmin = xs;
        int xmax = xs + 2 * r;
        if (xmax >= width) xmax = width - 1;
        int ymin = ys - r;
        if (ymin < 0) ymin = 0;
        int ymax = ys + r;
        if (ymax >= height) ymax = height - 1;
        int area = 0;
        int insideCount = 0;
        for (int x = xmin; (x <= xmax); x++) for (int y = ymin; y <= ymax; y++) {
            area++;
            if (inside(x, y)) insideCount++;
        }
        return ((double) insideCount) / area >= 0.75;
    }

    /** Traces the boundary of an area of uniform color, where
		'startX' and 'startY' are somewhere inside the area. 
		A 16 entry lookup table is used to determine the
		direction at each step of the tracing process. */
    public Polygon autoOutline(int startX, int startY) {
        int x = startX;
        int y = startY;
        int direction;
        do {
            x++;
        } while (inside(x, y));
        if (isLine(x, y)) {
            direction = UP;
        } else {
            if (!inside(x - 1, y - 1)) direction = RIGHT; else if (inside(x, y - 1)) direction = LEFT; else direction = DOWN;
        }
        return traceEdge(x, y, direction);
    }

    Polygon traceEdge(int xstart, int ystart, int startingDirection) {
        Polygon polygon = new Polygon();
        polygon.startMultiLine();
        ChainCodeHandler chainCodeHandler = new ChainCodeHandler(polygon.getAnnotatedShape());
        chainCodeHandler = new ChainCodeHandler(polygon.getAnnotatedShape());
        chainCodeHandler.setup();
        chainCodeHandler.setMultiLine(polygon.getCurrentMultiLine());
        chainCodeHandler.setFirstPoint(new CPointInt(xstart, ystart));
        int[] table = { NA, RIGHT, DOWN, RIGHT, UP, UP, UP_OR_DOWN, UP, LEFT, LEFT_OR_RIGHT, DOWN, RIGHT, LEFT, LEFT, DOWN, NA };
        int index;
        int newDirection;
        int x = xstart;
        int y = ystart;
        int direction = startingDirection;
        boolean UL = inside(x - 1, y - 1);
        boolean UR = inside(x, y - 1);
        boolean LL = inside(x - 1, y);
        boolean LR = inside(x, y);
        int count = 0;
        do {
            index = 0;
            if (LR) index |= 1;
            if (LL) index |= 2;
            if (UR) index |= 4;
            if (UL) index |= 8;
            newDirection = table[index];
            if (newDirection == UP_OR_DOWN) {
                if (direction == RIGHT) newDirection = UP; else newDirection = DOWN;
            }
            if (newDirection == LEFT_OR_RIGHT) {
                if (direction == UP) newDirection = LEFT; else newDirection = RIGHT;
            }
            count++;
            switch(newDirection) {
                case UP:
                    y = y - 1;
                    LL = UL;
                    LR = UR;
                    UL = inside(x - 1, y - 1);
                    UR = inside(x, y - 1);
                    break;
                case DOWN:
                    y = y + 1;
                    UL = LL;
                    UR = LR;
                    LL = inside(x - 1, y);
                    LR = inside(x, y);
                    break;
                case LEFT:
                    x = x - 1;
                    UR = UL;
                    LR = LL;
                    UL = inside(x - 1, y - 1);
                    LL = inside(x - 1, y);
                    break;
                case RIGHT:
                    x = x + 1;
                    UL = UR;
                    LL = LR;
                    UR = inside(x, y - 1);
                    LR = inside(x, y);
                    break;
            }
            direction = newDirection;
            if (!chainCodeHandler.addChainCode((byte) direction)) break;
        } while ((x != xstart || y != ystart || direction != startingDirection));
        chainCodeHandler.getValue();
        polygon.setPerimeter(chainCodeHandler.getPerimeter());
        polygon.getValue();
        polygon.getBBox().add(chainCodeHandler._bBox);
        return polygon;
    }
}
