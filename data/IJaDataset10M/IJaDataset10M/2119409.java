package org.jmlspecs.eclipse.jmlchecker;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.jmlspecs.eclipse.jmldom.ASTNode;
import org.jmlspecs.eclipse.jmldom.AnnotationTypeDeclaration;
import org.jmlspecs.eclipse.jmldom.EnumDeclaration;
import org.jmlspecs.eclipse.jmldom.TypeDeclaration;
import org.jmlspecs.eclipse.util.Log;

/**
 * Instances of this class constitute 'type' information within the
 * JML Abstract Syntax Trees.  There is a unique instance of TypeInfo
 * for a given type, managed by the database of type information in the
 * Types class.  COnsequently you should not instantiate instances of
 * this class yourself, but obtain new instances through the methods of
 * the Types class.
 * <p>
 * Eclipse provides ITypeBinding instances to contain type information about 
 * nodes in an AST.  Unfortunately that class is not entirely adequate
 * for the task at hand for these reasons:
 * <UL>
 * <LI> When Java code is parsed and an AST built by Eclipse, ITypeBinding
 * information is available for each node.  But no other ITypeBinding
 * instances can be created.  When parsing and typechecking the various
 * JML constructs associated with a Java file, one needs to create new types.
 * In particular there is no way to (a) get the type binding for a primitive
 * type, (b) create a type binding for an array of a given type, (c) get
 * the type binding for a component of a given type, (d) get the type binding
 * of a fully qualified class that was not part of the Java code.
 * <LI> The type binding information only represents the Java information and
 * cannot be augmented with JML members.
 * </UL>
 * <p>
 * We solve the problem of not being able to create new ITypeBindings by
 * the following hack: create a little compilation unit that contains a
 * field of the type in question, generate the type-checked AST for it,
 * and obtain the binding from the AST.  (FIXME - I don't actually know if 
 * bindings created in different calls of createAST are compatible).  
 * One drawback of this approach is that it requires that a parser be
 * available, which means that an Eclipse workspace must be active.
 * This is not the case for all of the JUnit test environments;
 * consequently some kinds of type checking are not active unless the
 * test is run as a Plug-in Test.
 * <p>
 * We solve the issue of having JML information available by keeping a
 * connection between the AST for a type and the type binding for a type -
 * both are pieces of the TypeInfo structure.  Consequently we use the
 * type info structure as the official type.
 * <p>
 * The TypeInfo structure can be in a few different states:
 * <UL>
 * <LI> name only - this is a place holder type.  No declaration or
 * type binding information is available.  This is the case for binary
 * types when no workspace is available and for items such as OK and
 * ERROR that are not true types.
 * <LI> name + type binding - types generated from binary information,
 * or types that have been referenced (and consequently a TypeInfo
 * structure created), but the AST declaring the type has not yet been
 * encountered (when it is the AbstractTypeDeclaration information will
 * be inserted).
 * <LI> name + type declaration + type binding - types generated from
 * source code
 * </UL>
 * <p>
 * FIXME: Model types are not yet handled.
 */
public class TypeInfo {

    /** These enums record the phases of the compilation and checking
   * of various compilation units.  The Java compilation and checking
   * are done by the Eclipse infrastructure.  Note that in order to 
   * generate a JML-signature of a type we do need to parse it in order
   * to JML-parse the comments to generate the signature.  However, we
   * only need to find other Java and JML types, we do not need to 
   * generate their signatures.  In order to do a JML_typechecking of an
   * implementation we may need other JML-signatures, but we do not need 
   * other modules to be JML-typechecked.  Note that compareTo can be used
   * to test the order of the types
   * <P>
   * NONE - not initialized or not a class/interface type 
   * <P>
   * TYPE_ONLY - defined: itype, cuinfo, packageName, publicName, superclass, interfaces
   * <P>
   * JML_SIGNATURE_ONLY - defined: JML signatures (e.g. additional fields and
   *    methods and types) and specs for this type and all of its members
   * <P>
   * TYPECHECKED - have completed the typechecking of the initializers of all
   *    fields and the implementations of all methods and the class initializers
   * 
   */
    public static enum State {

        /** Not initialized */
        NONE, /** Only defined: itype, cuinfo, packageName, publicName, superclass, interfaces */
        TYPE_ONLY, /** JML signatures and specs are defined */
        JML_SIGNATURE_ONLY, /** implementations of JML members are checked */
        TYPECHECKED
    }

    ;

    public CUInfo cuinfo;

    /**
   * This value is used for constructs that do not have a type (e.g.
   * a statement); it indicates that the content of the AST node has
   * been typechecked satisfactorily.
   */
    public static final TypeInfo OK = new TypeInfo("$$$ok", "$$$ok");

    /**
   * This TypeInfo value indicates that a type error has occurred and
   * been reported; it is used to represent an unknown or erroneous
   * type value.  When ERROR is the type of a component of an AST node,
   * no additional error message should be reported, since one should have
   * already been reported.
   */
    public static final TypeInfo ERROR = new TypeInfo("$$$error", "$$$error");

    /**
   * The binary name of the type.  
   * The binary name is used rather than the more convenient qualified name
   * because anonymous and local types do not have conventional qualified names.
   * (FIXME - what about type parameters).
   */
    private String lookupName;

    /** Returns the binary name of the type, also used as the lookup key.
   * 
   * @return the binary name of the type
   */
    public String getLookupName() {
        return lookupName;
    }

    /** Lazily cached value of the package name */
    private String packageName = null;

    /** Returns the package name with / delimiters
   * @return the package name
   */
    public String packageName() {
        if (packageName == null) {
            if (lookupName.charAt(0) == 'L' && lookupName.charAt(lookupName.length() - 1) == ';') {
                int index = lookupName.lastIndexOf('/');
                packageName = index == -1 ? "" : lookupName.substring(1, index);
            } else {
                Log.log("UNIMPLEMENTED CASE in TypeInfo.packageName()");
            }
        }
        return packageName;
    }

    private String typeName = null;

    public String typeName() {
        if (typeName == null) {
            int index = lookupName.lastIndexOf('/') + 1;
            typeName = lookupName.substring(index, lookupName.length() - 1);
        }
        return typeName;
    }

    /**
   * A name used for display (such as in error messages).
   */
    private String publicName;

    /**
   * The AbstractTypeDeclaration node containing the declaration of the
   * type, if it is a JML AST.  (FIXME - what about TypeParameter).
   */
    public ASTNode typedecl;

    /**
   * The AbstractTypeDeclaration node containing the declaration of the
   * type, if it is an Eclipse AST.  (FIXME - what about TypeParameter).
   */
    public org.eclipse.jdt.core.dom.ASTNode typeDeclE;

    /**
   * The ITypeBinding for this type, as provided by Eclipse.
   */
    public ITypeBinding typeBinding;

    /** The type of the superclass, if any */
    public TypeInfo superclass = null;

    /** A list of the types of the interfaces, if any */
    public List<TypeInfo> interfaces = null;

    public IType itype = null;

    public State state = State.NONE;

    /**
   * The textual (public) name of the type.
   */
    public String toString() {
        return publicName;
    }

    public TypeInfo(IType itype) {
        this.itype = itype;
        this.lookupName = itype.getKey();
        this.publicName = itype.getFullyQualifiedName();
    }

    /** Creates a TypeInfo structure with just a name.
   * @param publicName the (fully qualified) name by which the type is known in a program
   * @param lookupName the unique name by which it is known in the database
   */
    public TypeInfo(String publicName, String lookupName) {
        this.publicName = publicName;
        this.lookupName = lookupName;
    }

    public TypeInfo(String publicName, String lookupName, ITypeBinding typeBinding) {
        this.lookupName = lookupName;
        this.publicName = publicName;
        this.typeBinding = typeBinding;
    }

    /** Returns true if the type is a numeric primitive type.
   * @return true if the type is a numeric primitive type
   */
    public boolean isNumeric() {
        String s = publicName;
        return isIntegral() || s == "float" || s == "double" || s == "org.jmlspecs.lang.Real";
    }

    /** Returns true if the type is an integral primitive type.
   * @return true if the type is an integral primitive type
   */
    public boolean isIntegral() {
        String s = publicName;
        return s == "int" || s == "short" || s == "long" || s == "byte" || s == "char" || s == "java.math.BigInteger";
    }

    /** Returns true if the type is a primitive type.
   * @return true if the type is a primitive type
   */
    public boolean isPrimitive() {
        if (typeBinding != null) return typeBinding.isPrimitive();
        String s = publicName;
        return s == "boolean" || s == "void" || isNumeric();
    }

    /** Returns true if the type is a reference but not array type.
   * @return true if the type is a reference but not array type
   */
    public boolean isReferenceNotArray() {
        if (typeBinding != null) return !(typeBinding.isPrimitive() || typeBinding.isArray());
        return !(isPrimitive() || isArray());
    }

    /** Returns true if the type is a reference or array type.
   * @return true if the type is a reference or array type
   */
    public boolean isReferenceOrArray() {
        if (typeBinding != null) return !(typeBinding.isPrimitive());
        return !isPrimitive();
    }

    /** Returns true if the type is an interface, including annotations.
   * @return true if the type is an interface or annotation
   */
    public boolean isInterface() {
        if (typeBinding != null) return typeBinding.isInterface();
        if (isPrimitive() || isArray()) return false;
        if (typedecl instanceof EnumDeclaration) return false;
        if (typedecl instanceof AnnotationTypeDeclaration) return true;
        return ((TypeDeclaration) typedecl).isInterface();
    }

    /** Returns true if the type is a class, including enums and arrays
   * @return true if the type is a class
   */
    public boolean isClass() {
        if (typeBinding != null) return typeBinding.isClass();
        if (isPrimitive()) return false;
        if (isArray()) return true;
        if (typedecl instanceof EnumDeclaration) return true;
        if (typedecl instanceof AnnotationTypeDeclaration) return false;
        if (typedecl != null) return !((TypeDeclaration) typedecl).isInterface();
        return false;
    }

    /** Returns true if the type is an array type.
   * @return true if the type is an array type
   */
    public boolean isArray() {
        if (typeBinding != null) return typeBinding.isArray();
        return publicName.endsWith("]");
    }
}
