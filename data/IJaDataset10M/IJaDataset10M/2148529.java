package org.jsynthlib.synthdrivers.YamahaUB99;

import org.jsynthlib.core.Converter;
import org.jsynthlib.core.Patch;

public class YamahaUB99Converter extends Converter {

    private final YamahaUB99Driver singleDriver;

    public YamahaUB99Converter(YamahaUB99Driver singleDriver) {
        super(singleDriver.getDevice(), "Yamaha UB99 Converter", "Ton Holsink <a.j.m.holsink@chello.nl>");
        this.sysexID = "554239392056312E3030";
        this.patchSize = 0;
        this.singleDriver = singleDriver;
    }

    public boolean supportsPatch(String patchString, byte[] sysex) {
        StringBuilder compareString = new StringBuilder();
        for (int i = 0; i < sysexID.length(); i++) {
            compareString.append(sysexID.charAt(i));
        }
        return (compareString.toString().equalsIgnoreCase(patchString.substring(0, sysexID.length()))) && (sysex.length == YamahaUB99Const.BANK_SIZE);
    }

    public Patch[] extractPatch(Patch p) {
        byte[] sysex = p.getByteArray();
        sysex[0] = (byte) 0xF0;
        Patch[] newPatchArray = new Patch[1];
        newPatchArray[0] = new Patch(sysex, new YamahaUB99BankDriver(singleDriver));
        return newPatchArray;
    }
}
