package net.sourceforge.jnipp.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Node containing class-specific data for use by the code generators.
 *
 * Each instance of this class contains data for a specific class in the
 * dependency tree.  It is utilized by the code generators and contains a
 * list of dependency nodes, method nodes, field nodes and numerous helper
 * methods.
 *
 * @author $Author: ptrewhella $
 * @version $Revision: 1.34 $
 */
public class ClassNode {

    /**
	 * Map of loaded <code>ClassNode</code> instances.
	 *
	 * This map contains a <code>ClassNode</code> instance for each Java class that
	 * was encountered in the traversal from the input class to the code generator.
	 *
	 * @see #getClassNode
	 */
    private static Map loadedClassNodes = new HashMap();

    /**
	 * Fully-qualified Java class name (package.class).
	 *
	 * @see #getFullyQualifiedClassName
	 */
    private String fullyQualifiedClassName = "";

    /**
	 * The Java class name without the package.
	 *
	 * @see #getClassName
	 */
    private String className = "";

    /**
	 * The Java package.
	 *
	 * @see #getPackageName
	 */
    private String packageName = "";

    /**
	 * The C++ namespace.
	 *
	 * @see #getNamespace
	 */
    private String namespace = "";

    /**
	 * List of all namespace components.
	 *
	 * Contains a list of all of the individual namespace components in order of
	 * scope.
	 *
	 * @see #getNamespaceElements
	 */
    private List namespaceElements = new ArrayList();

    /**
	 * Boolean flag indicating if this is an interface.
	 *
	 * True if the class represented by the instance is an interface.
	 *
	 * @see #isInterface
	 */
    private boolean intfc = false;

    /**
	 * Boolean flag indicating if this is a primitive type.
	 *
	 * True if the type represented by the instance is primitive.
	 *
	 * @see #isPrimitive
	 */
    private boolean primitive = false;

    /**
	 * List of all constructors.
	 *
	 * This list contains all of the constructors declared for the class represented
	 * by this <code>ClassNode</code> instance.
	 *
	 * @see #getConstructors
	 */
    private List ctors = new ArrayList();

    /**
	 * List of all methods.
	 *
	 * This list contains all of the methods declared for the class represented
	 * by this <code>ClassNode</code> instance.
	 *
	 * @see #getMethods
	 */
    private List methods = new ArrayList();

    /**
	 * List of all fields.
	 *
	 * This list contains all of the fields declared for the class represented
	 * by this <code>ClassNode</code> instance.
	 *
	 * @see #getFields
	 */
    private List fields = new ArrayList();

    /**
	 * List of all inner classes.
	 *
	 * This list contains all of the inner classes declared for the class represented
	 * by this <code>ClassNode</code> instance.
	 *
	 * @see #getInnerClasses
	 */
    private List innerClasses = new ArrayList();

    /**
	 * String representation used in JNI calls to identify the class.
	 *
	 * This string uniquely identifies the class represented by this <code>ClassNode</code>
	 * instance and is utilized in calls to JNI methods such as <code>GetFieldID()</code> and
	 * as a component of a method-identifying string.
	 *
	 * @see #getJNIString
	 */
    private String jniString = "";

    /**
	 * Array dimensions.
	 *
	 * If the type represented by this <code>ClassNode</code> instance is an array,
	 * then this represents the dimensions of the array.
	 *
	 * @see #isArray
	 * @see #getArrayDims
	 */
    private int arrayDims = 0;

    /**
	 * Reference to the superclass <code>ClassNode</code> instance.
	 *
	 * If the class represented by this <code>ClassNode</code> instance has a superclass,
	 * then this points to its <code>ClassNode</code>.
	 *
	 * @see #getSuperClass
	 */
    private ClassNode superClass = null;

    /**
	 * List of implemented interfaces.
	 *
	 * If the class represented by this <code>ClassNode</code> instance implements
	 * any interfaces, then this list contains references to all of their <code>ClassNode</code>
	 * instances.
	 *
	 * @see #getInterfaces
	 */
    private List interfaceList = new ArrayList();

    /**
	 * List of all dependencies.
	 *
	 * This list contains all of the <code>ClassNode</code> instances for all classes
	 * that are used either in field, method or constructor declarations in the
	 * class represented by this <code>ClassNode</code> instance.
	 *
	 * @see #getDependencies
	 */
    private List dependencyList = new ArrayList();

    /**
	 * Component type of array.
	 *
	 * If the type represented by this <code>ClassNode</code> instance is an array,
	 * then this is a reference to the <code>ClassNode</code> instance of the component
	 * type of the array.
	 *
	 * @see #getComponentType
	 */
    private ClassNode componentType = null;

    /**
	 * Is this a <code>Throwable</code> object?
	 *
	 * @see java.lang.Throwable
	 */
    private boolean throwable = false;

    private HashSet cppMethodNames = new HashSet();

    /**
	 * Factory method for ClassNode objects.
	 *
	 * The <code>loadedClassNodes</code> map contains all of the ClassNode objects that
	 * have been loaded as part of the traversal.  It will include class nodes for the input
	 * class, its entire superclass inheritance ancestry, its entire interface implementation
	 * ancestry, all of the inner classes, parameter and return types that are encountered
	 * when evaluating the original input class.
	 *
	 * If the ClassNode already exists, it is found and returned.  If not, then the ClassNode
	 * is created, added to the map and returned.  The purpose of the map is to ensure that a
	 * single instance of each ClassNode is loaded, avoiding unnecessary work and possible
	 * negative side effects.
	 *
	 * @param fullyQualifiedClassName The fully qualified class name of the ClassNode object
	 * @return The ClassNode specified by the <code>fullyQualifiedClassName</code> parameter
	 * @exception ClassNotFoundException
	 * @see #loadedClassNodes
	 */
    public static ClassNode getClassNode(String fullyQualifiedClassName) throws ClassNotFoundException {
        ClassNode node = (ClassNode) loadedClassNodes.get(fullyQualifiedClassName);
        if (node == null) {
            node = new ClassNode(fullyQualifiedClassName);
            loadedClassNodes.put(fullyQualifiedClassName, node);
            node.init();
        }
        return node;
    }

    /**
	 * Returns <emph>true</emph> if the type is primitive.
	 *
	 * This method will return boolean <emph>true</emph> if the type supplied as
	 * parameter is a Java primitive type.
	 *
	 * @param type String representation of the type to be evaluated.
	 * @return Boolean <emph>true</emph> if the supplied type is primitve, false
	 * otherwise.
	 */
    private static boolean isTypePrimitive(String type) {
        if (type.length() > 8 || type.indexOf('.') != -1) return false;
        return (type.equals("boolean") || type.equals("byte") || type.equals("char") || type.equals("short") || type.equals("int") || type.equals("long") || type.equals("float") || type.equals("double") || type.equals("void"));
    }

    private static boolean shouldGenerateProxy(ClassNode node) {
        String typeName = node.getFullyQualifiedClassName();
        return (isTypePrimitive(typeName) == false && isTypeBuiltIn(node) == false);
    }

    private static boolean isTypeBuiltIn(ClassNode node) {
        String typeName = node.getFullyQualifiedClassName();
        return node.isArray() || typeName.equals("java.lang.String");
    }

    /**
	 * Class constructor.
	 *
	 * The class constructor accepts a string representation of the fully qualified
	 * class name.  This is utilized to obtain data about the class through Java's introspection
	 * API.  The queried data is utilized to build a data structure that assists the
	 * various code generators.
	 *
	 * @param fullyQualifiedClassName The fully qualifed name of the class
	 */
    private ClassNode(String fullyQualifiedClassName) {
        this.fullyQualifiedClassName = fullyQualifiedClassName;
    }

    /**
	 * Initializer method.
	 *
	 * This method is called from the <code>getClassNode()</code> factory method after
	 * the instance is created.  It will generate the dependency, constructor, method,
	 * field, inner class and exception lists and set various attributes that are
	 * used by the code generators.
	 *
	 * @exception ClassNotFoundException
	 * @see #getClassNode
	 */
    public void init() throws ClassNotFoundException {
        if (isTypePrimitive(fullyQualifiedClassName) == true) {
            primitive = true;
            className = fullyQualifiedClassName;
        } else {
            Class theClass = Class.forName(fullyQualifiedClassName);
            intfc = theClass.isInterface();
            throwable = new Throwable().getClass().isAssignableFrom(theClass);
            if (theClass.isArray() == true) {
                for (arrayDims = 0; theClass.isArray() == true; ++arrayDims, theClass = theClass.getComponentType()) ;
                componentType = ClassNode.getClassNode(theClass.getName());
                className = componentType.getClassName();
                for (int i = 0; i < arrayDims; ++i) className += "[]";
                namespaceElements.add("net");
                namespaceElements.add("sourceforge");
                namespaceElements.add("jnipp");
                namespace = "net::sourceforge::jnipp";
            } else {
                int pkgEnd = fullyQualifiedClassName.lastIndexOf(".") + 1;
                className = fullyQualifiedClassName.substring(pkgEnd);
                if (pkgEnd > 0) packageName = fullyQualifiedClassName.substring(0, pkgEnd - 1);
                StringTokenizer st = new StringTokenizer(packageName, ".");
                while (st.hasMoreTokens() == true) {
                    String next = st.nextToken();
                    namespaceElements.add(next);
                    namespace += next;
                    if (st.hasMoreTokens() == true) namespace += "::";
                }
                String typeName = fullyQualifiedClassName;
                if (needsProxy() == true) {
                    Constructor[] declaredCtors = theClass.getDeclaredConstructors();
                    Arrays.sort(declaredCtors, new Comparator() {

                        public int compare(Object o1, Object o2) {
                            return o1.toString().compareTo(o2.toString());
                        }

                        public boolean equals(Object obj) {
                            return false;
                        }
                    });
                    for (int i = 0; i < declaredCtors.length; ++i) ctors.add(new MethodNode(this, declaredCtors[i]));
                    Method[] declaredMethods = theClass.getDeclaredMethods();
                    Arrays.sort(declaredMethods, new Comparator() {

                        public int compare(Object o1, Object o2) {
                            return o1.toString().compareTo(o2.toString());
                        }

                        public boolean equals(Object obj) {
                            return false;
                        }
                    });
                    for (int i = 0; i < declaredMethods.length; ++i) methods.add(new MethodNode(this, declaredMethods[i]));
                    Class[] declaredClasses = theClass.getDeclaredClasses();
                    Arrays.sort(declaredClasses, new Comparator() {

                        public int compare(Object o1, Object o2) {
                            return o1.toString().compareTo(o2.toString());
                        }

                        public boolean equals(Object obj) {
                            return false;
                        }
                    });
                    for (int i = 0; i < declaredClasses.length; ++i) innerClasses.add(getClassNode(declaredClasses[i].getName()));
                    Field[] declaredFields = theClass.getDeclaredFields();
                    Arrays.sort(declaredFields, new Comparator() {

                        public int compare(Object o1, Object o2) {
                            return o1.toString().compareTo(o2.toString());
                        }

                        public boolean equals(Object obj) {
                            return false;
                        }
                    });
                    for (int i = 0; i < declaredFields.length; ++i) fields.add(new FieldNode(declaredFields[i]));
                    Class sc = theClass.getSuperclass();
                    if (sc != null) superClass = ClassNode.getClassNode(theClass.getSuperclass().getName());
                    Class[] intfcs = theClass.getInterfaces();
                    for (int i = 0; i < intfcs.length; ++i) interfaceList.add(ClassNode.getClassNode(intfcs[i].getName()));
                }
                generateDependencyList();
            }
        }
    }

    /**
	 * Method to generate dependencies for the class.
	 *
	 * This method is called from the <code>init()</code> method to generate the
	 * list of classes that are used in member and method declarations as either
	 * parameters or return values.
	 *
	 * @exception ClassNotFoundException
	 * @see #init
	 * @see #dependencyList
	 */
    private void generateDependencyList() throws ClassNotFoundException {
        Iterator ctorIter = ctors.iterator();
        while (ctorIter.hasNext() == true) {
            Iterator paramIter = ((MethodNode) ctorIter.next()).getParameterList();
            while (paramIter.hasNext() == true) {
                ClassNode current = (ClassNode) paramIter.next();
                String fullyQualifiedName = current.getFullyQualifiedClassName();
                if (current.isPrimitive() == false && dependencyList.contains(current) == false) {
                    dependencyList.add(current);
                    if (current.isArray() == true && current.getComponentType() != null && current.getComponentType().needsProxy() == true) dependencyList.add(current.getComponentType());
                }
            }
        }
        Iterator methodIter = methods.iterator();
        while (methodIter.hasNext() == true) {
            MethodNode methodNode = (MethodNode) methodIter.next();
            Iterator paramIter = methodNode.getParameterList();
            while (paramIter.hasNext() == true) {
                ClassNode current = (ClassNode) paramIter.next();
                String fullyQualifiedName = current.getFullyQualifiedClassName();
                if (current.isPrimitive() == false && dependencyList.contains(current) == false) {
                    dependencyList.add(current);
                    if (current.isArray() == true && current.getComponentType() != null && current.getComponentType().needsProxy() == true) dependencyList.add(current.getComponentType());
                }
            }
            ClassNode current = methodNode.getReturnType();
            String fullyQualifiedName = current.getFullyQualifiedClassName();
            if (current.isPrimitive() == false && dependencyList.contains(current) == false) {
                dependencyList.add(current);
                if (current.isArray() == true && current.getComponentType() != null && current.getComponentType().needsProxy() == true) dependencyList.add(current.getComponentType());
            }
        }
        Iterator fieldIter = fields.iterator();
        while (fieldIter.hasNext() == true) {
            ClassNode current = ((FieldNode) fieldIter.next()).getType();
            String fullyQualifiedName = current.getFullyQualifiedClassName();
            if (current.isPrimitive() == false && dependencyList.contains(current) == false) {
                dependencyList.add(current);
                if (current.isArray() == true && current.getComponentType() != null && current.getComponentType().needsProxy() == true) dependencyList.add(current.getComponentType());
            }
        }
        if (dependencyList.contains(this) == true) dependencyList.remove(this);
    }

    /**
	 * Accessor to query if this is an interface.
	 *
	 * This method will return boolean <emph>true</emph> if the type represented
	 * by this <code>ClassNode</code> instance is an interface.
	 *
	 * @return Boolean <emph>true</emph> if this is an interface, <emph>false</emph>
	 * otherwise.
	 * @see #intfc
	 */
    public boolean isInterface() {
        return intfc;
    }

    /**
	 * Accessor to query if this is a <code>Throwable</code> class.
	 *
	 * @return Boolean <emph>true</emph> if instances of this class can be "thrown",
	 * <emph>false</emph> otherwise.
	 * @see #throwable
	 */
    public boolean isThrowable() {
        return throwable;
    }

    /**
	 * Accessor to query if this is a primitive type.
	 *
	 * This method will return boolean <emph>true</emph> if the type represented
	 * by this <code>ClassNode</code> instance is primitive.
	 *
	 * @return Boolean <emph>true</emph> if this is primitive, <emph>false</emph>
	 * otherwise.
	 * @see #primitive
	 */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
	 * Accessor to query if this is an array.
	 *
	 * This method will return boolean <emph>true</emph> if the type represented
	 * by this <code>ClassNode</code> instance is an array.
	 *
	 * @return Boolean <emph>true</emph> if this is an array, <emph>false</emph>
	 * otherwise.
	 * @see #arrayDims
	 */
    public boolean isArray() {
        return arrayDims != 0;
    }

    public boolean isBuiltIn() {
        return isTypeBuiltIn(this);
    }

    /**
	 * Accessor to return the Java class name.
	 *
	 * This accessor method will return the Java class name of the type represented
	 * by this <code>ClassNode</code> instance (without the package prefix).
	 *
	 * @return The Java class name of this type.
	 * @see #className
	 */
    public String getClassName() {
        return className;
    }

    /**
	 * Accessor to return the fully-qualified Java class name.
	 *
	 * This accessor method will return the fully-qualified Java class name of the
	 * type represented by this <code>ClassNode</code> instance (with the package
	 * prefix).
	 *
	 * @return The fully-qualified Java class name of this type.
	 * @see #className
	 * @see #packageName
	 */
    public String getFullyQualifiedClassName() {
        if (arrayDims != 0 && componentType != null) {
            if (componentType.packageName.equals("") == true) return getClassName();
            return componentType.packageName + "." + getClassName();
        } else {
            if (packageName.equals("") == true) return getClassName();
            return packageName + "." + getClassName();
        }
    }

    /**
	 * Accessor to return the C++ class name.
	 *
	 * This accessor method will return the equivalent C++ class name of the type
	 * represented by this <code>ClassNode</code> instance (without the namespace
	 * prefix).
	 *
	 * @return The C++ class name of this type.
	 */
    public String getCPPClassName() {
        String cppName = Util.getCPPIdentifier(className);
        return cppName.replace('$', '_');
    }

    /**
	 * Accessor to return the fully-qualified C++ class name.
	 *
	 * This accessor method will return the equivalent fully-qualified C++ class
	 * name of the  type represented by this <code>ClassNode</code> instance (with
	 * the namespace prefix).
	 *
	 * @return The fully-qualified C++ class name of this type.
	 * @see #namespace
	 */
    public String getFullyQualifiedCPPClassName() {
        if (namespace.equals("") == true) return getCPPClassName();
        return namespace + "::" + getCPPClassName();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getNamespace() {
        return namespace;
    }

    public Iterator getNamespaceElements() {
        return namespaceElements.iterator();
    }

    public Iterator getConstructors() {
        return ctors.iterator();
    }

    public Iterator getMethods() {
        return methods.iterator();
    }

    public Iterator getFields() {
        return fields.iterator();
    }

    public Iterator getInnerClasses() {
        return innerClasses.iterator();
    }

    public ClassNode getComponentType() {
        return componentType;
    }

    public int getArrayDims() {
        return arrayDims;
    }

    public String getJNIString() {
        if (jniString.equals("") == false) return jniString;
        if (fullyQualifiedClassName.equals("void") == true) jniString = "V"; else if (arrayDims > 0) {
            for (int i = 0; i < arrayDims; ++i) jniString += "[";
            jniString += componentType.getJNIString();
        } else if (primitive == true) {
            if (fullyQualifiedClassName.equals("boolean") == true) jniString = "Z"; else if (fullyQualifiedClassName.equals("long") == true) jniString = "J"; else jniString += Character.toUpperCase(fullyQualifiedClassName.charAt(0));
        } else {
            jniString = "L" + fullyQualifiedClassName.replace('.', '/') + ";";
        }
        return jniString;
    }

    public Iterator getDependencies() {
        return dependencyList.iterator();
    }

    public Iterator getInterfaces() {
        return interfaceList.iterator();
    }

    public ClassNode getSuperClass() {
        return superClass;
    }

    public boolean needsProxy() {
        return shouldGenerateProxy(this);
    }

    public String getJNITypeName(boolean usePartialSpec) {
        if (needsProxy() == true) return "::" + namespace + "::" + getCPPClassName() + "Proxy";
        if (getFullyQualifiedClassName().equals("java.lang.String") == true) return "::net::sourceforge::jnipp::JStringHelper";
        if (isArray() == true && componentType.isPrimitive() == true) return "::net::sourceforge::jnipp::J" + Character.toUpperCase(componentType.getClassName().charAt(0)) + componentType.getClassName().substring(1) + "ArrayHelper<" + arrayDims + ">";
        if (isArray() == true && componentType.needsProxy() == true) if (usePartialSpec == true) return "::net::sourceforge::jnipp::ProxyArray< " + componentType.getJNITypeName(usePartialSpec) + ", " + arrayDims + " >"; else return "::net::sourceforge::jnipp::PA<" + componentType.getJNITypeName(usePartialSpec) + ">::ProxyArray<" + arrayDims + ">";
        if (isArray() == true && componentType.getFullyQualifiedClassName().equals("java.lang.String") == true) return "::net::sourceforge::jnipp::JStringHelperArray<" + arrayDims + ">";
        return getPlainJNITypeName();
    }

    public String getPlainJNITypeName() {
        if (primitive == true) if (getClassName().equals("void") == true) return "void"; else return "j" + className;
        if (isArray() == true) if (componentType.getFullyQualifiedClassName().equals("java.lang.String") == true || componentType.getFullyQualifiedClassName().equals("java.lang.Class") == true || arrayDims > 1) return "jobjectArray"; else return componentType.getPlainJNITypeName() + "Array";
        if (getFullyQualifiedClassName().equals("java.lang.String") == true) return "jstring";
        if (getFullyQualifiedClassName().equals("java.lang.Class") == true) return "jclass";
        return "jobject";
    }

    protected HashSet getCPPMethodNames() {
        return cppMethodNames;
    }
}
