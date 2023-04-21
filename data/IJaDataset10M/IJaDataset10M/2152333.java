package org.progeeks.meta;

import org.progeeks.util.Fixable;
import org.progeeks.util.IllegalModificationException;
import org.progeeks.util.ObjectUtils;

/**
 *  Describes the characteristics of a single functor.
 *  A functor is an action that can be performed on an object
 *  and the FunctorInfo describes the functor, the types of
 *  arguments it takes, the return type, etc..
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public class FunctorInfo implements Fixable, java.io.Serializable, Comparable {

    private boolean fixed = false;

    private String functorName;

    private String name;

    private String description;

    private PropertyType returnType;

    private MetaClass parameterClass;

    /**
     *  Creates an empty and unfixed functor info suitable for bean-like
     *  property loading.
     */
    public FunctorInfo() {
    }

    /**
     *  Creates an unfixed functor info initialized with the specified values.
     */
    public FunctorInfo(String functorName, String name, String description, PropertyType returnType, MetaClass parameterClass) {
        this.functorName = functorName;
        this.name = name;
        this.description = description;
        this.returnType = returnType;
        this.parameterClass = parameterClass;
    }

    /**
     *  Creates an unfixed functor info initialized with the specified values.
     */
    public FunctorInfo(String functorName, String name, String description, MetaClass parameterClass) {
        this(functorName, name, description, null, parameterClass);
    }

    /**
     *  Creates an unfixed functor info initialized with the specified values.
     */
    public FunctorInfo(String functorName, String name, String description, PropertyType returnType) {
        this(functorName, name, description, returnType, null);
    }

    /**
     *  Creates an unfixed functor info initialized with the specified values.
     */
    public FunctorInfo(String functorName, String name, String description) {
        this(functorName, name, description, null, null);
    }

    /**
     *  Creates a new FunctorInfo that is a clone of the specified
     *  FunctorInfo but with a different return type and parameter class.
     */
    public FunctorInfo(FunctorInfo info, PropertyType returnType, MetaClass parameterClass) {
        this(info.getFunctorName(), info.getName(), info.getDescription(), returnType, parameterClass);
    }

    /**
     *  Sets the mutable/immutable state of this object.  A value
     *  of true indicates that the object will be immutable after
     *  this call.
     *
     *  @throws IllegalModificationException
     *                      if the object does not support a specific
     *                      mutability shift.  For example, most immutable objects
     *                      will not allow setFixed( false ).
     */
    public void setFixed(boolean fixed) throws IllegalModificationException {
        if (!fixed && isFixed()) throw new IllegalModificationException("Fixed object cannot be modified.");
        if (fixed) {
            if (functorName == null) throw new RuntimeException("Property name must be specified.");
            if (name == null) name = PropertyInfo.getSplitName(functorName);
            if (description == null) description = name;
        }
        this.fixed = fixed;
    }

    /**
     *  Returns true if this object cannot be modified.
     */
    public boolean isFixed() {
        return (fixed);
    }

    protected void checkFixed() {
        if (isFixed()) throw new IllegalModificationException("Fixed object cannot be modified.");
    }

    /**
     *  Sets the identifying name for this functor.
     *  Calls to this method are only allowed if isFixed() returns false.
     */
    public void setFunctorName(String functorName) {
        checkFixed();
        this.functorName = functorName;
    }

    /**
     *  Returns the identifying name for this functor.
     */
    public String getFunctorName() {
        return (functorName);
    }

    /**
     *  Sets the display name for this functor.
     *  Calls to this method are only allowed if isFixed() returns false.
     */
    public void setName(String name) {
        checkFixed();
        this.name = name;
    }

    /**
     *  Returns the human-readable display name for this functor.
     */
    public String getName() {
        return (name);
    }

    /**
     *  Sets the expanded description for this functor.
     *  Calls to this method are only allowed if isFixed() returns false.
     */
    public void setDescription(String description) {
        checkFixed();
        this.description = description;
    }

    /**
     *  Returns the expanded description for this functor.
     */
    public String getDescription() {
        return (description);
    }

    /**
     *  Sets the return type for this functor.  Set to null for void
     *  functors.
     *  Calls to this method are only allowed if isFixed() returns false.
     */
    public void setReturnType(PropertyType returnType) {
        checkFixed();
        this.returnType = returnType;
    }

    /**
     *  Returns the return type for the functor or null if this
     *  FunctorInfo represents a void functor.
     */
    public PropertyType getReturnType() {
        return (returnType);
    }

    /**
     *  Sets the parameter meta-class for this functor.  Set to null
     *  for functors that do not take parameters.
     *  Calls to this method are only allowed if isFixed() returns false.
     */
    public void setParameterMetaClass(MetaClass parameterClass) {
        checkFixed();
        this.parameterClass = parameterClass;
    }

    /**
     *  Returns the parameter meta-class for this functor or null if
     *  the described functor takes no arguments.
     */
    public MetaClass getParameterMetaClass() {
        return (parameterClass);
    }

    public int hashCode() {
        return (functorName.hashCode());
    }

    public boolean equals(FunctorInfo obj) {
        if (!ObjectUtils.areEqual(functorName, obj.functorName)) return (false);
        if (!ObjectUtils.areEqual(name, obj.name)) return (false);
        if (!ObjectUtils.areEqual(description, obj.description)) return (false);
        if (!ObjectUtils.areEqual(returnType, obj.returnType)) return (false);
        if (!ObjectUtils.areEqual(parameterClass, obj.parameterClass)) return (false);
        return (true);
    }

    public boolean equals(Object obj) {
        if (obj == null) return (false);
        if (obj.getClass() == getClass()) return (equals((FunctorInfo) obj));
        return (false);
    }

    public int compareTo(Object o) {
        FunctorInfo i = (FunctorInfo) o;
        int c = functorName.compareTo(i.functorName);
        if (c != 0) return (c);
        if (parameterClass == null && i.parameterClass == null) return (0);
        if (parameterClass == null) return (-1);
        if (i.parameterClass == null) return (1);
        return (parameterClass.getName().compareTo(i.parameterClass.getName()));
    }

    public String toString() {
        return ("FunctorInfo[" + functorName + ", " + name + ", " + description + ", returnType=" + returnType + ", parmClass=" + parameterClass + "]");
    }
}
