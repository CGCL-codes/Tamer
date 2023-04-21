package org.gdbi.api;

/**
 * Subclass of context for PLAC.
 *
 * For Gedcom lines of the form:
 * 2 PLAC City, County, State, Country
 *
 * This is a separate subclass to identify where we need to append the value
 * to the place cache for getPlaces().
 */
class GdbiContextPlac extends GdbiContext {

    GdbiContextPlac(GdbiIntrContext iprop) {
        super(iprop);
    }
}
