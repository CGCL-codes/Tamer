package net.sourceforge.crystal;

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

    protected static boolean requestPlugin(String name, String intf) {
        int intfVersion = 0;
        int intfID = (int) iSCF.getSCF().GetInterfaceID(intf);
        boolean result = csInitializer._RequestPlugin(getTheObjectRegistry(), name, intf, intfID, intfVersion);
        System.out.println("Loading " + name + "? " + result);
        return result;
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
