package net.sf.jimo.loader.tests.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Map;
import junit.framework.TestCase;
import net.sf.jimo.common.filtermap.FilteredMap;
import net.sf.jimo.common.ldap.LdapPropertiesFilter;
import net.sf.jimo.common.ldap.LdapPropertiesMap;
import net.sf.jimo.loader.Loadable;
import net.sf.jimo.loader.xml.XmlLoader;
import net.sf.jimo.loader.xmlutil.DOMUtil;
import net.sf.jimo.loader.xmlutil.Xml;
import org.w3c.dom.Document;

/**
 * 
 * <p>
 * Type: <strong><code>net.sf.jimo.loader.tests.xml.XmlUtilTests</code></strong>
 * </p>
 *
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public class XmlUtilTests extends TestCase {

    private XmlLoader<Object, LdapPropertiesFilter, Map<? extends String, ? extends Object>> xmlLoader;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FilteredMap<Object, LdapPropertiesFilter, Map<? extends String, ? extends Object>> serviceMap = new LdapPropertiesMap<Object, Map<? extends String, ? extends Object>>();
        xmlLoader = new XmlLoader<Object, LdapPropertiesFilter, Map<? extends String, ? extends Object>>(serviceMap);
        xmlLoader.load("resource/xmlinit.xml");
    }

    public void testDomUtil() throws Exception {
        Collection<Object> items = xmlLoader.getLoadables("(objectClass=" + Loadable.class.getName() + ")");
        assertFalse(items.size() == 0);
        Document document = DOMUtil.createDocument(items);
        DOMUtil.writeValidateDocument(document, new FileOutputStream("test-out.xml"), Xml.XMLLOADER_SCHEMA, Xml.XMLLOADER_URI);
        XmlLoader<Object, LdapPropertiesFilter, Map<? extends String, ? extends Object>> loader2 = new XmlLoader<Object, LdapPropertiesFilter, Map<? extends String, ? extends Object>>(new LdapPropertiesMap<Object, Map<? extends String, ? extends Object>>());
        loader2.load("test-out.xml");
        Collection<Object> items2 = loader2.getLoadables("(objectClass=" + Loadable.class.getName() + ")");
        assertEquals(items.size(), items2.size());
    }
}
