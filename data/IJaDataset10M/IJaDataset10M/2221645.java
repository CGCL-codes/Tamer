package gov.ornl.hermes.Persistence.Extensions.Tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;
import gov.ornl.hermes.Persistence.Extensions.PersistentReactingElement;

/** 
 * <!-- begin-UML-doc -->
 * <p>A class that tests the behaviors of load() and persist() on PERSISTENTREACTINGELEMENT.</p>
 * <!-- end-UML-doc -->
 * @author s4h
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TestPersistentReactingElement {

    /** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private PersistentReactingElement persistentReactingElement;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation that tests the behavior of load on specified class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void testLoad() {
        persistentReactingElement = new PersistentReactingElement();
        String xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<ns3:persistentReactingElement xmlns:ns2=\"Element\" xmlns:ns3=\"ReactingElement\" reacLibType=\"0\" mtNumber=\"0\" crossSection=\"867.5309\" population=\"5.0\" name=\"deuterium\" massNumber=\"2\" id=\"1\" atomicNumber=\"1\"/>";
        InputStream is = new ByteArrayInputStream(xmlFile.getBytes());
        try {
            persistentReactingElement.load(is);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        assertEquals(persistentReactingElement.getName(), "deuterium");
        assertEquals(persistentReactingElement.getPopulation(), 5.0, 0.01);
        assertEquals(persistentReactingElement.getId(), 1, 0.01);
        assertEquals(persistentReactingElement.getMassNumber(), 2, 0.01);
        assertEquals(persistentReactingElement.getAtomicNumber(), 1, 0.01);
        assertEquals(persistentReactingElement.getCrossSection(), 867.5309, 0.01);
        assertEquals(persistentReactingElement.getMTNumber(), 0.0, 0.1);
        assertEquals(persistentReactingElement.getReacLibType(), 0.0, 0.1);
        persistentReactingElement = new PersistentReactingElement();
        xmlFile = null;
        is = null;
        try {
            persistentReactingElement.load(is);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "InputStreamException: InputStream can not be null");
        }
        persistentReactingElement = new PersistentReactingElement();
        xmlFile = "I am not XML!";
        is = new ByteArrayInputStream(xmlFile.getBytes());
        try {
            persistentReactingElement.load(is);
        } catch (Exception e) {
            assertEquals(e.getMessage(), null);
        }
        persistentReactingElement = new PersistentReactingElement();
        xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<IDOSTUFF xmlns=\"STUFF\" population=\"5.0\" name=\"deuterium\" massNumber=\"2\" id=\"1\" atomicNumber=\"1\"/>";
        is = new ByteArrayInputStream(xmlFile.getBytes());
        try {
            persistentReactingElement.load(is);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "unexpected element (uri:\"STUFF\", local:\"IDOSTUFF\"). Expected elements are <{}ReactingElement>,<{Element}element>,<{ReactingElement}persistentReactingElement>");
        }
        persistentReactingElement = new PersistentReactingElement();
        xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<ns3:persistentReactingElement xmlns:ns2=\"Element\" xmlns:ns3=\"ReactingElement\"  mtNumber=\"0\" reacLibType=\"0\" crossSection=\"867.5309\" population=\"5.0\" name=\"deuterium\" massNumber=\"2\" id=\"1\" atomicNumber=\"1\"/>";
        is = new ByteArrayInputStream(xmlFile.getBytes());
        try {
            persistentReactingElement.load(is);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        assertEquals(persistentReactingElement.getName(), "deuterium");
        assertEquals(persistentReactingElement.getPopulation(), 5.0, 0.01);
        assertEquals(persistentReactingElement.getId(), 1, 0.01);
        assertEquals(persistentReactingElement.getMassNumber(), 2, 0.01);
        assertEquals(persistentReactingElement.getAtomicNumber(), 1, 0.01);
        assertEquals(persistentReactingElement.getCrossSection(), 867.5309, 0.01);
        assertEquals(persistentReactingElement.getMTNumber(), 0.0, 0.1);
        assertEquals(persistentReactingElement.getReacLibType(), 0.0, 0.1);
        persistentReactingElement = new PersistentReactingElement();
        xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<ns3:persistentReactingElement xmlns:ns2=\"Element\" xmlns:ns3=\"ReactingElement\"  mtNumber=\"0\" reacLibType=\"0\" crossSection=\"867.5309\" population=\"5.0\" name=\"deuterium\" id=\"1\" atomicNumber=\"1\"/>";
        is = new ByteArrayInputStream(xmlFile.getBytes());
        try {
            persistentReactingElement.load(is);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        assertEquals(persistentReactingElement.getName(), "deuterium");
        assertEquals(persistentReactingElement.getPopulation(), 5.0, 0.01);
        assertEquals(persistentReactingElement.getId(), 1, 0.01);
        assertEquals(persistentReactingElement.getAtomicNumber(), 1, 0.01);
        assertEquals(persistentReactingElement.getCrossSection(), 867.5309, 0.01);
        assertEquals(persistentReactingElement.getMTNumber(), 0.0, 0.1);
        assertEquals(persistentReactingElement.getReacLibType(), 0.0, 0.1);
        persistentReactingElement = new PersistentReactingElement();
        xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<ns3:persistentReactingElement xmlns:ns2=\"Element\" xmlns:ns3=\"ReactingElement\"  mtNumber=\"0\" reacLibType=\"0\" crossSection=\"867.5309\" population=\"5.0\" name2=\"Bob\" name=\"deuterium\" massNumber=\"2\" id=\"1\" atomicNumber=\"1\"/>";
        is = new ByteArrayInputStream(xmlFile.getBytes());
        try {
            persistentReactingElement.load(is);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        assertEquals(persistentReactingElement.getName(), "deuterium");
        assertEquals(persistentReactingElement.getPopulation(), 5.0, 0.01);
        assertEquals(persistentReactingElement.getId(), 1, 0.01);
        assertEquals(persistentReactingElement.getMassNumber(), 2, 0.01);
        assertEquals(persistentReactingElement.getAtomicNumber(), 1, 0.01);
        assertEquals(persistentReactingElement.getCrossSection(), 867.5309, 0.01);
        assertEquals(persistentReactingElement.getMTNumber(), 0.0, 0.1);
        assertEquals(persistentReactingElement.getReacLibType(), 0.0, 0.1);
        persistentReactingElement = new PersistentReactingElement();
        xmlFile = "";
        is = new ByteArrayInputStream(xmlFile.getBytes());
        try {
            persistentReactingElement.load(is);
        } catch (Exception e) {
            assertEquals(e.getMessage(), null);
        }
        persistentReactingElement = new PersistentReactingElement();
        try {
            persistentReactingElement.fromString("name=deuterium id=1 population=5.0 z=1 a=2 " + "mt=0 crossSection=867.5309");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        xmlFile = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" + "<ns3:persistentReactingElement xmlns:ns2=\"Element\" xmlns:ns3=\"ReactingElement\"  mtNumber=\"0\" reacLibType=\"0\" crossSection=\"867.5309\" population=\"5.0\" name=\"deuterium\" massNumber=\"2\" id=\"1\" atomicNumber=\"1\"/>";
        is = new ByteArrayInputStream(xmlFile.getBytes());
        try {
            persistentReactingElement.load(is);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        assertEquals(persistentReactingElement.getName(), "deuterium");
        assertEquals(persistentReactingElement.getPopulation(), 5.0, 0.01);
        assertEquals(persistentReactingElement.getId(), 1, 0.01);
        assertEquals(persistentReactingElement.getMassNumber(), 2, 0.01);
        assertEquals(persistentReactingElement.getAtomicNumber(), 1, 0.01);
        assertEquals(persistentReactingElement.getCrossSection(), 867.5309, 0.01);
        assertEquals(persistentReactingElement.getMTNumber(), 0.0, 0.1);
        assertEquals(persistentReactingElement.getReacLibType(), 0.0, 0.1);
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>An operation that tests the behavior of persist on the specified class.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void testPersist() {
        File file;
        InputStream is;
        try {
            persistentReactingElement = new PersistentReactingElement();
            persistentReactingElement.fromString("name=deuterium id=1 population=5.0 z=1 a=2 " + "mt=0 crossSection=867.5309");
            persistentReactingElement.persist("./jaxb-xmlFile1.xml");
            file = new File("./jaxb-xmlFile1.xml");
            is = new FileInputStream(file);
            persistentReactingElement.load(is);
            assertEquals(persistentReactingElement.getName(), "deuterium");
            assertEquals(persistentReactingElement.getPopulation(), 5.0, 0.01);
            assertEquals(persistentReactingElement.getId(), 1, 0.01);
            assertEquals(persistentReactingElement.getMassNumber(), 2, 0.01);
            assertEquals(persistentReactingElement.getAtomicNumber(), 1, 0.01);
            assertEquals(persistentReactingElement.getCrossSection(), 867.5309, 0.01);
            assertEquals(persistentReactingElement.getMTNumber(), 0.0, 0.1);
            assertEquals(persistentReactingElement.getReacLibType(), 0.0, 0.1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
        try {
            persistentReactingElement = new PersistentReactingElement();
            persistentReactingElement.persist("NotXMLFile.stuff");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "File type not supported: NotXMLFile.stuff");
        }
        try {
            persistentReactingElement = new PersistentReactingElement();
            persistentReactingElement.persist("FileDoesntExist.xml");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "File not found: FileDoesntExist.xml");
        }
        try {
            persistentReactingElement = new PersistentReactingElement();
            persistentReactingElement.persist(null);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "NullPointerException: xmlFile can not be null");
        }
        try {
            persistentReactingElement = new PersistentReactingElement();
            persistentReactingElement.fromString("name=deuterium id=1 population=5.0 z=1 a=2 " + "mt=0 crossSection=867.5309");
            persistentReactingElement.persist("./jaxb-xmlFile1.xml");
            file = new File("./jaxb-xmlFile1.xml");
            is = new FileInputStream(file);
            persistentReactingElement.load(is);
            assertEquals(persistentReactingElement.getName(), "deuterium");
            assertEquals(persistentReactingElement.getPopulation(), 5.0, 0.01);
            assertEquals(persistentReactingElement.getId(), 1, 0.01);
            assertEquals(persistentReactingElement.getMassNumber(), 2, 0.01);
            assertEquals(persistentReactingElement.getAtomicNumber(), 1, 0.01);
            assertEquals(persistentReactingElement.getCrossSection(), 867.5309, 0.01);
            assertEquals(persistentReactingElement.getMTNumber(), 0.0, 0.1);
            assertEquals(persistentReactingElement.getReacLibType(), 0.0, 0.1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            fail();
        }
    }
}
