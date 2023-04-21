package org.omg.CosNaming.NamingContextExtPackage;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/**
 * Helper operations for the string name.
 * The string name is directly mapped into java String.
 * There is no separate "String name" class in the implementation.
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public abstract class StringNameHelper {

    /**
   * The string name repository id.
   */
    private static String _id = "IDL:omg.org/CosNaming/NamingContextExt/StringName:1.0";

    /**
   * The cached type code (string alias).
   */
    private static TypeCode typeCode;

    /**
   * Just extracts string from this {@link Any}.
   */
    public static String extract(Any a) {
        return a.extract_string();
    }

    /**
   * Get repository id.
   */
    public static String id() {
        return _id;
    }

    /**
   * Just inserts string into this {@link Any}.
   */
    public static void insert(Any a, String that) {
        a.insert_string(that);
    }

    /**
   * Delegates functionality to {@link InputStream#read_string()}.
   */
    public static String read(InputStream istream) {
        return istream.read_string();
    }

    /**
   * Return the "StringName", alias of String, typecode.
   */
    public static synchronized TypeCode type() {
        if (typeCode == null) {
            typeCode = ORB.init().create_string_tc(0);
            typeCode = ORB.init().create_alias_tc(id(), "StringName", typeCode);
        }
        return typeCode;
    }

    /**
   * Delegates functionality to {@link OutputStream#write_string}.
   */
    public static void write(OutputStream ostream, String value) {
        ostream.write_string(value);
    }
}
