package scene.factory;

import java.util.List;
import objects.base.ScalableObject;
import scene.RObject;
import scene.factory.property.FloatingPointProperty;
import scene.factory.property.Property;

/**
 * A factory for objects that have an associated size. These objects are
 * resizable in the level editor.
 * 
 * @author tom
 * @param <ObjectType>
 *            The type of objects creatd by the factory.
 */
public abstract class ScalableObjectFactory<ObjectType extends RObject & ScalableObject> extends BasicObjectFactory<ObjectType> {

    /**
	 * The width of the objects created by the factory.
	 */
    protected double width = 16;

    /**
	 * The height of the objects created by the factory.
	 */
    protected double height = 16;

    /**
	 * 
	 */
    public ScalableObjectFactory() {
        super();
        properties.put("width", Double.toString(width));
        properties.put("height", Double.toString(height));
    }

    @Override
    protected void handleProperties() throws InvalidValueException {
        super.handleProperties();
        width = getDoubleValue("width", width);
        height = getDoubleValue("height", height);
    }

    /**
	 * Set the width of the objects created by the factory.
	 * 
	 * @param w
	 *            The new width of the objects created by the factory (in screen
	 *            coordinates).
	 */
    public void setWidth(double w) {
        width = w;
        properties.put("width", Double.toString(w));
    }

    /**
	 * Set the height of the objects created by the factory.
	 * 
	 * @param h
	 *            The new height of the objects created by the factory (in
	 *            screen coordinates).
	 */
    public void setHeight(double h) {
        height = h;
        properties.put("height", Double.toString(h));
    }

    @Override
    protected List<Property> generateSpec() {
        List<Property> spec = super.generateSpec();
        spec.add(new FloatingPointProperty("width", "Width"));
        spec.add(new FloatingPointProperty("height", "Height"));
        return spec;
    }

    @Override
    protected void attachProperties(ObjectType o) {
        super.attachProperties(o);
        o.setWidth(width);
        o.setHeight(height);
    }
}
