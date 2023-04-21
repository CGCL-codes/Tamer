package jofc2.util;

import jofc2.model.elements.ShapeChart;
import jofc2.model.elements.ShapeChart.Point;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.path.PathTrackingWriter;

public class ShapePointConverter extends ConverterBase<Point> {

    @Override
    public void convert(ShapeChart.Point o, PathTrackingWriter writer, MarshallingContext mc) {
        writeNode(writer, "x", o.getX(), false);
        writeNode(writer, "y", o.getY(), false);
    }

    @SuppressWarnings("unchecked")
    public boolean canConvert(Class arg0) {
        return Point.class.isAssignableFrom(arg0);
    }
}
