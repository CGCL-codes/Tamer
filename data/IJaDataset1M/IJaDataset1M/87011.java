package net.adrianromero.tpv.scanpal2;

/**
 *
 * @author Administrador
 */
public class DeviceScannerException extends java.lang.Exception {

    /**
     * Creates a new instance of <code>DeviceScannerException</code> without detail message.
     */
    public DeviceScannerException() {
    }

    /**
     * Constructs an instance of <code>DeviceScannerException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DeviceScannerException(String msg) {
        super(msg);
    }

    public DeviceScannerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DeviceScannerException(Throwable cause) {
        super(cause);
    }
}
