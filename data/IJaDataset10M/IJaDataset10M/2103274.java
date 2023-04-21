package net.sourceforge.picdev.components;

import java.util.HashMap;

public class ProgramMemory extends Memory {

    private boolean[] breakpoint;

    private HashMap<Integer, Integer> lineToAddressMap = new HashMap<Integer, Integer>();

    private HashMap<Integer, Integer> addressToLineMap = new HashMap<Integer, Integer>();

    private HashMap<Integer, String> addressToFileMap = new HashMap<Integer, String>();

    ProgramMemory(int size, int dataword_length, boolean writeable) {
        super(size, dataword_length, writeable);
        for (int i = 0; i < memory.length; i++) {
            memory[i] = 0xffffffff;
        }
        breakpoint = new boolean[size];
    }

    public int getSourceLineFromAddress(String file, int address) {
        if (addressToLineMap.containsKey(address)) {
            int line = addressToLineMap.get(address);
            if (addressToFileMap.containsKey(address)) {
                return line;
            }
        }
        return -1;
    }

    public int getAddressFromSourceLine(String file, int line) {
        if (lineToAddressMap.containsKey(line)) {
            int address = lineToAddressMap.get(line);
            if (addressToFileMap.get(address).equals(file)) {
                return address;
            }
        }
        return -1;
    }

    public String getSourceFileAtAddress(int address) {
        return addressToFileMap.get(address);
    }

    public boolean hasBreakpoint(int address) {
        if (address >= getSize()) return false;
        return breakpoint[address];
    }

    public void setBreakpoint(int address, boolean enabled) {
        breakpoint[address] = enabled;
    }

    public void addSourceMapping(String file, int line, int address) {
        addressToLineMap.put(address, line);
        lineToAddressMap.put(line, address);
        addressToFileMap.put(address, file);
    }

    public void clearSourceMapping() {
        addressToFileMap.clear();
        addressToLineMap.clear();
        lineToAddressMap.clear();
    }
}
