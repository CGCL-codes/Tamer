package grammarscope.dependency.properties;

import grammarscope.dependency.Decorators.EdgeShaperClassDecorator;
import grammarscope.dependency.Decorators.StrokeDecorator;
import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import jung.EdgeShaper;

public class IconFactory {

    /**
	 * Make shaper icon
	 * 
	 * @param thisTransformerClass
	 *            shape transformer
	 * @return icon
	 */
    public static Icon makeShaperIcon(final Class<? extends EdgeShaper> thisTransformerClass) {
        try {
            final EdgeShaper thisEdgeShape = thisTransformerClass.newInstance();
            return IconFactory.makeShapeIcon(thisEdgeShape.toShape());
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Make shape icon
	 * 
	 * @param thisShape
	 *            shape
	 * @return icon
	 */
    public static Icon makeShapeIcon(final Shape thisShape) {
        try {
            return new ShapeIcon(thisShape, new BasicStroke(1), 32, 16);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * Make stroke icon
	 * 
	 * @param thisStroke
	 *            stroke
	 * @return icon
	 */
    public static Icon makeStrokeIcon(final Stroke thisStroke) {
        final Shape thisShape = new Line2D.Float(0.f, 0.f, 1.f, 0.f);
        try {
            return new ShapeIcon(thisShape, thisStroke, 32, 16);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Object, Icon> makeShapeIconMap(final EdgeShaperClassDecorator[] theseDecorators) {
        final Map<Object, Icon> thisMap = new HashMap<Object, Icon>();
        for (final EdgeShaperClassDecorator thisDecorator : theseDecorators) {
            thisMap.put(thisDecorator, thisDecorator.toIcon());
        }
        return thisMap;
    }

    public static Map<Object, Icon> makeStrokeIconMap(final StrokeDecorator[] theseDecorators) {
        final Map<Object, Icon> thisMap = new HashMap<Object, Icon>();
        for (final StrokeDecorator thisDecorator : theseDecorators) {
            thisMap.put(thisDecorator, thisDecorator.toIcon());
        }
        return thisMap;
    }
}
