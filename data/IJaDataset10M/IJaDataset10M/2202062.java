package org.fao.waicent.attributes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.apache.xalan.serialize.Serializer;
import org.apache.xalan.serialize.SerializerFactory;
import org.apache.xalan.templates.OutputProperties;
import org.apache.xpath.XPathAPI;
import org.fao.waicent.util.Debug;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class KeyedExtentDOM implements KeyedExtentInterface {

    /**     * Attribute that stores the Extent data in a DOM tree     */
    private Document document = null;

    /**     * Physical file from which the document was loaded.     */
    private File file = null;

    /**     * alisaf: added constructor to support building object from DOM.     * and storing a reference to the original KeyedExtent document (attributes.xml).     */
    public KeyedExtentDOM(Document document, File file) {
        this.document = document;
        this.file = file;
    }

    /**     *  alisaf: Implementation of the interface method in KeyedExtentInterface.     *     *  @param key Path to the set of indicators for which we want to get metadata.     *  @return Element The DOM object representing the metadata.     */
    public Element getSources(Key key) {
        resetSelectedSources();
        Element extent_element = null;
        try {
            NodeList nodelist = XPathAPI.selectNodeList(document, "/ROOT/SourceExtent/KeyedExtent/KeyedExtentEntry/Key");
            for (int i = 0; i < nodelist.getLength(); i++) {
                boolean match = true;
                for (int j = 0; j < key.size(); j++) {
                    int item = Integer.parseInt(((Element) nodelist.item(i)).getAttribute("D_" + j));
                    if (item != -1 && item != key.at(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    int adjusted_index = i + 1;
                    Node node = XPathAPI.selectSingleNode(document, "/ROOT/SourceExtent/KeyedExtent/KeyedExtentEntry[" + adjusted_index + "]");
                    String index = ((Element) node).getAttribute("index");
                    Element definition_element = ((Element) XPathAPI.selectSingleNode(document, "/ROOT/SourceExtent/KeyedExtent/Definition[@C_" + index + "]"));
                    if (definition_element.getAttribute("name") != null && definition_element.getAttribute("name").trim().length() != 0) {
                        definition_element.setAttribute("selected", "true");
                    }
                }
            }
            extent_element = (Element) XPathAPI.selectSingleNode(document, "/ROOT/SourceExtent");
        } catch (TransformerException e) {
            System.err.println("EXCEPTION:\n" + Debug.getCallingMethod() + "\n" + e);
        }
        return extent_element;
    }

    /**     *  alisaf: Implementation of the interface method in KeyedExtentInterface.     *  Note: This method is too complicated, and it's because the source DOM has     *  been adopted from the format of the Binary stream data.  So some modification     *  and rearchitecture of the DOM tree for Attributes (NoteExtent) could significantly     *  simplify this method.     *     *  @param key Path to the set of indicators for which we want to get metadata.     *  @return Document The DOM object representing the metadata.     */
    public Element getNotes(Key key) {
        resetSelectedNotes();
        Element extent_element = null;
        try {
            NodeList nodelist = XPathAPI.selectNodeList(document, "/ROOT/NoteExtent/KeyedExtent/KeyedExtentEntry/Key");
            for (int i = 0; i < nodelist.getLength(); i++) {
                boolean match = true;
                for (int j = 0; j < key.size(); j++) {
                    int item = Integer.parseInt(((Element) nodelist.item(i)).getAttribute("D_" + j));
                    if (item != -1 && item != key.at(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    int adjusted_index = i + 1;
                    Node node = XPathAPI.selectSingleNode(document, "/ROOT/NoteExtent/KeyedExtent/KeyedExtentEntry[" + adjusted_index + "]");
                    String index = ((Element) node).getAttribute("index");
                    ((Element) XPathAPI.selectSingleNode(document, "/ROOT/NoteExtent/KeyedExtent/Definition[@C_" + index + "]")).setAttribute("selected", "true");
                }
            }
            extent_element = (Element) XPathAPI.selectSingleNode(document, "/ROOT/NoteExtent");
        } catch (TransformerException e) {
            System.err.println("EXCEPTION:\n" + Debug.getCallingMethod() + "\n" + e);
        }
        return extent_element;
    }

    /**     *  alisaf: Implementation of the interface method in KeyedExtentInterface.     *  Note: This method is too complicated, and it's because the link DOM has     *  been adopted from the format of the Binary stream data.  So some modification     *  and rearchitecture of the DOM tree for Attributes (LinkExtent) could significantly     *  simplify this method.     *     *  @param key Path to the set of indicators for which we want to get metadata.     *  @return Element The DOM object representing the metadata.     */
    public Element getLinks(Key key) {
        resetSelectedLinks();
        Element extent_element = null;
        try {
            NodeList nodelist = XPathAPI.selectNodeList(document, "/ROOT/LinkExtent/KeyedExtent/KeyedExtentEntry/Key");
            for (int i = 0; i < nodelist.getLength(); i++) {
                boolean match = true;
                for (int j = 0; j < key.size(); j++) {
                    int item = Integer.parseInt(((Element) nodelist.item(i)).getAttribute("D_" + j));
                    if (item != -1 && item != key.at(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    int adjusted_index = i + 1;
                    Node node = XPathAPI.selectSingleNode(document, "/ROOT/LinkExtent/KeyedExtent/KeyedExtentEntry[" + adjusted_index + "]");
                    String index = ((Element) node).getAttribute("index");
                    ((Element) XPathAPI.selectSingleNode(document, "/ROOT/LinkExtent/KeyedExtent/Definition[@C_" + index + "]")).setAttribute("selected", "true");
                }
            }
            extent_element = (Element) XPathAPI.selectSingleNode(document, "/ROOT/LinkExtent");
        } catch (TransformerException e) {
            System.err.println("EXCEPTION:\n" + Debug.getCallingMethod() + "\n" + e);
        }
        return extent_element;
    }

    public void resetSelectedNotes() {
        try {
            NodeList nodelist = XPathAPI.selectNodeList(document, "/ROOT/NoteExtent/KeyedExtent/Definition");
            for (int i = 0; i < nodelist.getLength(); i++) {
                ((Element) nodelist.item(i)).setAttribute("selected", "false");
            }
        } catch (TransformerException e) {
            System.err.println("EXCEPTION:\n" + Debug.getCallingMethod() + "\n" + e);
        }
    }

    public void resetSelectedSources() {
        try {
            NodeList nodelist = XPathAPI.selectNodeList(document, "/ROOT/SourceExtent/KeyedExtent/Definition");
            for (int i = 0; i < nodelist.getLength(); i++) {
                ((Element) nodelist.item(i)).setAttribute("selected", "false");
            }
        } catch (TransformerException e) {
            System.err.println("EXCEPTION:\n" + Debug.getCallingMethod() + "\n" + e);
        }
    }

    public void resetSelectedLinks() {
        try {
            NodeList nodelist = XPathAPI.selectNodeList(document, "/ROOT/LinkExtent/KeyedExtent/Definition");
            for (int i = 0; i < nodelist.getLength(); i++) {
                ((Element) nodelist.item(i)).setAttribute("selected", "false");
            }
        } catch (TransformerException e) {
            System.err.println("EXCEPTION:\n" + Debug.getCallingMethod() + "\n" + e);
        }
    }

    public void save() {
        try {
            FileOutputStream xml_output_stream = new FileOutputStream(this.file);
            Serializer serializer = SerializerFactory.getSerializer(OutputProperties.getDefaultMethodProperties("xml"));
            ByteArrayOutputStream bao_stream = new ByteArrayOutputStream();
            serializer.setOutputStream(bao_stream);
            serializer.asDOMSerializer().serialize(this.document.getDocumentElement());
            bao_stream.writeTo(xml_output_stream);
            bao_stream.close();
            xml_output_stream.close();
        } catch (Exception e) {
            System.err.println("EXCEPTION:\n" + Debug.getCallingMethod() + "\n" + e);
        }
    }

    public DefinitionInterface getDefaultDefinition() {
        return null;
    }

    public void setKeyedExtent(Map parameters) {
        System.err.println("*****NOT IMPLEMENTED*****: " + Debug.getCallingMethod() + "\n");
    }

    public Element getKeyedExtentDOM() {
        System.err.println("*****NOT IMPLEMENTED*****: " + Debug.getCallingMethod() + "\n");
        return null;
    }

    public int getDefinitionSize() {
        System.err.println("*****NOT IMPLEMENTED*****: " + Debug.getCallingMethod() + "\n");
        return -1;
    }
}
