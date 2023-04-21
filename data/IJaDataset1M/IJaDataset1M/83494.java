package oqube.bytes.attributes;

import oqube.bytes.ClassFile;

/**
 * Represents runtime invisible annotations for a class,method or field.
 * 
 * @author nono
 * 
 */
public class RuntimeInvisibleAnnotationsAttribute extends AbstractAnnotationAttribute {

    private static final String attributeName = "RuntimeInvisibleAnnotations";

    public RuntimeInvisibleAnnotationsAttribute() {
    }

    public RuntimeInvisibleAnnotationsAttribute(ClassFile cf) {
        super(cf, attributeName);
    }
}
