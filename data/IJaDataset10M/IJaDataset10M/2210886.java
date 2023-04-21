package org.omg.IOP;

import gnu.CORBA.Minor;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/**
* A helper operations for the structure {@link ServiceContext}.
*
* @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
*/
public abstract class ServiceContextHelper {

    /**
   * The cached typecode value, computed only once.
   */
    private static TypeCode typeCode;

    /**
   * Create the ServiceContext typecode (structure,
   * named "ServiceContext").
   * The typecode states that the structure contains the
   * following fields: context_id, context_data.
   */
    public static TypeCode type() {
        if (typeCode == null) {
            ORB orb = ORB.init();
            StructMember[] members = new StructMember[2];
            TypeCode field;
            field = orb.create_alias_tc("IDL:omg.org/IOP/ServiceId:1.0", "ServiceId", orb.get_primitive_tc(TCKind.tk_ulong));
            members[0] = new StructMember("context_id", field, null);
            field = orb.create_sequence_tc(0, orb.get_primitive_tc(TCKind.tk_octet));
            members[1] = new StructMember("context_data", field, null);
            typeCode = orb.create_struct_tc(id(), "ServiceContext", members);
        }
        return typeCode;
    }

    /**
  * Insert the ServiceContext into the given Any.
  * This method uses the ServiceContextHolder.
  *
  * @param any the Any to insert into.
  * @param that the ServiceContext to insert.
  */
    public static void insert(Any any, ServiceContext that) {
        any.insert_Streamable(new ServiceContextHolder(that));
    }

    /**
   * Extract the ServiceContext from given Any.
   * This method uses the ServiceContextHolder.
   *
   * @throws BAD_OPERATION if the passed Any does not contain ServiceContext.
   */
    public static ServiceContext extract(Any any) {
        try {
            return ((ServiceContextHolder) any.extract_Streamable()).value;
        } catch (ClassCastException cex) {
            BAD_OPERATION bad = new BAD_OPERATION("ServiceContext expected");
            bad.minor = Minor.Any;
            bad.initCause(cex);
            throw bad;
        }
    }

    /**
   * Get the ServiceContext repository id.
   *
   * @return "IDL:omg.org/IOP/ServiceContext:1.0", always.
   */
    public static String id() {
        return "IDL:omg.org/IOP/ServiceContext:1.0";
    }

    /**
   * Read the context from the CDR intput stream (first id, then
   * data as a flexible length byte sequence).
   *
   * @param input a org.omg.CORBA.portable stream to read from.
   */
    public static ServiceContext read(InputStream input) {
        ServiceContext value = new ServiceContext();
        value.context_id = input.read_long();
        value.context_data = new byte[input.read_long()];
        for (int i0 = 0; i0 < value.context_data.length; i0++) value.context_data[i0] = input.read_octet();
        return value;
    }

    /**
   * Write the context to the CDR output stream (first id, then
   * data as a flexible length byte sequence).
   *
   * @param output a org.omg.CORBA.portable stream stream to write into.
   * @param value a value to write.
   */
    public static void write(OutputStream output, ServiceContext value) {
        output.write_long(value.context_id);
        output.write_long(value.context_data.length);
        for (int i0 = 0; i0 < value.context_data.length; i0++) output.write_octet(value.context_data[i0]);
    }
}
