package iwork.manager.security;

import java.io.*;
import java.security.*;

public class iSignLogin implements Serializable {

    private String _loginName;

    private String _password;

    private PublicKey _publicKey;

    public iSignLogin(String loginName, String password, PublicKey publicKey) {
        this._loginName = loginName;
        this._password = password;
        this._publicKey = publicKey;
    }

    public String getLoginName() {
        return _loginName;
    }

    public String getPassword() {
        return _password;
    }

    public PublicKey getPublicKey() {
        return _publicKey;
    }
}
