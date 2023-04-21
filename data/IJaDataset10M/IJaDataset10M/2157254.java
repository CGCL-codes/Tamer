package org.norecess.citkit.lir.memoryaddresses;

import org.norecess.citkit.lir.LIRMemoryAddress;
import org.norecess.citkit.lir.LIRRegister;
import org.norecess.citkit.visitors.LIRMemoryAddressVisitor;

public class LIRRegisterMA implements LIRMemoryAddress {

    private final LIRRegister myRegister;

    public LIRRegisterMA(LIRRegister register) {
        myRegister = register;
    }

    public LIRRegister getRegister() {
        return myRegister;
    }

    @Override
    public String toString() {
        return "[" + myRegister + "]";
    }

    public <T> T accept(LIRMemoryAddressVisitor<T> visitor) {
        return visitor.visitRegister(this);
    }
}
