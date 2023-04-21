package org.ejbca.cvc;

/**
 * Definitions of roles in CVC. Changed to conform to the ISO18013 spec rather
 * than passport EAC spec. 
 * 
 * @author Keijo Kurkinen, Swedish National Police Board
 *
 */
public class AuthorizationRole {

    public static final byte TRUST_ROOT = 0x20;

    public static final byte TRUST_TIME = 0x10;

    public static final byte NONE = 0x00;

    private byte role;

    private int trustLength = 0x0f;

    /** 
    * Constructs new authorization role
    * @param role the role (trust root and/or time)
    * @param trustLength certificate trust length (0x0f = infinite)
    */
    public AuthorizationRole(byte role, int trustLength) {
        if (trustLength > 0x0f || trustLength < 0) throw new IllegalArgumentException("trustLength wrong.");
        this.trustLength = trustLength;
        if ((byte) (role & ~((byte) (TRUST_ROOT | TRUST_TIME))) != (byte) 0) throw new IllegalArgumentException("Wrong role.");
        this.role = role;
    }

    /**
    * Default contructor - no trust, certificate trust length infinite
    *
    */
    public AuthorizationRole() {
        this.role = NONE;
    }

    /**
    * Returns the value as a bitmap
    * @return
    */
    public byte getByte() {
        return (byte) (role | trustLength);
    }

    public String toString() {
        String result = "";
        result += ((byte) (role & TRUST_ROOT) == TRUST_ROOT) ? "Trust root " : "";
        result += ((byte) (role & TRUST_TIME) == TRUST_TIME) ? "Trust time " : "";
        result += "Trust length: " + trustLength;
        return result;
    }
}
