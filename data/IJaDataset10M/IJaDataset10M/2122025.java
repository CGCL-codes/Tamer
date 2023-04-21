package uk.ac.rothamsted.ovtk.Filter.combination.BackendXML;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JTree;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.rothamsted.ovtk.Filter.combination.CombinationGUI;
import uk.ac.rothamsted.ovtk.Graph.VisualONDEXGraph;
import uk.ac.rothamsted.ovtk.Util.CustomFileFilter;
import backend.core.AbstractConcept;
import backend.core.AttributeName;
import backend.core.ConceptAccession;
import backend.core.ConceptGDS;
import backend.core.ConceptName;
import backend.core.security.Session;

public class OndexInfoPage {

    private JInternalFrame frame;

    private JTree tree;

    private VisualONDEXGraph ondexGraph;

    private Session s;

    /**
	 * This class extracts information from the ondex graph and generates/saves
	 * html code
	 * 
	 * @param frame -
	 *            parent internal frame
	 * @param tree -
	 *            jtree containing matches
	 * @param ondexGraph -
	 *            actual ondex graph
	 */
    public OndexInfoPage(Session s, JInternalFrame frame, JTree tree, VisualONDEXGraph ondexGraph) {
        this.s = s;
        this.frame = frame;
        this.tree = tree;
        this.ondexGraph = ondexGraph;
    }

    /**
	 * generates html code containing infos for a node extracted from the actual
	 * ondexgraph
	 * 
	 * @param mNode -
	 *            dom node to generate info for
	 * @return info in html code
	 * 
	 */
    public String generateInfo(Node mNode) {
        StringBuffer html = new StringBuffer();
        html.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n");
        Node tNode = mNode.getParentNode();
        Node qNode = tNode.getParentNode();
        String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
        String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
        int mLength = Integer.parseInt(mNode.getAttributes().getNamedItem("length").getNodeValue());
        int tLength = Integer.parseInt(tNode.getAttributes().getNamedItem("length").getNodeValue());
        int qLength = Integer.parseInt(qNode.getAttributes().getNamedItem("length").getNodeValue());
        double score = Double.parseDouble(mNode.getAttributes().getNamedItem("score").getNodeValue());
        html.append("<html><head><title>" + qName + "</title></head><body>\n");
        html.append("<h2>Infopage for query <i>" + qName + "</i></h2>\n");
        html.append("<h3>Match found for target <i>" + tONDEXid + "</i></h3>\n");
        html.append("Length of query sequence: " + qLength + "<br>\n");
        html.append("Length of target sequence: " + tLength + "<br>\n");
        html.append("Length of matching region: " + mLength + "<br>\n");
        html.append("Score of matching region: " + score + "<br>\n");
        html.append("<h3>Information on target <i>" + tONDEXid + "</i></h3>\n");
        CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
        if (!render.getConceptIndex().containsKey(tONDEXid.toLowerCase())) {
            html.append("nothing found in actual ONDEX graph\n");
        } else {
            int intId = render.getConceptIndex().getInt(tONDEXid.toLowerCase());
            AbstractConcept c = ondexGraph.getConcept(intId);
            if (c.getElementOf(s) != null) html.append("<b>Element of:</b> " + c.getElementOf(s).getId(s) + " (" + c.getElementOf(s).getId(s) + ")<br>\n");
            if (ondexGraph.getTaxId(c) != null) html.append("<b>Taxonomy ID:</b> " + ondexGraph.getTaxId(c) + "<br>\n");
            if (c.getOfType(s) != null) html.append("<b>Concept Class:</b> " + c.getOfType(s).getId(s) + " (" + c.getOfType(s).getId(s) + ")<br>\n");
            AttributeName attr = ondexGraph.getAbstractONDEXGraph().getONDEXGraphData(s).getAttributeName(s, "URL");
            if (attr != null) {
                if (c.getConceptGDS(s, attr).getValue(s) != null && c.getConceptGDS(s, attr).getValue(s).toString().length() > 0) {
                    html.append("<b>URL:</b> <a href=\"" + c.getConceptGDS(s, attr).getValue(s).toString() + "\">" + c.getConceptGDS(s, attr).getValue(s).toString() + "</a><br>\n");
                }
            }
            if (c.getDescription(s) != null && c.getDescription(s).length() > 0) html.append("<b>Description:</b> " + c.getDescription(s) + "<br>\n");
            Iterator<ConceptName> itcn = c.getConceptNames(s);
            if (itcn.hasNext()) {
                html.append("<b>Concept Names:</b><br>\n");
                html.append("<table>\n");
                while (itcn.hasNext()) {
                    ConceptName cn = itcn.next();
                    html.append("<tr><td><i>" + c.getElementOf(s).getId(s) + "</i></td>");
                    html.append("<td>" + cn.getName(s) + "</td></tr>\n");
                }
                html.append("</table>\n");
            }
            Iterator<ConceptAccession> itca = c.getConceptAccessions(s);
            if (itca.hasNext()) {
                html.append("<b>Concept Accs:</b><br>\n");
                html.append("<table>\n");
                while (itca.hasNext()) {
                    ConceptAccession ca = itca.next();
                    html.append("<tr><td><i>" + ca.getElementOf(s).getId(s) + "</i></td>");
                    html.append("<td>" + ca.getAccession(s) + "</td></tr>\n");
                }
                html.append("</table>\n");
            }
            AttributeName attr1 = ondexGraph.getAbstractONDEXGraph().getONDEXGraphData(s).getAttributeName(s, "AA");
            if (attr1 != null) {
                if (c.getConceptGDS(s, attr1).getValue(s) != null && c.getConceptGDS(s, attr1).getValue(s).toString().length() > 0) {
                    html.append("<b>Sequences:</b><br>\n");
                    html.append("<table>\n");
                    Iterator<ConceptGDS> itcgds = c.getConceptGDSs(s);
                    while (itcgds.hasNext()) {
                        ConceptGDS cgds = itcgds.next();
                        if (cgds.getAttrname(s).getId(s).equals("AA") || cgds.getAttrname(s).getId(s).equals("NA")) {
                            html.append("<tr><td><i>" + cgds.getAttrname(s).getDescription(s) + "</i></td>");
                            html.append("<td>" + cgds.getValue(s) + "</td></tr>\n");
                        }
                    }
                    html.append("</table>\n");
                }
            }
            if (c.getConceptGDSs(s) != null) {
                html.append("<b>GDS:</b><br>\n");
                html.append("<table>\n");
                Iterator<ConceptGDS> itcgds = c.getConceptGDSs(s);
                while (itcgds.hasNext()) {
                    ConceptGDS gds = (ConceptGDS) itcgds.next();
                    html.append("<tr><td><i>" + gds.getAttrname(s).getId(s) + "</i></td>");
                    html.append("<td>" + gds.getValue(s) + "</td></tr>\n");
                }
                html.append("</table>\n");
            }
        }
        html.append("</body></html>");
        return html.toString();
    }

    /**
	 * saves the generated html code according to the specifications
	 * 
	 * @param process -
	 *            ACTUAL,SELECTED,ALL
	 */
    public void saveInfo(int process) {
        if (process == CombinationGUI.ACTUAL) {
            Object o = tree.getLastSelectedPathComponent();
            if (o != null) {
                AdapterNode adpNode = (AdapterNode) o;
                if (adpNode.domNode.getNodeName().equals("match")) {
                    Node tNode = adpNode.domNode.getParentNode();
                    Node qNode = tNode.getParentNode();
                    String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                    qName = qName.substring(0, qName.indexOf(" "));
                    String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                    qName = qName.replaceAll("\\W", "");
                    tONDEXid = tONDEXid.replaceAll("\\W", "");
                    String filename = qName + "-" + tONDEXid + ".html";
                    JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
                    fc.setSelectedFile(new File(filename));
                    CustomFileFilter filter = new CustomFileFilter("html", "HTML Files");
                    fc.setFileFilter(filter);
                    int returnVal = fc.showSaveDialog(frame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                            writer.write(generateInfo(adpNode.domNode));
                            writer.flush();
                            writer.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                }
            }
        } else if (process == CombinationGUI.SELECTED) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            CustomFileFilter filter = new CustomFileFilter("html", "HTML Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showSaveDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
                Iterator it = render.getCheckBoxes().keySet().iterator();
                while (it.hasNext()) {
                    AdapterNode adpNode = (AdapterNode) it.next();
                    JCheckBox check = (JCheckBox) render.getCheckBoxes().get(adpNode);
                    if (check.isSelected()) {
                        Node tNode = adpNode.domNode.getParentNode();
                        Node qNode = tNode.getParentNode();
                        String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                        qName = qName.substring(0, qName.indexOf(" "));
                        String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                        qName = qName.replaceAll("\\W", "");
                        tONDEXid = tONDEXid.replaceAll("\\W", "");
                        String filename = file.getPath() + System.getProperty("file.separator") + qName + "-" + tONDEXid + ".html";
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                            writer.write(generateInfo(adpNode.domNode));
                            writer.flush();
                            writer.close();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                    }
                }
            }
        } else if (process == CombinationGUI.ALL) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            CustomFileFilter filter = new CustomFileFilter("html", "HTML Files");
            fc.setFileFilter(filter);
            int returnVal = fc.showSaveDialog(frame);
            CustomTreeCellRenderer render = (CustomTreeCellRenderer) tree.getCellRenderer();
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                DomToTreeModelAdapter adp = (DomToTreeModelAdapter) tree.getModel();
                Element root = adp.document.getDocumentElement();
                NodeList nl = root.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    Node qNode = nl.item(i);
                    NodeList nl2 = qNode.getChildNodes();
                    for (int j = 0; j < nl2.getLength(); j++) {
                        Node tNode = nl2.item(j);
                        if (tNode.hasAttributes()) {
                            Node mNode = tNode.getFirstChild();
                            String tONDEXid = tNode.getAttributes().getNamedItem("ondexid").getNodeValue();
                            if (render.getConceptIndex().containsKey(tONDEXid.toLowerCase())) {
                                String qName = qNode.getAttributes().getNamedItem("name").getNodeValue();
                                qName = qName.substring(0, qName.indexOf(" "));
                                qName = qName.replaceAll("\\W", "");
                                tONDEXid = tONDEXid.replaceAll("\\W", "");
                                String filename = file.getPath() + System.getProperty("file.separator") + qName + "-" + tONDEXid + ".html";
                                try {
                                    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                                    writer.write(generateInfo(mNode));
                                    writer.flush();
                                    writer.close();
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
