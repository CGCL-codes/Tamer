package praktikumid.k10.p28.paint;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;

/**
 * @author Ivor
 *
 */
public class ShapeStore extends JComponent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private List<Shape> shapes = new ArrayList<Shape>();

    /**
	 * Kujundite kasti kujundi lisamine.
	 * 
	 * @param shape
	 */
    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    /**
	 * Kujundite kasti joonistamine, aka kujundite joonistamine.
	 */
    public void paintShapes(Graphics g) {
        super.paint(g);
        for (Shape shape : shapes) {
            shape.paintShape(g);
        }
    }
}
