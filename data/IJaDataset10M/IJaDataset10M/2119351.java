package net.peelmeagrape.hibernate;

import org.dom4j.DocumentException;
import java.io.IOException;

public class PropertiesTest extends GenerateMappingsTestBase {

    public void testGetXmlFromClass() throws DocumentException, IOException {
        assertAnnotationsGenerateXml("<class name='net.peelmeagrape.hibernate.PropertiesTest$SomeEntity'>" + "<id name='id'/>" + "<properties name='prop'>" + "<property name='firstName'/>" + "<property name='lastName'/>" + "</properties>" + "</class>", SomeEntity.class);
    }

    @H8Class(properties = @Properties(name = "prop"))
    public static class SomeEntity {

        @Id
        Long id;

        @Property(properties = "prop")
        String firstName;

        @Property(properties = "")
        String lastName;
    }
}
