package net.sourceforge.ojb2hbm.converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import java.util.List;
import net.sourceforge.ojb2hbm.util.JDOMUtils;

public class IndexConverter extends AbstractConverter {

    private static final Log _logger = LogFactory.getLog(IndexConverter.class);

    public String getTransformationElementName() {
        return "index-descriptor";
    }

    protected String getPath(String columnName) {
        StringBuffer pathBuffer = new StringBuffer("//property/column[@name='");
        pathBuffer.append(columnName);
        pathBuffer.append("']");
        pathBuffer.append("|");
        pathBuffer.append("//id/column[@name='");
        pathBuffer.append(columnName);
        pathBuffer.append("']");
        return pathBuffer.toString();
    }

    public void convert(Element indexDescriptorElement, Element convertedRoot) {
        String indexName = indexDescriptorElement.getAttribute("name").getValue();
        List indexColumns = indexDescriptorElement.getChildren("index-column");
        for (int i = 0; i < indexColumns.size(); i++) {
            Element indexColumnElement = (Element) indexColumns.get(i);
            String columnName = indexColumnElement.getAttribute("name").getValue();
            try {
                XPath path = XPath.newInstance(getPath(columnName));
                List selectedNodes = path.selectNodes(convertedRoot.getDocument());
                Element columnElement = null;
                if (selectedNodes.size() > 0) {
                    columnElement = (Element) selectedNodes.get(0);
                } else {
                    _logger.warn("Unable to find property or id entry for : " + columnName);
                }
                String attributeName;
                if (JDOMUtils.isTrue(indexDescriptorElement.getAttribute("unique"))) {
                    attributeName = "unique-key";
                } else {
                    attributeName = "index";
                }
                if (columnElement != null) {
                    columnElement.setAttribute(attributeName, indexName);
                }
            } catch (Throwable e) {
                throw new RuntimeException("Couldn't insert index attribute for: " + columnName, e);
            }
        }
    }
}
