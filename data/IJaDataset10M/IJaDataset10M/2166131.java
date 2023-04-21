package org.ontospread.tester.xmlbind;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TestBatteryXMLBind {

    private static Logger logger = Logger.getLogger(TestBatteryXMLBind.class);

    public static final String PACKAGE_NAME = TestBatteryXMLBind.class.getPackage().getName();

    private Unmarshaller unmarshaller;

    private Marshaller marshaller;

    public TestBatteryXMLBind() {
        JAXBContext jc = null;
        try {
            jc = JAXBContext.newInstance(PACKAGE_NAME);
            this.unmarshaller = jc.createUnmarshaller();
            this.marshaller = jc.createMarshaller();
        } catch (JAXBException e) {
        }
    }

    public TestBattery restoreTestBattery(Node node) throws JAXBException {
        try {
            return (TestBattery) unmarshaller.unmarshal(node);
        } catch (JAXBException je) {
            throw je;
        }
    }

    public Document serializeTestBattery(TestBattery testBattery) throws JAXBException, DocumentBuilderException {
        try {
            Document doc = DocumentBuilderHelper.getEmptyDocument();
            marshaller.marshal(testBattery, doc);
            return doc;
        } catch (JAXBException je) {
            throw je;
        } catch (DocumentBuilderException e) {
            throw e;
        }
    }

    /**
	 * Singleton field
	 */
    private static TestBatteryXMLBind instance;

    /**
	 * Singleton method
	 * 
	 * @return The singleton instance of this class 
	 * @throws XmlBindException
	 */
    public static TestBatteryXMLBind getInstance() {
        if (instance == null) {
            instance = new TestBatteryXMLBind();
        }
        return instance;
    }
}
