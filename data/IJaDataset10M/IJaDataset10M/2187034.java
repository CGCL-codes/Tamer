package org.gregoire.tools.ant;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

/**
 * Allows loading of a native lib
 *
 * @author Paul Gregoire 2/2/2006
 */
public class LoadNativeLibrary extends Task {

    private String libName;

    /**
     * Set the library name
     */
    public void setLibrary(String name) {
        libName = name;
    }

    /**
    * Load it up
    */
    public void execute() throws BuildException {
        System.out.println("\tLoading library: " + libName);
        System.load(libName);
        System.out.println();
    }
}
