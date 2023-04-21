package org.epistem.code.javaclass.attributes;

import java.io.DataInput;
import java.io.IOException;
import org.epistem.code.javaclass.JavaAttribute;
import org.epistem.code.javaclass.JavaClassLoader;
import org.epistem.code.javaclass.io.ConstantPool;
import org.epistem.io.IndentingPrintWriter;

/**
 * The default values for annotation methods
 *
 * @author nickmain
 */
public class AnnotationDefaultAttribute extends JavaAttribute {

    /** The default value */
    public final JavaAnnotation.Value value;

    /**
     * @param value the default value
     */
    public AnnotationDefaultAttribute(JavaAnnotation.Value value) {
        super(JavaAttribute.Name.AnnotationDefault.name());
        this.value = value;
    }

    public static AnnotationDefaultAttribute parse(ConstantPool pool, JavaClassLoader loader, DataInput in) throws IOException {
        return new AnnotationDefaultAttribute(AnnotationAttribute.parseValue(pool, loader, in));
    }

    /** Dump for debug purposes */
    public final void dump(IndentingPrintWriter out) {
        out.print(name + " = ");
        value.dump(out);
        out.println();
    }
}
