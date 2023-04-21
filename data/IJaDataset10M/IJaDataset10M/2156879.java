package org.sempere.commons.print;

import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import org.junit.Test;

/**
 * Unit tests class for PrinterManager class.
 * 
 * @author bsempere
 */
public class PrinterManagerTest {

    @Test
    public void printObject() throws Exception {
        String expectedResult = "PrinterManagerTest.Property[name=myPropName,value=PrinterManagerTest.Property[name=myNestedPropName,value=myNestedPropValue]]";
        Property property = new Property("myPropName", new Property("myNestedPropName", "myNestedPropValue"));
        assertEquals(expectedResult, PrinterManager.getInstance().print(property));
    }

    @Test
    public void printObjectInOutputStream() throws Exception {
        String expectedResult = "PrinterManagerTest.Property[name=myPropName,value=PrinterManagerTest.Property[name=myNestedPropName,value=myNestedPropValue]]";
        Property property = new Property("myPropName", new Property("myNestedPropName", "myNestedPropValue"));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrinterManager.getInstance().print(property, outputStream);
        outputStream.close();
        assertEquals(expectedResult, new String(outputStream.toByteArray()));
    }

    @Test
    public void printObjectInWriter() throws Exception {
        String expectedResult = "PrinterManagerTest.Property[name=myPropName,value=PrinterManagerTest.Property[name=myNestedPropName,value=myNestedPropValue]]";
        Property property = new Property("myPropName", new Property("myNestedPropName", "myNestedPropValue"));
        StringWriter writer = new StringWriter();
        PrinterManager.getInstance().print(property, writer);
        writer.close();
        assertEquals(expectedResult, writer.getBuffer().toString());
    }

    private class Property extends PrintableObject {

        private String name;

        private Object value;

        public Property(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
