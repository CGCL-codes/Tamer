package parser.reader;

import java.io.File;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import parser.populator.XMLParser;
import parser.tester.Tester;
import classLoader.construction.ClassConstructor;
import classLoader.loader.CompilingClassLoader;
import common.CommonConstants;
import common.Config;

/**
 * @author srivanuj
 * 
 */
public class SchemaReader {

    public Document initializeParser(String p_strSchema, char chFile) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory m_docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder m_docBuilder;
        Document m_doc = null;
        try {
            m_docBuilder = m_docBuilderFactory.newDocumentBuilder();
            if (chFile == 's') {
                StringBufferInputStream bInputStream = new StringBufferInputStream(p_strSchema);
                m_doc = m_docBuilder.parse(bInputStream);
            } else if (chFile == 'f') m_doc = m_docBuilder.parse(p_strSchema);
        } catch (SAXException e) {
            throw new SAXException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new ParserConfigurationException(e.getMessage());
        }
        return m_doc;
    }

    public HashMap ParseElementNode(Node p_node) {
        HashMap<String, String> hAttrVals = new HashMap<String, String>();
        short iNtype = -1;
        iNtype = p_node.getNodeType();
        NamedNodeMap nnm;
        Attr attr = null;
        if ((iNtype == Node.ELEMENT_NODE)) {
            hAttrVals.put(CommonConstants.NODE_NAME, p_node.getNodeName());
            nnm = p_node.getAttributes();
            if (nnm != null) {
                int nlen = nnm.getLength();
                for (int i = 0; i < nlen; i++) {
                    attr = (Attr) nnm.item(i);
                    hAttrVals.put(attr.getNodeName(), attr.getNodeValue());
                }
            } else {
                return null;
            }
            return hAttrVals;
        } else {
            return null;
        }
    }

    public Node readFirstNode(Document p_schemaDoc) throws Exception {
        Node rootNode = getFirstNode(p_schemaDoc);
        try {
            String strType = getType(rootNode);
            if (strType.equals(CommonConstants.COMPLEX_TYPE)) {
                String strFirstEle = getFirstElement(p_schemaDoc);
            } else System.out.println("First node a non complex node-- cannot handle this!");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return rootNode;
    }

    public String getFirstElement(Document p_doc) throws Exception {
        setFirstElement(CommonConstants.PREFIX_NSPACE + "_schemaElement");
        return p_doc.getFirstChild().getNodeName();
    }

    public Node getFirstNode(Document p_doc) throws Exception {
        if (p_doc.getFirstChild().getNodeName() != "xs:schema") throw new Exception("An invalid schema encountered\n");
        Node node = p_doc.getFirstChild();
        return node;
    }

    public String getType(Node p_node) {
        if (p_node.getNodeName() == CommonConstants.ATTRIBUTE) return null;
        HashMap hmpType = ParseElementNode(p_node);
        if (hmpType == null) return CommonConstants.TEXT_NODE;
        if (hmpType.containsKey(CommonConstants.TYPE)) return hmpType.get(CommonConstants.TYPE).toString();
        if ((hmpType.containsKey(CommonConstants.NAME))) {
            if (p_node.getNodeName().equals(CommonConstants.COMPLEX_TYPE)) {
                return CommonConstants.COMPLEX_TYPE;
            }
            if (p_node.getNodeName().equals(CommonConstants.SIMPLE_TYPE)) return CommonConstants.SIMPLE_TYPE;
        }
        if (p_node.getFirstChild() != null) {
            Node node = p_node.getFirstChild().getNextSibling();
            String strNodeName = node.getNodeName();
            if (CommonConstants.TYPES.contains(strNodeName)) {
                return node.getNodeName();
            }
        }
        return null;
    }

    private String getAttrType(Node p_node, String p_strKey) {
        HashMap hmpType = ParseElementNode(p_node);
        if (hmpType == null) return CommonConstants.TEXT_NODE;
        if (hmpType.containsKey(p_strKey)) return hmpType.get(p_strKey).toString();
        Node node = p_node.getFirstChild().getNextSibling();
        if (node.getNodeName().toString().equals(CommonConstants.SIMPLE_TYPE)) {
            Node nodeRestriction = node.getFirstChild().getNextSibling();
            if (nodeRestriction.getNodeName().toString().equals(CommonConstants.RESTRICTION)) {
                String strRestriction = getAttrType(nodeRestriction, CommonConstants.BASE);
                return strRestriction;
            }
        }
        return node.getNodeName();
    }

    private Node getNextChildElement(Node p_node) {
        while (p_node.hasChildNodes()) {
            if ((p_node = p_node.getFirstChild().getNextSibling()).toString() == CommonConstants.ELEMENT) break;
        }
        return p_node;
    }

    private void addToClassHash(String p_strkey, String p_strVal) {
        p_strkey = p_strkey.replaceAll("-", "");
        p_strVal = p_strVal.replaceAll("-", "");
        if (m_htClassTable.containsKey(p_strkey)) {
            String strMemberVal = (String) m_htClassTable.get(p_strkey);
            strMemberVal += CommonConstants.MEMBER_SEPERATOR + p_strVal;
            m_htClassTable.put(p_strkey, strMemberVal);
        } else m_htClassTable.put(p_strkey, p_strVal);
    }

    private void addExtToClassHash(String p_strParent, String p_strExtensionClass) {
        String strMemberVal = null, strMemberExt = null;
        if (m_htClassTable.containsKey(p_strParent)) {
            strMemberVal = (String) m_htClassTable.get(p_strParent);
        }
        if (m_htClassTable.containsKey(p_strExtensionClass)) {
            strMemberExt = (String) m_htClassTable.get(p_strExtensionClass);
            m_htClassTable.remove(p_strParent);
            strMemberExt = strMemberExt.substring(strMemberExt.indexOf(CommonConstants.MEMBER_SEPERATOR));
            m_htClassTable.put(p_strParent, (strMemberVal + strMemberExt));
        } else System.out.println("SchemaReaer Failure: " + " Could not find base " + p_strExtensionClass + " Try putting it before other elemets in schema");
    }

    private String getNodeName(Node p_node) {
        HashMap hmpAttrs = ParseElementNode(p_node);
        if (hmpAttrs != null && hmpAttrs.containsKey(CommonConstants.NAME)) {
            return hmpAttrs.get(CommonConstants.NAME).toString();
        }
        return null;
    }

    private boolean isInfoNode(Node p_node) {
        return CommonConstants.INFO_NODES.contains(p_node.getNodeName());
    }

    public void generateScheleton(SchemaReader p_schemaRead, Node p_fnode, String p_strParent, int p_iNewParent, String p_strNewParent) {
        String strNodeType;
        if (!p_strParent.equals(CommonConstants.XS_SCHEMA) && (p_fnode.getNodeType() == Node.ELEMENT_NODE) && (p_fnode.getNodeName().equals(CommonConstants.EXTENSION_NODE))) {
            HashMap hmpExtension = ParseElementNode(p_fnode);
            String strExtensionClass = (String) hmpExtension.get("base");
            addExtToClassHash(p_strParent, strExtensionClass);
        }
        if (p_strParent.equals(CommonConstants.XS_SCHEMA) || (!p_schemaRead.isInfoNode(p_fnode))) {
            strNodeType = p_schemaRead.getType(p_fnode);
            if ((p_fnode.getNodeName() == CommonConstants.ATTRIBUTE)) {
                addToClassHash(p_strParent, p_schemaRead.getNodeName(p_fnode));
                addToClassHash(p_schemaRead.getNodeName(p_fnode), p_schemaRead.getAttrType(p_fnode, CommonConstants.TYPE));
            } else if (strNodeType != null && strNodeType.equals(CommonConstants.COMPLEX_TYPE)) {
                if (p_strParent.equals(CommonConstants.XS_SCHEMA)) p_strParent = p_strParent.replaceFirst(":", "_") + "Element";
                addToClassHash(p_strParent, p_schemaRead.getNodeName(p_fnode));
                addToClassHash(p_schemaRead.getNodeName(p_fnode), p_schemaRead.getType(p_fnode));
            } else if (strNodeType != null && strNodeType.equals(CommonConstants.SIMPLE_TYPE)) {
                addToClassHash(p_strParent, p_schemaRead.getNodeName(p_fnode));
                addToClassHash(p_schemaRead.getNodeName(p_fnode), p_schemaRead.getAttrType(p_fnode, CommonConstants.TYPE));
            } else if (strNodeType != null && strNodeType.equals(CommonConstants.TEXT_NODE)) {
            } else if (p_fnode.getNodeName().equals(CommonConstants.XS_SCHEMA)) {
                addToClassHash(CommonConstants.PREFIX_NSPACE + "_schemaElement", CommonConstants.COMPLEX_TYPE);
            } else {
                if (p_schemaRead.getNodeName(p_fnode) != null) {
                    addToClassHash(p_strParent, p_schemaRead.getNodeName(p_fnode));
                    addToClassHash(p_schemaRead.getNodeName(p_fnode), p_schemaRead.getType(p_fnode));
                }
            }
        }
        for (Node child = p_fnode.getFirstChild(), nParent = child; child != null; child = child.getNextSibling()) {
            if (child.getParentNode().getNodeName().equals(CommonConstants.XS_SCHEMA)) {
                p_iNewParent = 1;
            }
            if (p_iNewParent == 2) {
                p_strParent = p_strNewParent;
                p_iNewParent = 0;
            }
            if (p_iNewParent == 1) {
                p_iNewParent = 2;
                if ((p_schemaRead.getType(child) != null && ((p_schemaRead.getType(child)).equals(CommonConstants.COMPLEX_TYPE)))) {
                    p_strNewParent = p_schemaRead.getNodeName(child);
                } else p_iNewParent = 0;
            }
            if (nParent == child) p_iNewParent = 1;
            generateScheleton(p_schemaRead, child, p_strParent, p_iNewParent, p_strNewParent);
            XMLParser obj = new XMLParser();
            if (!(child.getNodeName().equals(CommonConstants.EXTENSION_NODE))) p_iNewParent = 1;
        }
    }

    private boolean isTypeDescriptionNode(Node p_node, SchemaReader p_schemaRead) {
        if ((p_node != null) && (CommonConstants.TYPE.contains(p_node.getNodeName())) && p_schemaRead.getNodeName(p_node) != null) {
            return true;
        }
        return false;
    }

    public void formObjects(String p_strElement) throws IOException, ClassNotFoundException {
        String strNewClasses = Config.readProperty("CREATE_CLASSFILES");
        if (strNewClasses.equalsIgnoreCase("true")) {
            ClassConstructor m_objClassConstructor = new ClassConstructor();
            CompilingClassLoader objCCL = null;
            String arrMem[] = null;
            int j = 0;
            int iContinue = 0;
            if (p_strElement == null) {
                p_strElement = CommonConstants.FIRST_ELE;
                Set arrMem1 = m_htClassTable.keySet();
                Iterator iter = arrMem1.iterator();
                while (iter.hasNext()) {
                    if (arrMem == null) {
                        arrMem = new String[1];
                    } else {
                        String arrMemNew[] = new String[arrMem.length + 1];
                        int iLen = arrMem.length;
                        int iIndex = 0;
                        while (iLen-- > 0) {
                            arrMemNew[iIndex] = arrMem[iIndex];
                            iIndex++;
                        }
                        arrMem = arrMemNew;
                    }
                    arrMem[j++] = iter.next().toString();
                }
            } else {
                arrMem = getMemberArray(p_strElement);
            }
            String strObj = p_strElement;
            String strMem, strType;
            int iLen = arrMem.length;
            if (m_vecTraversed.contains(strObj) && (m_objClassConstructor.ifPresentinKeys(CommonConstants.KEY_TYPES, strObj) == -1)) {
                iContinue = 1;
            }
            for (int i = 0; i < iLen; i++) {
                if ((strMem = arrMem[i]).contentEquals(p_strElement)) continue;
                if (iContinue == 1) continue;
                if (p_strElement != CommonConstants.FIRST_ELE) m_vecTraversed.add(strObj);
                strType = getArtificialType(strMem);
                String strDeafaultClasses = Config.readProperty("CREATE_ALL_CLASSES");
                if (strDeafaultClasses.equalsIgnoreCase("true") && !m_vecDefaultClasses.contains(strType) && !m_htClassTable.containsKey(strType)) {
                    m_objClassConstructor.createDummyClasses(strType);
                    m_vecDefaultClasses.add(strType);
                }
                if (strType.equals(CommonConstants.SIMPLE_TYPE)) {
                    if (!(CommonConstants.INFO_NODES.contains(strMem))) m_objClassConstructor.sendBytesForClass(strObj, strMem);
                } else if (strType.equals(CommonConstants.COMPLEX_TYPE)) {
                    if (!(CommonConstants.INFO_NODES.contains(strMem))) m_objClassConstructor.sendBytesForClass(strObj, strMem);
                    formObjects(strMem);
                } else {
                    if (!(CommonConstants.INFO_NODES.contains(strMem))) m_objClassConstructor.sendBytesForClass(strObj, strMem);
                    formObjects(strMem);
                }
            }
            m_objClassConstructor.completeClassSyntax();
        }
    }

    public Object loadNewClasses(CompilingClassLoader p_objLoader) {
        String strNewClasses = Config.readProperty("CREATE_CLASSFILES");
        Iterator iTer = m_htClassTable.keySet().iterator();
        while (iTer.hasNext()) {
            try {
                p_objLoader.loadClass("classFiles." + iTer.next().toString(), false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done...");
        XMLParser obj = new XMLParser();
        try {
            return obj.parseXMLContentString(Config.readProperty("XMLFILEPATH"), this);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getArtificialType(String p_strKey) {
        if (m_htClassTable.containsKey(p_strKey)) {
            String arrMem[] = getMemberArray(p_strKey);
            int i = 0;
            while (arrMem[i].contentEquals(p_strKey)) i++;
            return arrMem[i];
        } else return CommonConstants.SIMPLE_TYPE;
    }

    /**
	 * @return the m_stFirstElement
	 */
    public static String getFirstElement() {
        return m_stFirstElement;
    }

    /**
	 * @param firstElement
	 *            the m_stFirstElement to set
	 */
    public static void setFirstElement(String firstElement) {
        m_stFirstElement = firstElement;
    }

    private String[] getMemberArray(String p_strKey) {
        String strMembers = m_htClassTable.get(p_strKey);
        String arrMem[] = strMembers.split(CommonConstants.MEMBER_SEPERATOR);
        return arrMem;
    }

    public boolean classCreated(String p_strClassName) {
        return m_htClassTable.contains(p_strClassName);
    }

    public Hashtable<String, String> m_htClassTable = new Hashtable<String, String>();

    public Vector<String> m_vecTraversed = new Vector<String>();

    public Vector<String> m_vecDefaultClasses = new Vector<String>();

    public static String m_stFirstElement = null;
}
