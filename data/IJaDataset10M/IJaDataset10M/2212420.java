package com.amd.javalabs.tools.disasm.instructions;

import com.amd.javalabs.tools.disasm.Prefix;

public class PrefixOpcodeTable implements OpcodeTableEntry {

    private int opcode;

    public int getOpcode() {
        return opcode;
    }

    private final OpcodeTable noPrefixTable;

    private final OpcodeTable prefix66Table;

    private final OpcodeTable prefixF2Table;

    private final OpcodeTable prefixF3Table;

    public PrefixOpcodeTable(int _opcode, OpcodeTable _noPrefixTable, OpcodeTable _prefix66Table, OpcodeTable _prefixF2Table, OpcodeTable _prefixF3Table) {
        noPrefixTable = _noPrefixTable;
        prefix66Table = _prefix66Table;
        prefixF2Table = _prefixF2Table;
        prefixF3Table = _prefixF3Table;
    }

    public OpcodeTableEntry get(Prefix _prefixes, int _index) {
        OpcodeTable table = noPrefixTable;
        if (prefix66Table != null && _prefixes.isSet(Prefix.DATA)) {
            table = prefix66Table;
        } else if (prefixF2Table != null && _prefixes.isSet(Prefix.DATA)) {
            table = prefixF2Table;
        } else if (prefixF2Table != null && _prefixes.isSet(Prefix.DATA)) {
            table = prefixF3Table;
        }
        return (table.getTableEntry(_index));
    }
}
