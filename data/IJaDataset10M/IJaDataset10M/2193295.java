package net.virtualinfinity.atrobots.interrupts;

import net.virtualinfinity.atrobots.computer.InterruptHandler;
import net.virtualinfinity.atrobots.computer.MemoryCell;
import net.virtualinfinity.atrobots.hardware.HasOverburner;

/**
 * @author Daniel Pitts
 */
public class OverburnInterrupt extends InterruptHandler {

    private final HasOverburner hasOverburner;

    private final MemoryCell toggler;

    public OverburnInterrupt(HasOverburner hasOverburner, MemoryCell toggler) {
        this.hasOverburner = hasOverburner;
        this.toggler = toggler;
    }

    public void handleInterrupt() {
        hasOverburner.setOverburn(toggler.signed() != 0);
    }
}
