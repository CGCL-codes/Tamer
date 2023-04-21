package org.jsynthlib.synthdrivers.YamahaTX802;

import org.jsynthlib.core.Device;
import org.jsynthlib.core.Patch;
import org.jsynthlib.synthdrivers.YamahaDX7.common.DX7FamilyDevice;
import org.jsynthlib.synthdrivers.YamahaDX7.common.DX7FamilyVoiceBankDriver;

public class YamahaTX802VoiceBankDriver extends DX7FamilyVoiceBankDriver {

    public YamahaTX802VoiceBankDriver(final Device device) {
        super(device, YamahaTX802VoiceConstants.INIT_VOICE, YamahaTX802VoiceConstants.BANK_VOICE_PATCH_NUMBERS, YamahaTX802VoiceConstants.BANK_VOICE_BANK_NUMBERS);
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getSwOffMemProtFlag() & 0x01) == 1) {
            YamahaTX802SysexHelpers.swOffMemProt(this, (byte) (getChannel() + 0x10));
        } else {
            if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) {
                YamahaTX802Strings.dxShowInformation(toString(), YamahaTX802Strings.MEMORY_PROTECTION_STRING);
            }
        }
        YamahaTX802SysexHelpers.chBlock(this, (byte) (getChannel() + 0x10), (byte) (bankNum));
        sendPatchWorker(p);
    }

    public void requestPatchDump(int bankNum, int patchNum) {
        YamahaTX802SysexHelpers.chBlock(this, (byte) (getChannel() + 0x10), (byte) (bankNum));
        send(sysexRequestDump.toSysexMessage(getChannel() + 0x20));
    }
}
