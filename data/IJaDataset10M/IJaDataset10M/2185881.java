package nl.kbna.dioscuri.module.keyboard;

import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.kbna.dioscuri.Emulator;
import nl.kbna.dioscuri.exception.ModuleException;
import nl.kbna.dioscuri.exception.ModuleUnknownPort;
import nl.kbna.dioscuri.exception.ModuleWriteOnlyPortException;
import nl.kbna.dioscuri.module.Module;
import nl.kbna.dioscuri.module.ModuleKeyboard;
import nl.kbna.dioscuri.module.ModuleMotherboard;
import nl.kbna.dioscuri.module.ModulePIC;
import nl.kbna.dioscuri.module.ModuleRTC;

/**
 * An implementation of a keyboard module.
 *  
 * @see Module
 * 
 * Metadata module
 * ********************************************
 * general.type                : keyboard
 * general.name                : XT Keyboard
 * general.architecture        : Von Neumann
 * general.description         : Models a 101-key XT keyboard 
 * general.creator             : Tessella Support Services, Koninklijke Bibliotheek, Nationaal Archief of the Netherlands
 * general.version             : 1.0
 * general.keywords            : Keyboard, XT, Intel 8042
 * general.relations           : Motherboard
 * general.yearOfIntroduction  : 
 * general.yearOfEnding        : 
 * general.ancestor            : 
 * general.successor           : 
 * 
 * 
 */
public class Keyboard extends ModuleKeyboard {

    TheKeyboard keyboard = new TheKeyboard();

    ScanCodeSets scanCodeSet = new ScanCodeSets();

    private Emulator emu;

    private String[] moduleConnections = new String[] { "motherboard", "pic", "rtc" };

    private ModuleMotherboard motherboard;

    private ModulePIC pic;

    private ModuleRTC rtc;

    private boolean isObserved;

    private boolean debugMode;

    private int irqNumber;

    private boolean pendingIRQ;

    private int updateInterval;

    private static Logger logger = Logger.getLogger("nl.kbna.dioscuri.module.keyboard");

    private static final int DATA_PORT = 0x60;

    private static final int STATUS_PORT = 0x64;

    static int kbdInitialised = 0;

    public static final int MODULE_ID = 1;

    public static final String MODULE_TYPE = "keyboard";

    public static final String MODULE_NAME = "101-key PS/2 QWERTY keyboard";

    /**
     * Class constructor
     * 
     */
    public Keyboard(Emulator owner) {
        emu = owner;
        isObserved = false;
        debugMode = false;
        updateInterval = -1;
        irqNumber = -1;
        pendingIRQ = false;
        keyboard.internalBuffer.ledStatus = 0;
        keyboard.internalBuffer.scanningEnabled = 1;
        keyboard.controller.parityError = 0;
        keyboard.controller.timeOut = 0;
        keyboard.controller.auxBuffer = 0;
        keyboard.controller.keyboardLock = 1;
        keyboard.controller.commandData = 1;
        keyboard.controller.systemFlag = 0;
        keyboard.controller.inputBuffer = 0;
        keyboard.controller.outputBuffer = 0;
        keyboard.controller.kbdClockEnabled = 1;
        keyboard.controller.auxClockEnabled = 0;
        keyboard.controller.allowIRQ1 = 1;
        keyboard.controller.allowIRQ12 = 1;
        keyboard.controller.kbdOutputBuffer = 0;
        keyboard.controller.auxOutputBuffer = 0;
        keyboard.controller.lastCommand = 0;
        keyboard.controller.expectingPort60h = 0;
        keyboard.controller.irq1Requested = 0;
        keyboard.controller.irq12Requested = 0;
        keyboard.controller.batInProgress = 0;
        keyboard.controller.timerPending = 0;
        logger.log(Level.INFO, "[" + MODULE_TYPE + "]" + " Module created successfully.");
    }

    /**
     * Returns the ID of the module
     * 
     * @return string containing the ID of module 
     * @see Module
     */
    public int getID() {
        return MODULE_ID;
    }

    /**
     * Returns the type of the module
     * 
     * @return string containing the type of module 
     * @see Module
     */
    public String getType() {
        return MODULE_TYPE;
    }

    /**
     * Returns the name of the module
     * 
     * @return string containing the name of module 
     * @see Module
     */
    public String getName() {
        return MODULE_NAME;
    }

    /**
     * Returns a String[] with all names of modules it needs to be connected to
     * 
     * @return String[] containing the names of modules, or null if no connections
     */
    public String[] getConnection() {
        return moduleConnections;
    }

    /**
     * Sets up a connection with another module
     * 
     * @param mod   Module that is to be connected to this class
     * 
     * @return true if connection has been established successfully, false otherwise
     * 
     * @see Module
     */
    public boolean setConnection(Module mod) {
        if (mod.getType().equalsIgnoreCase("motherboard")) {
            this.motherboard = (ModuleMotherboard) mod;
            return true;
        } else if (mod.getType().equalsIgnoreCase("pic")) {
            this.pic = (ModulePIC) mod;
            return true;
        } else if (mod.getType().equalsIgnoreCase("rtc")) {
            this.rtc = (ModuleRTC) mod;
            return true;
        }
        return false;
    }

    /**
     * Checks if this module is connected to operate normally
     * 
     * @return true if this module is connected successfully, false otherwise
     */
    public boolean isConnected() {
        if (motherboard != null && pic != null) {
            return true;
        }
        return false;
    }

    /**
     * Default inherited reset. Calls specific reset(int)
     * 
     * @return boolean true if module has been reset successfully, false otherwise
     */
    public boolean reset() {
        motherboard.setIOPort(DATA_PORT, this);
        motherboard.setIOPort(STATUS_PORT, this);
        irqNumber = pic.requestIRQNumber(this);
        if (irqNumber > -1) {
            logger.log(Level.CONFIG, "[" + MODULE_TYPE + "]" + " IRQ number set to: " + irqNumber);
        } else {
            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Request of IRQ number failed.");
        }
        if (motherboard.requestTimer(this, updateInterval, true) == false) {
            return false;
        }
        motherboard.setTimerActiveState(this, true);
        rtc.setCMOSRegister(0x14, (byte) ((rtc.getCMOSRegister(0x14) | 0x04)));
        return reset(1);
    }

    /**
     * Keyboard specific reset, with value to indicate reset type
     * 
     * @param resetType Type of reset passed to keyboard<BR>
     *                  0: Warm reset (SW reset)<BR>
     *                  1: Cold reset (HW reset)
     * 
     * @return boolean true if module has been reset successfully, false otherwise
     */
    public boolean reset(int resetType) {
        keyboard.internalBuffer.buffer.clear();
        keyboard.internalBuffer.expectingTypematic = 0;
        keyboard.internalBuffer.expectingScancodeSet = 0;
        keyboard.controller.currentScancodeSet = 1;
        keyboard.controller.translateScancode = 1;
        if (resetType != 0) {
            keyboard.internalBuffer.expectingLEDWrite = 0;
            keyboard.internalBuffer.keyPressDelay = 1;
            keyboard.internalBuffer.keyRepeatRate = 0x0b;
        }
        logger.log(Level.INFO, "[" + MODULE_TYPE + "]" + " Module has been reset.");
        return true;
    }

    /**
     * Starts the module
     * @see Module
     */
    public void start() {
    }

    /**
     * Stops the module
     * @see Module
     */
    public void stop() {
    }

    /**
     * Returns the status of observed toggle
     * 
     * @return state of observed toggle
     * 
     * @see Module
     */
    public boolean isObserved() {
        return isObserved;
    }

    /**
     * Sets the observed toggle
     * 
     * @param status
     * 
     * @see Module
     */
    public void setObserved(boolean status) {
        isObserved = status;
    }

    /**
     * Returns the status of the debug mode toggle
     * 
     * @return state of debug mode toggle
     * 
     * @see Module
     */
    public boolean getDebugMode() {
        return debugMode;
    }

    /**
     * Sets the debug mode toggle
     * 
     * @param status
     * 
     * @see Module
     */
    public void setDebugMode(boolean status) {
        debugMode = status;
    }

    /**
     * Returns data from this module
     *
     * @param Module requester, the requester of the data
     * @return byte[] with data
     * 
     * @see Module
     */
    public byte[] getData(Module requester) {
        return null;
    }

    /**
     * Set data for this module
     *
     * @param byte[] containing data
     * @param Module sender, the sender of the data
     * 
     * @return true if data is set successfully, false otherwise
     * 
     * @see Module
     */
    public boolean setData(byte[] data, Module sender) {
        return false;
    }

    /**
     * Set String[] data for this module
     * 
     * @param String[] data
     * @param Module sender, the sender of the data
     * 
     * @return boolean true is successful, false otherwise
     * 
     * @see Module
     */
    public boolean setData(String[] data, Module sender) {
        return false;
    }

    /**
     * Returns a dump of this module
     * 
     * @return string
     * 
     * @see Module
     */
    public String getDump() {
        String keyboardDump = "Keyboard status:\n";
        keyboardDump += "Internal buffer contents:";
        keyboardDump += keyboard.internalBuffer.buffer + "\n";
        keyboardDump += "Controller queue contents:";
        keyboardDump += keyboard.controllerQueue + "\n";
        return keyboardDump;
    }

    /**
     * Retrieve the interval between subsequent updates
     * 
     * @return int interval in microseconds
     */
    public int getUpdateInterval() {
        return updateInterval;
    }

    /**
     * Defines the interval between subsequent updates
     * 
     * @param int interval in microseconds
     */
    public void setUpdateInterval(int interval) {
        if (interval > 0) {
            updateInterval = interval;
        } else {
            updateInterval = 200;
        }
        motherboard.resetTimer(this, updateInterval);
    }

    /**
     * Update device
     * Calls the keyboard 'poll' function and raises the IRQs resulting from that call
     */
    public void update() {
        int returnValue;
        returnValue = poll();
        if ((returnValue & 0x01) == 1) {
            setInterrupt();
            logger.log(Level.CONFIG, "[" + MODULE_TYPE + "]" + " timer raises IRQ1");
        }
        if ((returnValue & 0x02) == 2) {
            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Unsupported timer call to raise IRQ12");
        }
    }

    /**
     * IN instruction to keyboard<BR>
     * @param portAddress   the target port; should be either 0x60 or 0x64
     * <BR>
     * IN to portAddress 60h
     * IN to portAddress 64h returns the keyboard status
     * 
     * @return byte of data from output buffer
     */
    public byte getIOPortByte(int portAddress) throws ModuleUnknownPort, ModuleWriteOnlyPortException {
        byte value;
        switch(portAddress) {
            case (0x60):
                if (keyboard.controller.outputBuffer != 0) {
                    value = keyboard.controller.kbdOutputBuffer;
                    keyboard.controller.outputBuffer = 0;
                    keyboard.controller.auxBuffer = 0;
                    keyboard.controller.irq1Requested = 0;
                    keyboard.controller.batInProgress = 0;
                    if (!keyboard.controllerQueue.isEmpty()) {
                        keyboard.controller.auxOutputBuffer = (keyboard.controllerQueue.remove(0));
                        keyboard.controller.outputBuffer = 1;
                        keyboard.controller.auxBuffer = 1;
                        if (keyboard.controller.allowIRQ1 != 0) keyboard.controller.irq1Requested = 1;
                        logger.log(Level.CONFIG, "s.controller_Qsize: " + keyboard.controllerQueue.size() + 1);
                    }
                    clearInterrupt();
                    activate_timer();
                    logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Port 0x" + Integer.toHexString(portAddress).toUpperCase() + " read: " + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase() + "h");
                    return value;
                } else {
                    logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Internal buffer elements no.: " + keyboard.internalBuffer.buffer.size());
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Port 0x60 read but output buffer empty!");
                    return keyboard.controller.kbdOutputBuffer;
                }
            case (0x64):
                value = (byte) ((keyboard.controller.parityError << 7) | (keyboard.controller.timeOut << 6) | (keyboard.controller.auxBuffer << 5) | (keyboard.controller.keyboardLock << 4) | (keyboard.controller.commandData << 3) | (keyboard.controller.systemFlag << 2) | (keyboard.controller.inputBuffer << 1) | keyboard.controller.outputBuffer);
                keyboard.controller.timeOut = 0;
                return value;
            default:
                throw new ModuleUnknownPort(MODULE_TYPE + " does not recognise port 0x" + Integer.toHexString(portAddress).toUpperCase());
        }
    }

    /**
     * OUT instruction to keyboard<BR>
     * @param portAddress   the target port; should be either 0x60 or 0x64
     * @param value  the data written to the keyboard port
     * <BR>
     * OUT to portAddress 60h executes data port commands
     * OUT to portAddress 64h executes status port commands 
     */
    public void setIOPortByte(int portAddress, byte value) throws ModuleUnknownPort {
        logger.log(Level.CONFIG, "[" + MODULE_TYPE + "]" + " Port 0x" + Integer.toHexString(portAddress).toUpperCase() + " received write of 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
        switch(portAddress) {
            case 0x60:
                if (keyboard.controller.expectingPort60h != 0) {
                    keyboard.controller.expectingPort60h = 0;
                    keyboard.controller.commandData = 0;
                    if (keyboard.controller.inputBuffer != 0) {
                        logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + " Port 0x60 write but input buffer != 0");
                    }
                    switch(keyboard.controller.lastCommand) {
                        case 0x60:
                            {
                                byte disableKeyboard;
                                keyboard.controller.translateScancode = (byte) ((value >> 6) & 0x01);
                                disableKeyboard = (byte) ((value >> 4) & 0x01);
                                keyboard.controller.systemFlag = (byte) ((value >> 2) & 0x01);
                                keyboard.controller.allowIRQ1 = (byte) ((value >> 0) & 0x01);
                                keyboard.controller.allowIRQ12 = (byte) ((value >> 1) & 0x01);
                                setKeyboardClockState(disableKeyboard == 0 ? (byte) 1 : 0);
                                if ((keyboard.controller.allowIRQ1 != 0) && (keyboard.controller.outputBuffer != 0)) keyboard.controller.irq1Requested = 1;
                                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " IRQ1 (keyb) allowance set to " + keyboard.controller.allowIRQ1);
                                logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Unsupported IRQ12 (mouse) allowance! (not set to " + keyboard.controller.allowIRQ12 + ")");
                                if (keyboard.controller.translateScancode == 0) logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Scancode translation turned off");
                            }
                            return;
                        case (byte) 0xD1:
                            logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Writing value 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase() + " to output port P2");
                            logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + (((value & 0x02) == 2) ? "En" : "Dis") + "abled A20 gate");
                            motherboard.setA20((value & 0x02) != 0 ? true : false);
                            if (!((value & 0x01) != 0)) {
                                logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " System reset requested");
                                logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Function not (yet) implemented");
                            }
                            return;
                        case (byte) 0xD2:
                            enqueueControllerBuffer(value);
                            return;
                        case (byte) 0xD3:
                            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " No mouse support available");
                            return;
                        case (byte) 0xD4:
                            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " No mouse support available");
                            return;
                        default:
                            logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + " does not recognise command [" + Integer.toHexString(keyboard.controller.lastCommand).toUpperCase() + "] writing value " + Integer.toHexString(value).toUpperCase() + " to port " + Integer.toHexString(portAddress).toUpperCase());
                            throw new ModuleUnknownPort(MODULE_TYPE + " -> does not recognise command " + keyboard.controller.lastCommand + " writing value " + Integer.toHexString(value).toUpperCase() + " to port " + Integer.toHexString(portAddress).toUpperCase());
                    }
                } else {
                    keyboard.controller.commandData = 0;
                    keyboard.controller.expectingPort60h = 0;
                    if (keyboard.controller.kbdClockEnabled == 0) {
                        setKBClock((byte) 1);
                    }
                    dataPortToInternalKB(value);
                }
                return;
            case 0x64:
                keyboard.controller.commandData = 1;
                keyboard.controller.lastCommand = value;
                keyboard.controller.expectingPort60h = 0;
                switch(value) {
                    case 0x20:
                        logger.log(Level.FINE, "Read keyboard controller command byte");
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        byte command_byte = (byte) ((keyboard.controller.translateScancode << 6) | ((keyboard.controller.auxClockEnabled == 0 ? 1 : 0) << 5) | ((keyboard.controller.kbdClockEnabled == 0 ? 1 : 0) << 4) | (0 << 3) | (keyboard.controller.systemFlag << 2) | (keyboard.controller.allowIRQ12 << 1) | (keyboard.controller.allowIRQ1 << 0));
                        enqueueControllerBuffer(command_byte);
                        return;
                    case 0x60:
                        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Write keyboard controller command byte");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xA0:
                        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + "Unsupported command on port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                    case (byte) 0xA1:
                        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Controller firmware version request: ignored");
                        return;
                    case (byte) 0xA7:
                        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Mouse port (disable) not supported");
                        return;
                    case (byte) 0xA8:
                        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Mouse port (enable) not supported");
                        return;
                    case (byte) 0xA9:
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        enqueueControllerBuffer((byte) 0xFF);
                        return;
                    case (byte) 0xAA:
                        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Controller self test");
                        if (kbdInitialised == 0) {
                            keyboard.controllerQueue.clear();
                            keyboard.controller.outputBuffer = 0;
                            kbdInitialised++;
                        }
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        keyboard.controller.systemFlag = 1;
                        enqueueControllerBuffer((byte) 0x55);
                        return;
                    case (byte) 0xAB:
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        enqueueControllerBuffer((byte) 0x00);
                        return;
                    case (byte) 0xAD:
                        logger.log(Level.CONFIG, "[" + MODULE_TYPE + "]" + " Keyboard disabled");
                        setKBClock((byte) 0);
                        return;
                    case (byte) 0xAE:
                        logger.log(Level.CONFIG, "[" + MODULE_TYPE + "]" + " Keyboard enabled");
                        setKBClock((byte) 1);
                        return;
                    case (byte) 0xAF:
                        logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + "Unsupported command on port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                    case (byte) 0xC0:
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        enqueueControllerBuffer((byte) 0x80);
                        return;
                    case (byte) 0xC1:
                    case (byte) 0xC2:
                        logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + "Unsupported command on port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                    case (byte) 0xD0:
                        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + "Partially supported command on port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        if (keyboard.controller.outputBuffer != 0) {
                            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " command 0x" + Integer.toHexString(value).toUpperCase() + " encountered but output buffer not empty!");
                            return;
                        }
                        byte p2 = (byte) ((keyboard.controller.irq12Requested << 5) | (keyboard.controller.irq1Requested << 4) | (motherboard.getA20() ? 1 : 0 << 1) | 0x01);
                        enqueueControllerBuffer(p2);
                        return;
                    case (byte) 0xD1:
                        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Port 0x64: write output port P2");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xD2:
                        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Port 0x64: write keyboard output buffer");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xD3:
                        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Port 0x64: write mouse output buffer");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xD4:
                        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Port 0x64: write to mouse");
                        keyboard.controller.expectingPort60h = 1;
                        return;
                    case (byte) 0xDD:
                        motherboard.setA20(false);
                        return;
                    case (byte) 0xDF:
                        motherboard.setA20(true);
                        return;
                    case (byte) 0xE0:
                        logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + " Unsupported command to port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                    case (byte) 0xFE:
                        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Port 0x64: system reset");
                        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Function not (yet) implemented");
                        return;
                    default:
                        if ((value >= 0xF0 && value <= 0xFD) || value == 0xFF) {
                            logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Port 0x64: pulse output bits");
                            return;
                        }
                        logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + " Unsupported command to port 0x64: 0x" + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase());
                        return;
                }
            default:
                throw new ModuleUnknownPort("[" + MODULE_TYPE + "]" + " does not recognise OUT port 0x" + Integer.toHexString(portAddress).toUpperCase());
        }
    }

    public byte[] getIOPortWord(int portAddress) throws ModuleException, ModuleWriteOnlyPortException {
        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " IN command (word) to port 0x" + Integer.toHexString(portAddress).toUpperCase() + " received");
        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Returned default value 0xFFFF to AX");
        return new byte[] { (byte) 0x0FF, (byte) 0x0FF };
    }

    public void setIOPortWord(int portAddress, byte[] dataWord) throws ModuleException {
        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " OUT command (word) to port 0x" + Integer.toHexString(portAddress).toUpperCase() + " received. No action taken.");
        return;
    }

    public byte[] getIOPortDoubleWord(int portAddress) throws ModuleException, ModuleWriteOnlyPortException {
        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " IN command (double word) to port 0x" + Integer.toHexString(portAddress).toUpperCase() + " received");
        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Returned default value 0xFFFFFFFF to eAX");
        return new byte[] { (byte) 0x0FF, (byte) 0x0FF, (byte) 0x0FF, (byte) 0x0FF };
    }

    public void setIOPortDoubleWord(int portAddress, byte[] dataDoubleWord) throws ModuleException {
        logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " OUT command (double word) to port 0x" + Integer.toHexString(portAddress).toUpperCase() + " received. No action taken.");
        return;
    }

    protected void setInterrupt() {
        pic.setIRQ(1);
        pendingIRQ = true;
    }

    protected void clearInterrupt() {
        pic.clearIRQ(1);
        if (pendingIRQ == true) {
            pendingIRQ = false;
        }
    }

    /**
     * Set keyboard state (on/off) by enabling/disabling its clock line<BR>
     * @param value the state of the clock to be set
     * <BR>
     */
    void setKeyboardClockState(byte value) {
        byte oldKBDClockEnabled;
        if (value == 0) {
            keyboard.controller.kbdClockEnabled = 0;
        } else {
            oldKBDClockEnabled = keyboard.controller.kbdClockEnabled;
            keyboard.controller.kbdClockEnabled = 1;
            if (oldKBDClockEnabled == 0 && keyboard.controller.outputBuffer == 0) {
                activate_timer();
            }
        }
    }

    /**
     * Queue data in the keyboard controller buffer<BR>
     * @param data  the data to be added to the end of the queue
     * 
     */
    void enqueueControllerBuffer(byte data) {
        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Queueing 0x" + Integer.toHexString(data).toUpperCase() + " in keyboard controller buffer");
        if (keyboard.controller.outputBuffer != 0) {
            if (keyboard.controllerQueue.size() >= TheKeyboard.CONTROLLER_QUEUE_SIZE) {
                logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + " queueKBControllerBuffer(): Keyboard controller is full!");
            }
            keyboard.controllerQueue.add(data);
            return;
        }
        keyboard.controller.kbdOutputBuffer = data;
        keyboard.controller.outputBuffer = 1;
        keyboard.controller.auxBuffer = 0;
        keyboard.controller.inputBuffer = 0;
        if (keyboard.controller.allowIRQ1 != 0) {
            keyboard.controller.irq1Requested = 1;
        }
    }

    /**
     * Queue data in the internal keyboard buffer<BR>
     * @param scancode the data to be added to the end of the queue
     * 
     */
    void enqueueInternalBuffer(byte scancode) {
        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " enqueueInternalBuffer: 0x" + Integer.toHexString(0x100 | scancode & 0xFF).substring(1).toUpperCase());
        if (keyboard.internalBuffer.buffer.size() >= KeyboardInternalBuffer.NUM_ELEMENTS) {
            logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + "internal keyboard buffer full, ignoring scancode " + scancode);
            return;
        }
        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " enqueueInternalBuffer: adding scancode " + Integer.toHexString(0x100 | scancode & 0xFF).substring(1).toUpperCase() + "h to internal buffer");
        keyboard.internalBuffer.buffer.add(scancode);
        if (!(keyboard.controller.outputBuffer != 0) && (keyboard.controller.kbdClockEnabled != 0)) {
            activate_timer();
            logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Timer activated");
            return;
        }
    }

    /**
     * Activate a 'timer' to indicate to the periodic polling function<BR>
     * the internal keyboard queue should be checked for data.<BR><BR>
     * 
     * A timer can only be set, not disabled.
     * 
     */
    void activate_timer() {
        if (keyboard.controller.timerPending == 0) {
            keyboard.controller.timerPending = 1;
        }
    }

    /**
     * The keyboard polling function<BR>
     * This determines the IRQs to be raised and retrieves character data from the internal keyboard buffer, if available
     * 
     */
    int poll() {
        int returnValue;
        returnValue = (keyboard.controller.irq1Requested | (keyboard.controller.irq12Requested << 1));
        keyboard.controller.irq1Requested = 0;
        keyboard.controller.irq12Requested = 0;
        if (keyboard.controller.timerPending == 0) {
            logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " poll(): No timer pending");
            return (returnValue);
        }
        keyboard.controller.timerPending = 0;
        if (keyboard.controller.outputBuffer != 0) {
            logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " poll(): output buffer");
            return (returnValue);
        }
        if (!keyboard.internalBuffer.buffer.isEmpty() && (keyboard.controller.kbdClockEnabled != 0 || keyboard.controller.batInProgress != 0)) {
            logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " poll(): key in internal buffer waiting");
            keyboard.controller.kbdOutputBuffer = keyboard.internalBuffer.buffer.remove(0);
            keyboard.controller.outputBuffer = 1;
            if (keyboard.controller.allowIRQ1 != 0) keyboard.controller.irq1Requested = 1;
        } else {
            logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " poll(): no keys waiting");
        }
        return (returnValue);
    }

    /**
     * Set the keyboard clock, which determines the on/off state of the keyboard<BR>
     * @param value the state of the clock should be set to:<BR>
     *              0: keyboard clock is disabled, turning the keyboard off<BR>
     *              other: keyboard clock is enabled, turning the keyboard on 
     * <BR>
     */
    void setKBClock(byte value) {
        byte oldKBDClock;
        if (value == 0) {
            keyboard.controller.kbdClockEnabled = 0;
        } else {
            oldKBDClock = keyboard.controller.kbdClockEnabled;
            keyboard.controller.kbdClockEnabled = 1;
            if (oldKBDClock == 0 && keyboard.controller.outputBuffer == 0) ;
            {
                activate_timer();
            }
        }
    }

    /**
     * Data passing directly from keyboard controller to keyboard<BR>
     * The keyboard usually immediately responds by enqueueing data in its buffer for the keyboard controller<BR>
     * 
     * @param value the data passed from controller to keyboard <BR>
     */
    void dataPortToInternalKB(byte value) {
        logger.log(Level.CONFIG, "[" + MODULE_TYPE + "]" + " Controller passing byte " + Integer.toHexString(0x100 | value & 0xFF).substring(1).toUpperCase() + "h directly to keyboard");
        if (keyboard.internalBuffer.expectingTypematic != 0) {
            keyboard.internalBuffer.expectingTypematic = 0;
            keyboard.internalBuffer.keyPressDelay = (byte) ((value >> 5) & 0x03);
            switch(keyboard.internalBuffer.keyPressDelay) {
                case 0:
                    logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " typematic delay (unused) set to 250 ms");
                    break;
                case 1:
                    logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " typematic delay (unused) set to 500 ms");
                    break;
                case 2:
                    logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " typematic delay (unused) set to 750 ms");
                    break;
                case 3:
                    logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " typematic delay (unused) set to 1000 ms");
                    break;
            }
            keyboard.internalBuffer.keyRepeatRate = (byte) (value & 0x1f);
            double cps = 1000 / ((8 + (value & 0x07)) * Math.pow(2, ((value >> 3) & 0x03)) * 4.17);
            DecimalFormat format = new DecimalFormat("##.#");
            logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Repeat rate (unused) set to " + format.format(cps) + "char. per second");
            enqueueInternalBuffer((byte) 0xFA);
            return;
        }
        if (keyboard.internalBuffer.expectingLEDWrite != 0) {
            keyboard.internalBuffer.expectingLEDWrite = 0;
            keyboard.internalBuffer.ledStatus = value;
            logger.log(Level.SEVERE, "[" + MODULE_TYPE + "]" + " Status of LEDs set to " + Integer.toHexString(keyboard.internalBuffer.ledStatus));
            switch(value) {
                case 0x00:
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_NUMLOCK_OFF);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_CAPSLOCK_OFF);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_SCROLLLOCK_OFF);
                    break;
                case 0x01:
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_NUMLOCK_ON);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_CAPSLOCK_OFF);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_SCROLLLOCK_OFF);
                    break;
                case 0x02:
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_NUMLOCK_OFF);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_CAPSLOCK_ON);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_SCROLLLOCK_OFF);
                    break;
                case 0x03:
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_NUMLOCK_ON);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_CAPSLOCK_ON);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_SCROLLLOCK_OFF);
                    break;
                case 0x04:
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_NUMLOCK_OFF);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_CAPSLOCK_OFF);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_SCROLLLOCK_ON);
                    break;
                case 0x05:
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_NUMLOCK_ON);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_CAPSLOCK_OFF);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_SCROLLLOCK_ON);
                    break;
                case 0x06:
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_NUMLOCK_ON);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_CAPSLOCK_ON);
                    emu.statusChanged(Emulator.MODULE_KEYBOARD_SCROLLLOCK_ON);
                    break;
                default:
                    break;
            }
            enqueueInternalBuffer((byte) 0xFA);
            return;
        }
        if (keyboard.internalBuffer.expectingScancodeSet != 0) {
            keyboard.internalBuffer.expectingScancodeSet = 0;
            if (value != 0) {
                if (value < 4) {
                    keyboard.controller.currentScancodeSet = (byte) (value - 1);
                    logger.log(Level.INFO, "[" + MODULE_TYPE + "]" + " Switching to scancode set " + Integer.toHexString(keyboard.controller.currentScancodeSet + 1));
                    enqueueInternalBuffer((byte) 0xFA);
                } else {
                    logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Scancode set number out of range: " + Integer.toHexString(value).toUpperCase());
                    enqueueInternalBuffer((byte) 0xFF);
                }
            } else {
                enqueueInternalBuffer((byte) 0xFA);
                enqueueInternalBuffer((byte) (1 + (keyboard.controller.currentScancodeSet)));
            }
            return;
        }
        switch(value) {
            case (byte) 0x00:
                enqueueInternalBuffer((byte) 0xFA);
                break;
            case (byte) 0x05:
                keyboard.controller.systemFlag = 1;
                enqueueInternalBuffer((byte) 0xFE);
                break;
            case (byte) 0xD3:
                enqueueInternalBuffer((byte) 0xFA);
                break;
            case (byte) 0xED:
                keyboard.internalBuffer.expectingLEDWrite = 1;
                enqueueInternalBuffer((byte) 0xFA);
                break;
            case (byte) 0xEE:
                enqueueInternalBuffer((byte) 0xEE);
                break;
            case (byte) 0xF0:
                keyboard.internalBuffer.expectingScancodeSet = 1;
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Expecting scancode set information");
                enqueueInternalBuffer((byte) 0xFA);
                break;
            case (byte) 0xF2:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Read Keyboard ID command received");
                enqueueInternalBuffer((byte) 0xFA);
                enqueueInternalBuffer((byte) 0xAB);
                if (keyboard.controller.translateScancode != 0) enqueueInternalBuffer((byte) 0x41); else enqueueInternalBuffer((byte) 0x83);
                break;
            case (byte) 0xF3:
                keyboard.internalBuffer.expectingTypematic = 1;
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " Expecting Typematic Rate/Delay information");
                enqueueInternalBuffer((byte) 0xFA);
                break;
            case (byte) 0xF4:
                keyboard.internalBuffer.scanningEnabled = 1;
                enqueueInternalBuffer((byte) 0xFA);
                break;
            case (byte) 0xF5:
                reset(1);
                enqueueInternalBuffer((byte) 0xFA);
                keyboard.internalBuffer.scanningEnabled = 0;
                logger.log(Level.CONFIG, "[" + MODULE_TYPE + "]" + " Reset w/ Disable command received");
                break;
            case (byte) 0xF6:
                reset(1);
                enqueueInternalBuffer((byte) 0xFA);
                keyboard.internalBuffer.scanningEnabled = 1;
                logger.log(Level.CONFIG, "[" + MODULE_TYPE + "]" + " Reset w Enable command received");
                break;
            case (byte) 0xFE:
                logger.log(Level.WARNING, "[" + MODULE_TYPE + "]" + " Requesting resend: transmission error!!");
                break;
            case (byte) 0xFF:
                logger.log(Level.INFO, "[" + MODULE_TYPE + "]" + " Reset w/ BAT command received");
                reset(1);
                enqueueInternalBuffer((byte) 0xFA);
                keyboard.controller.batInProgress = 1;
                enqueueInternalBuffer((byte) 0xAA);
                break;
            case (byte) 0xF7:
            case (byte) 0xF8:
            case (byte) 0xF9:
            case (byte) 0xFA:
            case (byte) 0xFB:
            case (byte) 0xFC:
            case (byte) 0xFD:
            default:
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + " dataPortToInternalKB(): got value of " + value);
                enqueueInternalBuffer((byte) 0xFE);
                break;
        }
    }

    /**
     * Method generateScancode
     * Generates a scancode from a KeyEvent.<BR>
     * The scancode depends on what scancode set is currently active, and whether the key is pressed or released
     * 
     * @param keyEvent KeyEvent containing keypress information
     * @param eventType Type of KeyEvent, either pressed (0x00) or released (0x01)
     */
    public void generateScancode(KeyEvent keyEvent, int eventType) {
        String[] scancode;
        int i, parsedInt;
        logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + "generateScancode(): " + keyEvent.getKeyCode() + ((eventType == 1) ? " pressed" : " released"));
        if (keyboard.controller.kbdClockEnabled == 0 || keyboard.internalBuffer.scanningEnabled == 0) return;
        scancode = scanCodeSet.scancodes[keyboard.controller.currentScancodeSet][keyEvent.getKeyCode()][eventType].split(" ");
        if (keyEvent.getKeyCode() == KeyEvent.VK_CONTROL || keyEvent.getKeyCode() == KeyEvent.VK_ALT || keyEvent.getKeyCode() == KeyEvent.VK_SHIFT) {
            if (keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_LEFT) scanCodeSet.scancodes[keyboard.controller.currentScancodeSet][keyEvent.getKeyCode() - 3][eventType].split(" ");
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            if (keyEvent.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD) scanCodeSet.scancodes[keyboard.controller.currentScancodeSet][keyEvent.getKeyCode() + 1][eventType].split(" ");
        }
        if (keyboard.controller.translateScancode != 0) {
            int valueOR = 0x00;
            for (i = 0; i < scancode.length; i++) {
                parsedInt = Integer.parseInt(scancode[i], 16);
                if (parsedInt == 0xF0) {
                    valueOR = 0x80;
                } else {
                    logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + "generateScancode(): Translated scancode to " + (scanCodeSet.translate8042[parsedInt] | valueOR));
                    enqueueInternalBuffer((byte) (scanCodeSet.translate8042[parsedInt] | valueOR));
                    valueOR = 0x00;
                }
            }
        } else {
            for (i = 0; i < scancode.length; i++) {
                logger.log(Level.FINE, "[" + MODULE_TYPE + "]" + "generateScancode(): Writing raw " + scancode[i]);
                enqueueInternalBuffer((byte) Integer.parseInt(scancode[i], 16));
            }
        }
    }
}
