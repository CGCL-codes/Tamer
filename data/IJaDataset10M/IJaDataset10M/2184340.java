package com.amd.javalabs.tools.disasm.instructions;

import com.amd.javalabs.tools.disasm.InstructionBuildContext;
import com.amd.javalabs.tools.disasm.Prefix;

public class X64InstructionSet implements InstructionSet {

    public OpcodeTableEntry getOneByteTableEntry(int _opcode) {
        OpcodeTableEntry tableEntry = null;
        if (_opcode >= 0x40 && _opcode <= 0x4f) {
            switch(_opcode & 0xf) {
                case 0x00:
                    tableEntry = PrefixTableEntry.REX_____;
                    break;
                case 0x01:
                    tableEntry = PrefixTableEntry.REX____B;
                    break;
                case 0x02:
                    tableEntry = PrefixTableEntry.REX___X_;
                    break;
                case 0x03:
                    tableEntry = PrefixTableEntry.REX___XB;
                    break;
                case 0x04:
                    tableEntry = PrefixTableEntry.REX__R__;
                    break;
                case 0x05:
                    tableEntry = PrefixTableEntry.REX__R_B;
                    break;
                case 0x06:
                    tableEntry = PrefixTableEntry.REX__RX_;
                    break;
                case 0x07:
                    tableEntry = PrefixTableEntry.REX__RXB;
                    break;
                case 0x08:
                    tableEntry = PrefixTableEntry.REX_W___;
                    break;
                case 0x09:
                    tableEntry = PrefixTableEntry.REX_W__B;
                    break;
                case 0x0a:
                    tableEntry = PrefixTableEntry.REX_W_X_;
                    break;
                case 0x0b:
                    tableEntry = PrefixTableEntry.REX_W_XB;
                    break;
                case 0x0c:
                    tableEntry = PrefixTableEntry.REX_WR__;
                    break;
                case 0x0d:
                    tableEntry = PrefixTableEntry.REX_WR_B;
                    break;
                case 0x0e:
                    tableEntry = PrefixTableEntry.REX_WRX_;
                    break;
                case 0x0f:
                    tableEntry = PrefixTableEntry.REX_WRXB;
                    break;
            }
        } else {
            tableEntry = delegate.getOneByteTableEntry(_opcode);
        }
        return (tableEntry);
    }

    private static X64InstructionSet instance = new X64InstructionSet();

    private X86InstructionSet delegate = X86InstructionSet.getInstance();

    private X64InstructionSet() {
    }

    public static X64InstructionSet getInstance() {
        return (instance);
    }

    public OpcodeTable getOneByteTable() {
        return (delegate.getOneByteTable());
    }

    public Instruction buildInstruction(InstructionBuildContext _ibc) {
        return (delegate.buildInstruction(_ibc));
    }

    public void setBuildContextOperandAndAddressSize(InstructionBuildContext _ibc) {
        int operandSize = 32;
        int addressSize = 64;
        if ((_ibc.getPrefixes().getValue() & Prefix.DATA) == Prefix.DATA) {
            operandSize = 16;
        }
        if ((_ibc.getPrefixes().getValue() & Prefix.REX_W) == Prefix.REX_W) {
            operandSize = 64;
        }
        if ((_ibc.getPrefixes().getValue() & Prefix.ADR) == Prefix.ADR) {
            addressSize = 32;
        }
        _ibc.setOperandSize(operandSize);
        _ibc.setAddressSize(addressSize);
    }
}
