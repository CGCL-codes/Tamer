package org.t2framework.vili.model.maven;

import java.beans.Introspector;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import org.seasar.kvasir.util.io.IOUtils;
import org.t2framework.vili.model.maven.Repository;
import net.skirnir.xom.BeanAccessor;
import net.skirnir.xom.BeanAccessorFactory;
import net.skirnir.xom.XMLParserFactory;
import net.skirnir.xom.XOMapper;
import net.skirnir.xom.XOMapperFactory;
import net.skirnir.xom.annotation.impl.AnnotationBeanAccessor;
import junit.framework.TestCase;

public class RepositoryTest extends TestCase {

    private XOMapper mapper = XOMapperFactory.newInstance().setBeanAccessorFactory(new BeanAccessorFactory() {

        public BeanAccessor newInstance() {
            return new AnnotationBeanAccessor() {

                @Override
                protected String toXMLName(String javaName) {
                    return Introspector.decapitalize(javaName);
                }
            };
        }
    }).setStrict(true);

    public void testToBean1() throws Exception {
        Repository actual = mapper.toBean(XMLParserFactory.newInstance().parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(getClass().getName().replace('.', '/').concat("_repository1.xml")), "UTF-8")).getRootElement(), Repository.class);
        assertEquals("www.seasar.org", actual.getId());
        assertEquals("The Seasar Foundation Maven2 Repository", actual.getName());
        assertEquals("http://maven.seasar.org/maven2", actual.getUrl());
        assertNull(actual.getSnapshots());
        StringWriter sw = new StringWriter();
        mapper.toXML(actual, sw);
        System.out.println(sw.toString());
    }

    public void testToXML1() throws Exception {
        String expected = IOUtils.readString(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(getClass().getName().replace('.', '/').concat("_repository1.xml")), "UTF-8"), true);
        StringWriter actual = new StringWriter();
        mapper.toXML(mapper.toBean(XMLParserFactory.newInstance().parse(new StringReader(expected)).getRootElement(), Repository.class), actual);
        assertEquals(expected, actual.toString());
    }

    public void testToBean2() throws Exception {
        Repository actual = mapper.toBean(XMLParserFactory.newInstance().parse(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(getClass().getName().replace('.', '/').concat("_repository2.xml")), "UTF-8")).getRootElement(), Repository.class);
        assertEquals("snapshot.maven.seasar.org", actual.getId());
        assertEquals("The Seasar Foundation Maven2 Snapshot Repository", actual.getName());
        assertEquals("http://maven.seasar.org/maven2-snapshot", actual.getUrl());
        assertEquals("true", actual.getSnapshots().getEnabled());
        StringWriter sw = new StringWriter();
        mapper.toXML(actual, sw);
        System.out.println(sw.toString());
    }

    public void testToXML2() throws Exception {
        String expected = IOUtils.readString(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(getClass().getName().replace('.', '/').concat("_repository2.xml")), "UTF-8"), true);
        StringWriter actual = new StringWriter();
        mapper.toXML(mapper.toBean(XMLParserFactory.newInstance().parse(new StringReader(expected)).getRootElement(), Repository.class), actual);
        assertEquals(expected, actual.toString());
    }

    public void testEquals() throws Exception {
        Repository repository1 = new Repository("a", "b", "http:/a");
        Repository repository2 = new Repository("c", "d", "http:/a/");
        assertEquals(repository1, repository2);
        assertEquals(repository1.hashCode(), repository2.hashCode());
    }
}
