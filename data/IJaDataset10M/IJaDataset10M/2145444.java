package oscript.data;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import oscript.classwrap.ClassWrapGen;
import oscript.exceptions.PackagedScriptObjectException;
import oscript.util.MemberTable;
import oscript.util.StackFrame;
import oscript.util.SymbolMap;

/**
 * A wrapper for a java class.  Types should be intern'd.
 * 
 * @author Rob Clark (rob@ti.com)
 */
public class JavaClassWrapper extends Type {

    protected Class javaClass;

    private int id = -1;

    protected transient JavaClassWrapperImpl impl;

    protected transient JavaClassWrapperImpl wrapperImpl;

    /**
   * Table of all java-class-wrappers.  A <code>JavaClassWrapper</code>
   * instance is intern'd, so there shouldn't be two instances representing
   * the same java class.
   */
    private static Hashtable classWrapperCache = new Hashtable();

    /**
   * The type object for an script java type.
   */
    public static final Value TYPE = BuiltinType.makeBuiltinType("oscript.data.JavaClassWrapper");

    public static final String PARENT_TYPE_NAME = "oscript.data.OObject";

    public static final String TYPE_NAME = "JavaClass";

    public static final String[] MEMBER_NAMES = new String[] { "isA", "castToString", "castToJavaObject", "callAsConstructor", "callAsExtends", "getMember", "getClassLoader", "getName" };

    /**
   * The class wrapper instances need to be intern'd, so the types work out
   * right... otherwise you might have multiple wrappers per java class (ie
   * type), which would confuse type checking...
   * 
   * @param javaClass    the java class this object is a wrapper for
   */
    public static synchronized JavaClassWrapper getClassWrapper(Class javaClass) {
        javaClass = ClassWrapGen.getNonWrapperClass(javaClass);
        JavaClassWrapper jcw = (JavaClassWrapper) (classWrapperCache.get(javaClass));
        if (jcw == null) {
            jcw = new JavaClassWrapper(javaClass);
            classWrapperCache.put(javaClass, jcw);
        }
        return jcw;
    }

    public static JavaClassWrapper getClassWrapper(String className) throws ClassNotFoundException {
        return getClassWrapper(forName(className));
    }

    public static final Class forName(String className) throws ClassNotFoundException {
        return oscript.compiler.CompilerClassLoader.forName(className, true, null);
    }

    /**
   * Class Constructor.
   * 
   * @param javaClass    the java class this object is a wrapper for
   */
    protected JavaClassWrapper(Class javaClass) {
        super();
        this.javaClass = javaClass;
    }

    /**
   * Initialize this object.  Initialization is done on demand because
   * <code>impl</code> and <code>wrapperImpl</code> are transient, and
   * might not exist if this object gets unserialized...
   */
    protected synchronized void init() {
        if (impl == null) {
            this.id = Symbol.getSymbol(javaClass.getName()).getId();
            if (ClassWrapGen.canMakeWrapperClass(javaClass)) wrapperImpl = new JavaClassWrapperImpl(javaClass, true);
            impl = new JavaClassWrapperImpl(javaClass, false);
        }
    }

    /**
   * Get the type of this object.  The returned type doesn't have to take
   * into account the possibility of a script type extending a built-in
   * type, since that is handled by {@link #getType}.
   * 
   * @return the object's type
   */
    protected Value getTypeImpl() {
        return TYPE;
    }

    /**
   * Convert this object to a native java <code>String</code> value.
   * 
   * @return a String value
   * @throws PackagedScriptObjectException(NoSuchMethodException)
   */
    public String getName() {
        return javaClass.getName();
    }

    /**
   * If this object is a type, determine if an instance of this type is
   * an instance of the specified type, ie. if this is <code>type</code>,
   * or a subclass.
   * 
   * @param type         the type to compare this type to
   * @return <code>true</code> or <code>false</code>
   * @throws PackagedScriptObjectException(NoSuchMemberException)
   */
    public boolean isA(Value type) {
        type = type.unhand();
        Class c;
        if (super.isA(type)) return true;
        if (((c = javaClass.getSuperclass()) != null) && getClassWrapper(c).isA(type)) return true;
        Class[] interfaces = javaClass.getInterfaces();
        for (int i = 0; i < interfaces.length; i++) if (getClassWrapper(interfaces[i]).isA(type)) return true;
        return false;
    }

    /**
   * Convert this object to a native java <code>String</code> value.
   * 
   * @return a String value
   * @throws PackagedScriptObjectException(NoSuchMethodException)
   */
    public String castToString() throws PackagedScriptObjectException {
        return getName();
    }

    /**
   * Convert this object to a native java <code>Object</code> value.
   * 
   * @return a java object
   * @throws PackagedScriptObjectException(NoSuchMethodException)
   */
    public Object castToJavaObject() throws PackagedScriptObjectException {
        return javaClass;
    }

    /**
   * Call this object as a constructor.
   * 
   * @param sf           the current stack frame
   * @param args         the arguments to the function, or <code>null</code> if none
   * @return the newly constructed object
   * @throws PackagedScriptObjectException
   * @see Function
   */
    public Value callAsConstructor(StackFrame sf, MemberTable args) throws PackagedScriptObjectException {
        return JavaBridge.convertToScriptObject(doConstruct(sf, args, false));
    }

    /**
   * Call this object as a parent class constructor.
   * 
   * @param sf           the current stack frame
   * @param scope        the object
   * @param args         the arguments to the function, or <code>null</code> if none
   * @return the value returned by the function
   * @throws PackagedScriptObjectException
   * @see Function
   */
    public Value callAsExtends(StackFrame sf, Scope scope, MemberTable args) throws PackagedScriptObjectException {
        if (impl == null) init();
        if (wrapperImpl == null) throw PackagedScriptObjectException.makeExceptionWrapper(new OUnsupportedOperationException("java class is final; can't call as constructor"));
        Scope scriptObject = scope;
        while (scriptObject instanceof ForkScope) scriptObject = scriptObject.getPreviousScope();
        ClassWrapGen.linkObjects(doConstruct(sf, args, true), scriptObject);
        return scope;
    }

    protected Object doConstruct(StackFrame sf, MemberTable args, boolean isWrapper) {
        if (impl == null) init();
        if (isWrapper) return wrapperImpl.doConstruct(this, sf, args); else return impl.doConstruct(this, sf, args);
    }

    /**
   * Get a member of this object.
   * 
   * @param id           the id of the symbol that maps to the member
   * @param exception    whether an exception should be thrown if the
   *   member object is not resolved
   * @return a reference to the member
   * @throws PackagedScriptObjectException(NoSuchMethodException)
   * @throws PackagedScriptObjectException(NoSuchMemberException)
   */
    public Value getMember(int id, boolean exception) throws PackagedScriptObjectException {
        if (impl == null) init();
        Value val = impl.getMemberImpl(id);
        if (val == null) {
            try {
                if (mightBeClassName(id)) val = JavaClassWrapper.getClassWrapper(forName(getInnerClassName(javaClass, id)));
            } catch (ClassNotFoundException e) {
            }
        }
        if (val != null) return val; else return super.getMember(id, exception);
    }

    /**
   * Get a member of this type.  This is used to interface to the java
   * method of having members be attributes of a type.  Regular object-
   * script object's members are attributes of the object, but in the
   * case of java types (including built-in types), the members are
   * attributes of the type.
   * 
   * @param obj          an object of this type
   * @param id           the id of the symbol that maps to the member
   * @return a reference to the member, or null
   */
    protected Value getTypeMember(Value obj, int id) {
        if (impl == null) init();
        obj = obj.unhand();
        Object javaObj;
        if ((this instanceof BuiltinType) && !(obj instanceof ScriptObject)) javaObj = obj; else javaObj = obj.castToJavaObject();
        Value val = getTypeMemberImpl(javaObj, id);
        if (val == null) {
            if (obj instanceof ScriptObject) val = ScriptObject.TYPE.getTypeMemberImpl(obj, Symbol.getSymbol("_" + Symbol.getSymbol(id).castToString()).getId()); else val = Value.TYPE.getTypeMemberImpl(obj, id);
        }
        if (val == null) val = impl.getMemberImpl(id);
        return val;
    }

    protected Value getTypeMemberImpl(Object javaObj, int id) {
        if (impl == null) init();
        CacheEntry ce = null;
        if (ClassWrapGen.isWrapperInstance(javaObj)) ce = wrapperImpl.getTypeMemberCacheEntry(id);
        if (ce == null) ce = impl.getTypeMemberCacheEntry(id);
        if (ce == null) return null;
        return ce.getMember(id, javaObj);
    }

    /**
   * Get the {@link ClassLoader} object for the java class this class is
   * a wrapper for.
   * 
   * @return the {@link ClassLoader} of the java class
   */
    public ClassLoader getClassLoader() {
        return javaClass.getClassLoader();
    }

    /**
   * Implements the reflection stuff... this is broken out into it's own
   * class because a <code>JavaClassWrapper</code> might have two of these
   * (which get initialized at different times), one for the regular class
   * and one for the wrapper class.  The wrapper class is only used when
   * script code extends a java type.  Also, since we need to create a
   * new instance of <code>JavaInnerClassWrapper</code> for each access,
   * we cache the impl's for inner classes, to avoid having to go thru
   * the expensive init() process multiple times for the same java class.
   */
    protected static class JavaClassWrapperImpl {

        Class javaClass;

        private JavaConstructorCallable[] constructorCallables;

        /**
     * Cache of static fields, maps name id -> Field or Method[]
     */
        private SymbolMap classMemberCache;

        /**
     * Cache of instance field, maps name id -> Field or Method[]
     */
        private SymbolMap instanceMemberCache;

        private boolean isWrapper;

        private boolean initialized = false;

        /**
     * Class Constructor
     */
        JavaClassWrapperImpl(Class javaClass, boolean isWrapper) {
            this.javaClass = javaClass;
            this.isWrapper = isWrapper;
        }

        synchronized void init() {
            if (!initialized) {
                if (isWrapper) javaClass = ClassWrapGen.makeWrapperClass(javaClass);
                Constructor[] constructors = javaClass.getDeclaredConstructors();
                constructorCallables = new JavaConstructorCallable[constructors.length];
                for (int i = 0; i < constructors.length; i++) constructorCallables[i] = new JavaConstructorCallable(constructors[i]);
                boolean isNotPublic = !Modifier.isPublic(javaClass.getModifiers());
                instanceMemberCache = new SymbolMap();
                classMemberCache = new SymbolMap();
                if (isWrapper) {
                    Method[] methods = javaClass.getMethods();
                    SymbolMap methodMap = new SymbolMap();
                    for (int i = 0; i < methods.length; i++) {
                        if (!Modifier.isStatic(methods[i].getModifiers())) {
                            addMethodToCache(methodMap, Symbol.getSymbol(methods[i].getName()).getId(), methods[i]);
                        }
                    }
                    for (Iterator itr = methodMap.keys(); itr.hasNext(); ) {
                        int id = ((Integer) (itr.next())).intValue();
                        Object obj = methodMap.get(Symbol.getSymbol(ClassWrapGen.getOrigMethodName(Symbol.getSymbol(id).castToString())).getId());
                        if (obj != null) instanceMemberCache.put(id, obj);
                    }
                } else {
                    Method[] methods = javaClass.getMethods();
                    Field[] fields = javaClass.getFields();
                    for (int i = 0; i < fields.length; i++) {
                        int id = Symbol.getSymbol(fields[i].getName()).getId();
                        if (Modifier.isStatic(fields[i].getModifiers())) addFieldToCache(classMemberCache, id, fields[i]); else addFieldToCache(instanceMemberCache, id, fields[i]);
                    }
                    for (int i = 0; i < methods.length; i++) {
                        Method method = methods[i];
                        String methodName = method.getName();
                        int id = Symbol.getSymbol(methodName).getId();
                        if (isNotPublic) method = searchForAccessibleMethod(methodName, javaClass, method.getParameterTypes());
                        if (method != null) {
                            if (Modifier.isStatic(method.getModifiers())) addMethodToCache(classMemberCache, id, method); else addMethodToCache(instanceMemberCache, id, method);
                        }
                    }
                }
                initialized = true;
            }
        }

        /**
     * basically we decide on the constructor with the closest matching args, 
     * and call it to create a new instance.
     */
        Object doConstruct(JavaClassWrapper jcw, StackFrame sf, MemberTable args) {
            if (!initialized) init();
            return JavaBridge.call(jcw.id, null, constructorCallables, sf, args);
        }

        Value getMemberImpl(int id) {
            if (!initialized) init();
            CacheEntry ce = (CacheEntry) (classMemberCache.get(id));
            if (ce == null) return null;
            return ce.getMember(id, null);
        }

        CacheEntry getTypeMemberCacheEntry(int id) {
            if (!initialized) init();
            Object ce = instanceMemberCache.get(id);
            if (ce == null) {
                ce = getInnerClass(id);
                if (ce == null) ce = getBeanAccessor(id);
                if (ce == null) ce = Boolean.FALSE;
                instanceMemberCache.put(id, ce);
            }
            if (ce == Boolean.FALSE) ce = null;
            return (CacheEntry) ce;
        }

        private CacheEntry getInnerClass(int id) {
            try {
                if (!mightBeClassName(id)) return null;
                final Class innerClass = forName(getInnerClassName(javaClass, id));
                return new CacheEntry() {

                    private JavaClassWrapperImpl[] impls;

                    public Value getMember(int id, Object javaObj) {
                        Value obj = JavaBridge.convertToScriptObject(javaObj);
                        synchronized (javaClass) {
                            JavaInnerClassWrapper jicw = null;
                            if (impls == null) {
                                jicw = new JavaInnerClassWrapper(obj, innerClass);
                                jicw.init();
                                impls = new JavaClassWrapperImpl[2];
                                impls[0] = jicw.impl;
                                impls[1] = jicw.wrapperImpl;
                            }
                            if (jicw == null) {
                                jicw = new JavaInnerClassWrapper(obj, impls[0].javaClass);
                                jicw.impl = impls[0];
                                jicw.wrapperImpl = impls[1];
                            }
                            return jicw;
                        }
                    }
                };
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        /**
     * Check for "getter" and "setter" methods corresponding to <code>id</code>,
     * and if present return a bean-accessor, to allow access to properties of
     * java beans.
     */
        private CacheEntry getBeanAccessor(int id) {
            String name = Symbol.getSymbol(id).castToString();
            char l = name.charAt(0);
            if (!Character.isLowerCase(l)) return null;
            final String cname = name.substring(0, 1).toUpperCase() + ((name.length() > 1) ? name.substring(1) : "");
            Object obj = instanceMemberCache.get(Symbol.getSymbol("get" + cname).getId());
            final CacheEntry getterCE = (obj == Boolean.FALSE) ? null : (CacheEntry) obj;
            if (getterCE == null) return null;
            return new CacheEntry() {

                public Value getMember(final int id, final Object javaObj) {
                    if ((javaObj instanceof Value) && !(javaObj instanceof RegExpResult)) return null;
                    return new AbstractReference() {

                        private Value setter;

                        private Value getter;

                        public void opAssign(Value val) throws PackagedScriptObjectException {
                            if (setter == null) {
                                String setterName = "set" + cname;
                                Object obj = instanceMemberCache.get(Symbol.getSymbol(setterName).getId());
                                CacheEntry setterCE = (obj == Boolean.FALSE) ? null : (CacheEntry) obj;
                                if (setterCE != null) setter = setterCE.getMember(id, javaObj);
                                if (setter == null) throw noSuchMember(setterName);
                            }
                            setter.callAsFunction(new Value[] { val });
                        }

                        protected Value get() {
                            if (getter == null) getter = getterCE.getMember(id, javaObj);
                            return getter.callAsFunction(new Value[0]);
                        }
                    };
                }
            };
        }

        void populateTypeMemberSet(Set s) {
            for (Iterator itr = instanceMemberCache.keys(); itr.hasNext(); ) {
                int id = ((Integer) (itr.next())).intValue();
                if (instanceMemberCache.get(id) != Boolean.FALSE) s.add(Symbol.getSymbol(id));
            }
        }

        void populateMemberSet(Set s) {
            for (Iterator itr = classMemberCache.keys(); itr.hasNext(); ) s.add(Symbol.getSymbol(((Integer) (itr.next())).intValue()));
        }
    }

    private static class JavaConstructorCallable implements JavaBridge.JavaCallable {

        private final Constructor constructor;

        private Class[] parameterTypes;

        private JavaConstructorCallable(Constructor constructor) {
            this.constructor = constructor;
        }

        public Object call(Object javaObject, Object[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
            return constructor.newInstance(args);
        }

        public Class[] getParameterTypes() {
            if (parameterTypes == null) parameterTypes = constructor.getParameterTypes();
            return parameterTypes;
        }

        public Class getDeclaringClass() {
            return constructor.getDeclaringClass();
        }

        public boolean isVarArgs() {
            return constructor.isVarArgs();
        }
    }

    private static class JavaMethodCallable implements JavaBridge.JavaCallable {

        private final Method method;

        private Class[] parameterTypes;

        private Class returnType;

        JavaMethodCallable(Method method) {
            this.method = method;
        }

        public Object call(Object javaObject, Object[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
            if (returnType == null) returnType = method.getReturnType();
            Object ret = method.invoke(javaObject, args);
            if (returnType.equals(Void.TYPE)) ret = Value.UNDEFINED;
            return ret;
        }

        public Class[] getParameterTypes() {
            if (parameterTypes == null) parameterTypes = method.getParameterTypes();
            return parameterTypes;
        }

        public int hashCode() {
            return method.hashCode();
        }

        public boolean equals(Object obj) {
            return ((obj instanceof JavaMethodCallable) && (((JavaMethodCallable) obj).method.equals(method)));
        }

        public Class getDeclaringClass() {
            return method.getDeclaringClass();
        }

        public boolean isVarArgs() {
            return method.isVarArgs();
        }
    }

    private static final boolean ignoreClassCase = System.getProperty("oscript.ignore.classname.case", "false").equals("true");

    static final boolean mightBeClassName(int id) {
        if (ignoreClassCase) return true;
        String name = Symbol.getSymbol(id).castToString();
        return Character.isUpperCase(name.charAt(0));
    }

    private static String getInnerClassName(Class javaClass, int id) {
        return javaClass.getName() + "$" + Symbol.getSymbol(id).castToString();
    }

    /**
   */
    private interface CacheEntry {

        Value getMember(int id, Object obj);
    }

    /**
   * member-cache entry for fields
   */
    private static class FieldCacheEntry implements CacheEntry {

        private Field field;

        void add(Field field) {
            field.setAccessible(true);
            if ((this.field == null) || this.field.getDeclaringClass().isAssignableFrom(field.getDeclaringClass())) this.field = field;
        }

        public Value getMember(int id, final Object obj) {
            return new AbstractReference() {

                public void opAssign(Value val) throws PackagedScriptObjectException {
                    try {
                        field.set(obj, val.castToJavaObject());
                    } catch (IllegalAccessException e) {
                        throw OJavaException.makeJavaExceptionWrapper(e);
                    }
                }

                protected Value get() {
                    try {
                        return JavaBridge.convertToScriptObject(field.get(obj));
                    } catch (IllegalAccessException e) {
                        throw OJavaException.makeJavaExceptionWrapper(e);
                    }
                }
            };
        }
    }

    /**
   * member-cache entry for methods
   */
    private static class MethodCacheEntry implements CacheEntry {

        private Vector v;

        private JavaBridge.JavaCallable[] methods;

        public void add(JavaBridge.JavaCallable method) {
            if (v == null) {
                v = new Vector();
            }
            if (methods != null) {
                for (int i = 0; i < methods.length; i++) v.add(methods[i]);
                methods = null;
            }
            Class[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < v.size(); i++) {
                if (parameterTypesMatch(((JavaMethodCallable) (v.elementAt(i))).getParameterTypes(), parameterTypes)) {
                    if (((JavaMethodCallable) (v.elementAt(i))).getDeclaringClass().isAssignableFrom(method.getDeclaringClass())) v.set(i, method);
                    return;
                }
            }
            v.add(method);
        }

        public synchronized Value getMember(int id, Object obj) {
            if (methods == null) {
                methods = new JavaBridge.JavaCallable[v.size()];
                v.copyInto(methods);
                v = null;
            }
            return new JavaMethodWrapper(id, obj, methods);
        }
    }

    /**
   * add a field member to the specified cache
   */
    private static final void addFieldToCache(SymbolMap memberCache, int id, Field field) {
        FieldCacheEntry fce = (FieldCacheEntry) (memberCache.get(id));
        if (fce == null) memberCache.put(id, fce = new FieldCacheEntry());
        fce.add(field);
    }

    /**
   * add a method member to the specified cache, or if a member already exists
   * for a method with the same name, append this method to the existing member
   */
    private static final void addMethodToCache(SymbolMap memberCache, int id, Method method) {
        Object obj = memberCache.get(id);
        if ((obj == null) || !(obj instanceof MethodCacheEntry)) memberCache.put(id, obj = new MethodCacheEntry());
        MethodCacheEntry mce = (MethodCacheEntry) obj;
        try {
            tmp[0] = method;
            AccessibleObject.setAccessible(tmp, true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        mce.add(new JavaMethodCallable(method));
    }

    private static Method[] tmp = new Method[1];

    private static final boolean parameterTypesMatch(Class[] p1, Class[] p2) {
        if (p1.length == p2.length) {
            for (int i = 0; i < p1.length; i++) if (!p1[i].equals(p2[i])) return false;
            return true;
        }
        return false;
    }

    /**
   * Utility to search for accessible methods with the specified name and 
   * parameters.
   */
    static final Method searchForAccessibleMethod(String name, Class javaClass, Class[] paramTypes) {
        if (Modifier.isPublic(javaClass.getModifiers())) {
            try {
                Method method = javaClass.getDeclaredMethod(name, paramTypes);
                return method;
            } catch (NoSuchMethodException e) {
            }
        }
        Class[] interfaceClasses = javaClass.getInterfaces();
        for (int i = 0; i < interfaceClasses.length; i++) {
            Method method = searchForAccessibleMethod(name, interfaceClasses[i], paramTypes);
            if (method != null) return method;
        }
        Class superClass = javaClass.getSuperclass();
        if (superClass != null) return searchForAccessibleMethod(name, superClass, paramTypes);
        return null;
    }

    /**
   * maintains unique-ness of a JavaClassWrapper when stuff gets serialized or
   * un-serialized
   */
    Object readResolve() throws java.io.ObjectStreamException {
        Object obj;
        synchronized (JavaClassWrapper.class) {
            obj = classWrapperCache.get(javaClass);
            if (obj == null) {
                obj = this;
                classWrapperCache.put(javaClass, obj);
            }
        }
        return obj;
    }

    /**
   * Derived classes that implement {@link #getMember} should also
   * implement this.
   * 
   * @param s   the set to populate
   * @param debugger  <code>true</code> if being used by debugger, in
   *   which case both public and private/protected field names should 
   *   be returned
   * @see #getMember
   */
    protected void populateMemberSet(Set s, boolean debugger) {
        if (impl == null) init();
        impl.populateMemberSet(s);
    }

    /**
   * Derived classes that implement {@link #getTypeMember} should also
   * implement this.
   * 
   * @param s   the set to populate
   * @param debugger  <code>true</code> if being used by debugger, in
   *   which case both public and private/protected field names should 
   *   be returned
   * @see #getTypeMember
   */
    protected void populateTypeMemberSet(Set s, boolean debugger) {
        if (impl == null) init();
        impl.populateTypeMemberSet(s);
    }

    /**
   * used by Debugger#getMemberAccessor, allows access to non-public members
   */
    Debugger.MemberAccessor _getTypeMemberAccessor(Value obj, Value name) {
        if (impl == null) init();
        Value val = getTypeMember(obj, name);
        if (val != null) return new JavaClassWrapperMemberAccessor(val); else return null;
    }

    private static class JavaClassWrapperMemberAccessor implements Debugger.MemberAccessor {

        private Value val;

        /**
     * It is safe to use val instead of name, because we never replace an
     * entry in the member table.
     */
        JavaClassWrapperMemberAccessor(Value val) {
            this.val = val;
        }

        public int getAttr() {
            return Reference.ATTR_PUBLIC;
        }

        public Value getValue() {
            return val;
        }
    }

    /**
   * For use by test suite...
   */
    public static class Base {

        public static final int ID = 1;

        public int[] getFoo() {
            System.err.println("getFoo");
            return new int[] { 1, 2, 3 };
        }
    }

    public static class Derived extends Base {

        public static final int ID = 2;
    }
}
