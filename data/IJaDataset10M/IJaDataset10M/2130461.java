package uni.compilerbau.backend;

import java.io.IOException;
import java.io.OutputStream;
import uni.compilerbau.tabelle.Class;
import uni.compilerbau.wrapper.Feld;
import uni.compilerbau.wrapper.Konstruktor;
import uni.compilerbau.wrapper.Methode;

public class ClassFile {

    @Feld
    private final ConstantPool constantPool;

    @Feld
    private final Class thisClass;

    @Feld
    private final Class superClass;

    @Konstruktor
    public ClassFile(Class thisClass, ConstantPool constantPool) {
        this.constantPool = constantPool;
        this.thisClass = thisClass;
        this.superClass = thisClass.getSuperClass();
        return;
    }

    /**
	 * Schreibt die gesamte ClassFile (inklusive magic-teil, cp, methoden, ....)
	 * in den Writer und fertig. Bevor die Methode aufgerufen wird, muss alles
	 * andere fertig sein.
	 */
    @Methode
    public void write(OutputStream out) throws Exception {
        FieldsPool fields = new FieldsPool(this.getConstantPool(), this.thisClass.getFieldsList());
        MethodsPool methods = new MethodsPool(this.getConstantPool(), this.thisClass.getConstructors(), this.thisClass.getMethods());
        this.getConstantPool().getClassIndex(this.thisClass);
        this.getConstantPool().getClassIndex(this.superClass);
        ByteWriter bytes = new ByteWriter();
        this.writeMagic(bytes);
        this.writeVersion(bytes);
        this.writeConstantPool(bytes);
        this.writeAccessFlags(bytes);
        bytes.write2Byte(this.thisClass.getConstantPoolIndex());
        bytes.write2Byte(this.superClass.getConstantPoolIndex());
        this.writeInterfaces(bytes);
        fields.write(bytes);
        methods.write(bytes);
        this.writeAttributes(bytes);
        out.write(bytes.getByteArray());
        return;
    }

    /**
	 * Each value of the attributes table must be an attribute structure (§4.7).
	 * 
	 * The only attributes defined by this specification as appearing in the
	 * attributes table of a ClassFile structure are the SourceFile attribute
	 * (§4.7.7) and the Deprecated (§4.7.10) attribute.
	 * 
	 * A Java virtual machine implementation is required to silently ignore any
	 * or all attributes in the attributes table of a ClassFile structure that
	 * it does not recognize. Attributes not defined in this specification are
	 * not allowed to affect the semantics of the class file, but only to
	 * provide additional descriptive information (§4.7.1).
	 */
    @Methode
    private void writeAttributes(ByteWriter bytes) throws IOException {
        bytes.write2Byte(0);
        return;
    }

    @Methode
    private void writeInterfaces(ByteWriter bytes) throws IOException {
        bytes.write2Byte(0);
        return;
    }

    @Methode
    private void writeAccessFlags(ByteWriter bytes) throws IOException {
        bytes.write2Byte(0x0001);
        return;
    }

    @Methode
    private void writeVersion(ByteWriter bytes) throws IOException {
        bytes.write2Byte(0);
        bytes.write2Byte(50);
        return;
    }

    /** schreibt den bytecode für den constantpool */
    @Methode
    public void writeConstantPool(ByteWriter bytes) throws IOException {
        this.constantPool.write(bytes);
        return;
    }

    /**
	 * schreibt magic code
	 */
    @Methode
    public void writeMagic(ByteWriter out) throws IOException {
        out.write2Byte(0xcafe);
        out.write2Byte(0xbabe);
        return;
    }

    /** returns the constantPool */
    @Methode
    public ConstantPool getConstantPool() {
        return this.constantPool;
    }
}
