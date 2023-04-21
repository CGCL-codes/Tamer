package edu.jrous.core;

/**
 * 
 *  <p>Exception de Dispositivos no </p>
 *  <p>Unidos al sistema</p>
 * @author Manuel Sako
 * @version 1.0
 *
 */
public class DeviceNoConnectException extends Exception {

    /**
	 * Numero de Serie 
	 */
    private static final long serialVersionUID = 2804879254296143037L;

    /**
	 * <p>Represents el id que se trato de unir</p>
	 * 
	 */
    protected long idDevice;

    /**
	 * <p>Represents message</p>
	 * 
	 */
    protected String message;

    /**
	 * Constructor
	 * @param name
	 * @param message
	 */
    public DeviceNoConnectException(String message) {
        this.message = message;
    }

    /**
	 * @return Returns the message.
	 */
    public String getMessage() {
        return message;
    }
}
