package de.kapsi.net.daap.chunks.impl;

import de.kapsi.net.daap.chunks.UByteChunk;

/**
 * 
 *
 * @author  Roger Kapsi
 */
public class AuthenticationMethod extends UByteChunk {

    public static final int USERNAME_PASSWORD_METHOD = 0x01;

    public static final int PASSWORD_METHOD = 0x02;

    public AuthenticationMethod() {
        this(PASSWORD_METHOD);
    }

    public AuthenticationMethod(int method) {
        super("msau", "dmap.authenticationmethod", method);
    }
}
