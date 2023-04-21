package ofc4j.util;

import ofc4j.model.elements.StackedBarChart.StackKey;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.path.PathTrackingWriter;

public class StackKeyConverter extends ConverterBase<StackKey> {

    @SuppressWarnings("unchecked")
    public boolean canConvert(Class arg0) {
        return StackKey.class.isAssignableFrom(arg0);
    }

    @Override
    public void convert(StackKey o, PathTrackingWriter writer, MarshallingContext mc) {
        writeNode(writer, "text", o.getText());
        writeNode(writer, "colour", o.getColour());
        writeNode(writer, "font-size", o.getFontSize());
    }
}
