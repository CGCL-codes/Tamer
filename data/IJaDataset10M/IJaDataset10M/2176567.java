package org.tagunit.taginfo.jsp12;

import org.tagunit.taginfo.*;
import org.w3c.dom.*;

/**
 * A parser that turns an XML document into an in-memory representation of
 * the tag library descriptor.
 *
 * @author    Simon Brown
 */
public class Jsp12TagLibraryDescriptorParser extends TagLibraryDescriptorParser {

    /**
   * Default, no args constructor.
   */
    public Jsp12TagLibraryDescriptorParser() {
    }

    /**
   * Parses the supplied Document representing a TLD file.
   *
   * @param doc   an XML Document object
   * @return  a TagLibraryInfo object representing the TLD
   * @throws org.tagunit.taginfo.TagLibraryInfoParseException  if the Document can't be parsed
   */
    public TagLibraryInfo parse(Document doc) throws TagLibraryInfoParseException {
        TagLibraryInfo tagLibraryInfo = new TagLibraryInfo();
        parseTagLibraryInfo(doc, tagLibraryInfo);
        parseTags(doc, tagLibraryInfo);
        return tagLibraryInfo;
    }

    private void parseTagLibraryInfo(Document doc, TagLibraryInfo tagLibraryInfo) {
        Node node = doc.getDocumentElement();
        NodeList children = node.getChildNodes();
        Node child;
        for (int i = 0; i < children.getLength(); i++) {
            child = children.item(i);
            String nodeName = child.getNodeName();
            if (nodeName.equalsIgnoreCase("tlib-version")) {
                tagLibraryInfo.setVersion(getNodeValue(child));
            } else if (nodeName.equalsIgnoreCase("jsp-version")) {
                tagLibraryInfo.setJspVersion(getNodeValue(child));
            } else if (nodeName.equalsIgnoreCase("short-name")) {
                tagLibraryInfo.setShortName(getNodeValue(child));
            } else if (nodeName.equalsIgnoreCase("uri")) {
                tagLibraryInfo.setUri(getNodeValue(child));
            } else if (nodeName.equalsIgnoreCase("description")) {
                tagLibraryInfo.setDescription(getNodeValue(child));
            }
        }
    }

    private void parseTags(Document doc, TagLibraryInfo tagLibraryInfo) throws TagLibraryInfoParseException {
        TagInfo tag;
        NodeList tagNodes = doc.getElementsByTagName("tag");
        for (int i = 0; i < tagNodes.getLength(); i++) {
            tag = createTag(tagNodes.item(i));
            try {
                tagLibraryInfo.add(tag);
            } catch (DuplicateTagException e) {
                throw new TagLibraryInfoParseException("Duplicate tag called " + tag.getName() + " was found in the tag library");
            }
        }
    }

    private TagInfo createTag(Node node) {
        TagInfo tag = new TagInfo();
        NodeList children = node.getChildNodes();
        Node child;
        for (int i = 0; i < children.getLength(); i++) {
            child = children.item(i);
            String nodeName = child.getNodeName();
            if (nodeName.equalsIgnoreCase("name")) {
                tag.setName(getNodeValue(child));
            } else if (nodeName.equalsIgnoreCase("tag-class")) {
                tag.setTagHandlerClass(getNodeValue(child));
            } else if (nodeName.equalsIgnoreCase("tei-class")) {
                tag.setTagExtraInfoClass(getNodeValue(child));
            } else if (nodeName.equalsIgnoreCase("body-content")) {
                tag.setBodyContent(getNodeValue(child));
            } else if (nodeName.equalsIgnoreCase("description")) {
                tag.setDescription(getNodeValue(child));
            } else if (nodeName.equalsIgnoreCase("attribute")) {
                TagAttributeInfo attr = new TagAttributeInfo(child);
                tag.add(attr);
            } else if (nodeName.equalsIgnoreCase("variable")) {
                TagVariableInfo var = new TagVariableInfo(child);
                tag.add(var);
            }
        }
        return tag;
    }
}
