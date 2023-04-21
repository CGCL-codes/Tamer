package org.omg.CosNotifyFilter;

/**
 *	Generated from IDL definition of exception "CallbackNotFound"
 *	@author JacORB IDL compiler 
 */
public final class CallbackNotFoundHelper {

    private static org.omg.CORBA.TypeCode _type = org.omg.CORBA.ORB.init().create_exception_tc(org.omg.CosNotifyFilter.CallbackNotFoundHelper.id(), "CallbackNotFound", new org.omg.CORBA.StructMember[0]);

    public static void insert(final org.omg.CORBA.Any any, final org.omg.CosNotifyFilter.CallbackNotFound s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static org.omg.CosNotifyFilter.CallbackNotFound extract(final org.omg.CORBA.Any any) {
        return read(any.create_input_stream());
    }

    public static org.omg.CORBA.TypeCode type() {
        return _type;
    }

    public static String id() {
        return "IDL:omg.org/CosNotifyFilter/CallbackNotFound:1.0";
    }

    public static org.omg.CosNotifyFilter.CallbackNotFound read(final org.omg.CORBA.portable.InputStream in) {
        org.omg.CosNotifyFilter.CallbackNotFound result = new org.omg.CosNotifyFilter.CallbackNotFound();
        if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
        return result;
    }

    public static void write(final org.omg.CORBA.portable.OutputStream out, final org.omg.CosNotifyFilter.CallbackNotFound s) {
        out.write_string(id());
    }
}
