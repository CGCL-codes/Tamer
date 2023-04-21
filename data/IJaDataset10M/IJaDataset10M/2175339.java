package FIPA;

public final class stringsHolder implements org.omg.CORBA.portable.Streamable {

    public String[] value;

    public stringsHolder() {
        this(null);
    }

    public stringsHolder(String[] __arg) {
        value = __arg;
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        FIPA.stringsHelper.write(out, value);
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = FIPA.stringsHelper.read(in);
    }

    public org.omg.CORBA.TypeCode _type() {
        return FIPA.stringsHelper.type();
    }
}
