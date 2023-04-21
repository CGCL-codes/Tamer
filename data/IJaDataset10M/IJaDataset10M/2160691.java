package org.bluprint.model.java;

import org.testng.annotations.*;

public class JavaAttributeTest {

    @Test
    public void checkGetNameWithFirstUpper() {
        JavaAttribute attribute = new JavaAttribute("test");
        assert attribute.getNameWithFirstUpper().equals("Test");
    }

    @Test
    public void checkGetNameWithFirstUpperForSingleChar() {
        JavaAttribute attribute = new JavaAttribute("a");
        assert attribute.getNameWithFirstUpper().equals("A");
    }

    @Test
    public void checkGetNameWithFirstUpperForEmptyString() {
        JavaAttribute attribute = new JavaAttribute("");
        assert attribute.getNameWithFirstUpper().equals("");
    }

    @Test
    public void checkGetSingularName() {
        JavaAttribute attribute = new JavaAttribute("parts");
        attribute.setIsMany(true);
        assert attribute.getSingularName().equals("part");
    }

    @Test
    public void checkGetSingularNameWithFirstUpper() {
        JavaAttribute attribute = new JavaAttribute("products");
        attribute.setIsQualified(true);
        assert attribute.getSingularNameWithFirstUpper().equals("Product");
    }

    @Test
    public void checkGetSingularNameFromPluralEndingInES() {
        JavaAttribute attribute = new JavaAttribute("interfaces");
        attribute.setIsMany(true);
        assert attribute.getSingularName().equals("interface");
    }

    @Test
    public void checkGetSingularNameFromPluralEndingInSES() {
        JavaAttribute attribute = new JavaAttribute("classes");
        attribute.setIsMany(true);
        assert attribute.getSingularName().equals("class");
    }

    @Test
    public void checkGetSingularNameFromPluralEndingInIES() {
        JavaAttribute attribute = new JavaAttribute("parties");
        attribute.setIsMany(true);
        assert attribute.getSingularName().equals("party");
    }

    @Test
    public void checkGetJavaNameForNonReservedWord() {
        JavaAttribute attribute = new JavaAttribute("index");
        assert attribute.getJavaName().equals("index");
    }

    @Test
    public void checkGetJavaNameForReservedWord() {
        JavaAttribute attribute = new JavaAttribute("package");
        assert attribute.getJavaName().equals("_package");
    }

    @Test
    public void checkCopy() {
        JavaAttribute attribute = new JavaAttribute("NAME");
        attribute.setExpression("Fred");
        JavaAttribute copy = attribute.copy();
        assert attribute.getName().equals(copy.getName());
        assert attribute.getExpression().equals(copy.getExpression());
    }
}
