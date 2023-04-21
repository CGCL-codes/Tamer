package com.tapina.robe.swi.clib.string;

import com.tapina.robe.runtime.Environment;
import com.tapina.robe.swi.clib.Stub;

/**
 * This function searches for a2 (converted to an unsigned char) in the string pointed to by a1 (including null
 * terminator).
 * Returns: Pointer to the found character, or a null pointer if not found.
 */
public class StrChr extends Stub {

    public void executeStub(Environment environment) {
        final int[] R = environment.getCpu().R;
        final int pos = environment.getMemoryMap().getString0(R[0]).indexOf(R[1]);
        R[0] = (pos != -1) ? R[0] + pos : 0;
    }
}
