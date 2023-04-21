package fr.ign.cogit.geoxygene.style.converter;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import fr.ign.cogit.geoxygene.style.Graphic;
import fr.ign.cogit.geoxygene.style.PointSymbolizer;

/**
 * @author Julien Perret
 *
 */
public class PointSymbolizerConverter implements Converter {

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        PointSymbolizer symbolizer = (PointSymbolizer) source;
        if (symbolizer.getGeometryPropertyName().length() > 0) {
            writer.startNode("Geometry");
            writer.startNode("ogc:PropertyName");
            writer.setValue(symbolizer.getGeometryPropertyName());
            writer.endNode();
            writer.endNode();
        }
        if (symbolizer.getGraphic() != null) {
            writer.startNode("Graphic");
            context.convertAnother(symbolizer.getGraphic());
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        PointSymbolizer symbolizer = new PointSymbolizer();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if (reader.getNodeName().equalsIgnoreCase("geometry")) {
                reader.moveDown();
                if (reader.getNodeName().equalsIgnoreCase("ogc:PropertyName")) {
                    symbolizer.setGeometryPropertyName(reader.getValue());
                }
                reader.moveUp();
            } else if (reader.getNodeName().equalsIgnoreCase("graphic")) {
                Graphic graphic = (Graphic) context.convertAnother(symbolizer, Graphic.class, new GraphicConverter());
                symbolizer.setGraphic(graphic);
            }
            reader.moveUp();
        }
        return symbolizer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canConvert(Class classe) {
        return classe.equals(PointSymbolizer.class);
    }
}
