package test.oued.persistance.xmiconverter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import oued.persistance.xmiconverter.IXMIDocumentProxy;
import oued.persistance.xmiconverter.impl2_0.XMIDocumentProxy;
import junit.framework.TestCase;

public class TestXMI2_1DocumentProxy extends TestCase {

    private IXMIDocumentProxy documentProxy = null;

    public TestXMI2_1DocumentProxy() {
    }

    protected void setUp() throws Exception {
        this.init("D:\\donnees\\Workspace_2\\persistanceZero\\test\\oued\\persistance\\fc.xmi");
    }

    protected void tearDown() throws Exception {
        documentProxy = null;
    }

    /**
	 * M�thode de test de la m�thode getClassesNodes
	 */
    public void testGetClassesNodes() {
        List nodeList = documentProxy.getClassesNodes();
        assertTrue(nodeList.size() == 12);
        String nomCategory = null;
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = (Node) nodeList.get(i);
            String xmiType = documentProxy.getAttributValue(node, "xmi:type");
            assertTrue(xmiType.compareTo("uml:Class") == 0 || xmiType.compareTo(XMIDocumentProxy.ASSOCIATIONCLASS_NODE_TYPE) == 0);
            String nom = documentProxy.getAttributValue(node, "name");
            if (nom != null && nom.compareTo("Category") == 0) {
                nomCategory = nom;
            }
        }
        assertTrue(nomCategory != null);
    }

    /**
	 * Methode de test de  getSimplePropertiesNodes
	 */
    public void testGetSimplePropertiesNodes() {
        List nodeList = documentProxy.getClassesNodes();
        Node employeeNode = getNodeByName(nodeList, "Person");
        assertTrue(employeeNode != null);
        List nodePropertiesList = documentProxy.getSimplePropertiesNodes(employeeNode);
        String nomLastName = null;
        for (int i = 0; i < nodePropertiesList.size(); i++) {
            Node node = (Node) nodePropertiesList.get(i);
            String xmiType = documentProxy.getAttributValue(node, "xmi:type");
            assertTrue(xmiType.compareTo("uml:Property") == 0);
            String nom = documentProxy.getAttributValue(node, "name");
            if (nom != null && nom.compareTo("lastName") == 0) {
                nomLastName = nom;
            }
        }
        assertTrue(nomLastName != null);
        assertTrue(nomLastName.compareTo("lastName") == 0);
    }

    /**
	 * Methode de test de  getAssociationsNodes
	 */
    public void testGetAssociationsNodes() {
        List nodeList = documentProxy.getClassesNodes();
        Node employeeNode = getNodeByName(nodeList, "Employee");
        assertTrue(employeeNode != null);
        List nodeAssociationList = documentProxy.getAssociationsNodes(employeeNode);
        assertTrue(nodeAssociationList.size() == 7);
        String nomLastName = null;
        for (int i = 0; i < nodeAssociationList.size(); i++) {
            Node node = (Node) nodeAssociationList.get(i);
            String xmiType = documentProxy.getAttributValue(node, "xmi:type");
            assertTrue(xmiType.compareTo("uml:Property") == 0 || xmiType.compareTo("uml:Generalization") == 0);
            String association = documentProxy.getAttributValue(node, "association");
            assertTrue(association != null || xmiType.compareTo("uml:Generalization") == 0);
        }
    }

    /**
	 * 
	 */
    public void testGetNodeByXmiId() {
        Node node = documentProxy.getNodeByXmiId("uml:Class", "BOUML_0x1961b30");
        String nom = documentProxy.getAttributValue(node, "name");
        assertTrue(nom != null);
        assertTrue(nom.compareTo("Employee") == 0);
        Node node2 = documentProxy.getNodeByXmiId("uml:Property", "BOUML_0x1963130");
        String nom2 = documentProxy.getAttributValue(node2, "name");
        assertTrue(nom2 != null);
        assertTrue(nom2.compareTo("indice") == 0);
        Node node3 = documentProxy.getNodeByXmiId("uml:AssociationClass", "BOUML_0x19615e0");
        String nom3 = documentProxy.getAttributValue(node3, "name");
        assertTrue(nom3 != null);
        assertTrue(nom3.compareTo("EmployeeFC") == 0);
    }

    public void testGetTypesNodes() {
        String[] types = { "date", "double", "int", "string", "boolean" };
        List nodeList = documentProxy.getTypesNodes();
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = (Node) nodeList.get(i);
            String xmiType = documentProxy.getAttributValue(node, "xmi:type");
            assertTrue(xmiType.compareTo("uml:DataType") == 0);
            String nom = documentProxy.getAttributValue(node, "name");
            assertTrue(nom != null);
            boolean contain = false;
            for (int j = 0; j < types.length; j++) {
                if (nom.compareTo(types[j]) == 0) contain = true;
            }
            assertTrue(contain == true);
        }
    }

    /**
	 * Initialisation du fichier xmi
	 */
    private void init(String nomFichier) {
        DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
        DocumentBuilder constructor = null;
        Document document = null;
        try {
            constructor = fabrique.newDocumentBuilder();
            File f = new File(nomFichier);
            document = constructor.parse(f);
            documentProxy = new XMIDocumentProxy(document);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Node getNodeByName(List nodeList, String nodeName) {
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = (Node) nodeList.get(i);
            String nom = documentProxy.getAttributValue(node, "name");
            if (nom != null && nom.compareTo(nodeName) == 0) return node;
        }
        return null;
    }
}
