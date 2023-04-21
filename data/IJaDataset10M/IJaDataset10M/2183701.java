package net.peelmeagrape.hibernate.embedxml;

import net.peelmeagrape.hibernate.xmlgen.HibernateXml;
import net.peelmeagrape.hibernate.GenerateMappingsTestBase;
import org.dom4j.DocumentException;
import java.io.IOException;

public class BasicGenerateMappingsTest extends GenerateMappingsTestBase {

    private String resourceName = "net/peelmeagrape/hibernate/embedxml/BasicEmbedXmlTest.xml";

    public void testGetXmlFromClass() throws DocumentException, IOException {
        assertAnnotationsGenerateXmlResource(resourceName, SomeEntity.class);
    }

    @HibernateXml(xml = "<class name='anEntity' table='table'><id name='id'/></class>")
    public static class SomeEntity {
    }

    public void testGetXmlFromClassAndField() throws DocumentException, IOException {
        assertAnnotationsGenerateXmlResource(resourceName, EntityWithField.class);
    }

    @HibernateXml(xml = "<class name='anEntity' table='table'></class>")
    public static class EntityWithField {

        /**
         * Note: the field should be public
         */
        @HibernateXml(xml = "<id name='id'/>")
        private Long id;
    }

    public void testGetXmlFromClassAndGetter() throws DocumentException, IOException {
        assertAnnotationsGenerateXmlResource(resourceName, EntityWithGetter.class);
    }

    @HibernateXml(xml = "<class name='anEntity' table='table'></class>")
    public static class EntityWithGetter {

        public Long id;

        @HibernateXml(xml = "<id name='id'/>")
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
