package com.vangent.hieos.services.xds.bridge.mapper;

import java.io.InputStream;
import java.util.Map;
import com.vangent.hieos.services.xds.bridge.utils.JUnitHelper;
import com.vangent.hieos.xutil.iosupport.Io;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Class description
 *
 *
 * @version        v1.0, 2011-06-10
 * @author         Jim Horner
 */
public class ContentParserTest {

    /**
     * Method description
     *
     *
     * @throws Exception
     */
    @Test
    public void createTemplateVariablesTest() throws Exception {
        ClassLoader cl = getClass().getClassLoader();
        InputStream xmlis = cl.getResourceAsStream(JUnitHelper.SALLY_GRANT);
        assertNotNull(xmlis);
        byte[] xml = Io.getBytesFromInputStream(xmlis);
        assertNotNull(xml);
        assertTrue(xml.length > 0);
        ContentParserConfig config = JUnitHelper.createCDAToXDSContentParserConfig();
        ContentParser parser = new ContentParser();
        Map<String, String> tvars = parser.parse(config, xml);
        assertNotNull(tvars);
        assertEquals("1.2.36.1.2001.1003.0.8003621231167890", tvars.get(ContentVariableName.AuthorInstitutionRoot.toString()));
    }
}
