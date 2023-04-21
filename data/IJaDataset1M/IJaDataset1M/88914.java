package com.cateshop.exe.attribute;

import com.cateshop.def.attribute.CompositeDefinition;
import junit.framework.TestCase;

/**
 * @author notXX
 * 
 */
public class CompositeTest extends TestCase {

    /**
     * COMPONENT.
     */
    public static final String COMPONENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<AttributeDefinition>\r\n" + "\t<AttributeDefinition name=\"a\" type=\"INTEGER\">\r\n" + "\t</AttributeDefinition>\r\n" + "\t<AttributeDefinition name=\"b\" type=\"INTEGER\">\r\n" + "\t</AttributeDefinition>\r\n" + "\t<AttributeDefinition name=\"c\" type=\"COMPOSITE\">\r\n" + "\t\t<AttributeDefinition name=\"a\" type=\"INTEGER\">\r\n" + "\t\t</AttributeDefinition>\r\n" + "\t\t<AttributeDefinition name=\"b\" type=\"INTEGER\">\r\n" + "\t\t</AttributeDefinition>\r\n" + "\t\t<AttributeDefinition name=\"c\" type=\"COMPOSITE\">\r\n" + "\t\t\t<AttributeDefinition name=\"a\" type=\"INTEGER\">\r\n" + "\t\t\t</AttributeDefinition>\r\n" + "\t\t\t<AttributeDefinition name=\"b\" type=\"INTEGER\">\r\n" + "\t\t\t</AttributeDefinition>\r\n" + "\t\t</AttributeDefinition>\r\n" + "\t</AttributeDefinition>\r\n" + "</AttributeDefinition>\r\n";

    /**
     * Test method for
     * {@link com.cateshop.exe.attribute.Composite#Composite(java.lang.String, com.cateshop.def.attribute.CompositeDefinition)}.
     */
    public void testComposite() {
        CompositeDefinition compositeDefinition = new CompositeDefinition();
        compositeDefinition.setName("compositeDefinition");
        compositeDefinition.setComponent(COMPONENT);
        System.out.println(compositeDefinition.createAttribute());
    }
}
