package org.jpropeller.properties.primitive.impl;

import org.jpropeller.bean.Bean;
import org.jpropeller.info.PropInfo;
import org.jpropeller.map.PropMap;
import org.jpropeller.name.PropName;
import org.jpropeller.properties.UneditableProp;

/**
 * A UneditableProp which can only have a value of a primitive wrapper
 * type - that is:
 * 
 * Boolean
 * Short
 * Integer
 * Long
 * String
 * Float
 * Double
 *
 * All these wrapper types are immutable, so this prop only implements
 * shallow change notification
 *
 * @author shingoki
 *
 * @param <T>
 * 		The type of the Prop value
 */
public class UneditablePropPrimitive<T> implements UneditableProp<T> {

    PropMap propMap;

    T value;

    PropName<UneditableProp<T>, T> name;

    /**
	 * Create a prop
	 * @param name
	 * 		The name of the prop
	 * @param value
	 * 		The initial value of the prop
	 */
    public UneditablePropPrimitive(PropName<UneditableProp<T>, T> name, T value) {
        this.value = value;
        this.name = name;
    }

    @Override
    public void setPropMap(PropMap set) {
        if (propMap != null) throw new IllegalArgumentException("Prop '" + this + "' already has its PropMap set to '" + propMap + "'");
        if (set == null) throw new IllegalArgumentException("PropMap must be non-null");
        this.propMap = set;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public Bean getBean() {
        return props().getBean();
    }

    @Override
    public PropName<UneditableProp<T>, T> getName() {
        return name;
    }

    @Override
    public PropMap props() {
        return propMap;
    }

    @Override
    public PropInfo getInfo() {
        return PropInfo.UNEDITABLE;
    }

    @Override
    public String toString() {
        return "Uneditable Primitive Prop '" + getName().getString() + "' = '" + get() + "'";
    }

    /**
	 * Create a prop
	 * @param name
	 * 		The name of the prop
	 * @param value
	 * 		The initial value of the prop
	 * @return
	 * 		The new prop
	 */
    public static UneditablePropPrimitive<Double> create(String name, Double value) {
        return new UneditablePropPrimitive<Double>(PropName.uneditable(name, Double.class), value);
    }

    /**
	 * Create a prop
	 * @param name
	 * 		The name of the prop
	 * @param value
	 * 		The initial value of the prop
	 * @return
	 * 		The new prop
	 */
    public static UneditablePropPrimitive<Integer> create(String name, Integer value) {
        return new UneditablePropPrimitive<Integer>(PropName.uneditable(name, Integer.class), value);
    }

    /**
	 * Create a prop
	 * @param name
	 * 		The name of the prop
	 * @param value
	 * 		The initial value of the prop
	 * @return
	 * 		The new prop
	 */
    public static UneditablePropPrimitive<String> create(String name, String value) {
        return new UneditablePropPrimitive<String>(PropName.uneditable(name, String.class), value);
    }

    /**
	 * Create a prop
	 * @param name
	 * 		The name of the prop
	 * @param value
	 * 		The initial value of the prop
	 * @return
	 * 		The new prop
	 */
    public static UneditablePropPrimitive<Float> create(String name, Float value) {
        return new UneditablePropPrimitive<Float>(PropName.uneditable(name, Float.class), value);
    }

    /**
	 * Create a prop
	 * @param name
	 * 		The name of the prop
	 * @param value
	 * 		The initial value of the prop
	 * @return
	 * 		The new prop
	 */
    public static UneditablePropPrimitive<Byte> create(String name, Byte value) {
        return new UneditablePropPrimitive<Byte>(PropName.uneditable(name, Byte.class), value);
    }

    /**
	 * Create a prop
	 * @param name
	 * 		The name of the prop
	 * @param value
	 * 		The initial value of the prop
	 * @return
	 * 		The new prop
	 */
    public static UneditablePropPrimitive<Short> create(String name, Short value) {
        return new UneditablePropPrimitive<Short>(PropName.uneditable(name, Short.class), value);
    }

    /**
	 * Create a prop
	 * @param name
	 * 		The name of the prop
	 * @param value
	 * 		The initial value of the prop
	 * @return
	 * 		The new prop
	 */
    public static UneditablePropPrimitive<Long> create(String name, Long value) {
        return new UneditablePropPrimitive<Long>(PropName.uneditable(name, Long.class), value);
    }

    /**
	 * Create a prop
	 * @param name
	 * 		The name of the prop
	 * @param value
	 * 		The initial value of the prop
	 * @return
	 * 		The new prop
	 */
    public static UneditablePropPrimitive<Boolean> create(String name, Boolean value) {
        return new UneditablePropPrimitive<Boolean>(PropName.uneditable(name, Boolean.class), value);
    }
}
