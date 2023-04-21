package org.unicore.ajo;

import org.unicore.Unicore;

/**
 * Option represents a token/value pair.
 * <p>
 * These are used for either the environment variables or 
 * compiler preprocessing defines or undefines.
 *
 * @see FortranTask#getDefines
 * @see FortranTask#getUndefines
 * @see ExecuteTask#getEnvironmentVariables
 *
 * @author S. van den Berghe (fecit)
 *
 * @since AJO 1
 *
 * @version $Id: Option.java,v 1.2 2004/05/26 16:31:44 svenvdb Exp $
 * 
 **/
public final class Option extends Unicore {

    static final long serialVersionUID = -2046853465658039934L;

    public Option() {
        this("", "");
    }

    /**
     * Create an Option with just the token.
     *
     **/
    public Option(String token) {
        this(token, "");
    }

    /**
     * Create an Option mapping a token to a value.
     *
     **/
    public Option(String token, String value) {
        setToken(token);
        this.value = value;
    }

    private String token;

    /**
     * Return the token.
     *
     **/
    public String getToken() {
        return token;
    }

    /**
     * Change the token.
     *
     **/
    public void setToken(String token) {
        if (token == null) {
            this.token = "null_token";
        } else if (token.equals("")) {
            this.token = "empty_token";
        } else {
            this.token = token;
        }
    }

    private String value;

    /**
     * Return the value.
     * <p>
     *  A null or empty String implies no value.
     *
     **/
    public String getValue() {
        return value;
    }

    /**
     * Change the value.
     *
     **/
    public void setValue(String value) {
        this.value = value;
    }
}
