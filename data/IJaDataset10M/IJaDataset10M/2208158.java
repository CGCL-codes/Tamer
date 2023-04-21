package tcg.syscontrol.cos;

/**
 * Generated from IDL exception "CosNotInControlException".
 *
 * @author JacORB IDL compiler V 2.3-beta-2, 14-Oct-2006
 * @version generated at 22-Dec-2009 14:48:59
 */
public final class CosNotInControlExceptionHelper {

    private static org.omg.CORBA.TypeCode _type = null;

    public static org.omg.CORBA.TypeCode type() {
        if (_type == null) {
            _type = org.omg.CORBA.ORB.init().create_exception_tc(tcg.syscontrol.cos.CosNotInControlExceptionHelper.id(), "CosNotInControlException", new org.omg.CORBA.StructMember[0]);
        }
        return _type;
    }

    public static void insert(final org.omg.CORBA.Any any, final tcg.syscontrol.cos.CosNotInControlException s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static tcg.syscontrol.cos.CosNotInControlException extract(final org.omg.CORBA.Any any) {
        return read(any.create_input_stream());
    }

    public static String id() {
        return "IDL:tcg/syscontrol/cos/CosNotInControlException:1.0";
    }

    public static tcg.syscontrol.cos.CosNotInControlException read(final org.omg.CORBA.portable.InputStream in) {
        tcg.syscontrol.cos.CosNotInControlException result = new tcg.syscontrol.cos.CosNotInControlException();
        if (!in.read_string().equals(id())) throw new org.omg.CORBA.MARSHAL("wrong id");
        return result;
    }

    public static void write(final org.omg.CORBA.portable.OutputStream out, final tcg.syscontrol.cos.CosNotInControlException s) {
        out.write_string(id());
    }
}
