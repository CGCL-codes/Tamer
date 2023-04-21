package util.templateGen;

import util.Xml;
import java.util.Vector;

public class XmlGraphs {

    public Vector xmlGraphVec;

    public XmlGraphs() {
        xmlGraphVec = new Vector();
    }

    public void addGraph(int numOfTasks, String connectionScheme) {
        xmlGraphVec.add(new XmlGraph(xmlGraphVec.size(), numOfTasks, connectionScheme));
    }

    public void writeToXml(String filename) {
        Xml.openPrintStream(filename);
        Xml.opnBlk("graphs");
        for (int i = 0; i < xmlGraphVec.size(); i++) {
            ((XmlGraph) xmlGraphVec.elementAt(i)).writeToXml();
        }
        Xml.clsBlk();
        Xml.closePrintStream();
    }
}
