package oracle.toplink.libraries.asm;

/**
 * An empty {@link ClassVisitor ClassVisitor} that delegates to another {@link
 * ClassVisitor ClassVisitor}. This class can be used as a super class to
 * quickly implement usefull class adapter classes, just by overriding the
 * necessary methods.
 * 
 * @author Eric Bruneton
 */
public class ClassAdapter implements ClassVisitor {

    /**
   * The {@link ClassVisitor ClassVisitor} to which this adapter delegates
   * calls.
   */
    protected ClassVisitor cv;

    /**
   * Constructs a new {@link ClassAdapter ClassAdapter} object.
   *
   * @param cv the class visitor to which this adapter must delegate calls.
   */
    public ClassAdapter(final ClassVisitor cv) {
        this.cv = cv;
    }

    public void visit(final int version, final int access, final String name, final String superName, final String[] interfaces, final String sourceFile) {
        cv.visit(version, access, name, superName, interfaces, sourceFile);
    }

    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        cv.visitInnerClass(name, outerName, innerName, access);
    }

    public void visitField(final int access, final String name, final String desc, final Object value, final Attribute attrs) {
        cv.visitField(access, name, desc, value, attrs);
    }

    public CodeVisitor visitMethod(final int access, final String name, final String desc, final String[] exceptions, final Attribute attrs) {
        return new CodeAdapter(cv.visitMethod(access, name, desc, exceptions, attrs));
    }

    public void visitAttribute(final Attribute attr) {
        cv.visitAttribute(attr);
    }

    public void visitEnd() {
        cv.visitEnd();
    }
}
