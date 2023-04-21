package oracle.toplink.libraries.asm.attrs;

import oracle.toplink.libraries.asm.Attribute;
import oracle.toplink.libraries.asm.ByteVector;
import oracle.toplink.libraries.asm.ClassReader;
import oracle.toplink.libraries.asm.ClassWriter;
import oracle.toplink.libraries.asm.Label;

/**
 * The AnnotationDefault attribute is a variable length attribute in the
 * attributes table of certain method_info structures, namely those representing
 * elements of annotation types. The AnnotationDefault attribute records the
 * default value for the element represented by the method_info structure. Each
 * method_info structures representing an element of an annotation types may contain
 * at most one AnnotationDefault attribute. The JVM must make this default value
 * available so it can be applied by appropriate reflective APIs.
 * <p>
 * The AnnotationDefault attribute has the following format:
 * <pre>
 *    AnnotationDefault_attribute {
 *      u2 attribute_name_index;
 *      u4 attribute_length;
 *      element_value default_value;
 *    }
 * </pre>
 * The items of the AnnotationDefault structure are as follows:
 * <dl>
 * <dt>attribute_name_index</dt>
 * <dd>The value of the attribute_name_index item must be a valid index into the
 *     constant_pool table. The constant_pool entry at that index must be a
 *     CONSTANT_Utf8_info structure representing the string "AnnotationDefault".</dd>
 * <dt>attribute_length</dt>
 * <dd>The value of the attribute_length item indicates the length of the attribute,
 *     excluding the initial six bytes. The value of the attribute_length item is
 *     thus dependent on the default value.</dd>
 * <dt>default_value</dt>
 * <dd>The default_value item represents the default value of the annotation type
 *     {@link oracle.toplink.libraries.asm.attrs.AnnotationElementValue element} whose default
 *     value is represented by this AnnotationDefault attribute.</dd>
 * </dl>
 *
 * @see <a href="http://www.jcp.org/en/jsr/detail?id=175">JSR 175 : A Metadata
 * Facility for the Java Programming Language</a>
 *
 * @author Eugene Kuleshov
 */
public class AnnotationDefaultAttribute extends Attribute {

    /**
   * Default value for annotation. Could be one of
   * <code>Byte</code>, <code>Character</code>, <code>Double</code>, 
   * <code>Float</code>, <code>Integer</code>, <code>Long</code>, <code>Short</code>, 
   * <code>Boolean</code>, <code>String</code>, 
   * <code>Annotation.EnumConstValue</code>, <code>Type</code>, 
   * <code>Annotation</code> or <code>Object[]</code>.
   */
    public Object defaultValue;

    public AnnotationDefaultAttribute() {
        super("AnnotationDefault");
    }

    public AnnotationDefaultAttribute(Object defaultValue) {
        this();
        this.defaultValue = defaultValue;
    }

    protected Attribute read(ClassReader cr, int off, int len, char[] buf, int codeOff, Label[] labels) {
        return new AnnotationDefaultAttribute(Annotation.readValue(cr, new int[] { off }, buf));
    }

    protected ByteVector write(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals) {
        return Annotation.writeValue(new ByteVector(), defaultValue, cw);
    }

    /**
   * Returns value in the format described in JSR-175 for Java source code.
   * 
   * @return value in the format described in JSR-175 for Java source code.
   */
    public String toString() {
        return "default " + defaultValue;
    }
}
