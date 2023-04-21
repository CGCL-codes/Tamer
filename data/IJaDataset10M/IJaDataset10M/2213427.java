package net.java.dev.joode.graphics2D;

import java.util.HashMap;
import net.java.dev.joode.geom.Circle;
import net.java.dev.joode.geom.Rectangle;
import net.java.dev.joode.joint.JointHinge;

/**
 * Allows simulation objects to be associated with a factory 
 * for construction of graphical representations
 * @author s0570397
 *
 */
public class Converter2D {

    HashMap<Class<?>, Drawable2DFactory> adapters = new HashMap<Class<?>, Drawable2DFactory>();

    /**
	 * creates a default converter
	 *
	 */
    public static Converter2D getDefault() {
        Converter2D defaultConverter = new Converter2D();
        defaultConverter.setFactory(Circle.class, new Drawable2DFactory() {

            public Drawable2D convert(Object geom) {
                return new BoundCircle((Circle) geom);
            }
        });
        defaultConverter.setFactory(Rectangle.class, new Drawable2DFactory() {

            public Drawable2D convert(Object geom) {
                return new BoundRectangle((Rectangle) geom);
            }
        });
        defaultConverter.setFactory(JointHinge.class, new Drawable2DFactory() {

            public Drawable2D convert(Object obj) {
                JointHinge hinge = (JointHinge) obj;
                CompositeDrawable2D drawable = new CompositeDrawable2D();
                drawable.addDrawable(new BoundLine(hinge.getBody(0).getPosition(), hinge.getBody(0).getRotation(), hinge.getAnchor1()));
                if (hinge.getBody(1) != null) {
                    drawable.addDrawable(new BoundLine(hinge.getBody(1).getPosition(), hinge.getBody(1).getRotation(), hinge.getAnchor2()));
                } else {
                    drawable.addDrawable(new BoundLine(hinge.getBody(0).getPosition(), hinge.getAnchor2()));
                }
                return drawable;
            }
        });
        return defaultConverter;
    }

    public Drawable2D convert(Object simObject) {
        Drawable2DFactory factory = adapters.get(simObject.getClass());
        if (factory == null) return null; else {
            return factory.convert(simObject);
        }
    }

    public void setFactory(Class<?> objectClass, Drawable2DFactory factory) {
        adapters.put(objectClass, factory);
    }
}
