package uk.ac.mmu.manmetassembly.core;

import java.util.*;

/**
 *
 * 
 * @mmu:author Daniel Latter
 */
public class Register16 extends Register implements Settable {

    Register8 low;

    Register8 high;

    String regName;

    int valueFor16;

    public Register16(String theRegName) {
        super(theRegName);
        regName = theRegName;
    }

    public void initHighLow() {
        low = new Register8(regName.charAt(0) + "L");
        high = new Register8(regName.charAt(0) + "H");
    }

    public int getValue() {
        if (low == null || high == null) {
            return valueFor16;
        } else return high.getValue() * 256 + low.getValue();
    }

    public void setValue(int theValue) {
        if (theValue <= 65535) {
            System.out.println("IN SETVAL 16: " + theValue);
            if (low == null || high == null) {
                valueFor16 = theValue;
                System.out.println("LOW AND HIGH NULL " + valueFor16);
                setChanged();
                notifyObservers(valueFor16);
            } else if (theValue < 256) {
                low.setValue(theValue);
            } else {
                int highVal = (theValue / 256);
                int lowVal = (theValue % 256);
                high.setValue(highVal);
                low.setValue(lowVal);
            }
        } else {
        }
    }

    public int getLowValue() {
        return low.getValue();
    }

    public void setLowValue(int theValue) {
        low.setValue(theValue);
    }

    public int getHighValue() {
        return high.getValue();
    }

    public void setHighValue(int theValue) {
        high.setValue(theValue);
    }

    /**
     * Get the high 8 bit register. Used for observing the 8 bit registers
     *
     * @return Register8 - the high 8 bit register of this 16 bit regsietr
     */
    public Register8 getHighRegister() {
        return high;
    }

    /**
     * Get the low 8 bit register. Used for observing the 8 bit registers
     * 
     * @return Register8 - the low 8 bit register of this 16 bit regsietr
     */
    public Register8 getLowRegister() {
        return low;
    }
}
