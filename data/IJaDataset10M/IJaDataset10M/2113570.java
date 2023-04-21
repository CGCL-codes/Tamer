package tcg.syscontrol.cos;

/**
 * Generated from IDL enum "CosProcessTypeEnum".
 *
 * @author JacORB IDL compiler V 2.3-beta-2, 14-Oct-2006
 * @version generated at 22-Dec-2009 14:48:59
 */
public final class CosProcessTypeEnumHelper {

    private static org.omg.CORBA.TypeCode _type = null;

    public static org.omg.CORBA.TypeCode type() {
        if (_type == null) {
            _type = org.omg.CORBA.ORB.init().create_enum_tc(tcg.syscontrol.cos.CosProcessTypeEnumHelper.id(), "CosProcessTypeEnum", new String[] { "ProcThread", "ProcDataPointServer", "ProcAlarmServer", "ProcComposite" });
        }
        return _type;
    }

    public static void insert(final org.omg.CORBA.Any any, final tcg.syscontrol.cos.CosProcessTypeEnum s) {
        any.type(type());
        write(any.create_output_stream(), s);
    }

    public static tcg.syscontrol.cos.CosProcessTypeEnum extract(final org.omg.CORBA.Any any) {
        return read(any.create_input_stream());
    }

    public static String id() {
        return "IDL:tcg/syscontrol/cos/CosProcessTypeEnum:1.0";
    }

    public static CosProcessTypeEnum read(final org.omg.CORBA.portable.InputStream in) {
        return CosProcessTypeEnum.from_int(in.read_long());
    }

    public static void write(final org.omg.CORBA.portable.OutputStream out, final CosProcessTypeEnum s) {
        out.write_long(s.value());
    }
}
