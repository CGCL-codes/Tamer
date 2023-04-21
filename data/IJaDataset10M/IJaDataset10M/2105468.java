package edu.vub.at.objects.symbiosis;

import edu.vub.at.exceptions.InterpreterException;
import edu.vub.at.exceptions.XDuplicateSlot;
import edu.vub.at.exceptions.XIllegalOperation;
import edu.vub.at.exceptions.XTypeMismatch;
import edu.vub.at.exceptions.XUnassignableField;
import edu.vub.at.exceptions.XUndefinedSlot;
import edu.vub.at.objects.ATBoolean;
import edu.vub.at.objects.ATField;
import edu.vub.at.objects.ATMethod;
import edu.vub.at.objects.ATNil;
import edu.vub.at.objects.ATObject;
import edu.vub.at.objects.ATTable;
import edu.vub.at.objects.ATTypeTag;
import edu.vub.at.objects.grammar.ATSymbol;
import edu.vub.at.objects.mirrors.Reflection;
import edu.vub.at.objects.natives.NATBoolean;
import edu.vub.at.objects.natives.NATObject;
import edu.vub.at.objects.natives.NATTable;
import edu.vub.at.objects.natives.NATText;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import com.thoughtworks.xstream.core.util.ObjectIdDictionary;

/**
 * JavaObject instances represent java objects under symbiosis.
 * A Java Object is represented in AmbientTalk as an AmbientTalk object where:
 *  - the Java Object's fields and methods correspond to the AmbientTalk object's fields and methods
 *  - overloaded methods are coerced into a single AmbientTalk method, which must be disambiguated at call site
 *  - the Java Object has a shares-a relation with its class (i.e. the class of the object is its dynamic parent)
 * 
 * In addition, a JavaObject carries with it a static type. The static type is used during method dispatch
 * to select the appropriate Java method to be invoked. A JavaObject's static type can be altered by casting it.
 * Casting can be done using JavaClass instances.
 * 
 * @author tvcutsem
 */
public final class JavaObject extends NATObject implements ATObject {

    /**
	 * A thread-local identity hashmap pooling all of the JavaObject wrappers for
	 * the current actor, referring to them using SOFT references, such
	 * that unused wrappers can be GC-ed when running low on memory.
	 * 
	 * Note that the use of an identity hashmap rather than a normal hashmap
	 * is crucial here! Using a normal hashmap compares objects by means of their
	 * equals method, which means that two distinct Java objects could be assigned
	 * the same wrapper, which is obviously unwanted. Using an identity hashmap
	 * avoids this.
	 */
    private static final ThreadLocal _JAVAOBJECT_POOL_ = new ThreadLocal() {

        protected synchronized Object initialValue() {
            return new ObjectIdDictionary();
        }
    };

    /**
	 * Return a unique appearance for the Java object.
	 */
    public static final JavaObject wrapperFor(Object o) {
        ObjectIdDictionary map = (ObjectIdDictionary) _JAVAOBJECT_POOL_.get();
        if (map.containsId(o)) {
            SoftReference ref = (SoftReference) map.lookupId(o);
            JavaObject obj = (JavaObject) ref.get();
            if (obj != null) {
                return obj;
            } else {
                map.removeId(obj);
                obj = new JavaObject(o);
                map.associateId(o, new SoftReference(obj));
                return obj;
            }
        } else {
            JavaObject jo = new JavaObject(o);
            map.associateId(o, new SoftReference(jo));
            return jo;
        }
    }

    private final Object wrappedObject_;

    /**
	 * A JavaObject wrapping an object o has a dynamic SHARES-A parent pointing to the
	 * wrapper of o's class.
	 * 
	 * A symbiotic Java object is tagged with all of the Java interface
	 * type tags that correspond to the interface types implemented by the
	 * wrapped Java object's class.
	 */
    private JavaObject(Object wrappedObject) {
        super(JavaClass.wrapperFor(wrappedObject.getClass()), NATObject._SHARES_A_);
        wrappedObject_ = wrappedObject;
        Class[] extendedInterfaces = wrappedObject_.getClass().getInterfaces();
        if (extendedInterfaces.length > 0) {
            typeTags_ = new ATTypeTag[extendedInterfaces.length];
            for (int i = 0; i < extendedInterfaces.length; i++) {
                typeTags_[i] = JavaClass.wrapperFor(extendedInterfaces[i]);
            }
        }
    }

    /**
	 * @return the Java object denoted by this JavaObject
	 */
    public Object getWrappedObject() {
        return wrappedObject_;
    }

    public boolean isJavaObjectUnderSymbiosis() {
        return true;
    }

    public JavaObject asJavaObjectUnderSymbiosis() throws XTypeMismatch {
        return this;
    }

    /**
     * Fields can be defined within a symbiotic Java object. They are added
     * to its AmbientTalk symbiont, but only if they do not clash with already
     * existing field names.
     */
    public ATNil meta_defineField(ATSymbol name, ATObject value) throws InterpreterException {
        if (Symbiosis.hasField(wrappedObject_.getClass(), Reflection.upSelector(name), false)) {
            throw new XDuplicateSlot(name);
        } else {
            return super.meta_defineField(name, value);
        }
    }

    /**
     * Cloning a symbiotic object is not always possible as Java has no uniform cloning semantics.
     * Even if the symbiotic object implements java.lang.Cloneable, a clone cannot be made of
     * the wrapped object as java.lang.Object's clone method is protected, and must be overridden
     * by a public clone method in the cloneable subclass.
     */
    public ATObject meta_clone() throws InterpreterException {
        throw new XIllegalOperation("Cannot clone Java object under symbiosis: " + wrappedObject_.toString());
    }

    /**
	 * Invoking new on a JavaObject will exhibit the same behaviour as if new was invoked on the parent class.
	 */
    public ATObject meta_newInstance(ATTable initargs) throws InterpreterException {
        return base_super().meta_newInstance(initargs);
    }

    /**
     * Methods can be added to a symbiotic Java object provided they do not already
     * exist in the Java object's class.
     */
    public ATNil meta_addMethod(ATMethod method) throws InterpreterException {
        ATSymbol name = method.base_name();
        if (Symbiosis.hasMethod(wrappedObject_.getClass(), Reflection.upSelector(name), false)) {
            throw new XDuplicateSlot(name);
        } else {
            return super.meta_addMethod(method);
        }
    }

    /**
     * Fields can be grabbed from a symbiotic Java object. Fields that correspond
     * to fields in the Java object's class are returned as JavaField instances.
     */
    public ATField meta_grabField(ATSymbol fieldName) throws InterpreterException {
        try {
            return new JavaField(wrappedObject_, Symbiosis.getField(wrappedObject_.getClass(), Reflection.upSelector(fieldName), false));
        } catch (XUndefinedSlot e) {
            return super.meta_grabField(fieldName);
        }
    }

    /**
     * Methods can be grabbed from a symbiotic Java object. Methods that correspond
     * to methods in the Java object's class are returned as JavaMethod instances.
     */
    public ATMethod meta_grabMethod(ATSymbol methodName) throws InterpreterException {
        JavaMethod choices = Symbiosis.getMethods(wrappedObject_.getClass(), Reflection.upSelector(methodName), false);
        if (choices != null) {
            return choices;
        } else {
            return super.meta_grabMethod(methodName);
        }
    }

    /**
     * Querying a symbiotic Java object for its fields results in a table containing
     * both the 'native' Java fields and the fields of its AT symbiont
     */
    public ATTable meta_listFields() throws InterpreterException {
        JavaField[] jFields = Symbiosis.getAllFields(wrappedObject_, wrappedObject_.getClass());
        ATObject[] symbiontFields = super.meta_listFields().asNativeTable().elements_;
        return NATTable.atValue(NATTable.collate(jFields, symbiontFields));
    }

    /**
     * Querying a symbiotic Java object for its methods results in a table containing
     * both all 'native' Java instance methods and the methods of its AT symbiont 
     */
    public ATTable meta_listMethods() throws InterpreterException {
        JavaMethod[] jMethods = Symbiosis.getAllMethods(wrappedObject_.getClass(), false);
        ATObject[] symbiontMethods = super.meta_listMethods().asNativeTable().elements_;
        return NATTable.atValue(NATTable.collate(jMethods, symbiontMethods));
    }

    public ATBoolean meta_isCloneOf(ATObject original) throws InterpreterException {
        return NATBoolean.atValue(this == original);
    }

    public NATText meta_print() throws InterpreterException {
        return NATText.atValue("<java:" + wrappedObject_.toString() + ">");
    }

    /**
	 * Passing a Java Object wrapper to another actor has the following effect:
	 *  - if the wrapped Java object is serializable, the symbiotic AmbientTalk object
	 *    is treated as by copy (i.e. as an isolate).
	 *  - if the wrapped Java object is not serializable, the symbiotic AmbientTalk object
	 *    is treated as by reference and a far reference will be passed instead.
	 */
    public ATObject meta_pass() throws InterpreterException {
        if (wrappedObject_ instanceof Serializable) {
            return this;
        } else {
            return super.meta_pass();
        }
    }

    /**
	 * If the wrapped object was serializable, we may be asked to resolve ourselves.
	 */
    public ATObject meta_resolve() throws InterpreterException {
        if (wrappedObject_ instanceof Serializable) {
            return this;
        } else {
            return super.meta_resolve();
        }
    }

    /**
     * A symbiotic Java object has all of the public non-static fields
     * of its Java class plus all of the fields defined in the AT symbiont.
     */
    protected boolean hasLocalField(ATSymbol atSelector) throws InterpreterException {
        return Symbiosis.hasField(wrappedObject_.getClass(), Reflection.upSelector(atSelector), false) || super.hasLocalField(atSelector);
    }

    /**
     * A symbiotic Java object has all of the public non-static methods
     * of its Java class plus all of the methods defined in the AT symbiont.
     */
    protected boolean hasLocalMethod(ATSymbol atSelector) throws InterpreterException {
        return Symbiosis.hasMethod(wrappedObject_.getClass(), Reflection.upSelector(atSelector), false) || super.hasLocalMethod(atSelector);
    }

    protected ATObject getLocalField(ATSymbol selector) throws InterpreterException {
        try {
            return Symbiosis.readField(wrappedObject_, wrappedObject_.getClass(), Reflection.upSelector(selector));
        } catch (XUndefinedSlot e) {
            return super.getLocalField(selector);
        }
    }

    protected ATMethod getLocalMethod(ATSymbol methodName) throws InterpreterException {
        JavaMethod choices = Symbiosis.getMethods(wrappedObject_.getClass(), Reflection.upSelector(methodName), false);
        if (choices != null) {
            return choices;
        } else {
            return super.getLocalMethod(methodName);
        }
    }

    protected void setLocalField(ATSymbol selector, ATObject value) throws InterpreterException {
        try {
            Symbiosis.writeField(wrappedObject_, wrappedObject_.getClass(), Reflection.upSelector(selector), value);
        } catch (XUndefinedSlot e) {
            super.setLocalField(selector, value);
        } catch (XUnassignableField e) {
            super.setLocalField(selector, value);
        }
    }
}
