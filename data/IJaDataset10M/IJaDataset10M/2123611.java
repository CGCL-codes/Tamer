package org.cubicunit.internal.ie;

import java.util.ArrayList;
import java.util.List;
import org.jaxen.JaxenException;
import org.jaxen.XPath;
import com.tapsterrock.jiffie.ElementContainer;
import com.tapsterrock.jiffie.IHTMLDOMNode;
import com.tapsterrock.jiffie.xpath.JiffieXPath;

public class IeEngine {

    private ElementContainer container;

    public IeEngine(ElementContainer container) {
        this.container = container;
    }

    public List<IHTMLDOMNode> getDispatches(String xPath) {
        List<IHTMLDOMNode> result = new ArrayList<IHTMLDOMNode>();
        try {
            XPath xpath = new JiffieXPath(xPath);
            List nodes = xpath.selectNodes(container);
            for (Object obj : nodes) {
                result.add((IHTMLDOMNode) obj);
            }
        } catch (JaxenException e) {
            e.printStackTrace();
        }
        return result;
    }
}
