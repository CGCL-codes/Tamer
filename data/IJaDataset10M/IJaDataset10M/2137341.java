package org.chaoticengine.cgll.serialization;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 * Simple XML converter for the Slick2D class Rectangle.
 *
 * @author Matt v.d. Westhuizen
 */
public class CircleConverter implements Converter<Circle> {

    public Circle read(InputNode in) throws Exception {
        float radius = 0.0f;
        InputNode n = in.getNext();
        while (n != null) {
            if (n.getName().equals("radius")) {
                radius = Float.parseFloat(n.getValue());
            }
            n = in.getNext();
        }
        return (new Circle(0, 0, radius));
    }

    public void write(OutputNode on, Circle c) throws Exception {
        on.getChild("radius").setValue(Float.toString(c.getRadius()));
        on.commit();
    }
}
