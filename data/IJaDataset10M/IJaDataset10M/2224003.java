package org.smslib;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;
import org.smslib.CService.MessageClass;
import org.smslib.handler.*;

public abstract class AbstractATHandler {

    protected CSerialDriver serialDriver;

    protected Logger log;

    protected String storageLocations = "";

    protected CService srv;

    protected static final int DELAY_AT = 200;

    protected static final int DELAY_RESET = 20000;

    protected static final int DELAY_PIN = 12000;

    protected static final int DELAY_CMD_MODE = 1000;

    protected static final int DELAY_CMGS = 300;

    public AbstractATHandler(CSerialDriver serialDriver, Logger log, CService srv) {
        super();
        this.serialDriver = serialDriver;
        this.log = log;
        this.srv = srv;
        storageLocations = "";
    }

    protected abstract void setStorageLocations(String loc);

    protected abstract boolean dataAvailable() throws IOException;

    protected abstract void sync() throws IOException;

    protected abstract void reset() throws IOException;

    protected abstract void echoOff() throws IOException;

    protected abstract void init() throws IOException;

    protected abstract boolean isAlive() throws IOException;

    /**
	 * Issues the AT Command to check if the device is waiting for a PIN or PUK
	 * @return The response from the AT Handler, verbatim
	 * @throws IOException
	 */
    protected abstract String getPinResponse() throws IOException;

    /**
	 * Check the supplied response to the PIN AT command to see if a PIN is required.
	 * @param commandResponse
	 * @return <code>true</code> if a PIN is being waited for; <code>false</code> otherwise
	 */
    protected abstract boolean isWaitingForPin(String commandResponse);

    /**
	 * Check the supplied response to the PIN AT command to see if a PUK is required.
	 * @param commandResponse
	 * @return <code>true</code> if a PIN is being waited for; <code>false</code> otherwise
	 */
    protected abstract boolean isWaitingForPuk(String commandResponse);

    protected abstract boolean enterPin(String pin) throws IOException;

    protected abstract boolean setVerboseErrors() throws IOException;

    protected abstract boolean setPduMode() throws IOException;

    protected abstract boolean setTextMode() throws IOException;

    protected abstract boolean enableIndications() throws IOException;

    protected abstract boolean disableIndications() throws IOException;

    protected abstract String getManufacturer() throws IOException;

    protected abstract String getModel() throws IOException;

    protected abstract String getMsisdn() throws IOException;

    protected abstract String getSerialNo() throws IOException;

    protected abstract String getImsi() throws IOException;

    protected abstract String getSwVersion() throws IOException;

    protected abstract String getBatteryLevel() throws IOException;

    protected abstract String getSignalLevel() throws IOException;

    protected abstract boolean setMemoryLocation(String mem) throws IOException;

    /**
	 * Switches the serial communication mode from data mode to command mode.  Command
	 * mode allows the sending of AT commands.
	 * @throws IOException if there was a problem with the serial connection
	 */
    protected abstract void switchToCmdMode() throws IOException;

    protected abstract boolean keepGsmLinkOpen() throws IOException;

    protected abstract int sendMessage(int size, String pdu, String phone, String text) throws IOException, NoResponseException, UnrecognizedHandlerProtocolException;

    protected abstract String listMessages(MessageClass messageClass) throws IOException, UnrecognizedHandlerProtocolException, SMSLibDeviceException;

    protected abstract boolean deleteMessage(int memIndex, String memLocation) throws IOException;

    protected abstract String getGprsStatus() throws IOException;

    protected abstract String getNetworkRegistration() throws IOException;

    protected abstract void getStorageLocations() throws IOException;

    /**
	 * Checks whether this AT Handler has support for receiving SMS messages
	 * @return true if this AT handler supports receiving of SMS messages.
	 */
    protected abstract boolean supportsReceive();

    /**
	 * Checks whether this AT Handler has support for sending SMS binary messages
	 * @return true if this AT handler supports sending of SMS binary message.
	 */
    protected boolean supportsBinarySmsSending() {
        return true;
    }

    /**
	 * Checks whether this AT Handler has support for sending UCS-2 encoded text messages.
	 * @return true if this AT handler supports sending UCS-2 encoded text messages.
	 */
    public abstract boolean supportsUcs2SmsSending();

    /**
	 * Attempt to load a particular AT Handler.
	 * @param serialDriver
	 * @param log
	 * @param srv
	 * @param handlerClassName
	 * @return A new instance of the required handler.
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
    @SuppressWarnings("unchecked")
    private static AbstractATHandler load(CSerialDriver serialDriver, Logger log, CService srv, String handlerClassName) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, InvocationTargetException, IllegalAccessException {
        log.info("Attempting to load handler: " + handlerClassName);
        Class<AbstractATHandler> handlerClass = (Class<AbstractATHandler>) Class.forName(handlerClassName);
        java.lang.reflect.Constructor<AbstractATHandler> handlerConstructor = handlerClass.getConstructor(new Class[] { CSerialDriver.class, Logger.class, CService.class });
        AbstractATHandler atHandlerInstance = handlerConstructor.newInstance(new Object[] { serialDriver, log, srv });
        log.info("Successfully loaded handler: " + atHandlerInstance.getClass().getName());
        return atHandlerInstance;
    }

    /**
	 * 
	 * @param serialDriver
	 * @param log
	 * @param srv
	 * @param gsmDeviceManufacturer
	 * @param gsmDeviceModel
	 * @param catHandlerAlias
	 * @return
	 */
    static AbstractATHandler load(CSerialDriver serialDriver, Logger log, CService srv, String gsmDeviceManufacturer, String gsmDeviceModel, String catHandlerAlias) {
        log.trace("ENTRY");
        final String BASE_HANDLER = org.smslib.handler.CATHandler.class.getName();
        if (catHandlerAlias != null && !catHandlerAlias.equals("")) {
            String requestedHandlerName = BASE_HANDLER + "_" + catHandlerAlias;
            try {
                return load(serialDriver, log, srv, requestedHandlerName);
            } catch (Exception ex) {
                log.info("Could not load requested handler '" + requestedHandlerName + "'; will try more generic version.", ex);
            }
        }
        if (gsmDeviceManufacturer != null && !gsmDeviceManufacturer.equals("")) {
            String manufacturerHandlerName = BASE_HANDLER + "_" + gsmDeviceManufacturer;
            if (gsmDeviceModel != null && !gsmDeviceModel.equals("")) {
                String modelHandlerName = manufacturerHandlerName + "_" + gsmDeviceModel;
                try {
                    return load(serialDriver, log, srv, modelHandlerName);
                } catch (Exception ex) {
                    log.info("Could not load requested handler '" + modelHandlerName + "'; will try more generic version.", ex);
                }
            }
            try {
                return load(serialDriver, log, srv, manufacturerHandlerName);
            } catch (Exception ex) {
                log.info("Could not load requested handler '" + manufacturerHandlerName + "'; will try more generic version.", ex);
            }
        }
        return new CATHandler(serialDriver, log, srv);
    }

    /** List of all AT handler classes */
    @SuppressWarnings("unchecked")
    private static final Class[] HANDLERS = { CATHandler.class, CATHandler_Motorola_RAZRV3x.class, CATHandler_Nokia_S40_3ed.class, CATHandler_Samsung.class, CATHandler_Siemens_M55.class, CATHandler_Siemens_MC75.class, CATHandler_Siemens_S55.class, CATHandler_Siemens_TC35.class, CATHandler_SonyEricsson_GT48.class, CATHandler_SonyEricsson_W550i.class, CATHandler_SonyEricsson.class, CATHandler_Symbian_PiAccess.class, CATHandler_Wavecom_M1306B.class, CATHandler_Wavecom.class };

    /**
	 * Gets a list containing all available AT Handlers.
	 */
    @SuppressWarnings("unchecked")
    public static <T extends AbstractATHandler> Class<T>[] getHandlers() {
        return HANDLERS;
    }

    protected int getProtocol() {
        return CService.Protocol.PDU;
    }
}
