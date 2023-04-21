package org.jpropeller.name;

import org.jpropeller.info.PropInfo;
import org.jpropeller.map.PropMap;
import org.jpropeller.properties.EditableProp;
import org.jpropeller.properties.GeneralProp;
import org.jpropeller.properties.Prop;

/**
 * The name of a Prop&lt;T&gt;
 * 
 * <br />
 * <br />
 *
 * Immutable
 * 
 * <br />
 * <br />
 * 
 * Used to look up {@link Prop}s in a {@link PropMap}, and provided by {@link Prop}s
 * as a useful string name
 * 
 * <br />
 * <br />
 * 
 * One feature of the {@link GenericPropName} as a final class is that the parametric type
 * P and the {@link PropInfo} ALWAYS match up. In addition, the parametric type T
 * and the return of {@link #getPropClass()} ALWAYS match up, with the exception of the
 * awkward case where T is itself a generic type (e.g. List&lt;String&gt;). In this case, the
 * return of {@link #getPropClass()} CANNOT match up, since there is no {@link Class}
 * specific to List&lt;String&gt; - there is just List.class that appliest to List with any
 * parametric type.
 * 
 * <br />
 * <br />
 * 
 * This class deals with the case of T being a generic type in the best practical way - 
 * it makes it known that the class is parametric via the method {@link #isTGeneric()}
 * returning true. The class returned by {@link #getPropClass()} is still the correct
 * class, but obviously does not contain any details of the parametric type. In addition,
 * a user-defined string is provided which can be used to distinguish the type parameters
 * of T. It is recommended that this is used with the normal Java syntax, so for example
 * if T is List&lt;String&gt;, {@link #getPropClass()} should return List.class, and {@link #getPropParametricTypes()}
 * should return "&lt;String&gt;". Note that the parametric type string has whitespace stripped
 * so that e.g. "&lt;String, Integer&gt;" and "&lt;String,Integer &gt;" are equivalent.  
 * 
 * <br />
 * <br />
 * 
 * The subclass
 * {@link PropName} is a stricter version of this class - it is impossible to create a
 * {@link PropName} except by using a non-generic class or accepting warnings. Some methods
 * in JPropeller can only be called using a {@link PropName}, since they require an exactly
 * matching class to guarantee type safety. However other methods do not require this, and
 * so may be used with {@link GenericPropName}
 * 
 * If two {@link GenericPropName}s are equal, they must have equal parametric type 
 * P and T also, except that if T is generic, it may have a different generic type. 
 * This allows {@link GenericPropName}s to be used as keys to look up 
 * {@link Prop} instances and allow (most) casting to generic types safely. 
 * 
 * @param <P>
 * 		The type of {@link Prop} to which this {@link GenericPropName} belongs, e.g.
 * {@link Prop}, {@link EditableProp} etc.
 *
 * @param <T>
 * 		The parametric type of {@link Prop} this {@link GenericPropName} can belong to,
 * this is the type of data in the {@link Prop} itself
 */
public class GenericPropName<P extends GeneralProp<T>, T> {

    String s;

    Class<T> clazz;

    PropInfo info;

    String propParametricTypes;

    int hashCode;

    boolean tGeneric;

    /**
	 * Create a name
	 * @param s
	 * 		The string for the name
	 * @param clazz
	 * 		The class for the parametric type of Prop 
	 * named by this instance
	 * @param tGeneric
	 * 		True if T is a generic type
	 * @param propParametricTypes
	 * 		A string description of parametric types of the prop value. This
	 * will be compared to (and must match exactly in a string sense) the
	 * parametric types of other names with generic T types. Note that the
	 * string provided will have whitespace stripped before use.
	 * An example is if T is List&lt;String&gt;, propParametricTypes should
	 * be "&lt;String&gt;"
	 * @param info
	 * 		The {@link PropInfo} for props named by this name
	 */
    protected GenericPropName(String s, Class<T> clazz, boolean tGeneric, String propParametricTypes, PropInfo info) {
        this.s = s;
        this.clazz = clazz;
        this.tGeneric = tGeneric;
        this.propParametricTypes = propParametricTypes;
        this.info = info;
        hashCode = getString().hashCode() * 31 + getPropClass().hashCode() + getPropInfo().hashCode() * 19 + (isTGeneric() ? 17 : 1) + getPropParametricTypes().hashCode() * 5;
    }

    /**
	 * Get the string value of the name
	 * @return
	 * 		The string value of the name
	 */
    public String getString() {
        return s;
    }

    /**
	 * Returns true if the type T (the type of the data in
	 * named props) is generic. If this is the case, we cannot
	 * assume that the class returned by {@link #getClass()}
	 * completely describes the type of value returned by
	 * props. In this case, we only know the normal type/class
	 * of the value, not the parametric type. In some cases this
	 * will be adequate.
	 * @return
	 * 		True if type T is generic, false otherwise
	 */
    public boolean isTGeneric() {
        return tGeneric;
    }

    /**
	 * This must return "" if T is not generic (i.e. if {@link #isTGeneric()}
	 * returns false)
	 * A string description of parametric types of the prop value. This
	 * will be compared to (and must match exactly in a string sense) the
	 * parametric types of other names with generic T types. Note that the
	 * string provided will have had all whitespace stripped.
	 * @return
	 * 		A string description of the parametric types of T
	 */
    public String getPropParametricTypes() {
        return propParametricTypes;
    }

    /**
	 * Get the parametric class of the Prop
	 * @return
	 * 		clazz
	 */
    public Class<T> getPropClass() {
        return clazz;
    }

    @Override
    public String toString() {
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (hashCode != obj.hashCode()) return false;
        if (obj instanceof GenericPropName) {
            GenericPropName<?, ?> other = (GenericPropName<?, ?>) obj;
            return ((other.getString().equals(getString())) && (other.getPropClass().equals(getPropClass())) && (other.getPropInfo().equals(getPropInfo())) && (other.isTGeneric() == isTGeneric()) && (other.getPropParametricTypes().equals(getPropParametricTypes())));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    /**
	 * Get the type of prop referred to by this name
	 * @return
	 * 		The type of prop
	 */
    public PropInfo getPropInfo() {
        return info;
    }
}
