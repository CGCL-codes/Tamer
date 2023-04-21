package libgeo.kml;

import java.util.ArrayList;
import java.util.List;
import libgeo.Point;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class LineStyle implements KMLElement {

    public static final String NodeName = "LineStyle";

    public Color color;

    public Double width;

    public LineStyle() {
    }

    public LineStyle(Node node) {
        Node child;
        if ((child = Util.getChildByName(node, Color.NodeName)) != null) color = new Color(child);
        width = Util.getDouble(node, "width");
    }

    public Node toDomNode(Document doc) {
        Node node = doc.createElement(NodeName);
        if (color != null) node.appendChild(color.toDomNode(doc));
        if (width != null) node.appendChild(Util.createTextElement(doc, "width", width.toString()));
        return node;
    }
}
