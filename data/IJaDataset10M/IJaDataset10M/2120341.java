package org.encuestame.core.test.config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.encuestame.core.config.XMLConfigurationFileSupport;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Test for {@link XMLConfigurationFileSupport}.
 *
 * @author Picado, Juan juanATencuestame.org
 * @since Sep 4, 2011
 */
public class XmlConfigurationText extends TestCase {

    /**
     * @throws ConfigurationException
     *
     */
    @Test
    public void testXmlFile() {
        XMLConfiguration xmlConfiguration = null;
        try {
            Resource res = new ClassPathResource("properties-test/encuestame-test-config.xml");
            xmlConfiguration = new XMLConfiguration(res.getFile());
            xmlConfiguration.setAutoSave(true);
            xmlConfiguration.addProperty("juan", "juan");
            final Iterator i = xmlConfiguration.getKeys();
            while (i.hasNext()) {
                Object object = (Object) i.next();
                System.out.println(object);
            }
            List fields = xmlConfiguration.configurationsAt("tables.table(0).fields.field");
            for (Iterator it = fields.iterator(); it.hasNext(); ) {
                HierarchicalConfiguration sub = (HierarchicalConfiguration) it.next();
                String fieldName = sub.getString("name");
                String fieldType = sub.getString("type");
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
