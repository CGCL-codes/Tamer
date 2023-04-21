package org.epistem.code.javaclass.attributes;

import java.io.DataInput;
import java.io.IOException;
import org.epistem.code.javaclass.JavaAttribute;
import org.epistem.code.javaclass.JavaClassLoader;
import org.epistem.code.javaclass.io.ConstantPool;

/**
 * Runtime visible parameter annotations
 *
 * @author nickmain
 */
public class RuntimeVisibleParameterAnnotationsAttribute extends ParameterAnnotationAttribute {

    public RuntimeVisibleParameterAnnotationsAttribute() {
        super(JavaAttribute.Name.RuntimeVisibleParameterAnnotations, true);
    }

    public static RuntimeVisibleParameterAnnotationsAttribute parse(ConstantPool pool, JavaClassLoader loader, DataInput in) throws IOException {
        RuntimeVisibleParameterAnnotationsAttribute attr = new RuntimeVisibleParameterAnnotationsAttribute();
        attr.parseAnnotations(pool, loader, in);
        return attr;
    }
}
