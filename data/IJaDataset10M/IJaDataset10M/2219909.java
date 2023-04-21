package org.indi.serial.device;

import gnu.io.CommPortIdentifier;
import junit.framework.TestCase;
import org.indi.nativelib.NativeLibraryLoader;

public class TestLibrary extends TestCase {

    public void testLoadLibrary() throws Exception {
        NativeLibraryLoader.loadLibrary(Thread.currentThread().getContextClassLoader(), "rxtxSerial");
        listPorts();
    }

    static void listPorts() {
        java.util.Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = (CommPortIdentifier) portEnum.nextElement();
            System.out.println(portIdentifier.getName() + " - " + getPortTypeName(portIdentifier.getPortType()));
        }
    }

    static String getPortTypeName(int portType) {
        switch(portType) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
}
