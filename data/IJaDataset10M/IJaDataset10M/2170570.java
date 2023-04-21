package net.sf.bimbo.impl;

import java.security.Principal;

/**
 * Bimbo principal implementation.
 * 
 * @author fcorneli
 * 
 */
public class BimboPrincipal implements Principal {

    private String name;

    public BimboPrincipal(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
