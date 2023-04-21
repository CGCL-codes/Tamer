package gov.lanl.Authenticate;

public final class AuthenErrorTypeHolder implements org.omg.CORBA.portable.Streamable {

    public AuthenErrorType value;

    public AuthenErrorTypeHolder() {
    }

    public AuthenErrorTypeHolder(AuthenErrorType initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = AuthenErrorTypeHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        AuthenErrorTypeHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return AuthenErrorTypeHelper.type();
    }
}
