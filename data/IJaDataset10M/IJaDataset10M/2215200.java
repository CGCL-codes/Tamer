package net.sf.jode.bytecode;

import net.sf.jode.GlobalOptions;
import net.sf.jode.util.UnifyHash;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.List;
import java.util.ArrayList;
import java.lang.Comparable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Represents a class or interface.  It can't be used for primitive
 * or array types.  Every class/interface is associated with a class
 * path, which is used to load the class and its dependent classes.
 *
 * <h3>ClassInfo and ClassPath</h3>
 *
 * Every ClassInfo instance belongs to a {@link ClassPath}.  This
 * class path is used to find the class and its dependent classes,
 * e.g. the super class.  Even if you want to create a class info from
 * the scratch you have to associate it with a class path, in which
 * the dependent classes are searched.
 *
 * For every class path and every class name there exists at most one
 * class info object with this class name.  The only exception is when
 * you overwrite a loaded class, e.g. by calling setName().
 *
 * <h3>Creating a Class</h3> 
 * As you can see, there is no public constructor.  Instead you create
 * a new ClassInfo, by calling {@link ClassPath#getClassInfo}.
 * Multiple calls of this method with the same class name result in
 * the same object.  The resulting ClassInfo is initially empty and
 * you now have three different means to fill it with informations:
 * You can {@link #load load} the class from its classpath (from which
 * it was created), you can {@link #guess guess} the information
 * (useful if the class can't be loaded), or you build it from scratch
 * by setting its contents with the various <code>setSomething</code>
 * methods.
 *
 * <h3>Changing a Class</h3> 
 * Whether or not the classinfo was already filled with information,
 * you can change it.  You can, for example, provide another array of
 * methods, change the modifiers, or rename the class.  Use the
 * various <code>setSomething</code> methods.
 *
 * <h3>The Components of a Class</h3>
 * A class consists of several components:
 * <dl>
 * <dt>name</dt><dd>
 *   The name of the class.  The name is already set, when you create
 *   a new ClassInfo with getClassInfo.  If you change this name this
 *   has some consequences, read the description of the {@link
 *   #setName} method.
 * </dd>
 * <dt>class name</dt><dd>
 *   The short java name of this class, i.e. the name that appears
 *   behind the "class" keyword in the java source file.  While 
 *   <code>getClassName()</code> also works for package scope classes,
 *   setClassName() must only be called on inner classes and will not
 *   change the bytecode name.<br>
 *
 *   E.g.: The ClassName of <code>java.util.Map$Entry</code> is
 *   <code>Entry</code>.  If you change its ClassName to
 *   <code>Yrtne</code> and save it, it will still be in a file called
 *   <code>Map$Entry.class</code>, but a debugger would call it
 *   <code>java.util.Map.Yrtne</code>.  Note that you should also save
 *   <code>Map</code>, because it also has a reference to the
 *   ClassName.
 * </dd>
 * <dt>modifiers</dt><dd>
 *   There is a set of access modifiers (AKA access flags) attached to
 *   each class.  They are represented as integers (bitboard) and can
 *   be conveniently accessed via {@link java.lang.reflect.Modifier}.
 *   <br>
 *
 *   Inner classes can have more modifiers than normal classes, as
 *   they can be private, protected or static.  These extended modifiers
 *   are supported, too. <br>
 *
 *   <b>TODO:</b> Check that reflection returns the extended modifiers!
 * </dd>
 * <dt>superclass</dt><dd>
 *   Every class except <code>java.lang.Object</code> has a super
 *   class.  The super class is created in the same classpath as the
 *   current class.  Interfaces always have
 *   <code>java.lang.Object</code> as their super class.
 * </dd>
 * <dt>interfaces</dt><dd>
 *   Every class (resp. interfaces) can implement (resp. extend) 
 *   zero or more interfaces.
 * </dd>
 * <dt>signature</dt><dd>The classes super class and interfaces with
 * template information.</dd>
 *
 * <dt>fields</dt><dd>
 *   Fields are represented as {@link FieldInfo} objects.
 * </dd>
 * <dt>methods</dt><dd>
 *   Methods are represented as {@link MethodInfo} objects.
 * </dd>
 * <dt>method scoped</dt><dd>
 *   A boolean value; true if this class is an anonymous or method
 *   scoped class.
 * </dd>
 * <dt>outer class</dt><dd>
 *   the class in which this class or interface was declared.  It
 *   returns null for package scoped and method scoped classes.
 * </dd>
 * <dt>classes</dt><dd>
 *   the inner classes declared in this class.  This doesn't include
 *   method scoped classes.
 * </dd>
 * <dt>source file</dt><dd>
 *   The name of source file.  The JVM uses this field when a stack
 *   trace is produced.  It may be null if the class was compiled
 *   without debugging information.
 * </dd>
 * </dl>
 *
 * <h3>Inner Classes</h3> 
 * Inner classes are supported as far as the information is present in
 * the bytecode.  However, you can always ignore this inner
 * information, and access inner classes by their bytecode name,
 * e.g. <code>java.util.Map$Entry</code>.  There are four different
 * types of classes:
 * <dl>
 * <dt>normal package scoped classes</dt><dd>
 *   A class is package scoped if, and only if
 *   {@link #getOuterClass()} returns <code>null</code> and
 *   {@link #isMethodScoped()} returns <code>false</code>.
 * </dd>
 * <dt>class scoped classes (inner classes)</dt><dd>
 *   A class is class scoped if, and only if
 *   {@link #getOuterClass()} returns not <code>null</code>.
 *
 *   The bytecode name ({@link #getName()}) of an inner class is
 *   in normally of the form <code>Package.Outer$Inner</code>.  However,
 *   ClassInfo also supports differently named classes, as long as the
 *   InnerClass attribute is present.  The method
 *   {@link #getClassName()} returns the name of the inner class
 *   (<code>Inner</code> in the above example).
 *
 *   You can get all inner classes of a class with the
 *   method {@link #getClasses}.
 * </dd>
 * <dt>named method scoped classes</dt><dd>
 *   A class is a named method scoped class if, and only if
 *   {@link #isMethodScoped()} returns <code>true</code> and
 *   {@link #getClassName()} returns not <code>null</code>.  In
 *   that case {@link #getOuterClass()} returns <code>null</code>,
 *   too.<br><br>
 *
 *   The bytecode name ({@link #getName()}) of a method scoped class is
 *   normally of the form <code>Package.Outer$Number$Inner</code>.  However,
 *   ClassInfo also supports differently named classes, as long as the
 *   InnerClass attribute is present.  <br><br>
 *
 *   There's no way to get the method scoped classes of a method, except
 *   by analyzing its instructions.  And even that is error prone, since
 *   it could just be a method scoped class of an outer method.
 * </dd>
 * <dt>anonymous classes</dt><dd>
 *   A class is an anonymous class if, and only if
 *   {@link #isMethodScoped()} returns <code>true</code> and
 *   {@link #getClassName()} returns <code>null</code>.  In that
 *   case {@link #getOuterClass()} returns <code>null</code>,
 *   too.<br><br>
 *
 *   The bytecode name ({@link #getName()}) of a method scoped class
 *   is normally of the form <code>Package.Outer$Number</code>.
 *   However, ClassInfo also supports differently named classes, as
 *   long as the InnerClass attribute is present.  <br><br>
 *
 *   There's no way to get the anonymous classes of a method, except
 *   by analyzing its instructions.  And even that is error prone, since
 *   it could just be an anonymous class of an outer method.
 * </dd>
 * </dl>
 *
 * <hr>
 * <h3>Open Question</h3>
 *
 * I represent most types as {@link String} objects (type
 * signatures); this is convenient since java bytecode does the same.
 * On the other hand a class type should be represented as
 * {@link ClassInfo} object.  There is a method in {@link TypeSignature}
 * to convert between them, which needs a class path.  This is a
 * bit difficult to use.  <br>
 *
 * However the alternative would be to represents types as ClassInfo
 * and create ClassInfo objects for primitive and array types.  But
 * this contradicts the purpose of this class, which is to read and
 * write class files.  I think the current solution is okay.  <br>
 *
 * @author Jochen Hoenicke */
public final class ClassInfo extends BinaryInfo implements Comparable {

    private static ClassPath defaultClasspath;

    private int status = 0;

    private boolean modified = false;

    private boolean isGuessed = false;

    private ClassPath classpath;

    private int modifiers = -1;

    private boolean deprecatedFlag;

    private String name;

    private String className;

    private boolean methodScoped;

    private ClassInfo superclass;

    private ClassInfo outerClass;

    private ClassInfo[] interfaces;

    private ClassInfo[] innerClasses;

    private FieldInfo[] fields;

    private MethodInfo[] methods;

    private String sourceFile;

    private boolean hasInnerClassesAttr;

    /**
     * The type signature that also contains template information.
     */
    private String signature;

    private static final ClassInfo[] EMPTY_INNER = new ClassInfo[0];

    /** 
     * This constant can be used as parameter to drop.  It specifies
     * that no information at all should be kept for the current class.
     *
     * @see #load 
     */
    public static final int NONE = 0;

    /** 
     * This constant can be used as parameter to load.  It specifies
     * that at least the outer class information should be loaded,
     * i.e.  the outer class and the java class name.  It is the only
     * information that is loaded recursively: It is also
     * automatically loaded for all classes that are accessed by this
     * class.  The reason for the recursive load is simple: In java
     * bytecode a class contains the outer class information for all
     * classes that it accesses, so we can create this information
     * without the need to read the outer class.  We also need this
     * information when writing a class.
     *
     * @see #load 
     */
    public static final int OUTERCLASS = 5;

    /** 
     * This constant can be used as parameter to load.  It specifies
     * that at least the hierarchy information, i.e. the
     * superclass/interfaces fields and the modifiers 
     * of this class should be loaded.
     *
     * @see #load 
     */
    public static final int HIERARCHY = 10;

    /** 
     * This constant can be used as parameter to load.  It specifies
     * that all public fields, methods and inner class declarations
     * should be loaded.  It doesn't load method bodies.
     *
     * @see #load 
     */
    public static final int PUBLICDECLARATIONS = 20;

    /** 
     * This constant can be used as parameter to load.  It specifies
     * that all the fields, methods and inner class declaration
     * should be loaded.  It doesn't load method bodies.
     *
     * @see #load 
     */
    public static final int DECLARATIONS = 30;

    /** 
     * This constant can be used as parameter to load.  It specifies
     * that everything in the class except debugging information and
     * non-standard attributes should be loaded.
     *
     * @see #load 
     */
    public static final int NODEBUG = 80;

    /** 
     * This constant can be used as parameter to load.  It specifies
     * that everything in the class except non-standard attributes
     * should be loaded.
     *
     * @see #load 
     */
    public static final int ALMOSTALL = 90;

    /** 
     * This constant can be used as parameter to load.  It specifies
     * that everything in the class should be loaded.
     *
     * @see #load 
     */
    public static final int ALL = 100;

    /**
     * @deprecated
     */
    public static void setClassPath(String path) {
        setClassPath(new ClassPath(path));
    }

    /**
     * @deprecated
     */
    public static void setClassPath(ClassPath path) {
        defaultClasspath = path;
    }

    /**
     * @deprecated
     */
    public static boolean exists(String name) {
        return defaultClasspath.existsClass(name);
    }

    /**
     * @deprecated
     */
    public static boolean isPackage(String name) {
        return defaultClasspath.isDirectory(name.replace('.', '/'));
    }

    /**
     * @deprecated
     */
    public static Enumeration getClassesAndPackages(String packageName) {
        return defaultClasspath.listClassesAndPackages(packageName);
    }

    /**
     * @deprecated
     */
    public static ClassInfo forName(String name) {
        return defaultClasspath.getClassInfo(name);
    }

    /**
     * Disable the default constructor.
     * @exception InternalError always.
     */
    private ClassInfo() throws InternalError {
        throw new InternalError();
    }

    ClassInfo(String name, ClassPath classpath) {
        if (name != null) this.name = name.intern();
        this.classpath = classpath;
    }

    /**
     * Returns the classpath in which this class was created.
     */
    public ClassPath getClassPath() {
        return classpath;
    }

    /****** READING CLASS FILES ***************************************/
    private static int javaModifiersToBytecode(int javaModifiers) {
        int modifiers = javaModifiers & (Modifier.FINAL | 0x20 | Modifier.INTERFACE | Modifier.ABSTRACT);
        if ((javaModifiers & (Modifier.PUBLIC | Modifier.PROTECTED)) != 0) return Modifier.PUBLIC | modifiers; else return modifiers;
    }

    private void mergeModifiers(int newModifiers) throws ClassFormatException {
        if (modifiers == -1) {
            modifiers = newModifiers;
            return;
        }
        if (((modifiers ^ newModifiers) & ~0x20) == 0) {
            modifiers |= newModifiers;
            return;
        }
        int oldSimple = javaModifiersToBytecode(modifiers);
        if (((oldSimple ^ newModifiers) & 0xfdf) == 0) {
            modifiers |= newModifiers & 0x20;
            return;
        }
        int newSimple = javaModifiersToBytecode(newModifiers);
        if (((newSimple ^ modifiers) & 0xfdf) == 0) {
            modifiers = newModifiers | (modifiers & 0x20);
            return;
        }
        throw new ClassFormatException("modifiers in InnerClass info doesn't match: " + modifiers + "<->" + newModifiers);
    }

    private void mergeOuterInfo(String className, ClassInfo outer, int realModifiers, boolean ms) throws ClassFormatException {
        if (status >= OUTERCLASS) {
            if ((className == null ? this.className != null : !className.equals(this.className)) || this.outerClass != outer) {
                if (this.className == null && this.outerClass == null && (className != null || outer != null)) {
                    this.outerClass = outer;
                    this.className = className;
                    this.methodScoped = ms;
                } else if (className != null || outer != null) {
                    GlobalOptions.err.println("WARNING: Outer information mismatch " + name + ": " + className + "," + outer + "," + ms + "<->" + this.className + "," + this.outerClass + "," + this.methodScoped);
                }
            }
            if (realModifiers != -1) mergeModifiers(realModifiers);
        } else {
            if (realModifiers != -1) mergeModifiers(realModifiers);
            this.className = className;
            this.outerClass = outer;
            this.methodScoped = ms;
            this.status = OUTERCLASS;
        }
    }

    private void readInnerClassesAttribute(int length, ConstantPool cp, DataInputStream input) throws IOException {
        hasInnerClassesAttr = true;
        int count = input.readUnsignedShort();
        if (length != 2 + 8 * count) throw new ClassFormatException("InnerClasses attribute has wrong length");
        int innerCount = 0;
        ClassInfo[] innerCIs = new ClassInfo[count];
        for (int i = 0; i < count; i++) {
            int innerIndex = input.readUnsignedShort();
            int outerIndex = input.readUnsignedShort();
            int nameIndex = input.readUnsignedShort();
            String inner = cp.getClassName(innerIndex);
            String outer = outerIndex != 0 ? cp.getClassName(outerIndex) : null;
            String innername = nameIndex != 0 ? cp.getUTF8(nameIndex) : null;
            int access = input.readUnsignedShort();
            if (innername != null && innername.length() == 0) innername = null;
            if (innername == null) outer = null;
            if (outer != null && inner.length() > outer.length() + 2 + innername.length() && inner.startsWith(outer + "$") && inner.endsWith("$" + innername) && Character.isDigit(inner.charAt(outer.length() + 1))) outer = null;
            ClassInfo innerCI = classpath.getClassInfo(inner);
            ClassInfo outerCI = outer != null ? classpath.getClassInfo(outer) : null;
            innerCI.mergeOuterInfo(innername, outerCI, access, outerCI == null);
            if (outerCI == this) innerCIs[innerCount++] = innerCI;
        }
        if (innerCount > 0) {
            innerClasses = new ClassInfo[innerCount];
            System.arraycopy(innerCIs, 0, innerClasses, 0, innerCount);
        } else innerClasses = EMPTY_INNER;
    }

    protected void readAttribute(String name, int length, ConstantPool cp, DataInputStream input, int howMuch) throws IOException {
        if (howMuch >= ClassInfo.ALMOSTALL && name.equals("SourceFile")) {
            if (length != 2) throw new ClassFormatException("SourceFile attribute" + " has wrong length");
            sourceFile = cp.getUTF8(input.readUnsignedShort());
        } else if (howMuch >= ClassInfo.OUTERCLASS && name.equals("InnerClasses")) {
            readInnerClassesAttribute(length, cp, input);
        } else if (name.equals("Signature")) {
            signature = cp.getUTF8(input.readUnsignedShort());
        } else if (name.equals("Deprecated")) {
            deprecatedFlag = true;
            if (length != 0) throw new ClassFormatException("Deprecated attribute has wrong length");
        } else super.readAttribute(name, length, cp, input, howMuch);
    }

    void loadFromReflection(Class clazz, int howMuch) throws SecurityException, ClassFormatException {
        if (howMuch >= OUTERCLASS) {
            Class declarer = clazz.getDeclaringClass();
            if (declarer != null) {
                int dollar = name.lastIndexOf('$');
                className = name.substring(dollar + 1);
                outerClass = classpath.getClassInfo(declarer.getName());
                if (outerClass.status < OUTERCLASS) outerClass.loadFromReflection(declarer, OUTERCLASS);
            } else {
                int dollar = name.lastIndexOf('$');
                if (dollar >= 0 && Character.isDigit(name.charAt(dollar + 1))) {
                    className = null;
                    outerClass = null;
                    methodScoped = true;
                } else {
                    int dollar2 = name.lastIndexOf('$', dollar);
                    if (dollar2 >= 0 && Character.isDigit(name.charAt(dollar2 + 1))) {
                        className = name.substring(dollar + 1);
                        outerClass = null;
                        methodScoped = true;
                    }
                }
            }
        }
        if (howMuch >= HIERARCHY) {
            modifiers = clazz.getModifiers();
            if (clazz.getSuperclass() == null) superclass = clazz == Object.class ? null : classpath.getClassInfo("java.lang.Object"); else superclass = classpath.getClassInfo(clazz.getSuperclass().getName());
            Class[] ifaces = clazz.getInterfaces();
            interfaces = new ClassInfo[ifaces.length];
            for (int i = 0; i < ifaces.length; i++) interfaces[i] = classpath.getClassInfo(ifaces[i].getName());
        }
        if (howMuch >= PUBLICDECLARATIONS) {
            Field[] fs;
            Method[] ms;
            Constructor[] cs;
            Class[] is;
            if (howMuch == PUBLICDECLARATIONS) {
                fs = clazz.getFields();
                ms = clazz.getMethods();
                cs = clazz.getConstructors();
                is = clazz.getClasses();
            } else {
                fs = clazz.getDeclaredFields();
                ms = clazz.getDeclaredMethods();
                cs = clazz.getDeclaredConstructors();
                is = clazz.getDeclaredClasses();
            }
            int len = 0;
            for (int i = fs.length; --i >= 0; ) {
                if (fs[i].getDeclaringClass() == clazz) len++;
            }
            int fieldPtr = len;
            fields = new FieldInfo[len];
            for (int i = fs.length; --i >= 0; ) {
                if (fs[i].getDeclaringClass() == clazz) {
                    String type = TypeSignature.getSignature(fs[i].getType());
                    fields[--fieldPtr] = new FieldInfo(fs[i].getName(), type, fs[i].getModifiers());
                }
            }
            len = cs.length;
            for (int i = ms.length; --i >= 0; ) {
                if (ms[i].getDeclaringClass() == clazz) len++;
            }
            methods = new MethodInfo[len];
            int methodPtr = len;
            for (int i = ms.length; --i >= 0; ) {
                if (ms[i].getDeclaringClass() == clazz) {
                    String type = TypeSignature.getSignature(ms[i].getParameterTypes(), ms[i].getReturnType());
                    methods[--methodPtr] = new MethodInfo(ms[i].getName(), type, ms[i].getModifiers());
                }
            }
            for (int i = cs.length; --i >= 0; ) {
                String type = TypeSignature.getSignature(cs[i].getParameterTypes(), void.class);
                methods[--methodPtr] = new MethodInfo("<init>", type, cs[i].getModifiers());
            }
            if (is.length > 0) {
                innerClasses = new ClassInfo[is.length];
                for (int i = is.length; --i >= 0; ) {
                    innerClasses[i] = classpath.getClassInfo(is[i].getName());
                    if (innerClasses[i].status < OUTERCLASS) innerClasses[i].loadFromReflection(is[i], OUTERCLASS);
                }
            } else innerClasses = EMPTY_INNER;
        }
        status = howMuch;
    }

    /**
     * Reads a class file from a data input stream.  Normally you should
     * <code>load</code> a class from its classpath instead.  This may
     * be useful for special kinds of input streams, that ClassPath 
     * doesn't handle.
     *
     * @param input The input stream, containing the class in standard
     *              bytecode format.
     * @param howMuch The amount of information that should be read in, one
     *                of HIERARCHY, PUBLICDECLARATIONS, DECLARATIONS or ALL.
     * @exception ClassFormatException if the file doesn't denote a valid
     * class.  
     * @exception IOException if input throws an exception.
     * @exception IllegalStateException if this ClassInfo was modified.
     * @see #load
     */
    public void read(DataInputStream input, int howMuch) throws IOException {
        if (modified) throw new IllegalStateException(name);
        if (status >= howMuch) return;
        if (howMuch <= DECLARATIONS) howMuch = DECLARATIONS;
        if (input.readInt() != 0xcafebabe) throw new ClassFormatException("Wrong magic");
        int version = input.readUnsignedShort();
        version |= input.readUnsignedShort() << 16;
        if (version < (45 << 16 | 0)) throw new ClassFormatException("Wrong class version");
        ConstantPool cpool = new ConstantPool();
        cpool.read(input);
        modifiers = input.readUnsignedShort();
        String className = cpool.getClassName(input.readUnsignedShort());
        if (name == null) name = className; else if (!name.equals(className)) throw new ClassFormatException("wrong name " + className);
        int superID = input.readUnsignedShort();
        superclass = superID == 0 ? null : classpath.getClassInfo(cpool.getClassName(superID));
        int count = input.readUnsignedShort();
        interfaces = new ClassInfo[count];
        for (int i = 0; i < count; i++) {
            interfaces[i] = classpath.getClassInfo(cpool.getClassName(input.readUnsignedShort()));
        }
        count = input.readUnsignedShort();
        fields = new FieldInfo[count];
        for (int i = 0; i < count; i++) {
            fields[i] = new FieldInfo();
            fields[i].read(cpool, input, howMuch);
        }
        count = input.readUnsignedShort();
        methods = new MethodInfo[count];
        for (int i = 0; i < count; i++) {
            methods[i] = new MethodInfo();
            methods[i].read(cpool, input, howMuch);
        }
        innerClasses = EMPTY_INNER;
        readAttributes(cpool, input, howMuch);
        Iterator iter = cpool.iterateClassNames();
        while (iter.hasNext()) {
            ClassInfo ci = classpath.getClassInfo((String) iter.next());
            if (ci.status < OUTERCLASS) ci.mergeOuterInfo(null, null, -1, false);
        }
        status = howMuch;
    }

    /**
     * Reserves constant pool entries for String, Integer and Float
     * constants needed by the bytecode.  These constants should have
     * small constant pool indices so that a ldc instead of a ldc_w
     * bytecode can be used.
     */
    private void reserveSmallConstants(GrowableConstantPool gcp) {
        for (int i = 0; i < fields.length; i++) fields[i].reserveSmallConstants(gcp);
        for (int i = 0; i < methods.length; i++) methods[i].reserveSmallConstants(gcp);
    }

    /**
     * Reserves all constant pool entries needed by this class.  This
     * is necessary, because the constant pool is the first thing 
     * written to the class file.
     */
    private void prepareWriting(GrowableConstantPool gcp) {
        gcp.putClassName(name);
        gcp.putClassName(superclass.name);
        for (int i = 0; i < interfaces.length; i++) gcp.putClassName(interfaces[i].name);
        for (int i = 0; i < fields.length; i++) fields[i].prepareWriting(gcp);
        for (int i = 0; i < methods.length; i++) methods[i].prepareWriting(gcp);
        for (int i = 0; i < innerClasses.length; i++) gcp.putClassName(innerClasses[i].name);
        if (sourceFile != null) {
            gcp.putUTF8("SourceFile");
            gcp.putUTF8(sourceFile);
        }
        hasInnerClassesAttr = false;
        Iterator iter = gcp.iterateClassNames();
        while (iter.hasNext()) {
            ClassInfo ci = classpath.getClassInfo((String) iter.next());
            if (ci.status < OUTERCLASS) {
                GlobalOptions.err.println("WARNING: " + ci.name + "'s outer class isn't known.");
            } else {
                if ((ci.outerClass != null || ci.methodScoped) && !hasInnerClassesAttr) {
                    gcp.putUTF8("InnerClasses");
                    hasInnerClassesAttr = true;
                }
                if (ci.outerClass != null) gcp.putClassName(ci.outerClass.name);
                if (ci.className != null) gcp.putUTF8(ci.className);
            }
        }
        if (deprecatedFlag) gcp.putUTF8("Deprecated");
        prepareAttributes(gcp);
    }

    /**
     * Count the attributes needed by the class.
     */
    protected int getAttributeCount() {
        int count = super.getAttributeCount();
        if (sourceFile != null) count++;
        if (hasInnerClassesAttr) count++;
        return count;
    }

    /**
     * Write the attributes needed by the class, namely SourceFile
     * and InnerClasses attributes.
     */
    protected void writeAttributes(GrowableConstantPool gcp, DataOutputStream output) throws IOException {
        super.writeAttributes(gcp, output);
        if (sourceFile != null) {
            output.writeShort(gcp.putUTF8("SourceFile"));
            output.writeInt(2);
            output.writeShort(gcp.putUTF8(sourceFile));
        }
        List outers = new ArrayList();
        Iterator iter = gcp.iterateClassNames();
        while (iter.hasNext()) {
            ClassInfo ci = classpath.getClassInfo((String) iter.next());
            while (ci != null && ci.status >= OUTERCLASS && (ci.outerClass != null || ci.methodScoped)) {
                outers.remove(ci);
                outers.add(ci);
                ci = ci.outerClass;
            }
        }
        if (hasInnerClassesAttr) {
            int count = outers.size();
            output.writeShort(gcp.putUTF8("InnerClasses"));
            output.writeInt(2 + count * 8);
            output.writeShort(count);
            ListIterator listiter = outers.listIterator(count);
            while (listiter.hasPrevious()) {
                ClassInfo ci = (ClassInfo) listiter.previous();
                output.writeShort(gcp.putClassName(ci.name));
                output.writeShort(ci.outerClass == null ? 0 : gcp.putClassName(ci.outerClass.name));
                output.writeShort(ci.className == null ? 0 : gcp.putUTF8(ci.className));
                output.writeShort(ci.modifiers);
            }
        }
        if (deprecatedFlag) {
            output.writeShort(gcp.putUTF8("Deprecated"));
            output.writeInt(0);
        }
    }

    /**
     * Writes a class to the given DataOutputStream.  Of course this only
     * works if ALL information for this class is loaded/set.  If this
     * class has an outer class, inner classes or extra classes, their 
     * status must contain at least the OUTERCLASS information.
     * @param out the output stream.
     * @exception IOException if out throws io exception.
     * @exception IllegalStateException if not enough information is set.
     */
    public void write(DataOutputStream out) throws IOException {
        if (status < ALL) throw new IllegalStateException("state is " + status);
        GrowableConstantPool gcp = new GrowableConstantPool();
        reserveSmallConstants(gcp);
        prepareWriting(gcp);
        out.writeInt(0xcafebabe);
        out.writeShort(3);
        out.writeShort(45);
        gcp.write(out);
        out.writeShort(javaModifiersToBytecode(modifiers));
        out.writeShort(gcp.putClassName(name));
        out.writeShort(gcp.putClassName(superclass.getName()));
        out.writeShort(interfaces.length);
        for (int i = 0; i < interfaces.length; i++) out.writeShort(gcp.putClassName(interfaces[i].getName()));
        out.writeShort(fields.length);
        for (int i = 0; i < fields.length; i++) fields[i].write(gcp, out);
        out.writeShort(methods.length);
        for (int i = 0; i < methods.length; i++) methods[i].write(gcp, out);
        writeAttributes(gcp, out);
    }

    /**
     * Loads the contents of a class from its class path.
     * @param howMuch The amount of information that should be loaded
     * at least, one of {@link #OUTERCLASS}, {@link #HIERARCHY}, {@link
     * #PUBLICDECLARATIONS}, {@link #DECLARATIONS}, {@link #NODEBUG},
     * {@link #ALMOSTALL} or {@link #ALL}.  Note that more information
     * than requested can be loaded if this is convenient.
     * @exception ClassFormatException if the file doesn't denote a
     *            valid class.
     * @exception FileNotFoundException if class wasn't found in classpath.
     * @exception IOException if an io exception occured while reading
     * the class.
     * @exception SecurityException if a security manager prohibits loading
     * the class.
     * @exception IllegalStateException if this ClassInfo was modified by
     * calling one of the setSomething methods.
     */
    public void load(int howMuch) throws IOException {
        if (modified) throw new IllegalStateException(name);
        if (status >= howMuch) return;
        if (classpath.loadClass(this, howMuch)) {
            if (status < howMuch) throw new IllegalStateException("state = " + status);
            return;
        }
        throw new FileNotFoundException(name);
    }

    /**
     * Guess the contents of a class.  This is a last resort if the
     * file can't be read by the class path.  It generates outer class
     * information based on the class name, assumes that the class
     * extends java.lang.Object, implements no interfaces and has no
     * fields, methods or inner classes.
     *
     * @param howMuch The amount of information that should be read,
     * e.g.  {@link #HIERARCHY}.
     * @see #OUTERCLASS
     * @see #HIERARCHY
     * @see #PUBLICDECLARATIONS
     * @see #DECLARATIONS
     * @see #ALMOSTALL
     * @see #ALL 
     */
    public void guess(int howMuch) {
        if (howMuch <= status) throw new IllegalStateException("status = " + status);
        isGuessed = true;
        if (howMuch >= OUTERCLASS) {
            modifiers = Modifier.PUBLIC | 0x20;
            int dollar = name.lastIndexOf('$');
            if (dollar == -1) {
            } else if (Character.isDigit(name.charAt(dollar + 1))) {
                methodScoped = true;
            } else {
                className = name.substring(dollar + 1);
                int prevDollar = name.lastIndexOf('$', dollar);
                if (prevDollar >= 0 && Character.isDigit(name.charAt(prevDollar))) {
                    methodScoped = true;
                    outerClass = classpath.getClassInfo(name.substring(0, prevDollar));
                } else {
                    modifiers |= Modifier.STATIC;
                    outerClass = classpath.getClassInfo(name.substring(0, dollar));
                }
            }
        }
        if (howMuch >= HIERARCHY) {
            if (name.equals("java.lang.Object")) superclass = null; else superclass = classpath.getClassInfo("java.lang.Object");
            interfaces = new ClassInfo[0];
        }
        if (howMuch >= PUBLICDECLARATIONS) {
            methods = new MethodInfo[0];
            fields = new FieldInfo[0];
            innerClasses = EMPTY_INNER;
        }
        status = howMuch;
    }

    /**  
     * This is the counter part to load and guess.  It will drop all
     * informations bigger than "keep" and clean up the memory.  Note
     * that drop should be used with care if more than one thread 
     * accesses this ClassInfo.
     * @param keep tells how much info we should keep, can be
     *    {@link #NONE} or anything that <code>load</code> accepts.
     * @see #load
     */
    public void drop(int keep) {
        if (status <= keep) return;
        if (modified) {
            System.err.println("Dropping info between " + keep + " and " + status + " in modified class " + this + ".");
            Thread.dumpStack();
            return;
        }
        if (keep < HIERARCHY) {
            superclass = null;
            interfaces = null;
        }
        if (keep < OUTERCLASS) {
            methodScoped = false;
            outerClass = null;
            innerClasses = null;
        }
        if (keep < PUBLICDECLARATIONS) {
            fields = null;
            methods = null;
            status = keep;
        } else {
            if (status >= DECLARATIONS) keep = DECLARATIONS;
            for (int i = 0; i < fields.length; i++) fields[i].drop(keep);
            for (int i = 0; i < methods.length; i++) methods[i].drop(keep);
        }
        if (keep < ALMOSTALL) sourceFile = null;
        super.drop(keep);
        status = keep;
    }

    /**
     * Returns the full qualified name of this class.
     * @return the full qualified name of this class, an interned string.
     */
    public String getName() {
        return name;
    }

    /**
     * Tells whether the information in this class was guessed by a call
     * to {@link #guess}.
     * @return true if the information was guessed.
     */
    public boolean isGuessed() {
        return isGuessed;
    }

    /**
     * Returns the java class name of a class, without package or
     * outer classes.  This is null for an anonymous class.  For other
     * classes it is the name that occured after the
     * <code>class</code> keyword (provided it was compiled from
     * java).
     * This need OUTERCLASS information loaded to work properly.
     *
     * @return the short name of this class.  Returns null for
     * anonymous classes.
     *
     * @exception IllegalStateException if OUTERCLASS information wasn't
     * loaded yet.  */
    public String getClassName() {
        if (status < OUTERCLASS) throw new IllegalStateException("status is " + status);
        if (className != null || isMethodScoped()) return className;
        int dot = name.lastIndexOf('.');
        return name.substring(dot + 1);
    }

    /**
     * Returns the ClassInfo object for the super class.
     * @return the short name of this class.
     * @exception IllegalStateException if HIERARCHY information wasn't
     * loaded yet.
     */
    public ClassInfo getSuperclass() {
        if (status < HIERARCHY) throw new IllegalStateException("status is " + status);
        return superclass;
    }

    /**
     * Returns the ClassInfo object for the super class.
     * @return the short name of this class.
     * @exception IllegalStateException if HIERARCHY information wasn't
     * loaded yet.
     */
    public ClassInfo[] getInterfaces() {
        if (status < HIERARCHY) throw new IllegalStateException("status is " + status);
        return interfaces;
    }

    /**
     * Gets the type signature including template information of the class.
     * <b>WARNING:</b> This field may disappear and merged into getType later.
     * @return the type signature.
     * @see TypeSignature
     */
    public String getSignature() {
        if (status < HIERARCHY) throw new IllegalStateException("status is " + status);
        if (signature != null) return signature;
        StringBuffer sb = new StringBuffer();
        sb.append('L').append(superclass.getName().replace('.', '/')).append(";");
        for (int i = 0; i < interfaces.length; i++) {
            sb.append('L').append(interfaces[i].getName().replace('.', '/')).append(";");
        }
        return sb.toString();
    }

    /**
     * Gets the modifiers of this class, e.g. public or abstract.  The
     * information is only available if at least {@link #HIERARCHY} is
     * loaded.
     * @return a bitboard of the modifiers.
     * @see Class#getModifiers
     * @see BinaryInfo#ACC_PUBLIC ACC_* fields in BinaryInfo
     */
    public int getModifiers() {
        if (modifiers == -1) throw new IllegalStateException("status is " + status);
        return modifiers;
    }

    /**
     * Checks whether this class info represents an interface.  The
     * information is only available if at least {@link #HIERARCHY} is
     * loaded.
     * @return true if this class info represents an interface.
     */
    public boolean isInterface() {
        return Modifier.isInterface(getModifiers());
    }

    /**
     * Checks whether this class was declared as deprecated.  In bytecode
     * this is represented by a special attribute.
     * @return true if this class info represents a deprecated class.
     */
    public boolean isDeprecated() {
        return deprecatedFlag;
    }

    /**
     * Searches for a field with given name and type signature.
     * @param name the name of the field.
     * @param typeSig the {@link TypeSignature type signature} of the
     * field.
     * @return the field info for the field.  
     */
    public FieldInfo findField(String name, String typeSig) {
        if (status < PUBLICDECLARATIONS) throw new IllegalStateException("status is " + status);
        for (int i = 0; i < fields.length; i++) if (fields[i].getName().equals(name) && fields[i].getType().equals(typeSig)) return fields[i];
        return null;
    }

    /**
     * Searches for a method with given name and type signature.
     * @param name the name of the method.
     * @param typeSig the {@link TypeSignature type signature} of the
     * method.
     * @return the method info for the method.  
     */
    public MethodInfo findMethod(String name, String typeSig) {
        if (status < PUBLICDECLARATIONS) throw new IllegalStateException("status is " + status);
        for (int i = 0; i < methods.length; i++) if (methods[i].getName().equals(name) && methods[i].getType().equals(typeSig)) return methods[i];
        return null;
    }

    /**
     * Gets the methods of this class.
     */
    public MethodInfo[] getMethods() {
        if (status < PUBLICDECLARATIONS) throw new IllegalStateException("status is " + status);
        return methods;
    }

    /**
     * Gets the fields (class and member variables) of this class.
     */
    public FieldInfo[] getFields() {
        if (status < PUBLICDECLARATIONS) throw new IllegalStateException("status is " + status);
        return fields;
    }

    /**
     * Returns the outer class of this class if it is an inner class.
     * This needs the OUTERCLASS information loaded. 
     * @return The class that declared this class, null if the class
     * isn't declared in a class scope
     *
     * @exception IllegalStateException if OUTERCLASS information
     * wasn't loaded yet.  
     */
    public ClassInfo getOuterClass() {
        if (status < OUTERCLASS) throw new IllegalStateException("status is " + status);
        return outerClass;
    }

    /**
     * Tells whether the class was declared inside a method.
     * This needs the OUTERCLASS information loaded. 
     * @return true if this is a method scoped or an anonymous class,
     * false otherwise.
     *
     * @exception IllegalStateException if OUTERCLASS information
     * wasn't loaded yet.  
     */
    public boolean isMethodScoped() {
        if (status < OUTERCLASS) throw new IllegalStateException("status is " + status);
        return methodScoped;
    }

    /**
     * Gets the inner classes declared in this class.
     * This needs at least PUBLICDECLARATION information loaded. 
     * @return an array containing the inner classes, guaranteed != null.
     * @exception IllegalStateException if PUBLICDECLARATIONS information
     * wasn't loaded yet.  
     */
    public ClassInfo[] getClasses() {
        if (status < PUBLICDECLARATIONS) throw new IllegalStateException("status is " + status);
        return innerClasses;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    /**
     * Sets the name of this class info.  Note that by changing the
     * name you may overwrite an already loaded class.  This can have
     * ugly effects, as references to that overwritten class may still
     * exist.
     */
    public void setName(String newName) {
        classpath.renameClassInfo(this, newName);
        name = newName.intern();
        status = ALL;
        modified = true;
    }

    public void setSuperclass(ClassInfo newSuper) {
        superclass = newSuper;
        status = ALL;
        modified = true;
    }

    public void setInterfaces(ClassInfo[] newIfaces) {
        interfaces = newIfaces;
        status = ALL;
        modified = true;
    }

    public void setModifiers(int newModifiers) {
        modifiers = newModifiers;
        status = ALL;
        modified = true;
    }

    public void setDeprecated(boolean flag) {
        deprecatedFlag = flag;
    }

    public void setMethods(MethodInfo[] mi) {
        methods = mi;
        status = ALL;
        modified = true;
    }

    public void setFields(FieldInfo[] fi) {
        fields = fi;
        status = ALL;
        modified = true;
    }

    public void setOuterClass(ClassInfo oc) {
        outerClass = oc;
        status = ALL;
        modified = true;
    }

    public void setMethodScoped(boolean ms) {
        methodScoped = ms;
        status = ALL;
        modified = true;
    }

    public void setClasses(ClassInfo[] ic) {
        innerClasses = ic.length == 0 ? EMPTY_INNER : ic;
        status = ALL;
        modified = true;
    }

    public void setSourceFile(String newSource) {
        sourceFile = newSource;
        status = ALL;
        modified = true;
    }

    /** 
     * Gets the serial version UID of this class.  If a final static
     * long serialVersionUID  field is present, its constant value
     * is returned.  Otherwise the UID is calculated with the algorithm
     * in the serial version spec.
     * @return the serial version UID of this class.
     * @exception IllegalStateException if DECLARATIONS aren't loaded.
     * @exception NoSuchAlgorithmException if SHA-1 message digest is not
     * supported (needed for calculation of UID.
     */
    public long getSerialVersionUID() throws NoSuchAlgorithmException {
        if (status < DECLARATIONS) throw new IllegalStateException("status is " + status);
        FieldInfo fi = findField("serialVersionUID", "J");
        if (fi != null && ((fi.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) == (Modifier.STATIC | Modifier.FINAL)) && fi.getConstant() != null) return ((Long) fi.getConstant()).longValue();
        final MessageDigest md = MessageDigest.getInstance("SHA");
        OutputStream digest = new OutputStream() {

            public void write(int b) {
                md.update((byte) b);
            }

            public void write(byte[] data, int offset, int length) {
                md.update(data, offset, length);
            }
        };
        DataOutputStream out = new DataOutputStream(digest);
        try {
            out.writeUTF(this.name);
            int modifs = javaModifiersToBytecode(this.modifiers) & (Modifier.ABSTRACT | Modifier.FINAL | Modifier.INTERFACE | Modifier.PUBLIC);
            out.writeInt(modifs);
            ClassInfo[] interfaces = (ClassInfo[]) this.interfaces.clone();
            Arrays.sort(interfaces);
            for (int i = 0; i < interfaces.length; i++) out.writeUTF(interfaces[i].name);
            FieldInfo[] fields = (FieldInfo[]) this.fields.clone();
            Arrays.sort(fields);
            for (int i = 0; i < fields.length; i++) {
                modifs = fields[i].getModifiers();
                if ((modifs & Modifier.PRIVATE) != 0 && (modifs & (Modifier.STATIC | Modifier.TRANSIENT)) != 0) continue;
                out.writeUTF(fields[i].getName());
                out.writeInt(modifs);
                out.writeUTF(fields[i].getType());
            }
            MethodInfo[] methods = (MethodInfo[]) this.methods.clone();
            Arrays.sort(methods);
            for (int i = 0; i < methods.length; i++) {
                modifs = methods[i].getModifiers();
                if (methods[i].getName().equals("<clinit>")) modifs = Modifier.STATIC;
                if ((modifs & Modifier.PRIVATE) != 0) continue;
                out.writeUTF(methods[i].getName());
                out.writeInt(modifs);
                out.writeUTF(methods[i].getType().replace('/', '.'));
            }
            out.close();
            byte[] sha = md.digest();
            long result = 0;
            for (int i = 0; i < 8; i++) {
                result += (long) (sha[i] & 0xFF) << (8 * i);
            }
            return result;
        } catch (IOException ex) {
            throw new InternalError();
        }
    }

    /**
     * Compares two ClassInfo objects for name order.
     * @return a positive number if this name lexicographically
     * follows than other's name, a negative number if it preceeds the
     * other, 0 if they are equal.  
     * @exception ClassCastException if other is not a ClassInfo.
     */
    public int compareTo(Object other) {
        return name.compareTo(((ClassInfo) other).name);
    }

    /**
     * Checks if this class is a super class of child.  This loads the
     * complete hierarchy of child on demand and can throw an IOException
     * if some classes are not found or broken.
     *
     * It doesn't check for cycles in class hierarchy, so it may get
     * into an eternal loop.
     *
     * @param child the class that should be a child class of us.
     * @return true if this is as super class of child, false otherwise
     * @exception IOException if hierarchy of child could not be loaded.
     */
    public boolean superClassOf(ClassInfo child) throws IOException {
        while (child != this && child != null) {
            if (child.status < HIERARCHY) child.load(HIERARCHY);
            child = child.getSuperclass();
        }
        return child == this;
    }

    /**
     * Checks if this interface is implemented by clazz.  This loads the
     * complete hierarchy of clazz on demand and can throw an IOException
     * if some classes are not found or broken.  If this class is not an
     * interface it returns false, but you should check it yourself for 
     * better performance. <br>
     *
     * It doesn't check for cycles in class hierarchy, so it may get
     * into an eternal loop.
     * @param clazz the class to be checked.
     * @return true if this is a interface and is implemented by clazz,
     * false otherwise
     * @exception IOException if hierarchy of clazz could not be loaded.
     */
    public boolean implementedBy(ClassInfo clazz) throws IOException {
        while (clazz != this && clazz != null) {
            if (clazz.status < HIERARCHY) clazz.load(HIERARCHY);
            ClassInfo[] ifaces = clazz.getInterfaces();
            for (int i = 0; i < ifaces.length; i++) {
                if (implementedBy(ifaces[i])) return true;
            }
            clazz = clazz.getSuperclass();
        }
        return clazz == this;
    }

    /**
     * Returns a string representation of the class.  This is just the
     * full qualified class name.
     */
    public String toString() {
        return name;
    }
}
