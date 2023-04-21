package geocosm.gpx;

import java.io.ByteArrayOutputStream;
import org.w3c.dom.*;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;

/**
 * Automatic generated Classrepresentation of Wpt
 * 
 * <p>Copyright (c) 2002  Oliver Schünemann. All Rights Reserved.
 * 
 * @since 02/04/02 19:28
 * @version 1.0
 * @author Oliver Schünemann
 */
public class XML_Wpt {

    protected String Lat = null;

    protected String Lon = null;

    protected Node myNode = null;

    private XML_Ele Ele = null;

    private XML_Time Time = null;

    private XML_Name Name = null;

    private XML_Desc Desc = null;

    private XML_Sym Sym = null;

    private XML_Type Type = null;

    private XML_Cmt Cmt = null;

    String pcData = null;

    String NodeNames[] = new String[8];

    /**
   * Constructor constructing the object with an empty Dom-Node
   *
   */
    public XML_Wpt() {
        initNodeNames();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            myNode = document.createElement("wpt");
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Constructor constructing the object with the given Dom-Node
   * @param node Node representing the object
   * @throws SAXParseException
   *
   */
    public XML_Wpt(Node node) throws SAXParseException {
        initNodeNames();
        myNode = node;
        parse();
    }

    /** 
   *  Help-Method for initializing the nodeNames field
   * 
   */
    private void initNodeNames() {
        NodeNames[0] = "ele";
        NodeNames[1] = "time";
        NodeNames[2] = "name";
        NodeNames[3] = "desc";
        NodeNames[4] = "sym";
        NodeNames[5] = "type";
        NodeNames[6] = "cmt";
        NodeNames[7] = "#PCDATA";
    }

    /**
   *   sets a new value to the property Lat
   *   @param     newLat
   */
    public void setLat(String newLat) {
        Lat = newLat;
        NamedNodeMap map = myNode.getAttributes();
        if (map != null) {
            Node att = map.getNamedItem("lat");
            if ((newLat != null) && (newLat.length() > 0)) {
                if (att == null) {
                    att = myNode.getOwnerDocument().createAttribute("lat");
                    map.setNamedItem(att);
                }
                att.setNodeValue(newLat);
            } else if (att != null) {
                map.removeNamedItem("lat");
            }
        }
    }

    /**
   *   returns the current value of the property Lat
   *   @return String
   */
    public String getLat() {
        return Lat;
    }

    /**
   *   sets a new value to the property Lon
   *   @param     newLon
   */
    public void setLon(String newLon) {
        Lon = newLon;
        NamedNodeMap map = myNode.getAttributes();
        if (map != null) {
            Node att = map.getNamedItem("lon");
            if ((newLon != null) && (newLon.length() > 0)) {
                if (att == null) {
                    att = myNode.getOwnerDocument().createAttribute("lon");
                    map.setNamedItem(att);
                }
                att.setNodeValue(newLon);
            } else if (att != null) {
                map.removeNamedItem("lon");
            }
        }
    }

    /**
   *   returns the current value of the property Lon
   *   @return String
   */
    public String getLon() {
        return Lon;
    }

    /**
   *   sets a new value to the property Ele
   *   @param     newEle
   */
    public void setEle(XML_Ele newEle) throws SAXParseException {
        if (newEle != null) {
            Node newNode = newEle.myNode;
            if ((myNode != null) && (newNode != null)) {
                if ((newNode.getParentNode() != null) && newNode.getParentNode().equals(myNode)) {
                    Ele = newEle;
                } else {
                    Document ownerDocument = myNode.getOwnerDocument();
                    newNode = ownerDocument.importNode(newNode, true);
                    newEle.setNode(newNode);
                    Ele = newEle;
                    Node replaceNode = getReplaceNode("ele");
                    if (replaceNode == null) {
                        Node insertBeforeNode = getInsertBeforeNode("ele");
                        if (insertBeforeNode == null) {
                            myNode.appendChild(newNode);
                        } else {
                            myNode.insertBefore(newNode, insertBeforeNode);
                        }
                    } else {
                        myNode.replaceChild(newNode, replaceNode);
                    }
                }
            }
        } else if (Ele != null) {
            Node removeNode = Ele.myNode;
            myNode.removeChild(removeNode);
            Ele = null;
        }
    }

    /**
   *   returns the current value of the property Ele
   *   @return XML_Ele
   */
    public XML_Ele getEle() {
        return Ele;
    }

    /**
   *   sets a new value to the property Time
   *   @param     newTime
   */
    public void setTime(XML_Time newTime) throws SAXParseException {
        if (newTime != null) {
            Node newNode = newTime.myNode;
            if ((myNode != null) && (newNode != null)) {
                if ((newNode.getParentNode() != null) && newNode.getParentNode().equals(myNode)) {
                    Time = newTime;
                } else {
                    Document ownerDocument = myNode.getOwnerDocument();
                    newNode = ownerDocument.importNode(newNode, true);
                    newTime.setNode(newNode);
                    Time = newTime;
                    Node replaceNode = getReplaceNode("time");
                    if (replaceNode == null) {
                        Node insertBeforeNode = getInsertBeforeNode("time");
                        if (insertBeforeNode == null) {
                            myNode.appendChild(newNode);
                        } else {
                            myNode.insertBefore(newNode, insertBeforeNode);
                        }
                    } else {
                        myNode.replaceChild(newNode, replaceNode);
                    }
                }
            }
        } else if (Time != null) {
            Node removeNode = Time.myNode;
            myNode.removeChild(removeNode);
            Time = null;
        }
    }

    /**
   *   returns the current value of the property Time
   *   @return XML_Time
   */
    public XML_Time getTime() {
        return Time;
    }

    /**
   *   sets a new value to the property Name
   *   @param     newName
   */
    public void setName(XML_Name newName) throws SAXParseException {
        if (newName != null) {
            Node newNode = newName.myNode;
            if ((myNode != null) && (newNode != null)) {
                if ((newNode.getParentNode() != null) && newNode.getParentNode().equals(myNode)) {
                    Name = newName;
                } else {
                    Document ownerDocument = myNode.getOwnerDocument();
                    newNode = ownerDocument.importNode(newNode, true);
                    newName.setNode(newNode);
                    Name = newName;
                    Node replaceNode = getReplaceNode("name");
                    if (replaceNode == null) {
                        Node insertBeforeNode = getInsertBeforeNode("name");
                        if (insertBeforeNode == null) {
                            myNode.appendChild(newNode);
                        } else {
                            myNode.insertBefore(newNode, insertBeforeNode);
                        }
                    } else {
                        myNode.replaceChild(newNode, replaceNode);
                    }
                }
            }
        } else if (Name != null) {
            Node removeNode = Name.myNode;
            myNode.removeChild(removeNode);
            Name = null;
        }
    }

    /**
   *   returns the current value of the property Name
   *   @return XML_Name
   */
    public XML_Name getName() {
        return Name;
    }

    /**
   *   sets a new value to the property Desc
   *   @param     newDesc
   */
    public void setDesc(XML_Desc newDesc) throws SAXParseException {
        if (newDesc != null) {
            Node newNode = newDesc.myNode;
            if ((myNode != null) && (newNode != null)) {
                if ((newNode.getParentNode() != null) && newNode.getParentNode().equals(myNode)) {
                    Desc = newDesc;
                } else {
                    Document ownerDocument = myNode.getOwnerDocument();
                    newNode = ownerDocument.importNode(newNode, true);
                    newDesc.setNode(newNode);
                    Desc = newDesc;
                    Node replaceNode = getReplaceNode("desc");
                    if (replaceNode == null) {
                        Node insertBeforeNode = getInsertBeforeNode("desc");
                        if (insertBeforeNode == null) {
                            myNode.appendChild(newNode);
                        } else {
                            myNode.insertBefore(newNode, insertBeforeNode);
                        }
                    } else {
                        myNode.replaceChild(newNode, replaceNode);
                    }
                }
            }
        } else if (Desc != null) {
            Node removeNode = Desc.myNode;
            myNode.removeChild(removeNode);
            Desc = null;
        }
    }

    /**
   *   returns the current value of the property Desc
   *   @return XML_Desc
   */
    public XML_Desc getDesc() {
        return Desc;
    }

    /**
   *   sets a new value to the property Sym
   *   @param     newSym
   */
    public void setSym(XML_Sym newSym) throws SAXParseException {
        if (newSym != null) {
            Node newNode = newSym.myNode;
            if ((myNode != null) && (newNode != null)) {
                if ((newNode.getParentNode() != null) && newNode.getParentNode().equals(myNode)) {
                    Sym = newSym;
                } else {
                    Document ownerDocument = myNode.getOwnerDocument();
                    newNode = ownerDocument.importNode(newNode, true);
                    newSym.setNode(newNode);
                    Sym = newSym;
                    Node replaceNode = getReplaceNode("sym");
                    if (replaceNode == null) {
                        Node insertBeforeNode = getInsertBeforeNode("sym");
                        if (insertBeforeNode == null) {
                            myNode.appendChild(newNode);
                        } else {
                            myNode.insertBefore(newNode, insertBeforeNode);
                        }
                    } else {
                        myNode.replaceChild(newNode, replaceNode);
                    }
                }
            }
        } else if (Sym != null) {
            Node removeNode = Sym.myNode;
            myNode.removeChild(removeNode);
            Sym = null;
        }
    }

    /**
   *   returns the current value of the property Sym
   *   @return XML_Sym
   */
    public XML_Sym getSym() {
        return Sym;
    }

    /**
   *   sets a new value to the property Type
   *   @param     newType
   */
    public void setType(XML_Type newType) throws SAXParseException {
        if (newType != null) {
            Node newNode = newType.myNode;
            if ((myNode != null) && (newNode != null)) {
                if ((newNode.getParentNode() != null) && newNode.getParentNode().equals(myNode)) {
                    Type = newType;
                } else {
                    Document ownerDocument = myNode.getOwnerDocument();
                    newNode = ownerDocument.importNode(newNode, true);
                    newType.setNode(newNode);
                    Type = newType;
                    Node replaceNode = getReplaceNode("type");
                    if (replaceNode == null) {
                        Node insertBeforeNode = getInsertBeforeNode("type");
                        if (insertBeforeNode == null) {
                            myNode.appendChild(newNode);
                        } else {
                            myNode.insertBefore(newNode, insertBeforeNode);
                        }
                    } else {
                        myNode.replaceChild(newNode, replaceNode);
                    }
                }
            }
        } else if (Type != null) {
            Node removeNode = Type.myNode;
            myNode.removeChild(removeNode);
            Type = null;
        }
    }

    /**
   *   returns the current value of the property Type
   *   @return XML_Type
   */
    public XML_Type getType() {
        return Type;
    }

    /**
   *   sets a new value to the property Cmt
   *   @param     newCmt
   */
    public void setCmt(XML_Cmt newCmt) throws SAXParseException {
        if (newCmt != null) {
            Node newNode = newCmt.myNode;
            if ((myNode != null) && (newNode != null)) {
                if ((newNode.getParentNode() != null) && newNode.getParentNode().equals(myNode)) {
                    Cmt = newCmt;
                } else {
                    Document ownerDocument = myNode.getOwnerDocument();
                    newNode = ownerDocument.importNode(newNode, true);
                    newCmt.setNode(newNode);
                    Cmt = newCmt;
                    Node replaceNode = getReplaceNode("cmt");
                    if (replaceNode == null) {
                        Node insertBeforeNode = getInsertBeforeNode("cmt");
                        if (insertBeforeNode == null) {
                            myNode.appendChild(newNode);
                        } else {
                            myNode.insertBefore(newNode, insertBeforeNode);
                        }
                    } else {
                        myNode.replaceChild(newNode, replaceNode);
                    }
                }
            }
        } else if (Cmt != null) {
            Node removeNode = Cmt.myNode;
            myNode.removeChild(removeNode);
            Cmt = null;
        }
    }

    /**
   *   returns the current value of the property Cmt
   *   @return XML_Cmt
   */
    public XML_Cmt getCmt() {
        return Cmt;
    }

    public String getPCDATA() {
        return pcData;
    }

    public void setPCDATA(String newPcData) {
        pcData = newPcData;
        if ((newPcData != null) && (newPcData.length() > 0)) {
            Node tNode = myNode.getOwnerDocument().createTextNode(newPcData);
            myNode.appendChild(tNode);
        } else {
            NodeList list = myNode.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.TEXT_NODE) {
                    myNode.removeChild(node);
                }
            }
        }
    }

    private void parse() throws SAXParseException {
        NamedNodeMap att = myNode.getAttributes();
        Node temp;
        NodeList list = myNode.getChildNodes();
        int anz = list.getLength();
        for (int i = 0; i < anz; i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("ele")) {
                    Ele = new XML_Ele(node);
                }
                if (node.getNodeName().equals("time")) {
                    Time = new XML_Time(node);
                }
                if (node.getNodeName().equals("name")) {
                    Name = new XML_Name(node);
                }
                if (node.getNodeName().equals("desc")) {
                    Desc = new XML_Desc(node);
                }
                if (node.getNodeName().equals("sym")) {
                    Sym = new XML_Sym(node);
                }
                if (node.getNodeName().equals("type")) {
                    Type = new XML_Type(node);
                }
                if (node.getNodeName().equals("cmt")) {
                    Cmt = new XML_Cmt(node);
                }
            }
            if (node.getNodeType() == Node.TEXT_NODE) {
                pcData = node.getNodeValue();
            }
        }
        temp = att.getNamedItem("lat");
        if (temp != null) {
            Lat = temp.getNodeValue();
        }
        temp = att.getNamedItem("lon");
        if (temp != null) {
            Lon = temp.getNodeValue();
        }
    }

    public void setNode(Node newNode) throws SAXParseException {
        myNode = newNode;
        NamedNodeMap att = myNode.getAttributes();
        Node temp;
        NodeList list = myNode.getChildNodes();
        int anz = list.getLength();
        for (int i = 0; i < anz; i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("ele") && (Ele != null)) {
                    Ele.setNode(node);
                }
                if (node.getNodeName().equals("time") && (Time != null)) {
                    Time.setNode(node);
                }
                if (node.getNodeName().equals("name") && (Name != null)) {
                    Name.setNode(node);
                }
                if (node.getNodeName().equals("desc") && (Desc != null)) {
                    Desc.setNode(node);
                }
                if (node.getNodeName().equals("sym") && (Sym != null)) {
                    Sym.setNode(node);
                }
                if (node.getNodeName().equals("type") && (Type != null)) {
                    Type.setNode(node);
                }
                if (node.getNodeName().equals("cmt") && (Cmt != null)) {
                    Cmt.setNode(node);
                }
            }
            if (node.getNodeType() == Node.TEXT_NODE) {
                setPCDATA(node.getNodeValue());
            }
        }
        temp = att.getNamedItem("lat");
        if (temp != null) {
            setLat(temp.getNodeValue());
        }
        temp = att.getNamedItem("lon");
        if (temp != null) {
            setLon(temp.getNodeValue());
        }
    }

    public boolean equals(Object compare) {
        if (compare == null) {
            throw new NullPointerException("Can't compare XML_Wpt with null");
        }
        if (!(compare instanceof XML_Wpt)) {
            throw new UnsupportedOperationException("Can't compare with anything else then XML_Wpt");
        }
        return (Lat == null ? (((XML_Wpt) compare).getLat() == null) : Lat.equals(((XML_Wpt) compare).getLat())) && (Lon == null ? (((XML_Wpt) compare).getLon() == null) : Lon.equals(((XML_Wpt) compare).getLon())) && (Ele == null ? (((XML_Wpt) compare).getEle() == null) : Ele.equals(((XML_Wpt) compare).getEle())) && (Time == null ? (((XML_Wpt) compare).getTime() == null) : Time.equals(((XML_Wpt) compare).getTime())) && (Name == null ? (((XML_Wpt) compare).getName() == null) : Name.equals(((XML_Wpt) compare).getName())) && (Desc == null ? (((XML_Wpt) compare).getDesc() == null) : Desc.equals(((XML_Wpt) compare).getDesc())) && (Sym == null ? (((XML_Wpt) compare).getSym() == null) : Sym.equals(((XML_Wpt) compare).getSym())) && (Type == null ? (((XML_Wpt) compare).getType() == null) : Type.equals(((XML_Wpt) compare).getType())) && (Cmt == null ? (((XML_Wpt) compare).getCmt() == null) : Cmt.equals(((XML_Wpt) compare).getCmt()));
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    /**
   * This method is similar to clone but returns the correct class-type
   * @return
   * @throws SAXParseException
   */
    public XML_Wpt duplicate() throws SAXParseException {
        XML_Wpt ret = new XML_Wpt();
        ret.setLat(Lat == null ? null : new String(Lat));
        ret.setLon(Lon == null ? null : new String(Lon));
        if (Ele != null) {
            ret.setEle(Ele.duplicate());
        } else {
            ret.setEle(null);
        }
        if (Time != null) {
            ret.setTime(Time.duplicate());
        } else {
            ret.setTime(null);
        }
        if (Name != null) {
            ret.setName(Name.duplicate());
        } else {
            ret.setName(null);
        }
        if (Desc != null) {
            ret.setDesc(Desc.duplicate());
        } else {
            ret.setDesc(null);
        }
        if (Sym != null) {
            ret.setSym(Sym.duplicate());
        } else {
            ret.setSym(null);
        }
        if (Type != null) {
            ret.setType(Type.duplicate());
        } else {
            ret.setType(null);
        }
        if (Cmt != null) {
            ret.setCmt(Cmt.duplicate());
        } else {
            ret.setCmt(null);
        }
        return ret;
    }

    /**
   * Help method finding the node before which a node with name nodeName can be inserted
   * @param nodeName Name of Node that has to be inserted
   * @return
   */
    private Node getInsertBeforeNode(String nodeName) {
        Node ret = null;
        int length = NodeNames.length;
        int i, found = -1;
        for (i = 0; i < length; i++) {
            if (NodeNames[i].equals(nodeName)) {
                found = i;
                break;
            }
        }
        if ((found > 0) && (myNode != null)) {
            NodeList list = myNode.getChildNodes();
            for (i = found + 1; (ret == null) && (i < length); i++) {
                for (int j = 0; (ret == null) && (j < list.getLength()); j++) {
                    Node testNode = list.item(j);
                    if (testNode.getNodeName().equals(NodeNames[i])) {
                        ret = testNode;
                    }
                }
            }
        }
        return ret;
    }

    /**
   * Help method finding the node which a node with name nodeName can replace
   * @param nodeName Name of Node that has to be inserted
   * @return
   */
    private Node getReplaceNode(String nodeName) {
        Node ret = null;
        if (myNode != null) {
            NodeList list = myNode.getChildNodes();
            for (int j = 0; (ret == null) && (j < list.getLength()); j++) {
                Node testNode = list.item(j);
                if (testNode.getNodeName().equals(nodeName)) {
                    ret = testNode;
                }
            }
        }
        return ret;
    }

    public String toString() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            StreamResult t = new StreamResult(os);
            DOMSource source = new DOMSource(myNode);
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(source, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(os.toByteArray());
    }
}
