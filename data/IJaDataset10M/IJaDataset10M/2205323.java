package sw_emulator.hardware.chip;

import sw_emulator.util.Monitor;
import sw_emulator.hardware.powered;
import sw_emulator.hardware.signaller;
import sw_emulator.hardware.bus.readableBus;
import sw_emulator.hardware.bus.writeableBus;

/**
 * Emulate the Sid chip.
 *
 * @author Ice
 * @version 1.00 15/04/2000
 */
public class Sid extends Thread implements powered, signaller, readableBus, writeableBus {

    /**
   * The state of power
   */
    private boolean power = false;

    /**
   * The monitor where synchronization with a clock
   */
    protected Monitor monitor;

    /**
   * Write a byte to the bus at specific address location.
   *
   * @param addr the address location
   * @param value the byte value
   */
    public void write(int addr, byte value) {
        switch(addr) {
            default:
        }
    }

    /**
   * Read a byte from the bus at specific address location.
   *
   * @param addr the address location
   * @return the readed byte
   */
    public byte read(int addr) {
        switch(addr) {
            default:
                return (byte) 0xFF;
        }
    }

    /**
   * Execute the cycles of SID according to external clock.
   */
    public void run() {
        while (true) {
            if (!power) while (!power) {
                yield();
            }
            monitor.opWait();
        }
    }

    /**
   * Notify a signal to the chip
   *
   * @param type the type of signal
   * @param value the value of the signal (0/1)
   */
    public void notifySignal(int type, int value) {
        switch(type) {
            case S_RESET:
                break;
            default:
                System.err.println("ERROR: an invalid " + type + " signal was notify to SID");
        }
    }

    /**
   * Power on the electronic component
   */
    public void powerOn() {
        power = true;
    }

    /**
   * Power off the electronic component
   */
    public void powerOff() {
        power = false;
    }
}
