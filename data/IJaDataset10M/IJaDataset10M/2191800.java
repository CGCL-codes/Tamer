package com.memoire.bu;

/**
 * Abstract class for handling information.
 */
public abstract class BuInformationsAbstract {

    protected BuInformationsAbstract() {
    }

    protected String _(String _s) {
        return BuResource.BU.getString(_s);
    }
}
