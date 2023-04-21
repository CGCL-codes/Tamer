package com.hifiremote.jp1;

import java.util.LinkedHashMap;
import com.hifiremote.jp1.AssemblerOpCode.AddressMode;
import com.hifiremote.jp1.assembler.MAXQ610data;

public class MAXQProcessor extends LittleEndianProcessor {

    public MAXQProcessor(String name) {
        super(name);
        setRAMAddress(0x0100);
    }
}
