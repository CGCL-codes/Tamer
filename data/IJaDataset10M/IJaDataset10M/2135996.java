package test.com.adactus.mpeg21.didl.factory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import org.xml.sax.InputSource;
import test.com.adactus.mpeg21.didl.BaseTestCase;
import com.adactus.mpeg21.didl.entity.DIDLType;
import com.adactus.mpeg21.didl.xml.DigitalItemDeclarationsDocumentSource;
import com.adactus.mpeg21.didl.xml.DigitalItemDeclarationsFactory;
import com.adactus.mpeg21.xml.schema.ValidatingDocumentSource;

public class TestDigitalItemDeclarationsFactory extends BaseTestCase {

    private DigitalItemDeclarationsFactory<DIDLType> factory = new DigitalItemDeclarationsFactory<DIDLType>();

    /**
	 * 
	 * Validate a digital item declarations document
	 * 
	 * @param xml
	 * @throws Exception
	 */
    public void validateDigitalItemDeclaration(byte[] xml, String encoding) throws Exception {
        ValidatingDocumentSource validatingDocumentSource = new ValidatingDocumentSource(true);
        validatingDocumentSource.setSchema(new String[] { "src/main/resources/schema/didmodel.xsd", "src/main/resources/schema/didl.xsd" });
        validatingDocumentSource.initFactory();
        validatingDocumentSource.initBuilder();
        Schema schema = validatingDocumentSource.getSchema();
        Validator v = schema.newValidator();
        if (encoding == null) {
            v.validate(new SAXSource(new InputSource(new ByteArrayInputStream(xml))));
        } else {
            v.validate(new SAXSource(new InputSource(new InputStreamReader(new ByteArrayInputStream(xml), encoding))));
        }
        assertTrue(!validatingDocumentSource.wasError());
        assertTrue(!validatingDocumentSource.wasFatalError());
        assertTrue(!validatingDocumentSource.wasWaring());
    }

    public void testSimple() throws Exception {
        byte[] file = getFileContent("src/test/resources/xml/SimpleDigitalItem.xml");
        validateDigitalItemDeclaration(file, "UTF-8");
        DIDLType didl = factory.unmarshal("UTF-8", new ByteArrayInputStream(file));
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        factory.marshall(didl, bout, null);
        System.out.println(new String(bout.toByteArray()));
    }

    public void testMedium() throws Exception {
        byte[] file = getFileContent("src/test/resources/xml/MediumDigitalItem.xml");
        validateDigitalItemDeclaration(file, "UTF-8");
        DIDLType didl = factory.unmarshal("UTF-8", new ByteArrayInputStream(file));
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        factory.marshall(didl, bout, null);
        System.out.println(new String(bout.toByteArray()));
    }

    public void testComplex() throws Exception {
        byte[] file = getFileContent("src/test/resources/xml/ComplexDigitalItem.xml");
        validateDigitalItemDeclaration(file, "UTF-8");
        DIDLType didl = factory.unmarshal("UTF-8", new ByteArrayInputStream(file));
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        factory.marshall(didl, bout, null);
        System.out.println(new String(bout.toByteArray()));
    }

    /**
	 * 
	 * Validate against schema definiton via factory.
	 * 
	 * @throws Exception
	 */
    public void testFactory() throws Exception {
        DigitalItemDeclarationsDocumentSource digitalItemDeclarationsDocumentSource = new DigitalItemDeclarationsDocumentSource("src/main/resources/schema/didmodel.xsd", "src/main/resources/schema/didl.xsd");
        digitalItemDeclarationsDocumentSource.validate(getFileContent("src/test/resources/xml/ComplexDigitalItem.xml"), "UTF-8");
        digitalItemDeclarationsDocumentSource.validate(getFileContent("src/test/resources/xml/MediumDigitalItem.xml"), "UTF-8");
        digitalItemDeclarationsDocumentSource.validate(getFileContent("src/test/resources/xml/ComplexDigitalItem.xml"), "UTF-8");
    }
}
