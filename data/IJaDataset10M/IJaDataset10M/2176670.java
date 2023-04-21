package tcg.scada.cos;

/**
 * Generated from IDL interface "ICosDataPointServer".
 *
 * @author JacORB IDL compiler V 2.3-beta-2, 14-Oct-2006
 * @version generated at 18-Dec-2009 13:41:19
 */
public final class ICosDataPointServerHolder implements org.omg.CORBA.portable.Streamable {

    public ICosDataPointServer value;

    public ICosDataPointServerHolder() {
    }

    public ICosDataPointServerHolder(final ICosDataPointServer initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return ICosDataPointServerHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = ICosDataPointServerHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream _out) {
        ICosDataPointServerHelper.write(_out, value);
    }
}
