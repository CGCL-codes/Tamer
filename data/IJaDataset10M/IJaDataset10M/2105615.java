package servers;

import p5.serial.Serial;
import processing.core.PApplet;

/**
 * Together with the Firmata 2 firmware (an Arduino sketch uploaded to the
 * Arduino board), this class allows you to control the Arduino board from
 * Processing: reading from and writing to the digital pins and reading the
 * analog inputs.
 */
public class Arduino {

    /**
   * Constant to set a pin to input mode (in a call to pinMode()).
   */
    public static final int INPUT = 0;

    /**
   * Constant to set a pin to output mode (in a call to pinMode()).
   */
    public static final int OUTPUT = 1;

    /**
   * Constant to set a pin to analog mode (in a call to pinMode()).
   */
    public static final int ANALOG = 2;

    /**
   * Constant to set a pin to PWM mode (in a call to pinMode()).
   */
    public static final int PWM = 3;

    /**
   * Constant to set a pin to servo mode (in a call to pinMode()).
   */
    public static final int SERVO = 4;

    /**
   * Constant to set a pin to shiftIn/shiftOut mode (in a call to pinMode()).
   */
    public static final int SHIFT = 5;

    /**
   * Constant to set a pin to I2C mode (in a call to pinMode()).
   */
    public static final int I2C = 6;

    /**
   * Constant to write a high value (+5 volts) to a pin (in a call to
   * digitalWrite()).
   */
    public static final int LOW = 0;

    /**
   * Constant to write a low value (0 volts) to a pin (in a call to
   * digitalWrite()).
   */
    public static final int HIGH = 1;

    private final int MAX_DATA_BYTES = 32;

    private final int DIGITAL_MESSAGE = 0x90;

    private final int ANALOG_MESSAGE = 0xE0;

    private final int REPORT_ANALOG = 0xC0;

    private final int REPORT_DIGITAL = 0xD0;

    private final int SET_PIN_MODE = 0xF4;

    private final int REPORT_VERSION = 0xF9;

    private final int SYSTEM_RESET = 0xFF;

    private final int START_SYSEX = 0xF0;

    private final int END_SYSEX = 0xF7;

    PApplet parent;

    Serial serial;

    SerialProxy serialProxy;

    int waitForData = 0;

    int executeMultiByteCommand = 0;

    int multiByteChannel = 0;

    int[] storedInputData = new int[MAX_DATA_BYTES];

    boolean parsingSysex;

    int sysexBytesRead;

    int[] digitalOutputData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    int[] digitalInputData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    int[] analogInputData = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    int majorVersion = 0;

    int minorVersion = 0;

    public class SerialProxy extends PApplet {

        public SerialProxy() {
            disposeMethods = new RegisteredMethods();
        }

        public void serialEvent(Serial which) {
            while (available() > 0) processInput();
        }
    }

    public void dispose() {
        this.serial.dispose();
    }

    /**
   * Get a list of the available Arduino boards; currently all serial devices
   * (i.e. the same as Serial.list()).  In theory, this should figure out
   * what's an Arduino board and what's not.
   */
    public static String[] list() {
        return Serial.list();
    }

    /**
   * Create a proxy to an Arduino board running the Firmata 2 firmware at the
   * default baud rate of 57600.
   *
   * @param parent the Processing sketch creating this Arduino board
   * (i.e. "this").
   * @param iname the name of the serial device associated with the Arduino
   * board (e.g. one the elements of the array returned by Arduino.list())
   */
    public Arduino(PApplet parent, String iname) {
        this(parent, iname, 57600);
    }

    /**
   * Create a proxy to an Arduino board running the Firmata 2 firmware.
   *
   * @param parent the Processing sketch creating this Arduino board
   * (i.e. "this").
   * @param iname the name of the serial device associated with the Arduino
   * board (e.g. one the elements of the array returned by Arduino.list())
   * @param irate the baud rate to use to communicate with the Arduino board
   * (the firmata library defaults to 57600, and the examples use this rate,
   * but other firmwares may override it)
   */
    public Arduino(PApplet parent, String iname, int irate) {
        this.parent = parent;
        this.serialProxy = new SerialProxy();
        this.serial = new Serial(serialProxy, iname, irate);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
        }
        for (int i = 0; i < 6; i++) {
            serial.write(REPORT_ANALOG | i);
            serial.write(1);
        }
        for (int i = 0; i < 2; i++) {
            serial.write(REPORT_DIGITAL | i);
            serial.write(1);
        }
        parent.registerDispose(this);
    }

    /**
   * Returns the last known value read from the digital pin: HIGH or LOW.
   *
   * @param pin the digital pin whose value should be returned (from 2 to 13,
   * since pins 0 and 1 are used for serial communication)
   */
    public int digitalRead(int pin) {
        return (digitalInputData[pin >> 3] >> (pin & 0x07)) & 0x01;
    }

    /**
   * Returns the last known value read from the analog pin: 0 (0 volts) to
   * 1023 (5 volts).
   *
   * @param pin the analog pin whose value should be returned (from 0 to 5)
   */
    public int analogRead(int pin) {
        return analogInputData[pin];
    }

    /**
   * Set a digital pin to input or output mode.
   *
   * @param pin the pin whose mode to set (from 2 to 13)
   * @param mode either Arduino.INPUT or Arduino.OUTPUT
   */
    public void pinMode(int pin, int mode) {
        serial.write(SET_PIN_MODE);
        serial.write(pin);
        serial.write(mode);
    }

    /**
   * Write to a digital pin (the pin must have been put into output mode with
   * pinMode()).
   *
   * @param pin the pin to write to (from 2 to 13)
   * @param value the value to write: Arduino.LOW (0 volts) or Arduino.HIGH
   * (5 volts)
   */
    public void digitalWrite(int pin, int value) {
        int portNumber = (pin >> 3) & 0x0F;
        if (value == 0) digitalOutputData[portNumber] &= ~(1 << (pin & 0x07)); else digitalOutputData[portNumber] |= (1 << (pin & 0x07));
        serial.write(DIGITAL_MESSAGE | portNumber);
        serial.write(digitalOutputData[portNumber] & 0x7F);
        serial.write(digitalOutputData[portNumber] >> 7);
    }

    /**
   * Write an analog value (PWM-wave) to a digital pin.
   *
   * @param pin the pin to write to (must be 9, 10, or 11, as those are they
   * only ones which support hardware pwm)
   * @param the value: 0 being the lowest (always off), and 255 the highest
   * (always on)
   */
    public void analogWrite(int pin, int value) {
        pinMode(pin, PWM);
        serial.write(ANALOG_MESSAGE | (pin & 0x0F));
        serial.write(value & 0x7F);
        serial.write(value >> 7);
    }

    private void setDigitalInputs(int portNumber, int portData) {
        digitalInputData[portNumber] = portData;
    }

    private void setAnalogInput(int pin, int value) {
        analogInputData[pin] = value;
    }

    private void setVersion(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    private int available() {
        return serial.available();
    }

    private void processInput() {
        int inputData = serial.read();
        int command;
        if (parsingSysex) {
            if (inputData == END_SYSEX) {
                parsingSysex = false;
            } else {
                storedInputData[sysexBytesRead] = inputData;
                sysexBytesRead++;
            }
        } else if (waitForData > 0 && inputData < 128) {
            waitForData--;
            storedInputData[waitForData] = inputData;
            if (executeMultiByteCommand != 0 && waitForData == 0) {
                switch(executeMultiByteCommand) {
                    case DIGITAL_MESSAGE:
                        setDigitalInputs(multiByteChannel, (storedInputData[0] << 7) + storedInputData[1]);
                        break;
                    case ANALOG_MESSAGE:
                        setAnalogInput(multiByteChannel, (storedInputData[0] << 7) + storedInputData[1]);
                        break;
                    case REPORT_VERSION:
                        setVersion(storedInputData[1], storedInputData[0]);
                        break;
                }
            }
        } else {
            if (inputData < 0xF0) {
                command = inputData & 0xF0;
                multiByteChannel = inputData & 0x0F;
            } else {
                command = inputData;
            }
            switch(command) {
                case DIGITAL_MESSAGE:
                case ANALOG_MESSAGE:
                case REPORT_VERSION:
                    waitForData = 2;
                    executeMultiByteCommand = command;
                    break;
            }
        }
    }
}
