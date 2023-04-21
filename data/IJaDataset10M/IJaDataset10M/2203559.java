package gov.lanl.Authenticate;

public final class UserDataHolder implements org.omg.CORBA.portable.Streamable {

    public UserData value;

    public UserDataHolder() {
    }

    public UserDataHolder(UserData initial) {
        value = initial;
    }

    public void _read(org.omg.CORBA.portable.InputStream in) {
        value = UserDataHelper.read(in);
    }

    public void _write(org.omg.CORBA.portable.OutputStream out) {
        UserDataHelper.write(out, value);
    }

    public org.omg.CORBA.TypeCode _type() {
        return UserDataHelper.type();
    }
}
