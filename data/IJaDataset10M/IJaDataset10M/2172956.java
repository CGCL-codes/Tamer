package org.archive.openmbeans.factory;

import java.util.ArrayList;
import java.util.List;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;

/**
 * A mutable object that produces an immutable 
 * {@link OpenMBeanConstructorInfoSupport}.
 * 
 * @author pjack
 */
public class Cons {

    /** The name of the constructor. */
    public String name;

    /** The description of the constructor. */
    public String desc;

    /** The type names of the constructor's parameters. */
    public List<Param> sig = new ArrayList<Param>();

    /** Constructor. */
    public Cons() {
    }

    /**
     * Creates a new OpenMBeanConstructorInfoSupport object populated with 
     * this Cons's fields.
     * 
     * @return  the new OpenMBeanConstructorInfoSupport
     * @throws  IllegalArgumentException  if this object's fields cannot create
     *    a valid OpenMBeanConstructorInfoSupport (eg, if the name field is null)
     */
    public OpenMBeanConstructorInfoSupport make() {
        int sz = sig.size();
        OpenMBeanParameterInfoSupport[] arr = new OpenMBeanParameterInfoSupport[sz];
        for (int i = 0; i < sz; i++) {
            arr[i] = sig.get(i).make();
        }
        return new OpenMBeanConstructorInfoSupport(name, desc, arr);
    }

    /**
     * Converts a list of Cons objects into an array of 
     * OpenMBeanConstructorInfoSupport objects.
     * 
     * @param list   the list of Cons objects
     * @return   the array of OpenMBeanConstructorInfoSupport objects
     */
    public static OpenMBeanConstructorInfoSupport[] array(List<Cons> list) {
        OpenMBeanConstructorInfoSupport[] arr = new OpenMBeanConstructorInfoSupport[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i).make();
        }
        return arr;
    }
}
