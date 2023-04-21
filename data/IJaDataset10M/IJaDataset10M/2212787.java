package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Arrays;
import java.util.List;

public class MusicGameShape {

    private final Color color;

    private final Stroke stroke;

    private final List<Shape> shapes;

    private final boolean fill;

    /**
	 * Creates the music game shape that is used to draw awt-shapes in the GUI.
	 * 
	 * @param shapes a list of shapes to be drawn.
	 * @param color the color of the shapes.
	 * @param stroke the stroke to use when drawing.
	 * @param fill true iff the shape should be filled.
	 */
    public MusicGameShape(List<Shape> shapes, Color color, Stroke stroke, boolean fill) {
        this.shapes = shapes;
        this.color = color;
        this.stroke = stroke;
        this.fill = fill;
    }

    /**
	 * Creates the music game shape that is used to draw an awt-shape in the GUI.
	 * 
	 * @param shape the shape to be drawn.
	 * @param color the color of the shapes.
	 * @param stroke the stroke to use when drawing.
	 * @param fill true iff the shape should be filled.
	 */
    public MusicGameShape(Shape shape, Color color, Stroke stroke, boolean fill) {
        this(Arrays.asList(shape), color, stroke, fill);
    }

    /**
	 * Creates the music game shape that is used to draw an awt-shape in the GUI.
	 * The colour will be black, the stroke used is BasicStroke(2) and it is not filled.
	 * 
	 * @param shape the shape to be drawn.
	 */
    public MusicGameShape(Shape shape) {
        this(Arrays.asList(shape), Color.BLACK, new BasicStroke(2), false);
    }

    /**
	 * @return the color of the shape
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * @return the stroke of the shape
	 */
    public Stroke getStroke() {
        return stroke;
    }

    /**
	 * @return the Java Shape objects associated with this MusicGameShape
	 */
    public List<Shape> getShapes() {
        return shapes;
    }

    /**
	 * @return boolean whether the shapes should be filled or not.
	 */
    public boolean shouldFill() {
        return fill;
    }
}
