package org.bcholmes.jmicro.cif.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ClassUtils;
import org.bcholmes.jmicro.TypeFinder;
import org.bcholmes.jmicro.TypeSelector;
import org.bcholmes.jmicro.util.xml.DocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ModelTest extends TestCase {

    public class ModelEntitySelector extends TypeSelector {

        @Override
        public boolean isSelected(Class<?> type) {
            return type.isAnnotationPresent(Entity.class);
        }

        @Override
        public boolean isSelected(String className) {
            return ClassUtils.getPackageName(className).equals(ClassUtils.getPackageName(ModelTest.class));
        }
    }

    public void testShouldRegisterModelObjectsInHibernate() throws Exception {
        List<Class<?>> modelClasses = findModelClasses();
        List<String> hibernateTypes = findHibernateTypes();
        for (Class<?> c : modelClasses) {
            assertTrue(c.getName(), hibernateTypes.contains(c.getName()));
        }
    }

    public void testShouldEnsureThatAllHibernateRegisteredClassesExist() throws Exception {
        List<Class<?>> modelClasses = findModelClasses();
        List<String> hibernateTypes = findHibernateTypes();
        for (String string : hibernateTypes) {
            try {
                assertTrue(string, modelClasses.contains(Class.forName(string)));
            } catch (ClassNotFoundException e) {
                fail("Class not found: " + string);
            }
        }
    }

    private List<String> findHibernateTypes() throws Exception {
        List<String> result = new ArrayList<String>();
        Document document = readHibernateConfiguration();
        NodeList list = document.getElementsByTagName("mapping");
        for (int i = 0; i < list.getLength(); i++) {
            Element mapping = (Element) list.item(i);
            result.add(mapping.getAttribute("class"));
        }
        return result;
    }

    private List<Class<?>> findModelClasses() throws Exception {
        return new TypeFinder(new ModelEntitySelector()).findTypes();
    }

    private Document readHibernateConfiguration() throws SAXException, IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream("hibernate.cfg.xml");
        try {
            return new DocumentFactory().createFromInputStream(input);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
}
