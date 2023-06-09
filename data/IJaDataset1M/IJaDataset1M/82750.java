package net.sf.xslthl;

import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xalan.extensions.ExpressionContext;
import org.apache.xpath.NodeSet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XalanConnector for XsltHL. Usage: Add the namespace
 * <code>xmlns:xhl="xalan://net.sf.xslthl.XalanConnector"</code> then you can
 * use the function:
 * <code>xhl:highlight($language, exsl:node-set($content))</code> or <code>xhl:highlight($language, exsl:node-set($content), 'myConfigFile')</code>
 * 
 * @author Michiel Hendriks
 */
public class ConnectorXalan {

    protected static NodeList highlight(ExpressionContext c, String source, MainHighlighter hl, Config config) throws Exception {
        NodeSet list = new NodeSet();
        List<Block> l = hl.highlight(source);
        for (Block b : l) {
            Node elm;
            if (b.isStyled()) {
                elm = DocumentInstance.doc.createElementNS(config.uri, ((StyledBlock) b).getStyle());
                elm.setTextContent(b.getText());
            } else {
                elm = DocumentInstance.doc.createTextNode(b.getText());
            }
            list.addElement(elm);
        }
        return list;
    }

    /**
	 * highlight the given nodes using the standard configuration
	 * 
	 * @param context
	 * @param hlCode
	 * @param nodes
	 * @return
	 */
    public static final NodeList highlight(ExpressionContext context, String hlCode, NodeList nodes) {
        return highlight(context, hlCode, nodes, null);
    }

    /**
	 * highlight the given nodes
	 * 
	 * @param context
	 * @param hlCode
	 * @param nodes
	 * @param configFilename
	 * @return
	 */
    public static final NodeList highlight(ExpressionContext context, String hlCode, NodeList nodes, String configFilename) {
        try {
            Config c = Config.getInstance(configFilename);
            MainHighlighter hl = c.getMainHighlighter(hlCode);
            NodeSet list = new NodeSet();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                NodeList childs = n.getChildNodes();
                for (int j = 0; j < childs.getLength(); j++) {
                    Node m = childs.item(j);
                    if (m.getNodeType() == Node.TEXT_NODE) {
                        if (hl != null) {
                            list.addNodes(highlight(context, m.getNodeValue(), hl, c));
                        } else {
                            list.addNode(DocumentInstance.doc.createTextNode(m.getNodeValue()));
                        }
                    } else {
                        list.addNode(m);
                    }
                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * Used for note construction. (lazy loading)
	 */
    private static class DocumentInstance {

        private static final Document doc;

        static {
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
