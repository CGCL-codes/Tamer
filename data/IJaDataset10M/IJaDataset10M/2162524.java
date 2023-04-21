package nl.kbna.dioscuri.module.keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal keyboard buffer
 * Virtual buffer - located in the keyboard hardware 
 * Contains data intended for controller (ACKS/NACKS, scancodes, etc.) 
 *
 */
public class KeyboardInternalBuffer {

    protected static final int NUM_ELEMENTS = 16;

    List<Byte> buffer = new ArrayList<Byte>(NUM_ELEMENTS);

    byte expectingTypematic;

    byte expectingLEDWrite;

    byte expectingScancodeSet;

    byte keyPressDelay;

    byte keyRepeatRate;

    byte ledStatus;

    byte scanningEnabled;
}
