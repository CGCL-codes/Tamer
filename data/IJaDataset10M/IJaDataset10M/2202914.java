package de.jmda.fx.sandbox.property;

import static org.junit.Assert.assertEquals;
import javafx.beans.property.ObjectProperty;
import org.junit.Test;

public class JUT_PropertyEncapsulation {

    @Test
    public void testRWObjectProperty() {
        TypeWithProperties typeWithProperties = new TypeWithProperties("initial");
        ObjectProperty<String> stringRWProperty = typeWithProperties.stringRWProperty();
        assertEquals("initial", stringRWProperty.get());
        stringRWProperty.set("y");
        assertEquals("y", stringRWProperty.get());
    }

    @Test
    public void testROObjectProperty() {
    }
}
