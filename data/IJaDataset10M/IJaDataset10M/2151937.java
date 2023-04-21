package org.nakedobjects.applib;

public interface TitledObject {

    Title title();

    /**
     * Typically return the toString() of title()
     */
    String titleString();
}
