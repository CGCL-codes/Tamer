package org.sbfc.converter.svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Here we edit simple svg file (from the conversion from .dot) and render them
 * 
 * doc for svg: http://www.w3schools.com/svg/svg_examples.asp
 * 
 * @author jalowcki
 * 
 */
public class SvgRenderer {

    /**
	 * Read the attributes of a given {@link Node} and return a {@link HashMap}.
	 * Return null if the given node has no attributes.
	 * Gives a warning if it will return null for given {@link Node}.
	 * Example: TODO
	 * 
	 * @param tree
	 * @return {@link HashMap} or null
	 */
    private static HashMap<String, String> accessAttributesOfNodes(Node node) {
        System.out.println("in access function");
        if (!node.hasAttributes()) {
            System.out.println("warning node " + node.getLocalName() + " has no attributes.");
            return null;
        }
        Integer numberOfAttr = node.getAttributes().getLength();
        HashMap<String, String> attributesMap = new HashMap<String, String>(numberOfAttr);
        for (int i = 0; i < numberOfAttr; i++) {
            String attr = node.getAttributes().item(i).toString();
            System.out.println("attribut of node = " + attr);
            String[] couple = attr.split("=");
            attributesMap.put(couple[0], couple[1]);
        }
        return attributesMap;
    }

    /**
	 * Recursive function which depth-first search svg grouping {@link Node}s, render them and modify the dom file.
	 * @param {@link Node}
	 * @param {@link Document}
	 */
    private static void renderGroupingNodes(Node parentNode, Document dom) {
        System.out.println("in renderGroupingNodes");
        HashMap<String, String> nodeAttributes = accessAttributesOfNodes(parentNode);
        for (Entry<String, String> entry : nodeAttributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("key = " + key + " value = " + value);
        }
        if (parentNode.hasChildNodes()) {
            NodeList childrenList = parentNode.getChildNodes();
            Integer numberOfChildren = childrenList.getLength();
            System.out.println("numberOfChildren = " + numberOfChildren);
            for (int index = 0; index < numberOfChildren; index++) {
                Node child = childrenList.item(index);
                if (child == null) {
                    continue;
                }
                if (child.getNodeName() == "g") {
                    System.out.println("launching again");
                    renderGroupingNodes(child, dom);
                }
                System.out.println("    child is a = " + child.getNodeName() + " parent " + index);
                if (child.getNodeName().equals("image")) {
                    HashMap<String, String> imageAttr = accessAttributesOfNodes(child);
                    for (Entry<String, String> entry : imageAttr.entrySet()) {
                        System.out.println("		cle = " + entry.getKey() + " ### valeur = " + entry.getValue());
                        if (entry.getKey().equals("xlink:href")) {
                            String pathImage = entry.getValue();
                            String imageName = pathImage.replaceAll("^.*/", "");
                            System.out.println("in the perturbation road...1 = " + imageName);
                            if (imageName.matches("^perturbation.*")) {
                                System.out.println("in the perturbation road...2");
                                Element newChild = dom.createElement("polygon");
                                parentNode.replaceChild(newChild, child);
                                newChild.setAttribute("points", "100,100 125,125 100,150 200,150 175,125 200,100 100,100");
                            } else if (imageName.matches("^geneticEntity.*")) {
                                Element roundedRect = dom.createElement("rect");
                                Element rect1 = dom.createElement("rect");
                                Element rect2 = dom.createElement("rect");
                                parentNode.replaceChild(rect2, child);
                                parentNode.insertBefore(rect1, rect2);
                                parentNode.insertBefore(roundedRect, rect1);
                                setAttributesRoundedRect(roundedRect, "", "", "", "", "", "");
                                setAttributesRect(rect1, "", "", "", "");
                                setAttributesRect(rect2, "", "", "", "");
                                Element linkLine = dom.createElement("line");
                                Element upLine = dom.createElement("line");
                                Element rightLine = dom.createElement("line");
                                parentNode.appendChild(linkLine);
                                parentNode.appendChild(upLine);
                                parentNode.appendChild(rightLine);
                                setAttributesLine(linkLine, "", "", "", "");
                                setAttributesLine(upLine, "", "", "", "");
                                setAttributesLine(rightLine, "", "", "", "");
                            } else if (imageName.equals("multimerNAF.png")) {
                                System.out.println("in the perturbation road...3");
                            }
                        }
                    }
                }
                if (child.getNodeName().equals("text") && child.getTextContent().equals("Ø")) {
                    Element circle = dom.createElement("circle");
                    parentNode.replaceChild(circle, child);
                    setAttributesCircle(circle, "", "", "");
                    Element line = dom.createElement("line");
                    parentNode.appendChild(line);
                    setAttributesLine(line, "", "", "", "");
                }
                if (child.getNodeName().equals("complex")) {
                    Element octa = dom.createElement("polygon");
                    parentNode.appendChild(octa);
                    octa.setAttribute("points", "");
                }
                if (child.getNodeName().equals("multimer")) {
                }
            }
        }
    }

    /**
	 * Render the tree {@link Element} by modifying its attributes.
	 * Modify the given {@link Document} dom.
	 * 
	 */
    private static void renderTree(Element tree, Document dom) {
        System.out.println("in render tree");
        if (tree.getNodeName() != "svg") {
            return;
        }
        HashMap<String, String> treeAttributes = accessAttributesOfNodes(tree);
        if (treeAttributes == null) {
            return;
        }
        for (Entry<String, String> entry : treeAttributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("cle = " + key + " value = " + value);
            if (key.equals("height")) {
                System.out.println("confirmation passage in LOOP IF");
                tree.setAttribute("height", "1111cm");
            }
        }
    }

    /**
	 * Set circle attributes. Do not make any layout calculations.
	 * @param circle
	 * @param {@link String} cx, {@link String} cy, {@link String} r
	 */
    private static void setAttributesCircle(Element circle, String cx, String cy, String r) {
        circle.setAttribute("cx", cx);
        circle.setAttribute("cy", cy);
        circle.setAttribute("r", r);
        return;
    }

    /**
	 * Set the attributes of a SVG line. Do not make any layout calculations.
	 * @param Attributes: {@link String} x1, {@link String} y1, {@link String} x2, {@link String} y2
	 * @param line
	 */
    private static void setAttributesLine(Element line, String x1, String y1, String x2, String y2) {
        line.setAttribute("x1", x1);
        line.setAttribute("y1", y1);
        line.setAttribute("x2", x2);
        line.setAttribute("y2", y2);
        return;
    }

    /**
	 * Set the attributes of a rectangle. Do not make any layout calculations.
	 * @param rect
	 * @param {@link String} x, {@link String} y, {@link String} width, {@link String} height, {@link String} rx, {@link String} ry
	 */
    private static void setAttributesRect(Element rect, String x, String y, String width, String height) {
        rect.setAttribute("x", x);
        rect.setAttribute("y", y);
        rect.setAttribute("width", width);
        rect.setAttribute("height", height);
        return;
    }

    /**
	 * Set the attributes of a rounded rectangle. Do not make any layout calculations.
	 * @param rect
	 * @param {@link String} x, {@link String} y, {@link String} width, {@link String} height, {@link String} rx, {@link String} ry
	 */
    private static void setAttributesRoundedRect(Element rect, String x, String y, String width, String height, String rx, String ry) {
        rect.setAttribute("x", x);
        rect.setAttribute("y", y);
        rect.setAttribute("width", width);
        rect.setAttribute("height", height);
        rect.setAttribute("rx", rx);
        rect.setAttribute("ry", ry);
        return;
    }

    /**
	 *
	 * Convert a DOM document into xml file
	 * @param doc
	 * @param filename
	 */
    public static void writeXmlFile(Document doc, String filename) {
        System.out.println("writing...");
        try {
            Source source = new DOMSource(doc);
            File file = new File(filename);
            Result result = new StreamResult(file);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            System.out.println("dom doc...");
            xformer.transform(source, result);
            System.out.println("... done");
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        String inputFileName = "/automount/nas17b_vol-vol_homes-homes/jalowcki/workspace/sbfc/sbfc/trunk/src/org/sbfc/converter/svg/example_out.svg";
        String outputFileName = "/automount/nas17b_vol-vol_homes-homes/jalowcki/workspace/sbfc/sbfc/trunk/src/org/sbfc/converter/svg/example_out2.svg";
        System.out.println("outputfilename = " + outputFileName);
        if (inputFileName == null) {
            System.out.println("In main: inputFileName == null");
            System.exit(1);
        }
        System.out.println("inputFileName = " + inputFileName);
        Document dom;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            System.out.println("Namespace aware : " + builder.isNamespaceAware());
            System.out.println("Validating : " + builder.isValidating());
            dom = builder.parse(new FileInputStream(inputFileName));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        Element tree = dom.getDocumentElement();
        System.out.println("ROOT? = " + tree.getNodeName());
        renderTree(tree, dom);
        if (tree.hasChildNodes()) {
            NodeList nl = tree.getChildNodes();
            System.out.println("length NodeList of tree = " + nl.getLength());
            for (int index = 0; index < nl.getLength(); index++) {
                Node node = nl.item(index);
                System.out.println(node.getNodeName());
                if (node.getNodeName() == "g") {
                    renderGroupingNodes(node, dom);
                }
            }
        } else {
            System.out.println("Warning: root element do not have any child!");
        }
        writeXmlFile(dom, outputFileName);
    }
}
