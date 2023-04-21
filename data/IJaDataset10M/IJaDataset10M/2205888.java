package org.flexharmony.harmonizer.javadef;

import static org.flexharmony.harmonizer.utils.CommonMethods.*;
import java.util.ArrayList;
import java.util.List;
import org.flexharmony.ASClass;
import org.flexharmony.harmonizer.utils.Pair;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Converts the ASM class bytecode representation into a {@link JavaClassDefinition}.
 * 
 * @author Corey Baswell
 */
public class JavaClassVisitor implements ClassVisitor, AnnotationVisitor {

    private JavaType classType;

    /**
   * If this class has an {@link ASClass} annotation its values will be captured here.
   */
    private ASClassImpl asClass;

    /**
   * A {@link JavaFieldVisitor} is created for each field in this class and at the
   * end of the class visiting process ({@link #visitEnd()}) each field visitor
   * will return a {@link JavaFieldDefinition}.
   */
    private List<JavaFieldVisitor> fieldVisitors = new ArrayList<JavaFieldVisitor>();

    /**
   * Don't need much data related to the methods (just the signature) so these
   * structures are created by this class and not a separate visitor.
   */
    private List<JavaMethodSignature> methodDefs = new ArrayList<JavaMethodSignature>();

    private JavaClassDefinition classDef;

    public JavaClassVisitor() {
    }

    /**
   * 
   * @return The converted class definition from the bytecode representation.
   */
    public JavaClassDefinition getClassDef() {
        return classDef;
    }

    /**
   * @see ClassVisitor#visit(int, int, String, String, String, String[])
   */
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if ((Opcodes.ACC_PUBLIC & access) != 0) {
            classType = new JavaType(name, superName);
        }
    }

    /**
   * @see ClassVisitor#visitAnnotation(String, boolean)
   */
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        String name = convertByteCodeTypeName(desc);
        if (ASClass.class.getCanonicalName().equals(name)) {
            asClass = new ASClassImpl();
            return this;
        } else {
            return null;
        }
    }

    /**
   * @see ClassVisitor#visitField(int, String, String, String, Object)
   */
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        JavaFieldVisitor fieldVisitor = new JavaFieldVisitor(access, name, desc, value);
        fieldVisitors.add(fieldVisitor);
        return fieldVisitor;
    }

    /**
   * @see ClassVisitor#visitEnd()
   */
    public void visitEnd() {
        List<JavaFieldDefinition> fieldDefs = new ArrayList<JavaFieldDefinition>();
        for (JavaFieldVisitor fieldVisitor : fieldVisitors) {
            fieldDefs.add(fieldVisitor.getFieldDefinition());
        }
        classDef = new JavaClassDefinition(classType, asClass, fieldDefs, methodDefs);
    }

    /**
   * @see ClassVisitor#visitMethod(int, String, String, String, String[])
   */
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        JavaVisibility visibility = getVisiblity(access);
        Pair<List<JavaType>, JavaType> pair = parseMethodDescription(desc);
        JavaMethodSignature methodDef = new JavaMethodSignature(visibility, name, pair.getX(), pair.getY());
        methodDefs.add(methodDef);
        return null;
    }

    /**
   * @see ClassVisitor#visitAttribute(Attribute)
   */
    public void visitAttribute(Attribute attr) {
    }

    /**
   * @see ClassVisitor#visitInnerClass(String, String, String, int)
   */
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    /**
   * @see ClassVisitor#visitOuterClass(String, String, String)
   */
    public void visitOuterClass(String owner, String name, String desc) {
    }

    /**
   * @see ClassVisitor#visitSource(String, String)
   */
    public void visitSource(String source, String debug) {
    }

    /**
   * @see AnnotationVisitor#visit(String, Object)
   */
    public void visit(String name, Object value) {
        if (name.equals("syncAllFields")) {
            asClass.syncAllFields = Boolean.parseBoolean(value.toString());
        }
    }

    /**
   * @see AnnotationVisitor#visitAnnotation(String, String)
   */
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        return null;
    }

    /**
   * @see AnnotationVisitor#visitArray(String)
   */
    public AnnotationVisitor visitArray(String name) {
        return null;
    }

    /**
   * @see AnnotationVisitor#visitEnum(String, String, String)
   */
    public void visitEnum(String name, String desc, String value) {
    }

    /**
   * Parsers the given method bytecode description into its parameter and return
   * types.
   */
    Pair<List<JavaType>, JavaType> parseMethodDescription(String methodDescription) {
        List<JavaType> parameterTypes = new ArrayList<JavaType>();
        JavaType returnType = null;
        int endIndex = methodDescription.indexOf(')');
        if (endIndex > 1) {
            String parameters = methodDescription.substring(1, endIndex);
            String[] paramValues = parameters.split(";");
            for (String paramValue : paramValues) {
                parameterTypes.add(new JavaType(paramValue));
            }
        }
        String returnVal = methodDescription.substring((endIndex + 1), methodDescription.length());
        if (!returnVal.equals("V")) {
            returnType = new JavaType(returnVal);
        }
        return new Pair<List<JavaType>, JavaType>(parameterTypes, returnType);
    }
}
