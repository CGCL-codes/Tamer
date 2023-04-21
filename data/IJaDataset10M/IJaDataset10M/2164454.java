package com.fluo;

import java.io.Serializable;

public class Message implements Serializable {

    String myOSType;

    String username;

    String password;

    public Message(String OS) {
        myOSType = OS;
    }

    public String getOSType() {
        return myOSType;
    }

    public Message(String un, String pw) {
        username = un;
        password = pw;
    }
}
