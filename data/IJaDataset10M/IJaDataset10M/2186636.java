package geovista.toolkitcore.marshal;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import geovista.touchgraph.SubspaceLinkGraph;

public class SubspaceLinkGraphConverter extends VizBeanConverter {

    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(SubspaceLinkGraph.class);
    }

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        super.marshal(value, writer, context);
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        SubspaceLinkGraph plot = new SubspaceLinkGraph();
        plot = (SubspaceLinkGraph) super.unmarshal(reader, context, plot);
        return plot;
    }
}
