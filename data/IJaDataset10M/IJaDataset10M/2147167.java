package org.argouml.model;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Test against the UML model.
 */
public class TestAgainstUmlModel extends TestCase {

    private static Set<String> dontTest = new HashSet<String>();

    private static Hashtable<String, String> remap = new Hashtable<String, String>();

    private static boolean uml2 = false;

    /**
     * The constructor.
     *
     * @param n the name
     */
    public TestAgainstUmlModel(String n) {
        super(n);
    }

    /**
     * @throws SAXException when things go wrong with SAX
     * @throws IOException when there's an IO error
     * @throws ParserConfigurationException when the parser finds wrong syntax
     * 
     * TODO: Unused?
     */
    public void testDataModel() throws SAXException, IOException, ParserConfigurationException {
        Document doc = prepareDocument();
        if (doc == null) {
            return;
        }
        List<String> classNames = getMetaclassNames(doc);
        for (String className : classNames) {
            processClass(className);
        }
    }

    private static List<String> getMetaclassNames(Document doc) {
        List<String> result = new ArrayList<String>();
        int abstractCount = 0;
        if (uml2) {
            NodeList list = doc.getElementsByTagName("ownedMember");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                Node typeAttr = node.getAttributes().getNamedItem("xmi:type");
                if ("cmof:Class".equals(typeAttr.getNodeValue())) {
                    if (!isAbstract(node)) {
                        result.add(getNames(node));
                    } else {
                        abstractCount++;
                    }
                }
            }
        } else {
            NodeList list = doc.getElementsByTagName("Model:Class");
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (!isAbstract(node)) {
                    result.add(getNames(node));
                } else {
                    abstractCount++;
                }
            }
        }
        System.out.println("Skipping " + abstractCount + " abstract elements");
        return result;
    }

    private static String getNames(Node node) {
        Node pkg = node;
        while (pkg != null && !isPackage(pkg)) {
            pkg = pkg.getParentNode();
        }
        if (pkg == null) {
            return getName(node);
        }
        return getName(pkg) + ":" + getName(node);
    }

    private static boolean isPackage(Node node) {
        if ("Model:Package".equals(node.getNodeName())) {
            return true;
        } else {
            Node type = node.getAttributes().getNamedItem("xmi:type");
            if (type != null && "cmof:Package".equals(type.getNodeValue())) {
                return true;
            }
        }
        return false;
    }

    private static String getName(Node node) {
        return node.getAttributes().getNamedItem("name").getNodeValue();
    }

    private static boolean isAbstract(Node node) {
        Node isAbstract = node.getAttributes().getNamedItem("isAbstract");
        return isAbstract != null && "true".equals(isAbstract.getNodeValue());
    }

    /**
     * Print a message that this test case is inconclusive because of
     * the UML file missing.
     *
     * @param message that is to me printed.
     */
    private static void printInconclusiveMessage(String message) {
        System.out.println(TestAgainstUmlModel.class.getName() + ": WARNING: INCONCLUSIVE TEST!");
        System.out.println(message);
        System.out.println("You will have to fetch the model using the command" + " ant junit-get-model");
    }

    /**
     * Make all preparations for the tests by preparing the document.
     *
     * @return the document or null if not available.
     * @throws SAXException when things go wrong with SAX
     * @throws IOException when there's an IO error
     * @throws ParserConfigurationException when the parser finds wrong syntax
     */
    private static Document prepareDocument() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        String fileName = System.getProperty("test.model.uml");
        if (fileName == null) {
            printInconclusiveMessage("The property test.model.uml " + "is not set.");
            return null;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            printInconclusiveMessage("The file " + fileName + " cannot be found.");
            return null;
        }
        Document document = builder.parse(file);
        Element root = document.getDocumentElement();
        NamedNodeMap attributes = root.getAttributes();
        Node versionNode = attributes.getNamedItem("xmi:version");
        if (versionNode == null) {
            versionNode = attributes.getNamedItem("xmi.version");
        }
        String version = versionNode.getNodeValue();
        if ("1.1".equals(version)) {
            uml2 = false;
        } else if ("2.1".equals(version)) {
            uml2 = true;
        } else {
            System.out.println("Unknown metamodel type");
        }
        return document;
    }

    /**
     * Walk through the UML Classes found.

     */
    private void processClass(String className) {
        String name;
        if (remap.containsKey(className)) {
            name = remap.get(className);
        } else {
            name = className;
        }
        String[] pieces = name.split(":");
        String umlclass = pieces[1];
        String pkgName = pieces[0].replaceAll("_", "");
        if (className.equals(name) && remap.containsKey(pkgName)) {
            pkgName = remap.get(pkgName);
        }
        String getter = "get" + pkgName + "Factory";
        try {
            Method getMethod = Model.class.getMethod(getter, new Class[0]);
            Object factory = getMethod.invoke(null, new Object[0]);
            if (!(factory instanceof Factory)) {
                fail("Factory for " + name + "isn't an instanceof Model.Factory");
            }
            if (dontTest.contains(umlclass) || dontTest.contains(name)) {
                System.out.println("Skipping " + name);
                return;
            }
            String[] classarg = { umlclass, null };
            CheckUMLModelHelper.createAndRelease(factory, classarg);
        } catch (Exception e) {
            fail("Failed to get factory for " + name + " - " + e);
        }
    }

    /**
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite("Tests for " + TestAgainstUmlModel.class.getPackage().getName());
        Document doc = null;
        try {
            doc = prepareDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc == null) {
            return suite;
        }
        for (String metaclassName : getMetaclassNames(doc)) {
            suite.addTest(new TestAgainstUmlModel(metaclassName));
        }
        return suite;
    }

    protected void runTest() throws Throwable {
        processClass(getName());
    }

    /**
     * Initialize the lookup map to link the uml class names
     * to the factories.
     *
     * This brute force method should be investigated
     * in favor of determining the Uml Class namespace from
     * the UML metamodel and computing the factory
     * at run time.
     *
     * Certain classes that cannot be tested directly in this way
     * should be calculated.  Event and StateVertex, for example,
     * are marked abstract in the model.  But we need to make sure
     * that the reverse is true, that there are no elements
     * marked abstract in the model that in fact are instantiable
     * by the model subsystem.
     */
    static {
        InitializeModel.initializeDefault();
        dontTest.add("ProgrammingLanguageDataType");
        dontTest.add("UseCaseInstance");
        dontTest.add("ArgListsExpression");
        dontTest.add("BooleanExpression");
        dontTest.add("IterationExpression");
        dontTest.add("MappingExpression");
        dontTest.add("ObjectSetExpression");
        dontTest.add("ProcedureExpression");
        dontTest.add("TimeExpression");
        dontTest.add("TypeExpression");
        remap.put("Core:Stereotype", "ExtensionMechanisms:Stereotype");
        remap.put("Core:TaggedValue", "ExtensionMechanisms:TaggedValue");
        remap.put("Core:TagDefinition", "ExtensionMechanisms:TagDefinition");
        remap.put("Kernel:PrimitiveType", "Core:Primitive");
        remap.put("Kernel:Expression", "DataTypes:Expression");
        remap.put("Kernel", "Core");
        remap.put("Interfaces", "Core");
        remap.put("Dependencies", "Core");
        remap.put("Nodes", "Core");
        remap.put("SimpleTime", "Core");
        remap.put("AssociationClasses", "Core");
        remap.put("Communications", "StateMachines");
        remap.put("BehaviorStateMachines", "StateMachines");
        remap.put("ProtocolStateMachines", "StateMachines");
        remap.put("Models", "ModelManagement");
    }
}
