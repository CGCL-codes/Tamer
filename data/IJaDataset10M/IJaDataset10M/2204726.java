package org.tzi.use.gui.xmlparser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.tzi.use.gui.views.diagrams.DiamondNode;
import org.tzi.use.gui.views.diagrams.EdgeBase;
import org.tzi.use.gui.views.diagrams.LayoutInfos;
import org.tzi.use.gui.views.diagrams.NodeBase;
import org.tzi.use.gui.views.diagrams.NodeOnEdge;
import org.tzi.use.uml.mm.MAssociation;
import org.tzi.use.uml.mm.MClass;
import org.tzi.use.uml.mm.MGeneralization;
import org.tzi.use.uml.ocl.type.EnumType;
import org.tzi.use.uml.sys.MLink;
import org.tzi.use.uml.sys.MObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses the content of a layout xml file.
 * @version     $ProjectVersion: 0.393 $
 * @author      Fabian Gutsche 
 */
public class LayoutContentHandler extends ContentHandler {

    private String fTagContent;

    private boolean fHide;

    private Context fCtx;

    public LayoutContentHandler(LayoutInfos layoutInfos, boolean hide) {
        fCtx = new Context(layoutInfos);
        fHide = hide;
    }

    public void startElement(String nsURI, String localName, String qName, Attributes atts) throws SAXException {
        fTagContent = "";
        if (qName.equals(LayoutTags.NODE) || qName.equals(LayoutTags.EDGE) || qName.equals(LayoutTags.EDGEPROPERTY)) {
            String type = atts.getValue("type");
            fCtx.pushType(type);
            if (type != null && type.equals(LayoutTags.DIAMONDNODE)) {
                fCtx.getConnectedNodes().clear();
            }
            fCtx.setKind(atts.getValue("kind"));
        }
    }

    /**
     * This method is called to read the text within a xml-tag.
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        fTagContent += new String(ch, start, length);
    }

    /**
     * This method is called when the xml-tag ends. The value of tag is saved.
     */
    public void endElement(String nsURI, String ln, String qName) throws SAXException {
        if (qName.equals(LayoutTags.NODE) || (qName.equals(LayoutTags.EDGE) && fCtx.peekType() != null && !fCtx.peekType().equals(LayoutTags.DIAMONDNODE))) {
            fCtx.reset();
        }
        if (qName.equals(LayoutTags.NODE) || qName.equals(LayoutTags.EDGE) || qName.equals(LayoutTags.EDGEPROPERTY)) {
            fCtx.popType();
        }
        if (qName.equals(LayoutTags.ANTIALIASING)) {
            fCtx.getOpt().setDoAntiAliasing(Boolean.valueOf(fTagContent.trim()).booleanValue());
        }
        if (qName.equals(LayoutTags.AUTOLAYOUT)) {
            fCtx.getOpt().setDoAutoLayout(Boolean.valueOf(fTagContent.trim()).booleanValue());
        }
        if (qName.equals(LayoutTags.SHOWASSOCNAMES)) {
            fCtx.getOpt().setShowAssocNames(Boolean.valueOf(fTagContent.trim()).booleanValue());
        }
        if (qName.equals(LayoutTags.SHOWATTRIBUTES)) {
            fCtx.getOpt().setShowAttributes(Boolean.valueOf(fTagContent.trim()).booleanValue());
        }
        if (qName.equals(LayoutTags.SHOWMULTIPLICITIES)) {
            fCtx.getOpt().setShowMutliplicities(Boolean.valueOf(fTagContent.trim()).booleanValue());
        }
        if (qName.equals(LayoutTags.SHOWOPERATIONS)) {
            fCtx.getOpt().setShowOperations(Boolean.valueOf(fTagContent.trim()).booleanValue());
        }
        if (qName.equals(LayoutTags.SHOWROLENAMES)) {
            fCtx.getOpt().setShowRolenames(Boolean.valueOf(fTagContent.trim()).booleanValue());
        }
        if (fCtx.peekType() != null) {
            if (fCtx.peekType().equals(LayoutTags.CLASS)) {
                if (qName.equals(LayoutTags.NAME)) {
                    MClass cls = fCtx.getModel().getClass(fTagContent.trim());
                    fCtx.setActualNode((NodeBase) fCtx.getActualMap().get(cls));
                    fCtx.setActualObj(cls);
                } else {
                    parseNode(qName, fTagContent.trim());
                }
            }
            if (fCtx.peekType().equals(LayoutTags.OBJECT)) {
                if (qName.equals(LayoutTags.NAME)) {
                    MObject obj = fCtx.getSystemState().objectByName(fTagContent.trim());
                    fCtx.setActualNode((NodeBase) fCtx.getActualMap().get(obj));
                    fCtx.setActualObj(obj);
                } else {
                    parseNode(qName, fTagContent.trim());
                }
            }
            if (fCtx.peekType().equals(LayoutTags.ENUMERATION)) {
                if (qName.equals(LayoutTags.NAME)) {
                    EnumType enumeration = fCtx.getModel().enumType(fTagContent.trim());
                    fCtx.setActualNode((NodeBase) fCtx.getActualMap().get(enumeration));
                    fCtx.setActualObj(enumeration);
                } else {
                    parseNode(qName, fTagContent.trim());
                }
            }
            if (fCtx.peekType().equals(LayoutTags.DIAMONDNODE)) {
                parseDiamondNode(qName, fTagContent.trim());
            }
            if (fCtx.peekType().equals(LayoutTags.BINARYEDGE)) {
                parseEdge(qName, fTagContent.trim());
            }
            if (fCtx.peekType().equals(LayoutTags.HALFEDGE)) {
                parseHalfEdge(qName, fTagContent.trim());
            }
            if (fCtx.peekType().equals(LayoutTags.EDGENODE)) {
                if (fCtx.getActualNode() != null && fCtx.getActualNode() instanceof DiamondNode) {
                    parseTNaryEdgeNode(qName, fTagContent.trim());
                } else {
                    parseEdge(qName, fTagContent.trim());
                }
            }
            if (fCtx.peekType().equals(LayoutTags.INHERITANCE)) {
                parseInheritance(qName, fTagContent.trim());
            }
            if (fCtx.peekType().equals(LayoutTags.ROLENAME)) {
                if (fCtx.getKind().equals(LayoutTags.SOURCE) && fCtx.getActualEdge() != null) {
                    fCtx.setActualEdgeProperty(fCtx.getActualEdge().getSourceRolename());
                }
                if (fCtx.getKind().equals(LayoutTags.TARGET) && fCtx.getActualEdge() != null) {
                    fCtx.setActualEdgeProperty(fCtx.getActualEdge().getTargetRolename());
                }
                parseEdgeProperty(qName, fTagContent.trim());
            }
            if (fCtx.peekType().equals(LayoutTags.MULTIPLICITY)) {
                if (fCtx.getKind().equals(LayoutTags.SOURCE) && fCtx.getActualEdge() != null) {
                    fCtx.setActualEdgeProperty(fCtx.getActualEdge().getSourceMultiplicity());
                }
                if (fCtx.getKind().equals(LayoutTags.TARGET) && fCtx.getActualEdge() != null) {
                    fCtx.setActualEdgeProperty(fCtx.getActualEdge().getTargetMultiplicity());
                }
                parseEdgeProperty(qName, fTagContent.trim());
            }
            if (fCtx.peekType().equals(LayoutTags.ASSOCNAME)) {
                if (fCtx.getActualEdge() != null) {
                    fCtx.setActualEdgeProperty(fCtx.getActualEdge().getAssocName());
                    parseEdgeProperty(qName, fTagContent.trim());
                } else if (fCtx.getActualNode() != null && fCtx.getActualNode() instanceof DiamondNode) {
                    fCtx.setActualEdgeProperty(((DiamondNode) fCtx.getActualNode()).getAssocName());
                    parseEdgeProperty(qName, fTagContent.trim());
                }
            }
            if (fCtx.peekType().equals(LayoutTags.NODEONEDGE)) {
                if (qName.equals(LayoutTags.ID)) {
                    fCtx.setID(Integer.parseInt(fTagContent.trim()));
                }
                if (qName.equals(LayoutTags.SPECIALID)) {
                    fCtx.setSpecialID(Integer.parseInt(fTagContent.trim()));
                }
                if (fCtx.getID() != -1 && fCtx.getSpecialID() != -1) {
                    fCtx.setActualEdgeProperty(findNodeOnEdge());
                }
                if (fCtx.getActualEdgeProperty() != null) {
                    parseEdgeProperty(qName, fTagContent.trim());
                }
            }
        }
    }

    /**
     * Finds the NodeOnEdge which needs to be placed correctly. If 
     * the NodeOnEdge does not exists so far a new NodeOnEdge will 
     * be created.
     */
    private NodeOnEdge findNodeOnEdge() {
        if (fCtx.getActualEdge() == null) {
            return null;
        }
        NodeOnEdge n = null;
        Iterator it = fCtx.getActualEdge().getNodesOnEdge().iterator();
        while (it.hasNext()) {
            NodeOnEdge node = (NodeOnEdge) it.next();
            if (fCtx.getActualEdge().isNodeSpecial(node) && node.getSpecialID() == fCtx.getSpecialID()) {
                node.setID(fCtx.getID());
                fCtx.getActualEdge().sortNodesOnEdge();
                fCtx.setSpecialID(-1);
                fCtx.setID(-1);
                return node;
            }
        }
        String name = null;
        if (fCtx.getActualEdge().getAssocName() == null) {
            name = "Inheritance";
        } else {
            fCtx.getActualEdge().getAssocName().name();
        }
        n = new NodeOnEdge(0.0, 0.0, (NodeBase) fCtx.getSourceNode(), (NodeBase) fCtx.getTargetNode(), fCtx.getActualEdge(), fCtx.getID(), fCtx.getSpecialID(), name, fCtx.getOpt());
        fCtx.getActualEdge().getNodesOnEdge().add(n);
        fCtx.getActualEdge().sortNodesOnEdge();
        fCtx.setSpecialID(-1);
        fCtx.setID(-1);
        return n;
    }

    /**
     * Parses a NodeBase and sets its position correctly, if the NodeBase
     * is hidden, the node is added to the hidden nodes list.
     * @param tag The actual parsed xml tag.
     * @param content The content of the actual parsed xml tag.
     */
    private void parseNode(String tag, String content) {
        if (fCtx.getActualNode() == null) {
            return;
        }
        if (tag.equals(LayoutTags.X_COORD)) {
            fCtx.getActualNode().setX(Double.parseDouble(content));
        }
        if (tag.equals(LayoutTags.Y_COORD)) {
            fCtx.getActualNode().setY(Double.parseDouble(content));
        }
        if (tag.equals(LayoutTags.HIDDEN) && fHide) {
            if (Boolean.valueOf(fTagContent.trim()).booleanValue()) {
                fCtx.getHiddenNodes().add(fCtx.getActualObj());
                String xml = fCtx.getLayoutInfos().getHiddenElementsXML();
                xml += fCtx.getActualNode().storePlacementInfo(fHide) + LayoutTags.NL;
                fCtx.getLayoutInfos().setHiddenElementsXML(xml);
            }
        }
    }

    /**
     * Parses a DiamondNode and sets its position correctly, if the DiamondNode
     * is hidden, the node is added to the hidden nodes list. The EdgeProperty
     * AssocName is set as well.
     * @param tag The actual parsed xml tag.
     * @param content The content of the actual parsed xml tag.
     */
    private void parseDiamondNode(String tag, String content) {
        if (tag.equals(LayoutTags.CON_NODE)) {
            if (fCtx.getKind().equals(LayoutTags.LINK)) {
                fCtx.getConnectedNodes().add(fCtx.getSystemState().objectByName(content));
            }
            if (fCtx.getKind().equals(LayoutTags.ASSOCIATION)) {
                fCtx.getConnectedNodes().add(fCtx.getSystem().model().getClass(content));
            }
        }
        if (tag.equals(LayoutTags.NAME)) {
            if (fCtx.getKind().equals(LayoutTags.LINK)) {
                MAssociation assoc = fCtx.getSystem().model().getAssociation(content);
                MLink link = fCtx.getSystemState().linkBetweenObjects(assoc, fCtx.getConnectedNodes());
                fCtx.setActualNode((NodeBase) fCtx.getActualMap().get(link));
                fCtx.setActualObj(link);
            }
            if (fCtx.getKind().equals(LayoutTags.ASSOCIATION)) {
                MAssociation assoc = fCtx.getSystem().model().getAssociation(content);
                fCtx.setActualNode((NodeBase) fCtx.getActualMap().get(assoc));
                fCtx.setActualObj(assoc);
            }
        }
        if (tag.equals(LayoutTags.X_COORD)) {
            fCtx.getActualNode().setX(Double.parseDouble(content));
        }
        if (tag.equals(LayoutTags.Y_COORD)) {
            fCtx.getActualNode().setY(Double.parseDouble(content));
        }
        if (tag.equals(LayoutTags.HIDDEN) && fHide) {
            if (Boolean.valueOf(fTagContent.trim()).booleanValue()) {
                fCtx.getHiddenEdges().add(fCtx.getActualObj());
                String xml = fCtx.getLayoutInfos().getHiddenElementsXML();
                xml += fCtx.getActualNode().storePlacementInfo(fHide) + LayoutTags.NL;
                fCtx.getLayoutInfos().setHiddenElementsXML(xml);
            }
        }
    }

    private void parseEdge(String tag, String content) {
        if (tag.equals(LayoutTags.SOURCE)) {
            if (fCtx.getKind().equals(LayoutTags.LINK)) {
                fCtx.setSource(fCtx.getSystemState().objectByName(content));
            }
            if (fCtx.getKind().equals(LayoutTags.ASSOCIATION)) {
                fCtx.setSource(fCtx.getSystem().model().getClass(content));
            }
        }
        if (tag.equals(LayoutTags.TARGET)) {
            if (fCtx.getKind().equals(LayoutTags.LINK)) {
                fCtx.setTarget(fCtx.getSystemState().objectByName(content));
            }
            if (fCtx.getKind().equals(LayoutTags.ASSOCIATION)) {
                fCtx.setTarget(fCtx.getSystem().model().getClass(content));
            }
        }
        if (tag.equals(LayoutTags.NAME)) {
            if (fCtx.getKind().equals(LayoutTags.LINK)) {
                MAssociation assoc = fCtx.getSystem().model().getAssociation(content);
                Set objects = new HashSet();
                objects.add((MObject) fCtx.getSource());
                objects.add((MObject) fCtx.getTarget());
                MLink link = fCtx.getSystemState().linkBetweenObjects(assoc, objects);
                fCtx.setActualEdge((EdgeBase) fCtx.getActualMap().get(link));
                fCtx.setActualObj(link);
            }
            if (fCtx.getKind().equals(LayoutTags.ASSOCIATION)) {
                MAssociation assoc = fCtx.getSystem().model().getAssociation(content);
                fCtx.setActualEdge((EdgeBase) fCtx.getActualMap().get(assoc));
                fCtx.setActualObj(assoc);
            }
        }
        if (tag.equals(LayoutTags.HIDDEN) && fHide) {
            if (Boolean.valueOf(fTagContent.trim()).booleanValue()) {
                fCtx.getHiddenEdges().add(fCtx.getActualObj());
                String xml = fCtx.getLayoutInfos().getHiddenElementsXML();
                xml += fCtx.getActualEdge().storePlacementInfo(fHide) + LayoutTags.NL;
                fCtx.getLayoutInfos().setHiddenElementsXML(xml);
            }
        }
    }

    private void parseTNaryEdgeNode(String tag, String content) {
        if (tag.equals(LayoutTags.NAME)) {
            if (fCtx.getKind().equals(LayoutTags.LINK)) {
                MAssociation assoc = fCtx.getSystem().model().getAssociation(content);
                MLink link = fCtx.getSystemState().linkBetweenObjects(assoc, fCtx.getConnectedNodes());
                EdgeBase e = (EdgeBase) fCtx.getActualMap().get(link);
                fCtx.setActualEdge(e);
                fCtx.setActualObj(link);
            }
            if (fCtx.getKind().equals(LayoutTags.ASSOCIATION)) {
                MAssociation assoc = fCtx.getSystem().model().getAssociation(content);
                EdgeBase e = (EdgeBase) fCtx.getActualMap().get(assoc);
                fCtx.setActualEdge(e);
                fCtx.setActualObj(assoc);
            }
        }
        if (tag.equals(LayoutTags.HIDDEN) && fHide) {
            if (Boolean.valueOf(fTagContent.trim()).booleanValue()) {
                fCtx.getHiddenEdges().add(fCtx.getActualObj());
                String xml = fCtx.getLayoutInfos().getHiddenElementsXML();
                xml += fCtx.getActualEdge().storePlacementInfo(fHide) + LayoutTags.NL;
                fCtx.getLayoutInfos().setHiddenElementsXML(xml);
            }
        }
    }

    private void parseHalfEdge(String tag, String content) {
        if (tag.equals(LayoutTags.SOURCE)) {
            if (fCtx.getKind().equals(LayoutTags.LINK)) {
                MAssociation assoc = fCtx.getSystem().model().getAssociation(content);
                MLink link = fCtx.getSystemState().linkBetweenObjects(assoc, fCtx.getConnectedNodes());
                Map diamondMap = (Map) fCtx.getAllMappings().get(LayoutTags.DIAMONDNODE);
                fCtx.setSource(diamondMap.get(link));
                fCtx.setActualObj(link);
            }
            if (fCtx.getKind().equals(LayoutTags.ASSOCIATION)) {
                MAssociation assoc = fCtx.getSystem().model().getAssociation(content);
                Map diamondMap = (Map) fCtx.getAllMappings().get(LayoutTags.DIAMONDNODE);
                fCtx.setSource(diamondMap.get(assoc));
            }
        }
        if (tag.equals(LayoutTags.TARGET)) {
            if (fCtx.getKind().equals(LayoutTags.LINK)) {
                fCtx.setTarget(fCtx.getSystemState().objectByName(content));
            }
            if (fCtx.getKind().equals(LayoutTags.ASSOCIATION)) {
                fCtx.setTarget(fCtx.getSystem().model().getClass(content));
            }
        }
        if (tag.equals(LayoutTags.NAME)) {
            if (fCtx.getKind().equals(LayoutTags.LINK)) {
                MLink link = (MLink) fCtx.getActualObj();
                List halfEdges = (ArrayList) fCtx.getActualMap().get(link);
                Iterator it = halfEdges.iterator();
                while (it.hasNext()) {
                    EdgeBase e = (EdgeBase) it.next();
                    if (((NodeBase) e.source()).name().equals(((NodeBase) fCtx.getSource()).name()) && ((NodeBase) e.target()).name().equals(((MObject) fCtx.getTarget()).name())) {
                        fCtx.setActualEdge(e);
                    }
                }
            }
            if (fCtx.getKind().equals(LayoutTags.ASSOCIATION)) {
                MAssociation assoc = fCtx.getSystem().model().getAssociation(content);
                List halfEdges = (ArrayList) fCtx.getActualMap().get(assoc);
                Iterator it = halfEdges.iterator();
                while (it.hasNext()) {
                    EdgeBase e = (EdgeBase) it.next();
                    if (((NodeBase) e.source()).name().equals(((NodeBase) fCtx.getSource()).name()) && ((NodeBase) e.target()).name().equals(((MClass) fCtx.getTarget()).name())) {
                        fCtx.setActualEdge(e);
                    }
                }
            }
        }
        if (tag.equals(LayoutTags.HIDDEN) && fHide) {
            if (Boolean.valueOf(fTagContent.trim()).booleanValue()) {
                fCtx.getHiddenEdges().add(fCtx.getActualObj());
                String xml = fCtx.getLayoutInfos().getHiddenElementsXML();
                xml += fCtx.getActualEdge().storePlacementInfo(fHide) + LayoutTags.NL;
                fCtx.getLayoutInfos().setHiddenElementsXML(xml);
            }
        }
    }

    private void parseInheritance(String tag, String content) {
        if (tag.equals(LayoutTags.SOURCE)) {
            fCtx.setSource(fCtx.getSystem().model().getClass(content));
        }
        if (tag.equals(LayoutTags.TARGET)) {
            fCtx.setTarget(fCtx.getSystem().model().getClass(content));
        }
        if (fCtx.getSource() != null && fCtx.getTarget() != null) {
            Set edges = fCtx.getSystem().model().generalizationGraph().edgesBetween(fCtx.getSource(), fCtx.getTarget());
            Iterator it = edges.iterator();
            while (it.hasNext()) {
                MGeneralization gen = (MGeneralization) it.next();
                fCtx.setActualEdge((EdgeBase) fCtx.getActualMap().get(gen));
                fCtx.setActualObj(gen);
            }
            if (tag.equals(LayoutTags.HIDDEN) && fHide && fCtx.getActualEdge() != null) {
                if (Boolean.valueOf(fTagContent.trim()).booleanValue()) {
                    fCtx.getHiddenEdges().add(fCtx.getActualObj());
                    String xml = fCtx.getLayoutInfos().getHiddenElementsXML();
                    xml += fCtx.getActualEdge().storePlacementInfo(fHide) + LayoutTags.NL;
                    fCtx.getLayoutInfos().setHiddenElementsXML(xml);
                }
            }
        }
    }

    private void parseEdgeProperty(String tag, String content) {
        if (fCtx.getActualEdgeProperty() == null) {
            return;
        }
        fCtx.getActualEdgeProperty().setLoadingLayout(true);
        if (tag.equals(LayoutTags.X_COORD)) {
            double x = Double.parseDouble(content);
            if (x >= 0) {
                fCtx.getActualEdgeProperty().setX(x);
                fCtx.getActualEdgeProperty().setX_UserDefined(x);
            }
        }
        if (tag.equals(LayoutTags.Y_COORD)) {
            double y = Double.parseDouble(content);
            if (y >= 0) {
                fCtx.getActualEdgeProperty().setY(y);
                fCtx.getActualEdgeProperty().setY_UserDefined(y);
            }
        }
        fCtx.getActualEdgeProperty().setLoadingLayout(false);
    }
}
