package org.gregoire.tools.ant;

import java.io.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

/**
 * Simple utility to add a new path to the system path for the current process.
 * This is intended to be used with DLLs that reference _other_ DLLs, which
 * are loaded using only the system path, not the path specified with
 * java.load.library.
 *
 * @author Paul Gregoire (mondain@gmail.com)
 */
public class PathTask extends Task {

    private static boolean libraryLoaded = false;

    private String path;

    private String libraryName = "path.dll";

    {
        if (!libraryLoaded) {
            LibraryLoader.load(PathTask.class.getClassLoader(), libraryName);
            libraryLoaded = true;
        }
    }

    /**
     * Set the os type
     */
    public void setOS(String type) {
        if (type.toLowerCase().indexOf("osx") != -1 || type.toLowerCase().indexOf("mac") != -1) {
            libraryName = "libpath";
            System.out.println("Loading mac lib: " + libraryName);
        } else if (type.indexOf("nix") != -1) {
            libraryName = "libpath.so";
            System.out.println("Loading unix lib: " + libraryName);
        } else {
            System.out.println("Loading windows lib: " + libraryName);
        }
    }

    /**
     * Set the jvm path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
    * Updated the jvm path  
    */
    public void execute() throws BuildException {
        addToPath(path);
    }

    /**
	 * Add the specified path to the environment path of the current process.
	 * The specified path is appended to the current path.
	 * <blockquote>
	 * PATH = %PATH%;&lt;path&gt;
	 * </blockquote>
	 * <p>
	 * This only affects the current process, and so can be run using any user permissions.
	 *
	 * @param path The path fragment that should be appended to the system path.
	 */
    public static native void addToPath(String path);
}
