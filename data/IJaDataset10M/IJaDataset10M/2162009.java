package org.ji18n.core.test;

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.ji18n.core.translation.xliff12.jaxb.Body;
import org.ji18n.core.translation.xliff12.jaxb.File;
import org.ji18n.core.translation.xliff12.jaxb.Source;
import org.ji18n.core.translation.xliff12.jaxb.TransUnit;
import org.ji18n.core.translation.xliff12.jaxb.Xliff;
import org.testng.annotations.Test;

/**
 * @version $Id: Xliff12Tests.java 162 2008-08-06 03:49:43Z david_ward2 $
 * @author david at ji18n.org
 */
public class Xliff12Tests extends AbstractCoreTests {

    @Test
    public void testUnmarshal() throws Exception {
        InputStream is = null;
        try {
            is = Xliff12Tests.class.getResourceAsStream("/xliff12-sample.xml");
            JAXBContext jc = JAXBContext.newInstance("org.ji18n.core.translation.xliff12.jaxb");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Xliff xliff = (Xliff) unmarshaller.unmarshal(is);
            File file = (File) xliff.getAnyAndFile().get(0);
            Body body = file.getBody();
            TransUnit transUnit = (TransUnit) body.getGroupOrTransUnitOrBinUnit().get(0);
            assert transUnit.getId().equals("hi");
            Source source = transUnit.getSource();
            String content = (String) source.getContent().get(0);
            assert content.equals("Hello world");
        } finally {
            try {
                if (is != null) is.close();
            } catch (Throwable t) {
            }
        }
    }
}
