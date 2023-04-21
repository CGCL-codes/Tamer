package persister.xml.converter;

import persister.IndexCardVoteDot;
import persister.VoteDot;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class IndexCardVoteDotConverter implements Converter {

    @Override
    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        VoteDot dot = (VoteDot) value;
        writer.startNode("VoteDot");
        writer.addAttribute("cardID", String.valueOf(dot.getCardId()));
        writer.addAttribute("xCord", String.valueOf(dot.getX()));
        writer.addAttribute("yCord", String.valueOf(dot.getY()));
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        IndexCardVoteDot dot = new IndexCardVoteDot();
        dot.setCardId(Long.valueOf(reader.getAttribute("cardID")));
        dot.setX(Integer.valueOf(reader.getAttribute("xCord")));
        dot.setY(Integer.valueOf(reader.getAttribute("yCord")));
        return dot;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canConvert(Class clazz) {
        return clazz.equals(IndexCardVoteDot.class);
    }
}
