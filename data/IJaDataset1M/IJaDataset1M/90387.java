package server;

/**
 * Generated from IDL interface "DatiSicuri".
 *
 * @author JacORB IDL compiler V 2.3.0, 17-Feb-2007
 * @version generated at 22-mag-2007 13.06.01
 */
public final class DatiSicuriHolder implements org.omg.CORBA.portable.Streamable {

    public DatiSicuri value;

    public DatiSicuriHolder() {
    }

    public DatiSicuriHolder(final DatiSicuri initial) {
        value = initial;
    }

    public org.omg.CORBA.TypeCode _type() {
        return DatiSicuriHelper.type();
    }

    public void _read(final org.omg.CORBA.portable.InputStream in) {
        value = DatiSicuriHelper.read(in);
    }

    public void _write(final org.omg.CORBA.portable.OutputStream _out) {
        DatiSicuriHelper.write(_out, value);
    }
}
