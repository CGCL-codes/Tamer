package edu.vub.at.objects.symbiosis;

import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.exceptions.XArityMismatch;
import edu.vub.at.exceptions.XDuplicateSlot;
import edu.vub.at.exceptions.XTypeMismatch;
import edu.vub.at.exceptions.XUnassignableField;
import edu.vub.at.exceptions.XUndefinedSlot;
import edu.vub.at.objects.ATBoolean;
import edu.vub.at.objects.ATContext;
import edu.vub.at.objects.ATField;
import edu.vub.at.objects.ATMethod;
import edu.vub.at.objects.ATNil;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.ATTable;
import edu.vub.at.objects.ATTypeTag;
import edu.vub.at.objects.coercion.NativeTypeTags;
import edu.vub.at.objects.grammar.ATSymbol;
import edu.vub.at.objects.mirrors.PrimitiveMethod;
import edu.vub.at.objects.mirrors.Reflection;
import edu.vub.at.objects.natives.NATBoolean;
import edu.vub.at.objects.natives.NATNumber;
import edu.vub.at.objects.natives.NATObject;
import edu.vub.at.objects.natives.NATTable;
import edu.vub.at.objects.natives.NATText;
import edu.vub.at.objects.natives.grammar.AGSymbol;
import edu.vub.at.util.logging.Logging;
import edu.vub.util.TempFieldGenerator;
import java.lang.ref.SoftReference;
import com.thoughtworks.xstream.core.util.ObjectIdDictionary;

/**
 * A JavaClass instance represents a Java Class under symbiosis.
 * 
 * Java classes are treated as AmbientTalk 'singleton' objects:
 * 
 *  - cloning a Java class results in the same Java class instance
 *  - sending 'new' to a Java class invokes the constructor and returns a new instance of the class under symbiosis
 *  - all static fields and methods of the Java class are reflected under symbiosis as fields and methods of the AT object
 *  
 * A Java Class object that represents an interface can furthermore be used
 * as an AmbientTalk type. The type's name corresponds to the interface's full name.
 *  
 * JavaClass instances are pooled (on a per-actor basis): there should exist only one JavaClass instance
 * for each Java class loaded into the JVM. Because the JVM ensures that a Java class
 * can only be loaded once, we can use the Java class wrapped by the JavaClass instance
 * as a unique key to identify its corresponding JavaClass instance.
 *  
 * @author tvcutsem
 */
public final class JavaClass extends NATObject implements ATTypeTag {

    /**
	 * A thread-local hashmap pooling all of the JavaClass wrappers for
	 * the current actor, referring to them using SOFT references, such
	 * that unused wrappers can be GC-ed when running low on memory.
	 */
    private static final ThreadLocal _JAVACLASS_POOL_ = new ThreadLocal() {

        protected synchronized Object initialValue() {
            return new ObjectIdDictionary();
        }
    };

    /**
	 * Allocate a unique symbiont object for the given Java class.
	 */
    public static final JavaClass wrapperFor(Class c) {
        ObjectIdDictionary map = (ObjectIdDictionary) _JAVACLASS_POOL_.get();
        if (map.containsId(c)) {
            SoftReference ref = (SoftReference) map.lookupId(c);
            JavaClass cls = (JavaClass) ref.get();
            if (cls != null) {
                return cls;
            } else {
                map.removeId(c);
                cls = new JavaClass(c);
                map.associateId(c, new SoftReference(cls));
                return cls;
            }
        } else {
            JavaClass jc = new JavaClass(c);
            map.associateId(c, new SoftReference(jc));
            return jc;
        }
    }

    private static final AGSymbol _PTS_NAME_ = AGSymbol.jAlloc("parentTypes");

    private static final AGSymbol _TNM_NAME_ = AGSymbol.jAlloc("typeName");

    /** def isSubtypeOf(type) { nil } */
    private static final PrimitiveMethod _PRIM_STP_ = new PrimitiveMethod(AGSymbol.jAlloc("isSubtypeOf"), NATTable.atValue(new ATObject[] { AGSymbol.jAlloc("type") })) {

        private static final long serialVersionUID = -6864350539143194204L;

        public ATObject base_apply(ATTable arguments, ATContext ctx) throws InterpreterException {
            if (!arguments.base_length().equals(NATNumber.ONE)) {
                throw new XArityMismatch("isSubtypeOf", 1, arguments.base_length().asNativeNumber().javaValue);
            }
            return ctx.base_lexicalScope().asJavaClassUnderSymbiosis().base_isSubtypeOf(arguments.base_at(NATNumber.ONE).asTypeTag());
        }
    };

    private final Class wrappedClass_;

    /**
	 * A JavaClass wrapping a class c is an object that has the lexical scope as its lexical parent
	 * and has NIL as its dynamic parent.
	 * 
	 * If the JavaClass wraps a Java interface type, JavaClass instances are
	 * also types.
	 */
    private JavaClass(Class wrappedClass) {
        super(wrappedClass.isInterface() ? new ATTypeTag[] { NativeTypeTags._TYPETAG_, NativeTypeTags._ISOLATE_ } : NATObject._NO_TYPETAGS_);
        wrappedClass_ = wrappedClass;
        try {
            super.meta_addMethod(new JavaConstructor(wrappedClass_));
        } catch (InterpreterException e) {
            Logging.Actor_LOG.fatal("Error while initializing Java Class constructor: " + wrappedClass.getName(), e);
        }
        if (wrappedClass.isInterface()) {
            Class[] extendedInterfaces = wrappedClass_.getInterfaces();
            ATObject[] types = new ATObject[extendedInterfaces.length];
            for (int i = 0; i < extendedInterfaces.length; i++) {
                types[i] = JavaClass.wrapperFor(extendedInterfaces[i]);
            }
            try {
                super.meta_defineField(_PTS_NAME_, NATTable.atValue(types));
                super.meta_defineField(_TNM_NAME_, AGSymbol.jAlloc(wrappedClass_.getName()));
                super.meta_addMethod(_PRIM_STP_);
            } catch (InterpreterException e) {
                Logging.Actor_LOG.fatal("Error while initializing Java Class as type tag: " + wrappedClass.getName(), e);
            }
        }
        Class nestedClasses[] = wrappedClass.getDeclaredClasses();
        for (Class nested : nestedClasses) {
            String simpleName = nested.getSimpleName();
            try {
                super.meta_defineField(AGSymbol.jAlloc(simpleName), JavaClass.wrapperFor(nested));
            } catch (InterpreterException e) {
                Logging.Actor_LOG.error("Could not assign nested class " + nested.getName() + " to field " + simpleName + " in class " + wrappedClass.getName(), e);
            }
        }
    }

    /** return the class object denoted by this AmbientTalk symbiont */
    public Class getWrappedClass() {
        return wrappedClass_;
    }

    public boolean isJavaClassUnderSymbiosis() {
        return true;
    }

    public JavaClass asJavaClassUnderSymbiosis() throws XTypeMismatch {
        return this;
    }

    public ATBoolean base__opeql__opeql_(ATObject comparand) throws InterpreterException {
        if (comparand.isJavaClassUnderSymbiosis()) {
            return NATBoolean.atValue(wrappedClass_.equals(comparand.asJavaClassUnderSymbiosis().wrappedClass_));
        } else {
            return NATBoolean._FALSE_;
        }
    }

    /**
     * Fields can be defined within a symbiotic Java class object. They are added
     * to its AmbientTalk symbiont, but only if they do not clash with already
     * existing field names.
     */
    public ATNil meta_defineField(ATSymbol name, ATObject value) throws InterpreterException {
        if (Symbiosis.hasField(wrappedClass_, Reflection.upSelector(name), true)) {
            throw new XDuplicateSlot(name);
        } else {
            return super.meta_defineField(name, value);
        }
    }

    /**
	 * Symbiotic Java class objects are singletons.
	 */
    public ATObject meta_clone() throws InterpreterException {
        return this;
    }

    /**
	 * aJavaClass.new(@args) == invoke a Java constructor
	 * AmbientTalk objects can add a custom new method to the class in order to intercept
	 * instance creation. The original instance can then be performed by invoking the old new(@args).
	 * 
	 * For example, imagine we want to extend the class java.lang.Point with a 3D coordinate, e.g. a 'z' field:
	 * <tt>
	 * def Point := jlobby.java.awt.Point;
	 * def oldnew := Point.new;
	 * def Point.new(x,y,z) { // 'override' the new method
	 *   def point := oldnew(x,y); // invokes the Java constructor
	 *   def point.z := z; // adds a field dynamically to the new JavaObject wrapper
	 *   point; // important! new should return the newly created instance
	 * }
	 * def mypoint := Point.new(1,2,3);
	 * </tt>
	 */
    public ATObject meta_newInstance(ATTable initargs) throws InterpreterException {
        return Symbiosis.symbioticInstanceCreation(new JavaConstructor(wrappedClass_), initargs.asNativeTable().elements_);
    }

    /**
     * Methods can be added to a symbiotic Java class object provided they do not already
     * exist in the Java class.
     */
    public ATNil meta_addMethod(ATMethod method) throws InterpreterException {
        ATSymbol name = method.base_name();
        if (Symbiosis.hasMethod(wrappedClass_, Reflection.upSelector(name), true)) {
            throw new XDuplicateSlot(name);
        } else {
            return super.meta_addMethod(method);
        }
    }

    /**
     * Fields can be grabbed from a symbiotic Java class object. Fields that correspond
     * to static fields in the Java class are returned as JavaField instances.
     */
    public ATField meta_grabField(ATSymbol fieldName) throws InterpreterException {
        try {
            return new JavaField(null, Symbiosis.getField(wrappedClass_, Reflection.upSelector(fieldName), true));
        } catch (XUndefinedSlot e) {
            return super.meta_grabField(fieldName);
        }
    }

    /**
     * Methods can be grabbed from a symbiotic Java class object. Methods that correspond
     * to static methods in the Java class are returned as JavaMethod instances.
     */
    public ATMethod meta_grabMethod(ATSymbol methodName) throws InterpreterException {
        JavaMethod choices = Symbiosis.getMethods(wrappedClass_, Reflection.upSelector(methodName), true);
        if (choices != null) {
            return choices;
        } else {
            return super.meta_grabMethod(methodName);
        }
    }

    /**
     * Querying a symbiotic Java class object for its fields results in a table containing
     * both 'native' static Java fields and the fields of its AT symbiont
     */
    public ATTable meta_listFields() throws InterpreterException {
        JavaField[] jFields = Symbiosis.getAllFields(null, wrappedClass_);
        ATObject[] symbiontFields = super.meta_listFields().asNativeTable().elements_;
        return NATTable.atValue(NATTable.collate(jFields, symbiontFields));
    }

    /**
     * Querying a symbiotic Java class object for its methods results in a table containing
     * both 'native' static Java methods and the methods of its AT symbiont
     */
    public ATTable meta_listMethods() throws InterpreterException {
        JavaMethod[] jMethods = Symbiosis.getAllMethods(wrappedClass_, true);
        ATObject[] symbiontMethods = super.meta_listMethods().asNativeTable().elements_;
        return NATTable.atValue(NATTable.collate(jMethods, symbiontMethods));
    }

    public ATBoolean meta_isCloneOf(ATObject original) throws InterpreterException {
        return NATBoolean.atValue(this == original);
    }

    public NATText meta_print() throws InterpreterException {
        return NATText.atValue("<java:" + wrappedClass_.toString() + ">");
    }

    public NATText impl_asCode(TempFieldGenerator objectMap) throws InterpreterException {
        String simpleClassName = wrappedClass_.getSimpleName();
        char[] simpleClassNameChars = simpleClassName.toCharArray();
        if (Character.isLowerCase(simpleClassNameChars[0])) {
            String packageName = wrappedClass_.getPackage().getName();
            return NATText.atValue("jlobby." + packageName + ".class(`" + simpleClassName + ")");
        } else {
            return NATText.atValue("jlobby." + wrappedClass_.getCanonicalName());
        }
    }

    /**
	 * Java class wrappers may be passed by-copy since Java Class
	 * objects are serializable. Upon deserialization however, the
	 * class wrapper returned will be the one local to the receiving
	 * actor.
	 */
    public ATObject meta_pass() throws InterpreterException {
        return this;
    }

    /**
     * A Java Class object remains unique within an actor.
     */
    public ATObject meta_resolve() throws InterpreterException {
        return wrapperFor(wrappedClass_);
    }

    /**
     * If this class represents an interface type, parentTypes
     * are wrappers for all interfaces extended by this Java interface type
     */
    public ATTable base_superTypes() throws InterpreterException {
        return super.impl_invokeAccessor(this, _PTS_NAME_, NATTable.EMPTY).asTable();
    }

    public ATSymbol base_typeName() throws InterpreterException {
        return super.impl_invokeAccessor(this, _TNM_NAME_, NATTable.EMPTY).asSymbol();
    }

    /**
	 * A Java interface type used as a type can only be a subtype of another
	 * Java interface type used as a type, and only if this type is assignable
	 * to the other type.
	 */
    public ATBoolean base_isSubtypeOf(ATTypeTag other) throws InterpreterException {
        if (other instanceof JavaClass) {
            JavaClass otherClass = (JavaClass) other;
            return NATBoolean.atValue(otherClass.wrappedClass_.isAssignableFrom(wrappedClass_));
        } else {
            return NATBoolean._FALSE_;
        }
    }

    /**
	 * When a Java interface type is used to annotate a message, this does not lead to
	 * any additional metadata being added to the message.
	 */
    public ATObject base_annotateMessage(ATObject originalMessage) {
        return originalMessage;
    }

    /**
	 * When a Java interface type is used to annotate a message, this does not lead to
	 * any additional metadata being added to the message.
	 */
    public ATMethod base_annotateMethod(ATMethod originalMethod) {
        return originalMethod;
    }

    /**
     * A symbiotic Java class object has all of the public static fields
     * of its Java class plus all of the fields defined in the AT symbiont.
     */
    protected boolean hasLocalField(ATSymbol atSelector) throws InterpreterException {
        return Symbiosis.hasField(wrappedClass_, Reflection.upSelector(atSelector), true) || super.hasLocalField(atSelector);
    }

    /**
     * A symbiotic Java class object has all of the public static methods
     * of its Java class plus all of the methods defined in the AT symbiont.
     */
    protected boolean hasLocalMethod(ATSymbol atSelector) throws InterpreterException {
        return Symbiosis.hasMethod(wrappedClass_, Reflection.upSelector(atSelector), true) || super.hasLocalMethod(atSelector);
    }

    protected ATObject getLocalField(ATSymbol selector) throws InterpreterException {
        try {
            return Symbiosis.readField(null, wrappedClass_, Reflection.upSelector(selector));
        } catch (XUndefinedSlot e) {
            return super.getLocalField(selector);
        }
    }

    protected ATMethod getLocalMethod(ATSymbol methodName) throws InterpreterException {
        JavaMethod choices = Symbiosis.getMethods(wrappedClass_, Reflection.upSelector(methodName), true);
        if (choices != null) {
            return choices;
        } else {
            return super.getLocalMethod(methodName);
        }
    }

    protected void setLocalField(ATSymbol selector, ATObject value) throws InterpreterException {
        try {
            Symbiosis.writeField(null, wrappedClass_, Reflection.upSelector(selector), value);
        } catch (XUndefinedSlot e) {
            super.setLocalField(selector, value);
        } catch (XUnassignableField e) {
            super.setLocalField(selector, value);
        }
    }
}
