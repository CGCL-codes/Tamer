package net.sourceforge.fluxion.runcible.graph.view;

import net.sourceforge.fluxion.graph.view.EdgeView;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Javadocs go here
 *
 * @author Tony Burdett
 * @date 02-Feb-2009
 */
public class FlexEdgeView implements EdgeView {

    private String headNodeName;

    private String tailNodeName;

    private String label;

    private Map<String, String> displayParams = new HashMap<String, String>();

    private XMLOutputFactory factory;

    public FlexEdgeView() {
        factory = XMLOutputFactory.newInstance();
    }

    public void setDisplayParams(Map<String, String> displayParams) {
        this.displayParams = displayParams;
    }

    public Map<String, String> getDisplayParams() {
        return displayParams;
    }

    public void render(OutputStream outputStream) throws IOException {
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream);
            writer.writeEmptyElement("Edge");
            writer.writeAttribute("fromID", headNodeName);
            writer.writeAttribute("toID", tailNodeName);
            if (label != null) {
                writer.writeAttribute("edgeLabel", label);
            }
            for (String key : displayParams.keySet()) {
                writer.writeAttribute(key, displayParams.get(key));
            }
            writer.writeCharacters("");
            writer.flush();
        } catch (XMLStreamException e) {
            throw new IOException(e.getMessage());
        }
    }

    public String getHeadNodeName() {
        return headNodeName;
    }

    public void setHeadNodeName(String headNodeName) {
        this.headNodeName = headNodeName;
    }

    public String getTailNodeName() {
        return tailNodeName;
    }

    public void setTailNodeName(String tailNodeName) {
        this.tailNodeName = tailNodeName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
