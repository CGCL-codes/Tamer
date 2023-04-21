package tcg.syscontrol.cos;

/**
 * Generated from IDL alias "CosProcessDataSeq".
 *
 * @author JacORB IDL compiler V 2.3-beta-2, 14-Oct-2006
 * @version generated at 27-Nov-2009 18:09:27
 */
public final class CosProcessDataSeqHolder implements org.omg.CORBA.portable.Streamable {

    public tcg.syscontrol.cos.CosProcessDataStruct[] value;

    public CosProcessDataSeqHolder() {
    }

    public CosProcessDataSeqHolder(final tcg.syscontrol.cos.CosProcessDataStruct[] initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return CosProcessDataSeqHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = CosProcessDataSeqHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream out) {
        CosProcessDataSeqHelper.write(out, value);
    }
}
