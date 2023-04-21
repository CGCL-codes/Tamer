package com.tapina.robe.runtime.instruction;

import com.tapina.robe.runtime.CPU;
import java.io.Writer;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: gareth
 * Date: Sep 2, 2003
 * Time: 4:55:29 PM
 */
public final class AutoPreIndexedAddressSource extends PreIndexedAddressSource {

    public AutoPreIndexedAddressSource(int baseRegister, Operand offset) {
        super(baseRegister, offset);
    }

    public final int getTransferAddress(CPU cpu) {
        final int address = super.getTransferAddress(cpu);
        if (baseRegister != 15) {
            cpu.R[baseRegister] = address;
        } else {
            cpu.setPC(address);
        }
        return address;
    }

    public void dumpJavaSource(String varName, Writer out) throws IOException {
        offset.dumpJavaSource("offset", out);
        out.write("final int " + varName + " = ");
        if (baseRegister == 15) {
            out.write("cpu.getPC()");
        } else {
            out.write("R[" + baseRegister + "]");
        }
        out.write(" + offset;\n");
        if (baseRegister != 15) {
            out.write("R[" + baseRegister + "] = " + varName + ";\n");
        } else {
            out.write("cpu.setPC(" + varName + ");\n");
        }
    }
}
