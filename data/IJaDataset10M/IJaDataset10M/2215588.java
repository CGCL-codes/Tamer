package net.sf.lavalamp.device.impl;

import com.sun.jna.Library;

public interface TellStickLibrary extends Library {

    boolean devTurnOn(int intDeviceId);

    boolean devTurnOff(int intDeviceId);
}
