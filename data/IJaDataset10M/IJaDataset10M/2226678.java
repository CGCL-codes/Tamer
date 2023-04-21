package net.peelmeagrape.hibernate;

import org.dom4j.DocumentException;
import java.io.IOException;

public class InlineSubclassMappingTest extends GenerateMappingsTestBase {

    public void testGetXmlFromClass() throws DocumentException, IOException {
        assertAnnotationsGenerateXml("<class name='net.peelmeagrape.hibernate.InlineSubclassMappingTest$SomeEntity'>" + "<id name='id'/>" + "<subclass name='net.peelmeagrape.hibernate.InlineSubclassMappingTest$SubEntity'>" + "<property name='firstName'/>" + "</subclass>" + "</class>", SomeEntity.class);
    }

    @H8Class(subclasses = @Subclass(SubEntity.class))
    public static class SomeEntity {

        @Id
        public Long id;
    }

    public static class SubEntity extends SomeEntity {

        @Property
        public String firstName;
    }
}
