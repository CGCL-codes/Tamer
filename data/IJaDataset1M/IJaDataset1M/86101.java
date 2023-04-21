package org.vikulin.utils;

import org.mortbay.jetty.security.Password;

public class PasswordObfuscator {

    public static void main(String[] args) {
        System.out.println(Password.obfuscate("komkom"));
    }
}
