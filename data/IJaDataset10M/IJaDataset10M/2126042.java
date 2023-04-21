package com.genia.toolbox.uml_generator.transformer.model;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.genia.toolbox.uml_generator.model.impl.ClassModel;

public class TestAbstractObject {

    @Test
    public void testAbstractObject() {
        ClassModel classModel = new ClassModel();
        classModel.setName("String(20)");
        assertEquals("String", classModel.getName());
        assertEquals(20, classModel.getLength().intValue());
    }
}
