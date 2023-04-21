package bsh.org.objectweb.asm.commons;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import bsh.org.objectweb.asm.ClassAdapter;
import bsh.org.objectweb.asm.ClassVisitor;
import bsh.org.objectweb.asm.FieldVisitor;
import bsh.org.objectweb.asm.MethodVisitor;
import bsh.org.objectweb.asm.Opcodes;

/**
 * A {@link ClassAdapter} that adds a serial version unique identifier to a
 * class if missing. Here is typical usage of this class:
 * 
 * <pre>
 *   ClassWriter cw = new ClassWriter(...);
 *   ClassVisitor sv = new SerialVersionUIDAdder(cw);
 *   ClassVisitor ca = new MyClassAdapter(sv);
 *   new ClassReader(orginalClass).accept(ca, false);
 * </pre>
 * 
 * The SVUID algorithm can be found <a href=
 * "http://java.sun.com/j2se/1.4.2/docs/guide/serialization/spec/class.html"
 * >http://java.sun.com/j2se/1.4.2/docs/guide/serialization/spec/class.html</a>:
 * 
 * <pre>
 * The serialVersionUID is computed using the signature of a stream of bytes
 * that reflect the class definition. The National Institute of Standards and
 * Technology (NIST) Secure Hash Algorithm (SHA-1) is used to compute a
 * signature for the stream. The first two 32-bit quantities are used to form a
 * 64-bit hash. A java.lang.DataOutputStream is used to convert primitive data
 * types to a sequence of bytes. The values input to the stream are defined by
 * the Java Virtual Machine (VM) specification for classes.
 *
 * The sequence of items in the stream is as follows:
 *
 * 1. The class name written using UTF encoding.
 * 2. The class modifiers written as a 32-bit integer.
 * 3. The name of each interface sorted by name written using UTF encoding.
 * 4. For each field of the class sorted by field name (except private static
 * and private transient fields):
 * 1. The name of the field in UTF encoding.
 * 2. The modifiers of the field written as a 32-bit integer.
 * 3. The descriptor of the field in UTF encoding
 * 5. If a class initializer exists, write out the following:
 * 1. The name of the method, &lt;clinit&gt;, in UTF encoding.
 * 2. The modifier of the method, java.lang.reflect.Modifier.STATIC,
 * written as a 32-bit integer.
 * 3. The descriptor of the method, ()V, in UTF encoding.
 * 6. For each non-private constructor sorted by method name and signature:
 * 1. The name of the method, &lt;init&gt;, in UTF encoding.
 * 2. The modifiers of the method written as a 32-bit integer.
 * 3. The descriptor of the method in UTF encoding.
 * 7. For each non-private method sorted by method name and signature:
 * 1. The name of the method in UTF encoding.
 * 2. The modifiers of the method written as a 32-bit integer.
 * 3. The descriptor of the method in UTF encoding.
 * 8. The SHA-1 algorithm is executed on the stream of bytes produced by
 * DataOutputStream and produces five 32-bit values sha[0..4].
 *
 * 9. The hash value is assembled from the first and second 32-bit values of 
 * the SHA-1 message digest. If the result of the message digest, the five
 * 32-bit words H0 H1 H2 H3 H4, is in an array of five int values named 
 * sha, the hash value would be computed as follows:
 *
 * long hash = ((sha[0] &gt;&gt;&gt; 24) &amp; 0xFF) |
 * ((sha[0] &gt;&gt;&gt; 16) &amp; 0xFF) &lt;&lt; 8 |
 * ((sha[0] &gt;&gt;&gt; 8) &amp; 0xFF) &lt;&lt; 16 |
 * ((sha[0] &gt;&gt;&gt; 0) &amp; 0xFF) &lt;&lt; 24 |
 * ((sha[1] &gt;&gt;&gt; 24) &amp; 0xFF) &lt;&lt; 32 |
 * ((sha[1] &gt;&gt;&gt; 16) &amp; 0xFF) &lt;&lt; 40 |
 * ((sha[1] &gt;&gt;&gt; 8) &amp; 0xFF) &lt;&lt; 48 |
 * ((sha[1] &gt;&gt;&gt; 0) &amp; 0xFF) &lt;&lt; 56;
 * </pre>
 * 
 * @author Rajendra Inamdar, Vishal Vishnoi
 */
public class SerialVersionUIDAdder extends ClassAdapter {

    /**
     * Flag that indicates if we need to compute SVUID.
     */
    protected boolean computeSVUID;

    /**
     * Set to true if the class already has SVUID.
     */
    protected boolean hasSVUID;

    /**
     * Classes access flags.
     */
    protected int access;

    /**
     * Internal name of the class
     */
    protected String name;

    /**
     * Interfaces implemented by the class.
     */
    protected String[] interfaces;

    /**
     * Collection of fields. (except private static and private transient
     * fields)
     */
    protected Collection svuidFields;

    /**
     * Set to true if the class has static initializer.
     */
    protected boolean hasStaticInitializer;

    /**
     * Collection of non-private constructors.
     */
    protected Collection svuidConstructors;

    /**
     * Collection of non-private methods.
     */
    protected Collection svuidMethods;

    /**
     * Creates a new {@link SerialVersionUIDAdder}.
     * 
     * @param cv a {@link ClassVisitor} to which this visitor will delegate
     *        calls.
     */
    public SerialVersionUIDAdder(final ClassVisitor cv) {
        super(cv);
        svuidFields = new ArrayList();
        svuidConstructors = new ArrayList();
        svuidMethods = new ArrayList();
    }

    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        computeSVUID = (access & Opcodes.ACC_INTERFACE) == 0;
        if (computeSVUID) {
            this.name = name;
            this.access = access;
            this.interfaces = interfaces;
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        if (computeSVUID) {
            if ("<clinit>".equals(name)) {
                hasStaticInitializer = true;
            }
            int mods = access & (Opcodes.ACC_PUBLIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_SYNCHRONIZED | Opcodes.ACC_NATIVE | Opcodes.ACC_ABSTRACT | Opcodes.ACC_STRICT);
            if ((access & Opcodes.ACC_PRIVATE) == 0) {
                if ("<init>".equals(name)) {
                    svuidConstructors.add(new Item(name, mods, desc));
                } else if (!"<clinit>".equals(name)) {
                    svuidMethods.add(new Item(name, mods, desc));
                }
            }
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);
    }

    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        if (computeSVUID) {
            if ("serialVersionUID".equals(name)) {
                computeSVUID = false;
                hasSVUID = true;
            }
            int mods = access & (Opcodes.ACC_PUBLIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_VOLATILE | Opcodes.ACC_TRANSIENT);
            if ((access & Opcodes.ACC_PRIVATE) == 0 || (access & (Opcodes.ACC_STATIC | Opcodes.ACC_TRANSIENT)) == 0) {
                svuidFields.add(new Item(name, mods, desc));
            }
        }
        return super.visitField(access, name, desc, signature, value);
    }

    public void visitEnd() {
        if (computeSVUID && !hasSVUID) {
            try {
                cv.visitField(Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "serialVersionUID", "J", null, new Long(computeSVUID()));
            } catch (Throwable e) {
                throw new RuntimeException("Error while computing SVUID for " + name, e);
            }
        }
        super.visitEnd();
    }

    /**
     * Returns the value of SVUID if the class doesn't have one already. Please
     * note that 0 is returned if the class already has SVUID, thus use
     * <code>isHasSVUID</code> to determine if the class already had an SVUID.
     * 
     * @return Returns the serial version UID
     */
    protected long computeSVUID() throws IOException {
        ByteArrayOutputStream bos;
        DataOutputStream dos = null;
        long svuid = 0;
        try {
            bos = new ByteArrayOutputStream();
            dos = new DataOutputStream(bos);
            dos.writeUTF(name.replace('/', '.'));
            dos.writeInt(access & (Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL | Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT));
            Arrays.sort(interfaces);
            for (int i = 0; i < interfaces.length; i++) {
                dos.writeUTF(interfaces[i].replace('/', '.'));
            }
            writeItems(svuidFields, dos, false);
            if (hasStaticInitializer) {
                dos.writeUTF("<clinit>");
                dos.writeInt(Opcodes.ACC_STATIC);
                dos.writeUTF("()V");
            }
            writeItems(svuidConstructors, dos, true);
            writeItems(svuidMethods, dos, true);
            dos.flush();
            byte[] hashBytes = computeSHAdigest(bos.toByteArray());
            for (int i = Math.min(hashBytes.length, 8) - 1; i >= 0; i--) {
                svuid = (svuid << 8) | (hashBytes[i] & 0xFF);
            }
        } finally {
            if (dos != null) {
                dos.close();
            }
        }
        return svuid;
    }

    /**
     * Returns the SHA-1 message digest of the given value.
     * 
     * @param value the value whose SHA message digest must be computed.
     * @return the SHA-1 message digest of the given value.
     */
    protected byte[] computeSHAdigest(final byte[] value) {
        try {
            return MessageDigest.getInstance("SHA").digest(value);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.toString());
        }
    }

    /**
     * Sorts the items in the collection and writes it to the data output stream
     * 
     * @param itemCollection collection of items
     * @param dos a <code>DataOutputStream</code> value
     * @param dotted a <code>boolean</code> value
     * @exception IOException if an error occurs
     */
    private static void writeItems(final Collection itemCollection, final DataOutput dos, final boolean dotted) throws IOException {
        int size = itemCollection.size();
        Item[] items = (Item[]) itemCollection.toArray(new Item[size]);
        Arrays.sort(items);
        for (int i = 0; i < size; i++) {
            dos.writeUTF(items[i].name);
            dos.writeInt(items[i].access);
            dos.writeUTF(dotted ? items[i].desc.replace('/', '.') : items[i].desc);
        }
    }

    static class Item implements Comparable {

        final String name;

        final int access;

        final String desc;

        Item(final String name, final int access, final String desc) {
            this.name = name;
            this.access = access;
            this.desc = desc;
        }

        public int compareTo(final Object o) {
            Item other = (Item) o;
            int retVal = name.compareTo(other.name);
            if (retVal == 0) {
                retVal = desc.compareTo(other.desc);
            }
            return retVal;
        }
    }
}
