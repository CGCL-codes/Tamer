package jcckit.graphic;

/**
 *  A rectangle.
 *
 *  @author Franz-Josef Elmer
 */
public class Rectangle extends BasicGraphicalElement {

    private final GraphPoint _center;

    private final double _width;

    private final double _height;

    /**
   *  Creates a new instance.
   *  @param center The position of the center of this element.
   *  @param width The width.
   *  @param height The height.
   *  @param attributes Drawing attributes. Can be <tt>null</tt>.
   */
    public Rectangle(GraphPoint center, double width, double height, GraphicAttributes attributes) {
        super(attributes);
        _center = center;
        _width = width;
        _height = height;
    }

    /** Returns the center of this element. */
    public GraphPoint getCenter() {
        return _center;
    }

    /** Returns the width of this element. */
    public double getWidth() {
        return _width;
    }

    /** Returns the height of this element. */
    public double getHeight() {
        return _height;
    }

    /**
   *  Renders this rectangle with the specified {@link Renderer}.
   *  @param renderer An instance of {@link RectangleRenderer}.
   *  @throws IllegalArgumentException if <tt>renderer</tt> is not
   *          an instance of <tt>RectangleRenderer</tt>.
   */
    public void renderWith(Renderer renderer) {
        if (renderer instanceof RectangleRenderer) {
            ((RectangleRenderer) renderer).render(this);
        } else {
            throw new IllegalArgumentException(renderer + " does not implements RectangleRenderer.");
        }
    }
}
