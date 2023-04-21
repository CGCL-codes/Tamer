package datasoul.serviceitems.song;

import datasoul.util.SerializableItf;
import datasoul.util.ZipReader;
import datasoul.util.ZipWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Administrador
 */
public class Chord implements SerializableItf {

    private ArrayList<String> shapes;

    private int defaultShape;

    private String name;

    /** Creates a new instance of Chord */
    public Chord(String chordName) {
        shapes = new ArrayList<String>();
        setDefaultShape(0);
        this.setName(chordName);
    }

    public Chord() {
        shapes = new ArrayList<String>();
        setDefaultShape(0);
    }

    public void addShape(String shape) {
        shapes.add(shape);
    }

    public void removeShape(String shape) {
        shapes.remove(shape);
    }

    public void removeAllShapes() {
        shapes = new ArrayList<String>();
    }

    public void changeShape(String oldShape, String newShape) {
        shapes.set(shapes.indexOf(oldShape), newShape);
    }

    public int getDefaultShape() {
        return this.defaultShape;
    }

    public void setDefaultShape(int defaultShape) {
        this.defaultShape = defaultShape;
    }

    public String getShape() {
        if (shapes.size() > defaultShape) {
            return shapes.get(defaultShape);
        } else {
            return "";
        }
    }

    public ArrayList<String> getShapes() {
        return this.shapes;
    }

    @Override
    public Node writeObject(ZipWriter zip) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        Node nodeOut = doc.createElement("Chord");
        Node node;
        String paramValue;
        node = doc.createElement("name");
        node.setTextContent(getName());
        nodeOut.appendChild(node);
        for (int i = 0; i < shapes.size(); i++) {
            paramValue = shapes.get(i);
            node = doc.createElement("shape");
            node.setTextContent(paramValue);
            nodeOut.appendChild(node);
        }
        return nodeOut;
    }

    public final String toString() {
        return name;
    }

    @Override
    public void readObject(Node nodeIn, ZipReader zip) {
        NodeList nodeList = nodeIn.getChildNodes();
        Node node;
        String paramName;
        String paramValue;
        for (int i = 0; i < nodeList.getLength(); i++) {
            node = nodeList.item(i);
            if (nodeList.item(i).getNodeType() == 1) {
                if (node.getNodeName().equals("name")) {
                    paramValue = nodeList.item(i).getTextContent();
                    this.setName(paramValue);
                } else if (node.getNodeName().equals("shape")) {
                    paramValue = nodeList.item(i).getTextContent();
                    this.shapes.add(paramValue);
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
