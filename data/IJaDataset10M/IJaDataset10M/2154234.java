package net.javacrumbs.springws.test.validator;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.javacrumbs.springws.test.WsTestException;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.validation.XmlValidatorFactory;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

public class SchemaRequestValidatorTest extends AbstractValidatorTest {

    public SchemaRequestValidatorTest() throws Exception {
    }

    protected SchemaRequestValidator createValidator() throws Exception {
        SchemaRequestValidator validator = new SchemaRequestValidator();
        validator.setSchema(new ClassPathResource("xml/schema.xsd"));
        validator.setSchemaLanguage(XmlValidatorFactory.SCHEMA_W3C_XML);
        assertEquals(1, validator.getSchemas().length);
        validator.afterPropertiesSet();
        assertEquals(XmlValidatorFactory.SCHEMA_W3C_XML, validator.getSchemaLanguage());
        return validator;
    }

    @Test
    public void testValid() throws Exception {
        WebServiceMessage message = getValidMessage();
        createValidator().validateRequest(null, message);
    }

    @Test
    public void testEmpty() throws Exception {
        WebServiceMessage message = createMock(WebServiceMessage.class);
        expect(message.getPayloadSource()).andReturn(null);
        replay(message);
        createValidator().validateRequest(null, message);
        verify(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotInitialized() throws Exception {
        SchemaRequestValidator validator = new SchemaRequestValidator();
        validator.afterPropertiesSet();
    }

    @Test
    public void testInvalid() throws Exception {
        WebServiceMessage message = getInvalidMessage();
        try {
            createValidator().validateRequest(null, message);
            fail("Exception expected");
        } catch (WsTestException e) {
            assertTrue(e.getMessage().contains("Source message:"));
        }
    }

    @Test
    public void testSetSchema() throws Exception {
        SchemaRequestValidator validator = new SchemaRequestValidator();
        validator.setXsdSchema(new SimpleXsdSchema(new ClassPathResource("xml/schema.xsd")));
        validator.afterPropertiesSet();
        assertNotNull(validator.getValidator());
        WebServiceMessage message = getInvalidMessage();
        try {
            createValidator().validateRequest(null, message);
            fail("Exception expected");
        } catch (WsTestException e) {
            assertTrue(e.getMessage().contains("Source message:"));
        }
    }

    @Test
    public void testSetSchemaCollection() throws Exception {
        SchemaRequestValidator validator = new SchemaRequestValidator();
        CommonsXsdSchemaCollection schemaCollection = new CommonsXsdSchemaCollection(new Resource[] { new ClassPathResource("xml/schema.xsd") });
        schemaCollection.afterPropertiesSet();
        validator.setXsdSchemaCollection(schemaCollection);
        validator.afterPropertiesSet();
        assertNotNull(validator.getValidator());
        WebServiceMessage message = getInvalidMessage();
        try {
            createValidator().validateRequest(null, message);
            fail("Exception expected");
        } catch (WsTestException e) {
            assertTrue(e.getMessage().contains("Source message:"));
        }
    }
}
