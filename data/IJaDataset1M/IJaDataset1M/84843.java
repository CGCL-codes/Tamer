package edu.hawaii.ics.disif.lib.access;

import java.applet.Applet;
import java.awt.Image;
import java.io.IOException;

/**
 * Provides basic functionality independant of GUI. Supported are Applets and
 * general Applications.
 *
 * @author   king
 * @since    April 23, 2003
 */
public abstract class Access {

    /** The class that returns basic access defined by abstract functions in this class. */
    private static Access access;

    /**
   * Inits the access class with the type of program to use. Supported are applications
   * and applets.
   * 
   * @param accessObject  If your program is an application, set this value to null, if it
   *                      is an Applet or JApplet, set this parameter to the Applet.
   */
    public static void init(Object accessObject) {
        if (accessObject == null) {
            access = new AccessApplication();
        } else if (accessObject instanceof Applet) {
            access = new AccessApplet((Applet) accessObject);
        }
    }

    /**
   * Returns an instance of this access object.
   * 
   * @return  The access object. There is only one access object in the system!
   */
    public static Access getInstance() {
        return access;
    }

    /**
   * Returns the image for a certain path.
   * 
   * @param path  Path and name to the image.
   * @return      The requested image.
   * @throws IOException  If the image couldn't be loaded.
   */
    public abstract Image getImage(String path) throws IOException;

    /**
   * Ends the program without further notification.
   */
    public abstract void exit();
}
