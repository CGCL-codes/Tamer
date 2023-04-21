package de.tuc.in.sse.weit.export.ootrans.transform;

import java.io.File;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import de.tuc.in.sse.weit.export.ootrans.odt.ODTFile;
import de.tuc.in.sse.weit.export.ootrans.xml.ElemIterator;
import de.tuc.in.sse.weit.export.ootrans.xml.XMLInserter;
import de.tuc.in.sse.weit.export.ootrans.xml.XMLUtilities;

public class ListHandler implements TransformCmdHandler {

    private String cmdList = null;

    private String cmdListItem = "li";

    private static final String NODE_OO_LIST = "text:list";

    private static final String ATTR_OO_LIST_STYLE = "text:style-name";

    private static final String NODE_OO_LIST_ENTRY = "text:list-item";

    private static final Logger logger = Logger.getLogger(ListHandler.class);

    /** List style */
    private String styleName = null;

    private Element styleData = null;

    public XMLInserter handleCmd(Node cmd, XMLInserter insert, Transformer transformer, boolean lastCmd) {
        if (cmd.getNodeType() != Node.ELEMENT_NODE || !cmd.getNodeName().equals(cmdList)) return null;
        Element listNode = insert.insertNewElement(NODE_OO_LIST);
        if (styleName != null && !styleName.equals("")) listNode.setAttribute(ATTR_OO_LIST_STYLE, styleName);
        XMLInserter listInserter = new XMLInserter(listNode);
        for (Element child : new ElemIterator(cmd)) handleListEntry(child, listInserter, transformer);
        return ParagraphHandler.insertParagraph(listNode, insert);
    }

    public void preprocess(Document doc, Object userData) {
        ODTFile outputFile = (ODTFile) userData;
        if (styleData != null) {
            styleName = outputFile.ensureAutoStyle(styleData);
        }
    }

    public void postprocess(Document doc, Object userData) {
    }

    private void handleListEntry(Element entry, XMLInserter listInserter, Transformer transformer) {
        if (!entry.getNodeName().equals(cmdListItem)) {
            logger.warn("Unknown tag in list: " + entry.getNodeName() + " (list entry expected)");
            return;
        }
        Element entryNode = listInserter.insertNewElement(NODE_OO_LIST_ENTRY);
        Element containingParagraph = ParagraphHandler.getContainingParagraph(listInserter.getContainingNode());
        Element paragraphNode;
        if (containingParagraph == null) paragraphNode = new XMLInserter(entryNode).insertNewElement(ParagraphHandler.NODE_OO_PARAGRAPH); else paragraphNode = (Element) new XMLInserter(entryNode).importNode(containingParagraph, false);
        transformer.handleCmds(entry, new XMLInserter(paragraphNode));
    }

    public void loadConfig(Element node, File parentPath) {
        cmdList = node.getAttribute("tag");
        if (cmdList == null || cmdList.equals("")) {
            logger.warn("List configuration: Missing tag name!");
            return;
        }
        if (node.hasAttribute("item-tag")) cmdListItem = node.getAttribute("item-tag");
        if (node.hasAttribute("style")) {
            styleName = node.getAttribute("style");
            return;
        }
        styleData = XMLUtilities.getElem(node, ODTFile.NODE_LIST_STYLE);
    }
}
