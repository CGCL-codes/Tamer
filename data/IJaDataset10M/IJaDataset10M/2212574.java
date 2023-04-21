package com.lambelly.lambnes.platform.ppu.registers;

import org.apache.log4j.*;

public class PPUSprRamAddressRegister {

    public static final int REGISTER_ADDRESS = 0x2003;

    private static final int CYCLES_PER_EXECUTION = 0;

    private Integer rawControlByte = null;

    private Logger logger = Logger.getLogger(PPUSprRamAddressRegister.class);

    private PPUSprRamAddressRegister() {
    }

    public int cycle() {
        return PPUSprRamAddressRegister.CYCLES_PER_EXECUTION;
    }

    public void setRegisterValue(int value) {
        this.setRawControlByte(value);
    }

    public Integer getRawControlByte() {
        if (rawControlByte != null) {
            return rawControlByte;
        } else {
            return 0;
        }
    }

    private void setRawControlByte(Integer rawControlByte) {
        this.rawControlByte = rawControlByte;
    }
}
