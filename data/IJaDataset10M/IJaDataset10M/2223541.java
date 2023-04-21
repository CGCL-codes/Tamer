package fastforward.meta.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Test;
import fastforward.meta.PropertyContextPath;
import fastforward.meta.util.BeanXmlIO;

public class BeanXmlIOTest extends MetadataTester {

    @Test
    public void testToXml() {
        Bean1 bean1 = createTestBean1();
        Document doc = new BeanXmlIO(getMetadataRepository()).convertBeanToXml(bean1, true, true);
        StringWriter out = new StringWriter();
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        try {
            outputter.output(doc, out);
        } catch (IOException e) {
            fail();
        }
        try {
            String expectedXml = readFileAsString(new File("test/fastforward/meta/test/util/expected.xml"));
            assertEquals(expectedXml.trim(), out.toString().trim());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testBothWay() {
        Bean1 bean1 = createTestBean1();
        Document doc = new BeanXmlIO(getMetadataRepository()).convertBeanToXml(bean1, true, true);
        Bean1 bean1Copy = (Bean1) new BeanXmlIO(getMetadataRepository()).populateBeanFromXml(new Bean1(), doc);
        compareBeans(bean1, bean1Copy, new PropertyContextPath(Bean1.class));
    }
}
