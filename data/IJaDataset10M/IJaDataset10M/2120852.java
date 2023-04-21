package org.omg.DynamicAny;

import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Any;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.portable.OutputStream;

/**
 * The helper operations for {@link DynFixed}. Following the 1.5 JDK
 * specifications, DynFixed is always a local object, so the two methods of this
 * helper ({@link #read} and {@link #write} are not in use, always throwing
 * {@link MARSHAL}.
 * 
 * @specnote always throwing MARSHAL in read and write ensures compatibility
 * with other popular implementations like Sun's.
 * 
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public abstract class DynFixedHelper {

    /**
   * Cast the passed object into the DynFixed. As DynFixed is a local object,
   * the method just uses java type cast.
   * 
   * @param obj the object to narrow.
   * @return narrowed instance.
   * @throws BAD_PARAM if the passed object is not a DynFixed.
   */
    public static DynFixed narrow(org.omg.CORBA.Object obj) {
        try {
            return (DynFixed) obj;
        } catch (ClassCastException cex) {
            throw new BAD_PARAM(obj.getClass().getName() + " is not a DynFixed");
        }
    }

    /**
   * Narrow the given object to the DynFixed. For the objects that are
   * always local, this operation does not differ from the ordinary
   * {@link #narrow} (ClassCastException will be thrown if narrowing something
   * different).
   * 
   * @param obj the object to cast.
   * 
   * @return the casted DynFixed.
   * 
   * @since 1.5 
   * 
   * @see OMG issue 4158.
   */
    public static DynFixed unchecked_narrow(org.omg.CORBA.Object obj) {
        return narrow(obj);
    }

    /**
   * Get the type code of the {@link DynFixed}.
   */
    public static TypeCode type() {
        return ORB.init().create_interface_tc(id(), "DynFixed");
    }

    /**
   * Insert the DynFixed into the given Any.
   * 
   * @param any the Any to insert into.
   * 
   * @param that the DynFixed to insert.
   */
    public static void insert(Any any, DynFixed that) {
        any.insert_Object(that);
    }

    /**
   * Extract the DynFixed from given Any.
   * 
   * @throws BAD_OPERATION if the passed Any does not contain DynFixed.
   */
    public static DynFixed extract(Any any) {
        return narrow(any.extract_Object());
    }

    /**
   * Get the DynFixed repository id.
   * 
   * @return "IDL:omg.org/DynamicAny/DynFixed:1.0", always.
   */
    public static String id() {
        return "IDL:omg.org/DynamicAny/DynFixed:1.0";
    }

    /**
   * This should read DynFixed from the CDR input stream, but (following the JDK
   * 1.5 API) it does not.
   * 
   * @param input a org.omg.CORBA.portable stream to read from.
   * 
   * @specenote Sun throws the same exception.
   * 
   * @throws MARSHAL always.
   */
    public static DynFixed read(InputStream input) {
        throw new MARSHAL(DynAnyFactoryHelper.not_applicable(id()));
    }

    /**
   * This should read DynFixed from the CDR input stream, but (following the JDK
   * 1.5 API) it does not.
   * 
   * @param input a org.omg.CORBA.portable stream to read from.
   * 
   * @specenote Sun throws the same exception.
   * 
   * @throws MARSHAL always.
   */
    public static void write(OutputStream output, DynFixed value) {
        throw new MARSHAL(DynAnyFactoryHelper.not_applicable(id()));
    }
}
