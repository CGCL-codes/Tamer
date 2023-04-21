package br.com.medical.to;

import java.security.KeyPair;
import javax.crypto.SecretKey;

public final class UserTO implements TO {

    private static final long serialVersionUID = 7528074175899972805L;

    public String name;

    public String password;

    public KeyPair keyPair;

    public SecretKey secretKey;

    public UserTO(String name) {
    }

    public UserTO(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserTO)) {
            return false;
        }
        return this.name.equals(((UserTO) obj).name);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
