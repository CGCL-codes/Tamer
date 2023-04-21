package org.crystalspace3d;

import org.crystalspace3d.csPluginRequest;
import java.util.Enumeration;
import java.util.Vector;

public class CS extends cspace {

    public static String CSJAVA_LIB = "csjava";

    static {
        try {
            System.out.println("Loading CrystalSpace library");
            System.loadLibrary(CSJAVA_LIB);
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Fatal Error: unresolved symbol in '" + CSJAVA_LIB + "'.");
            System.out.println(e.toString());
            System.exit(-1);
        } catch (SecurityException e) {
            System.out.println("Fatal Error: no permission to open '" + CSJAVA_LIB + "'.");
            System.out.println(e.toString());
            System.exit(-1);
        }
    }

    public static boolean requestPlugins(Vector plugins) {
        csPluginRequestArray arr = new csPluginRequestArray(0);
        Enumeration e = plugins.elements();
        while (e.hasMoreElements()) arr.Push((csPluginRequest) e.nextElement());
        return csInitializer._RequestPlugins(getTheObjectRegistry(), arr);
    }

    public static csVector3 CS_VEC_FORWARD = new csVector3(0, 0, 1);

    public static csVector3 CS_VEC_BACKWARD = new csVector3(0, 0, -1);

    public static csVector3 CS_VEC_RIGHT = new csVector3(1, 0, 0);

    public static csVector3 CS_VEC_LEFT = new csVector3(-1, 0, 0);

    public static csVector3 CS_VEC_UP = new csVector3(0, 1, 0);

    public static csVector3 CS_VEC_DOWN = new csVector3(0, -1, 0);

    public static csVector3 CS_VEC_ROT_RIGHT = new csVector3(0, 1, 0);

    public static csVector3 CS_VEC_ROT_LEFT = new csVector3(0, -1, 0);

    public static csVector3 CS_VEC_TILT_RIGHT = new csVector3(0, 0, -1);

    public static csVector3 CS_VEC_TILT_LEFT = new csVector3(0, 0, 1);

    public static csVector3 CS_VEC_TILT_UP = new csVector3(-1, 0, 0);

    public static csVector3 CS_VEC_TILT_DOWN = new csVector3(1, 0, 0);
}

;
