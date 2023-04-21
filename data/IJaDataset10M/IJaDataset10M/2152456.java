package tudresden.ocl20.pivot.standardlibrary.java.internal.library;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.Platform;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclBag;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclBoolean;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclCollection;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclEnumLiteral;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclOrderedSet;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclRoot;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclSequence;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclSet;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.OclType;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.base.AbstractOclAdapter;

/**
 * 
 * 
 * @author Ronny Brandt
 * @version 1.0 31.08.2007
 */
public class JavaOclRoot extends AbstractOclAdapter implements OclRoot {

    protected String undefinedreason = null;

    /**
	 * Instantiates a new java ocl root.
	 * 
	 * @param adaptee
	 *            the adaptee
	 */
    public JavaOclRoot(Object adaptee) {
        super(adaptee);
    }

    /**
	 * This could be done via ModelInstanceProvider
	 * 
	 * @see tudresden.ocl20.pivot.essentialocl.standardlibrary.OclRoot#allInstances()
	 */
    public <T extends OclRoot> OclSet<T> allInstances() {
        return null;
    }

    public OclRoot getPropertyValue(String propertyName) throws NoSuchFieldException, IllegalAccessException {
        Field field = null;
        NoSuchFieldException nsfe = null;
        Class c = null;
        Object object = null;
        if (getAdaptee() instanceof Class) {
            c = (Class) getAdaptee();
            object = null;
        } else {
            c = getAdapteeClass();
            object = getAdaptee();
        }
        while (c != null && field == null) {
            try {
                field = c.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e1) {
                if (nsfe == null) nsfe = e1;
                try {
                    field = c.getDeclaredField("my" + StringUtils.capitalize(propertyName));
                } catch (NoSuchFieldException e2) {
                }
            } finally {
                c = c.getSuperclass();
            }
        }
        if (field == null) {
            c = this.getClass();
            object = this;
            while (c != null && field == null) {
                try {
                    field = c.getDeclaredField(propertyName);
                } catch (NoSuchFieldException e1) {
                } finally {
                    c = c.getSuperclass();
                }
            }
        }
        if (field == null) {
            try {
                return invokeOperation("get" + StringUtils.capitalize(propertyName), new OclRoot[0]);
            } catch (NoSuchMethodException e) {
                throw nsfe;
            }
        } else {
            field.setAccessible(true);
            Object property = null;
            try {
                property = field.get(object);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (property instanceof OclRoot) return (OclRoot) property; else if (property == null) return JavaOclVoid.getInstance(); else if (property.getClass().isEnum()) return (OclRoot) Platform.getAdapterManager().getAdapter(property, OclEnumLiteral.class); else return (OclRoot) Platform.getAdapterManager().getAdapter(property, OclRoot.class);
        }
    }

    public OclRoot getPropertyValue(String propertyName, OclRoot... qualifier) {
        return null;
    }

    public OclBag<OclRoot> getPropertyValueAsBag(String propertyName) throws NoSuchFieldException, IllegalAccessException {
        OclRoot property = getPropertyValue(propertyName);
        if (property instanceof OclCollection) return ((OclCollection<OclRoot>) property).asBag(); else {
            List<OclRoot> temp = new ArrayList<OclRoot>();
            temp.add(property);
            return new JavaOclBag(temp);
        }
    }

    public OclOrderedSet<OclRoot> getPropertyValueAsOrderedSet(String propertyName) throws NoSuchFieldException, IllegalAccessException {
        OclRoot property = getPropertyValue(propertyName);
        if (property instanceof OclCollection) return ((OclCollection<OclRoot>) property).asOrderedSet(); else {
            List<OclRoot> temp = new ArrayList<OclRoot>();
            temp.add(property);
            return new JavaOclOrderedSet(temp);
        }
    }

    public OclSequence<OclRoot> getPropertyValueAsSequence(String propertyName) throws NoSuchFieldException, IllegalAccessException {
        OclRoot property = getPropertyValue(propertyName);
        if (property instanceof OclCollection) return ((OclCollection<OclRoot>) property).asSequence(); else {
            List<OclRoot> temp = new ArrayList<OclRoot>();
            temp.add(property);
            return new JavaOclSequence(temp);
        }
    }

    public OclSet<OclRoot> getPropertyValueAsSet(String propertyName) throws NoSuchFieldException, IllegalAccessException {
        OclRoot property = getPropertyValue(propertyName);
        return property.asSet();
    }

    /**
	 * Invoke model operation.
	 * 
	 * @param operationName
	 *            the operation name
	 * @param parameters
	 *            the parameters
	 * 
	 * @return the ocl root
	 * 
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
    protected OclRoot invokeModelOperation(String operationName, OclRoot... parameters) throws NoSuchMethodException {
        Method m = null;
        Class params_class[];
        Object params_objects[];
        if (parameters.length == 0) {
            params_class = null;
            params_objects = null;
        } else {
            params_class = new Class[parameters.length];
            params_objects = new Object[parameters.length];
        }
        OclRoot result = null;
        Class c = null;
        Object o = null;
        try {
            if (getAdaptee() instanceof Class) {
                c = (Class) getAdaptee();
                o = null;
            } else {
                c = getAdapteeClass();
                o = getAdaptee();
            }
            for (ListIterator<OclRoot> orIt = Arrays.asList(parameters).listIterator(); orIt.hasNext(); ) {
                OclRoot or = orIt.next();
                params_class[orIt.previousIndex()] = or.getAdapteeClass();
                params_objects[orIt.previousIndex()] = or.getAdaptee();
            }
            m = new BetterMethodFinder(c).findMethod(operationName, params_class);
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            throw e1;
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        }
        Object inv = null;
        m.setAccessible(true);
        try {
            inv = m.invoke(c.cast(o), params_objects);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (inv instanceof OclRoot) result = (OclRoot) inv; else if (inv == null) result = JavaOclVoid.getInstance(); else result = (OclRoot) Platform.getAdapterManager().getAdapter(inv, OclRoot.class);
        return result;
    }

    /**
	 * Invoke library operation.
	 * 
	 * @param operationName
	 *            the operation name
	 * @param parameters
	 *            the parameters
	 * 
	 * @return the ocl root
	 * 
	 * @throws NoSuchMethodException
	 *             the no such method exception
	 */
    public OclRoot invokeLibraryOperation(String operationName, OclRoot... parameters) throws NoSuchMethodException {
        Method m = null;
        Class params_class[];
        Object params_objects[];
        if (parameters.length == 0) {
            params_class = null;
            params_objects = null;
        } else {
            params_class = new Class[parameters.length];
            params_objects = new Object[parameters.length];
        }
        OclRoot result = null;
        Class c = null;
        Object o = null;
        try {
            c = this.getClass();
            for (ListIterator<OclRoot> orIt = Arrays.asList(parameters).listIterator(); orIt.hasNext(); ) {
                OclRoot or = orIt.next();
                params_class[orIt.previousIndex()] = or.getClass();
                params_objects[orIt.previousIndex()] = or;
            }
            try {
                m = new BetterMethodFinder(c).findMethod(operationName, params_class);
                o = this;
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                throw e;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            throw e1;
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        }
        Object inv = null;
        m.setAccessible(true);
        try {
            inv = m.invoke(c.cast(o), params_objects);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (inv instanceof OclRoot) result = (OclRoot) inv; else if (inv == null) result = JavaOclVoid.getInstance(); else result = (OclRoot) Platform.getAdapterManager().getAdapter(inv, OclRoot.class);
        return result;
    }

    public OclRoot invokeOperation(String operationName, OclRoot... parameters) throws NoSuchMethodException {
        try {
            return invokeModelOperation(operationName, parameters);
        } catch (NoSuchMethodException e) {
            return invokeLibraryOperation(operationName, parameters);
        }
    }

    public OclBoolean isEqualTo(OclRoot object2) {
        if (isOclUndefined().isTrue()) {
            OclBoolean ret = JavaOclBoolean.getInstance(null);
            ret.setUndefinedreason(getUndefinedreason());
            return ret;
        }
        if (object2.isOclUndefined().isTrue()) {
            OclBoolean ret = JavaOclBoolean.getInstance(null);
            ret.setUndefinedreason(object2.getUndefinedreason());
            return ret;
        }
        return JavaOclBoolean.getInstance(getAdaptee().equals(object2.getAdaptee()));
    }

    public OclBoolean isNotEqualTo(OclRoot object2) {
        return isEqualTo(object2).not();
    }

    public OclBoolean isOclUndefined() {
        return JavaOclBoolean.getInstance(this.undefinedreason != null);
    }

    public OclRoot oclAsType(OclType typespec) {
        return typespec.createInstance(this);
    }

    public OclBoolean oclIsInvalid() {
        return JavaOclBoolean.getInstance(false);
    }

    public OclBoolean oclIsKindOf(OclType typespec) {
        if (isOclUndefined().isTrue()) {
            OclBoolean ret = JavaOclBoolean.getInstance(null);
            ret.setUndefinedreason(getUndefinedreason());
            return ret;
        } else return JavaOclBoolean.getInstance(typespec.isOfKind(this).isTrue());
    }

    /**
	 * Has to be done via interpreter
	 * 
	 * @see tudresden.ocl20.pivot.essentialocl.standardlibrary.OclRoot#oclIsNew()
	 */
    public OclBoolean oclIsNew() {
        return null;
    }

    public OclBoolean oclIsTypeOf(OclType typespec) {
        if (isOclUndefined().isTrue()) {
            OclBoolean ret = JavaOclBoolean.getInstance(null);
            ret.setUndefinedreason(getUndefinedreason());
            return ret;
        } else return JavaOclBoolean.getInstance(typespec.isOfType(this).isTrue());
    }

    public void setUndefinedreason(String undefinedreason) {
        this.undefinedreason = undefinedreason;
    }

    public String getUndefinedreason() {
        return undefinedreason;
    }

    public <T extends OclRoot> OclSet<T> asSet() {
        Set<T> col = new HashSet<T>(Arrays.asList((T) this));
        OclSet<T> ret = new JavaOclSet<T>(col);
        return ret;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "(" + (getAdaptee() != null ? getAdaptee().toString() : (undefinedreason != null ? "undefined: " + undefinedreason : "")) + ")";
    }

    public OclType getType() {
        return null;
    }
}
