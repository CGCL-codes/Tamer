package pl.edu.amu.wmi.kino.visualjavafx.nodes.objects.shapes;

import java.beans.IntrospectionException;
import org.openide.nodes.Children;
import pl.edu.amu.wmi.kino.visualjavafx.model.objects.shapes.Rectangle;
import pl.edu.amu.wmi.kino.visualjavafx.nodes.objects.helpers.JavaFxNode;

/**
 *
 * @author psychollek
 */
public class RectangleNode extends JavaFxNode<Rectangle> {

    private Rectangle rectangle = null;

    public RectangleNode(Rectangle rectangle) throws IntrospectionException {
        super(rectangle, Children.LEAF);
        this.rectangle = rectangle;
    }
}
