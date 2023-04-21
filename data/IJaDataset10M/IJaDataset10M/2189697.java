package tcg.syscontrol.cos;

/**
 * Generated from IDL interface "ICosManagedProcess".
 *
 * @author JacORB IDL compiler V 2.3-beta-2, 14-Oct-2006
 * @version generated at 27-Nov-2009 18:09:26
 */
public final class ICosManagedProcessHelper {

    public static void insert(final org.omg.CORBA.Any any, final tcg.syscontrol.cos.ICosManagedProcess s) {
        any.insert_Object(s);
    }

    public static tcg.syscontrol.cos.ICosManagedProcess extract(final org.omg.CORBA.Any any) {
        return narrow(any.extract_Object());
    }

    public static org.omg.CORBA.TypeCode type() {
        return org.omg.CORBA.ORB.init().create_interface_tc("IDL:tcg/syscontrol/cos/ICosManagedProcess:1.0", "ICosManagedProcess");
    }

    public static String id() {
        return "IDL:tcg/syscontrol/cos/ICosManagedProcess:1.0";
    }

    public static ICosManagedProcess read(final org.omg.CORBA.portable.InputStream in) {
        return narrow(in.read_Object(tcg.syscontrol.cos._ICosManagedProcessStub.class));
    }

    public static void write(final org.omg.CORBA.portable.OutputStream _out, final tcg.syscontrol.cos.ICosManagedProcess s) {
        _out.write_Object(s);
    }

    public static tcg.syscontrol.cos.ICosManagedProcess narrow(final org.omg.CORBA.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof tcg.syscontrol.cos.ICosManagedProcess) {
            return (tcg.syscontrol.cos.ICosManagedProcess) obj;
        } else if (obj._is_a("IDL:tcg/syscontrol/cos/ICosManagedProcess:1.0")) {
            tcg.syscontrol.cos._ICosManagedProcessStub stub;
            stub = new tcg.syscontrol.cos._ICosManagedProcessStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
            return stub;
        } else {
            throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
        }
    }

    public static tcg.syscontrol.cos.ICosManagedProcess unchecked_narrow(final org.omg.CORBA.Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof tcg.syscontrol.cos.ICosManagedProcess) {
            return (tcg.syscontrol.cos.ICosManagedProcess) obj;
        } else {
            tcg.syscontrol.cos._ICosManagedProcessStub stub;
            stub = new tcg.syscontrol.cos._ICosManagedProcessStub();
            stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate());
            return stub;
        }
    }
}
