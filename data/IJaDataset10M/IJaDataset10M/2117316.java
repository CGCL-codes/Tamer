package intermediate;

import frame.*;

public abstract class ThreeAddressCode {

    public Access op1, op2, dst;

    public Frame frame;

    public ThreeAddressCode(Frame frame) {
        this.frame = frame;
    }

    public abstract ThreeAddressCode clone();
}
