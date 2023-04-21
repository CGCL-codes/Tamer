package newtonbabel.serialwrapper;

/**
 * This class wraps Sun's JavaComm API. It let's you transparently use this API
 * without worrying whether it's actually there or not.
 * @author Jochen Sch�fer
 * @see newtonbabel.serialwrapper.SerialWrapper
 */
class JavaCommWrapper extends SerialWrapper {

    /**
   * The class initializer. We get all the Class and Method objects we
   * need via Reflection.
   * @author Jochen Sch�fer
   */
    private void init() {
        try {
            CommDriver = Class.forName("javax.comm.CommDriver");
            CommPortEnumerator = Class.forName("javax.comm.CommPortEnumerator");
            CommPortIdentifier = Class.forName("javax.comm.CommPortIdentifier");
            SerialPort = Class.forName("javax.comm.SerialPort");
            CommPortIdentifier_GetName = CommPortIdentifier.getMethod("getName", null);
            CommPortIdentifier_GetPortIdentifiers = CommPortIdentifier.getMethod("getPortIdentifiers", null);
            CommPortIdentifier_GetPortType = CommPortIdentifier.getMethod("getPortType", null);
            CommPortIdentifier_PORT_SERIAL = CommPortIdentifier.getField("PORT_SERIAL").getInt(null);
            CommPortIdentifier_GetPortIdentifier_STRING = CommPortIdentifier.getMethod("getPortIdentifier", new Class[] { String.class });
            CommPortIdentifier_IsCurrentlyOwned = CommPortIdentifier.getMethod("isCurrentlyOwned", null);
            CommPortIdentifier_Open = CommPortIdentifier.getMethod("open", new Class[] { String.class, int.class });
        } catch (Exception excp) {
            CommDriver = null;
            CommPortEnumerator = null;
            CommPortIdentifier = null;
            SerialPort = null;
            CommPortIdentifier_GetName = null;
            CommPortIdentifier_GetPortIdentifiers = null;
            CommPortIdentifier_GetPortType = null;
            CommPortIdentifier_PORT_SERIAL = 0;
            CommPortIdentifier_GetPortIdentifier_STRING = null;
            CommPortIdentifier_IsCurrentlyOwned = null;
            CommPortIdentifier_Open = null;
        }
    }

    /**
   * The constructor of JavaCommWrapper. It just calls test ( @see newtonbabel.
   * serialwrapper.SerialWrapper#test() ) to test all reflection variables.
   * @author Jochen Sch�fer
   * @see newtonbabel.serialwrapper.SerialWrapper#SerialWrapper()
   */
    JavaCommWrapper() throws ClassNotFoundException {
        super();
        init();
        if (!test()) {
            throw new ClassNotFoundException("Could not initialize JavaCommWrapper!");
        }
    }
}
