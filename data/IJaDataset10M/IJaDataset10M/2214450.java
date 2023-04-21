package com.lambelly.lambnes.platform.apu.registers;

import org.apache.log4j.Logger;

public class APUPulse1ChannelRegister {

    public static final int REGISTER_ADDRESS = 0x4000;

    private static APUPulse1ChannelRegister register = new APUPulse1ChannelRegister();

    private int dutyCycle = 0;

    private int lengthCounterHalt = 0;

    private int envelope = 0;

    private static Integer rawControlByte = null;

    private Logger logger = Logger.getLogger(APUPulse1ChannelRegister.class);

    private APUPulse1ChannelRegister() {
    }

    public int cycle() {
        if (APUPulse1ChannelRegister.getRawControlByte() != null) {
            this.parseWrite(APUPulse1ChannelRegister.getRawControlByte());
        }
        return 0;
    }

    private void parseWrite(int rawControlByte) {
        this.setDutyCycle(((rawControlByte & 0xC0) >> 6));
        this.setLengthCounterHalt(((rawControlByte & 0x20) >> 5));
        this.setEnvelope(rawControlByte & 0x1F);
    }

    public static void setRegisterValue(int value) {
        APUPulse1ChannelRegister.setRawControlByte(value);
    }

    private static Integer getRawControlByte() {
        return rawControlByte;
    }

    private static void setRawControlByte(Integer rawControlByte) {
        APUPulse1ChannelRegister.rawControlByte = rawControlByte;
    }

    public static APUPulse1ChannelRegister getRegister() {
        return register;
    }

    private static void setRegister(APUPulse1ChannelRegister register) {
        APUPulse1ChannelRegister.register = register;
    }

    public int getDutyCycle() {
        return dutyCycle;
    }

    public void setDutyCycle(int dutyCycle) {
        this.dutyCycle = dutyCycle;
    }

    public int getLengthCounterHalt() {
        return lengthCounterHalt;
    }

    public void setLengthCounterHalt(int lengthCounterHalt) {
        this.lengthCounterHalt = lengthCounterHalt;
    }

    public int getEnvelope() {
        return envelope;
    }

    public void setEnvelope(int envelope) {
        this.envelope = envelope;
    }
}
