package org.omg.PortableServer.POAPackage;

import gnu.CORBA.EmptyExceptionHolder;
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
 * The helper operations for the exception {@link ObjectNotActive}.
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public abstract class ObjectNotActiveHelper {

    /**
   * The cached typecode value, computed only once.
   */
    private static TypeCode typeCode;

    /**
   * Create the ObjectNotActive typecode (empty structure,
   * named "ObjectNotActive").
   */
    public static TypeCode type() {
        if (typeCode == null) {
            ORB orb = ORB.init();
            StructMember[] members = new StructMember[0];
            typeCode = orb.create_exception_tc(id(), "ObjectNotActive", members);
        }
        return typeCode;
    }

    /**
   * Insert the ObjectNotActive into the given Any.
   *
   * @param any the Any to insert into.
   * @param that the ObjectNotActive to insert.
   */
    public static void insert(Any any, ObjectNotActive that) {
        any.insert_Streamable(new EmptyExceptionHolder(that, type()));
    }

    /**
   * Extract the ObjectNotActive from given Any.
   *
   * @throws BAD_OPERATION if the passed Any does not contain ObjectNotActive.
   */
    public static ObjectNotActive extract(Any any) {
        try {
            EmptyExceptionHolder h = (EmptyExceptionHolder) any.extract_Streamable();
            return (ObjectNotActive) h.value;
        } catch (ClassCastException cex) {
            BAD_OPERATION bad = new BAD_OPERATION("ObjectNotActive expected");
            bad.minor = Minor.Any;
            bad.initCause(cex);
            throw bad;
        }
    }

    /**
   * Get the ObjectNotActive repository id.
   *
   * @return "IDL:omg.org/PortableServer/POA/ObjectNotActive:1.0", always.
   */
    public static String id() {
        return "IDL:omg.org/PortableServer/POA/ObjectNotActive:1.0";
    }

    /**
   * Read the exception from the CDR intput stream.
   *
   * @param input a org.omg.CORBA.portable stream to read from.
   */
    public static ObjectNotActive read(InputStream input) {
        String id = input.read_string();
        ObjectNotActive value = new ObjectNotActive(id);
        return value;
    }

    /**
   * Write the exception to the CDR output stream.
   *
   * @param output a org.omg.CORBA.portable stream stream to write into.
   * @param value a value to write.
   */
    public static void write(OutputStream output, ObjectNotActive value) {
        output.write_string(id());
    }
}
