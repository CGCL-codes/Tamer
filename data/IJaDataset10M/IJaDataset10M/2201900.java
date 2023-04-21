package logon;

import encoders.MD5PasswordEncoder;

public class SecureMD5Logon extends Logon {

    public SecureMD5Logon() {
        setEncodingMethod(new MD5PasswordEncoder());
    }
}
