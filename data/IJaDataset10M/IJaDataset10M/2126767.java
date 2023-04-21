package orbe.model.io;

import net.sf.doolin.oxml.OXMLContext;
import net.sf.doolin.oxml.adapter.OXMLAdapter;
import net.sf.doolin.util.xml.DOMUtils;
import orbe.model.OrbeMap;
import orbe.model.style.TextStyle;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TextStyleOXMLAdapter implements OXMLAdapter {

    public Object adapt(Node currentNode, String path, OXMLContext context) {
        Node styleNode = context.getXpath().selectSingleNode(currentNode, path);
        if (styleNode == null) {
            return null;
        } else {
            int id = DOMUtils.getIntAttribute((Element) styleNode, "id", true, 0);
            OrbeMap map = (OrbeMap) context.instanceGet("Map");
            TextStyle textStyle = map.getSettings().getTextStyles().getStyle(id);
            return textStyle;
        }
    }
}
