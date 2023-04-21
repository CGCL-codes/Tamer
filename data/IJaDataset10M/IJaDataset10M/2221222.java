package geovista.toolkitcore.marshal;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import geovista.animation.IndicationAnimator;

public class IndicationAnimatorConverter extends VizBeanConverter {

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(IndicationAnimator.class);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        super.marshal(value, writer, context);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        IndicationAnimator plot = new IndicationAnimator();
        plot = (IndicationAnimator) super.unmarshal(reader, context, plot);
        return plot;
    }
}
