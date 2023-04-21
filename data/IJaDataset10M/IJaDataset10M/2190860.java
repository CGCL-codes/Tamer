package net.sf.layoutdesigner.file;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import net.sf.layoutdesigner.scenery.Branch;
import net.sf.layoutdesigner.scenery.Junction;
import net.sf.layoutdesigner.scenery.RailCar;
import net.sf.layoutdesigner.scenery.SceneryManager;
import net.sf.layoutdesigner.scenery.Train;
import net.sf.layoutdesigner.util.Geometry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Writes objects in terms of point locations.  SceneryManager can regenerate
 * objects using points.  Points are usually clones, not references, so storage 
 * is simplified.
 * 
 * @author Jeff_Eltgroth
 *
 */
public class XMLWriter {

    private Document doc;

    private Element branches;

    private Element trains;

    private Element junctions;

    public static void main(String[] args) throws Exception {
        XMLWriter writer = new XMLWriter();
        SceneryManager.getInstance().createTestScenery();
        File file = new File(System.getProperty("user.home") + "\\Documents\\test_layout.xml");
        writer.writeFile(file);
    }

    public void writeFile(File file) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.newDocument();
        Element root = doc.createElement("layout");
        doc.appendChild(root);
        root.setAttribute("width", String.valueOf(SceneryManager.getInstance().getWidth()));
        root.setAttribute("height", String.valueOf(SceneryManager.getInstance().getHeight()));
        branches = doc.createElement("branches");
        root.appendChild(branches);
        for (Branch branch : SceneryManager.getInstance().getBranches()) {
            writeBranch(branch);
        }
        trains = doc.createElement("trains");
        root.appendChild(trains);
        for (Train train : SceneryManager.getInstance().getTrains()) {
            writeTrain(train);
        }
        junctions = doc.createElement("junctions");
        root.appendChild(junctions);
        for (Junction junction : SceneryManager.getInstance().getJunctions()) {
            writeJunction(junction);
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    public void writeBranch(Branch branch) {
        Element branchElement = doc.createElement("branch");
        branches.appendChild(branchElement);
        for (Shape shape : branch.getShapes()) {
            Point2D[] points = Geometry.getPointsFromShape(shape);
            if (shape instanceof Line2D) {
                Element line = doc.createElement("straight");
                line.setAttribute("x1", String.valueOf(points[0].getX()));
                line.setAttribute("y1", String.valueOf(points[0].getY()));
                line.setAttribute("x2", String.valueOf(points[1].getX()));
                line.setAttribute("y2", String.valueOf(points[1].getY()));
                branchElement.appendChild(line);
            } else if (shape instanceof Arc2D) {
                Element curve = doc.createElement("curve");
                curve.setAttribute("x1", String.valueOf(points[0].getX()));
                curve.setAttribute("y1", String.valueOf(points[0].getY()));
                curve.setAttribute("x2", String.valueOf(points[1].getX()));
                curve.setAttribute("y2", String.valueOf(points[1].getY()));
                curve.setAttribute("xc", String.valueOf(points[2].getX()));
                curve.setAttribute("yc", String.valueOf(points[2].getY()));
                branchElement.appendChild(curve);
            }
        }
    }

    public void writeTrain(Train train) {
        Element trainElement = doc.createElement("train");
        trains.appendChild(trainElement);
        for (RailCar car : train.getCars()) {
            Element carElement = doc.createElement("car");
            carElement.setAttribute("locX", String.valueOf(car.getLoc().getX()));
            carElement.setAttribute("locY", String.valueOf(car.getLoc().getY()));
            carElement.setAttribute("toX", String.valueOf(car.getPointTowards().getX()));
            carElement.setAttribute("toY", String.valueOf(car.getPointTowards().getY()));
            trainElement.appendChild(carElement);
        }
    }

    public void writeJunction(Junction junction) {
        Element junctionElement = doc.createElement("junction");
        junctions.appendChild(junctionElement);
        junctionElement.setAttribute("locX", String.valueOf(junction.getLocation().getX()));
        junctionElement.setAttribute("locY", String.valueOf(junction.getLocation().getY()));
    }
}
