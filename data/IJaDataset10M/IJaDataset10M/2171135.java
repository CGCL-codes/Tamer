package net.sf.jcorrect.standard.validator;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import javax.validation.ElementDescriptor;
import org.junit.Test;

public class TypeElementDescriptorImplTest {

    @Test
    public void creation() {
        ElementDescriptor desc = new TypeElementDescriptorImpl(String.class, new Annotation[0]);
        org.junit.Assert.assertEquals(ElementType.TYPE, desc.getElementType());
        org.junit.Assert.assertEquals("", desc.getPropertyPath());
        org.junit.Assert.assertEquals(String.class, desc.getReturnType());
        org.junit.Assert.assertEquals(0, desc.getConstraintDescriptors().size());
    }

    @Test(expected = AssertionError.class)
    public void creationWithoutType() {
        ElementDescriptor desc = new TypeElementDescriptorImpl(null, new Annotation[0]);
    }
}
