package com.hifiremote.jp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import com.hifiremote.jp1.AssemblerOpCode.AddressMode;
import com.hifiremote.jp1.AssemblerOpCode.OpArg;
import com.hifiremote.jp1.AssemblerOpCode.Token;
import com.hifiremote.jp1.AssemblerOpCode.TokenType;

/**
 * The Class Processor.
 */
public abstract class Processor {

    /**
   * Instantiates a new processor.
   * 
   * @param name
   *          the name
   */
    public Processor(String name) {
        this(name, null, false);
    }

    /**
   * Instantiates a new processor.
   * 
   * @param name
   *          the name
   * @param reverse
   *          the reverse
   */
    public Processor(String name, boolean reverse) {
        this(name, null, reverse);
    }

    /**
   * Instantiates a new processor.
   * 
   * @param name
   *          the name
   * @param version
   *          the version
   */
    public Processor(String name, String version) {
        this(name, version, false);
    }

    /**
   * Instantiates a new processor.
   * 
   * @param name
   *          the name
   * @param version
   *          the version
   * @param reverse
   *          the reverse
   */
    public Processor(String name, String version, boolean reverse) {
        this.name = name;
        this.version = version;
    }

    /**
   * Sets the vector edit data.
   * 
   * @param opcodes
   *          the opcodes
   * @param addresses
   *          the addresses
   */
    public void setVectorEditData(int[] opcodes, int[] addresses) {
        this.opCodes = opcodes;
        this.addresses = addresses;
    }

    /**
   * Sets the data edit data.
   * 
   * @param min
   *          the min
   * @param max
   *          the max
   */
    public void setDataEditData(int min, int max) {
        minDataAddress = min;
        maxDataAddress = max;
    }

    /**
   * Gets the name.
   * 
   * @return the name
   */
    public String getName() {
        return name;
    }

    /**
   * Gets the version.
   * 
   * @return the version
   */
    public String getVersion() {
        return version;
    }

    /**
   * Gets the full name.
   * 
   * @return the full name
   */
    public String getFullName() {
        if (version == null) {
            return name;
        } else {
            return name + '-' + version;
        }
    }

    /**
   * Gets the equivalent name.
   * 
   * @return the equivalent name
   */
    public String getEquivalentName() {
        return getFullName();
    }

    /**
   * Translate.
   * 
   * @param hex
   *          the hex
   * @param remote
   *          the remote
   * @return the hex
   */
    public Hex translate(Hex hex, Remote remote) {
        int vectorOffset = remote.getProtocolVectorOffset();
        int dataOffset = remote.getProtocolDataOffset();
        if (vectorOffset != 0 || dataOffset != 0) {
            try {
                hex = (Hex) hex.clone();
            } catch (CloneNotSupportedException ex) {
                ex.printStackTrace(System.err);
            }
        }
        if (vectorOffset != 0) {
            doVectorEdit(hex, vectorOffset);
        }
        if (dataOffset != 0) {
            doDataEdit(hex, dataOffset);
        }
        return hex;
    }

    /**
   * Import code.
   * 
   * @param code
   *          the code
   * @param processorName
   *          the processor name
   * @return the hex
   */
    public Hex importCode(Hex code, String processorName) {
        return code;
    }

    /**
   * Gets the int.
   * 
   * @param data
   *          the data
   * @param offset
   *          the offset
   * @return the int
   */
    public abstract int getInt(short[] data, int offset);

    /**
   * Put int.
   * 
   * @param val
   *          the val
   * @param data
   *          the data
   * @param offset
   *          the offset
   */
    public abstract void putInt(int val, short[] data, int offset);

    /**
   * Do vector edit.
   * 
   * @param hex
   *          the hex
   * @param vectorOffset
   *          the vector offset
   */
    private void doVectorEdit(Hex hex, int vectorOffset) {
        short[] data = hex.getData();
        for (int i = 0; i < data.length; i++) {
            short opCode = data[i];
            for (int j = 0; j < opCodes.length; j++) {
                if (opCode == opCodes[j]) {
                    int address = getInt(data, i + 1);
                    for (int k = 0; k < addresses.length; k++) {
                        if (addresses[k] == address) {
                            address += vectorOffset;
                            putInt(address, data, i + 1);
                            break;
                        }
                    }
                    i += 2;
                    break;
                }
            }
        }
    }

    /**
   * Do data edit.
   * 
   * @param hex
   *          the hex
   * @param dataOffset
   *          the data offset
   */
    private void doDataEdit(Hex hex, int dataOffset) {
        short[] data = hex.getData();
        for (int i = 0; i < data.length - 1; i++) {
            if ((data[i] & Hex.ADD_OFFSET) != 0 && (data[i + 1] & Hex.ADD_OFFSET) != 0) {
                int temp = getInt(data, i);
                if (temp < minDataAddress || temp > maxDataAddress) {
                    continue;
                }
                temp += dataOffset;
                putInt(temp, data, i);
                i++;
            }
        }
        for (int i = 0; i < data.length; i++) {
            int temp = data[i];
            if ((temp & Hex.ADD_OFFSET) != 0) {
                temp &= 0xFF;
                temp += dataOffset;
                data[i] = (short) (temp & 0xFF);
            }
        }
    }

    @Override
    public String toString() {
        return getFullName();
    }

    public int getRAMAddress() {
        return RAMAddress;
    }

    public void setRAMAddress(int address) {
        RAMAddress = address;
    }

    public List<AssemblerOpCode[]> getInstructions() {
        return instructions;
    }

    public LinkedHashMap<String, AddressMode> getAddressModes() {
        return addressModes;
    }

    public void setAddressModes(String[][] modeArray) {
        addressModes.clear();
        modesByOutline.clear();
        for (int i = 0; i < modeArray.length; i++) {
            AddressMode mode = new AddressMode(modeArray[i]);
            mode.outline = simplifyOutline(mode.outline);
            addressModes.put(modeArray[i][0], mode);
            if (modesByOutline.containsKey(mode.outline)) {
                modesByOutline.get(mode.outline).add(mode.name);
            } else {
                List<String> list = new ArrayList<String>();
                list.add(mode.name);
                modesByOutline.put(mode.outline, list);
            }
        }
    }

    public LinkedHashMap<Integer, String> getAbsLabels() {
        return absLabels;
    }

    public LinkedHashMap<String, Integer> getAbsAddresses() {
        return absAddresses;
    }

    public void setAbsLabels(String[][] labelArray) {
        absLabels.clear();
        for (int i = 0; i < labelArray.length; i++) {
            absLabels.put(Integer.parseInt(labelArray[i][1], 16), labelArray[i][0]);
            absAddresses.put(labelArray[i][0], Integer.parseInt(labelArray[i][1], 16));
        }
    }

    public LinkedHashMap<Integer, String[]> getZeroLabels() {
        return zeroLabels;
    }

    public LinkedHashMap<String, Integer> getZeroSizes() {
        return zeroSizes;
    }

    public LinkedHashMap<String, Integer> getZeroAddresses() {
        return zeroAddresses;
    }

    public void setZeroLabels(String[][] labelArray) {
        zeroLabels.clear();
        for (int i = 0; i < labelArray.length; i++) {
            int n = 0;
            String[] strArray = null;
            if (labelArray[i].length > 3) {
                strArray = new String[2];
                strArray[1] = labelArray[i][2];
                n = Integer.parseInt(labelArray[i][3], 16);
                zeroSizes.put(labelArray[i][0], n);
            } else {
                strArray = new String[1];
            }
            n = Integer.parseInt(labelArray[i][1], 16);
            strArray[0] = labelArray[i][0];
            zeroLabels.put(n, strArray);
            zeroAddresses.put(labelArray[i][0], n);
        }
    }

    public LinkedHashMap<String, String> getAsmLabels() {
        LinkedHashMap<String, String> labels = new LinkedHashMap<String, String>();
        String formatAddr = null;
        if (getAddressModes().get("EQUR") == null) {
            formatAddr = getAddressModes().get("EQU2").format;
        } else {
            formatAddr = getAddressModes().get("EQUR").format;
        }
        for (String text : getZeroSizes().keySet()) {
            int addr = getZeroAddresses().get(text);
            int size = getZeroSizes().get(text);
            String labelBody = getZeroLabels().get(addr)[1];
            if (labelBody.length() >= text.length()) {
                labels.put(text.toUpperCase(), String.format(formatAddr, addr));
            } else for (int i = 0; i < size; i++) {
                String formatLbl = labelBody + "%0" + (text.length() - labelBody.length()) + "X";
                labels.put(String.format(formatLbl, i).toUpperCase(), String.format(formatAddr, addr + i));
            }
        }
        ;
        for (String text : getZeroAddresses().keySet()) {
            int addr = getZeroAddresses().get(text);
            if (getZeroSizes().keySet().contains(text)) continue;
            labels.put(text.toUpperCase(), String.format(formatAddr, addr));
        }
        formatAddr = getAddressModes().get("EQU4").format;
        for (String text : getAbsAddresses().keySet()) {
            int addr = getAbsAddresses().get(text);
            labels.put(text.toUpperCase(), String.format(formatAddr, addr));
        }
        return labels;
    }

    public void setInstructions(String[][][] instArray) {
        instructions.clear();
        HashMap<Integer, Integer> firstBytes = new HashMap<Integer, Integer>();
        for (int i = 0; i < instArray.length; i++) {
            AssemblerOpCode[] opCodes = new AssemblerOpCode[instArray[i].length];
            for (int j = 0; j < instArray[i].length; j++) {
                Hex hex = new Hex(new short[] { (short) j });
                AssemblerOpCode op = new AssemblerOpCode(this, instArray[i][j]);
                if (op.getName().equals("*") && op.getIndex() > 0) {
                    firstBytes.put(op.getIndex(), j);
                } else if (i > 0 && firstBytes.get(i) != null) {
                    hex = new Hex(2);
                    hex.set(firstBytes.get(i).shortValue(), 0);
                    hex.set((short) j, 1);
                }
                op.setHex(hex);
                op.setLength(hex.length());
                opCodes[j] = op;
            }
            instructions.add(opCodes);
        }
        for (int i = 0; i < instructions.size(); i++) {
            for (int j = 0; j < instructions.get(i).length; j++) {
                addToMap(instructions.get(i)[j]);
            }
        }
    }

    public void addToMap(AssemblerOpCode op) {
        String name = op.getName();
        if (op.getMode() == null) return;
        String modeName = op.getMode().name;
        LinkedHashMap<String, AssemblerOpCode> map = null;
        if (opMap.containsKey(name)) {
            map = opMap.get(name);
        } else {
            map = new LinkedHashMap<String, AssemblerOpCode>();
            opMap.put(name, map);
        }
        if (!map.containsKey(modeName)) {
            map.put(modeName, op);
        }
    }

    public void disasmModify(AddressMode mode, Object[] obj) {
        switch(mode.modifier) {
            case 1:
                obj[0] = (Integer) obj[0] >> 1;
                break;
            case 2:
                obj[0] = (Integer) obj[0] + 0xFF00;
                break;
        }
    }

    public void asmModify(int modifier, int[] obj) {
        switch(modifier) {
            case 1:
                obj[0] = obj[0] << 1;
                break;
            case 2:
                obj[0] = obj[0] & 0xFF;
        }
    }

    public boolean checkModifier(AddressMode mode, OpArg args) {
        for (int i = 0; i < args.size(); i++) {
            Token t = args.get(i);
            if (t.type == TokenType.NUMBER) {
                int val = t.value;
                if (val < 0 || val > mode.argLimits[i]) return false;
            }
        }
        switch(mode.modifier) {
            case 2:
                Integer val = args.get(0).value;
                return val != null && val >= 0xFF00;
            default:
                return true;
        }
    }

    public AssemblerOpCode getOpCode(Hex hex) {
        if (hex == null || hex.length() == 0) return null;
        AssemblerOpCode opCode = instructions.get(0)[hex.getData()[0]].clone();
        if (opCode.getIndex() > 0 && hex.length() > 1) {
            opCode = instructions.get(opCode.getIndex())[hex.getData()[1]].clone();
        }
        return opCode;
    }

    public OpArg getArgs(String argText, LinkedHashMap<String, String> labels) {
        return OpArg.getArgs(argText, this, labels);
    }

    public List<String> getAddressModes(OpArg args) {
        List<String> modes = new ArrayList<String>();
        String simpleOutline = simplifyOutline(args.outline);
        if (modesByOutline.get(simpleOutline) == null) return modes;
        for (String mode : modesByOutline.get(simpleOutline)) modes.add(mode);
        Iterator<String> it = modes.iterator();
        while (it.hasNext()) {
            AddressMode mode = addressModes.get(it.next());
            if (!checkModifier(mode, args)) {
                it.remove();
                continue;
            }
            for (int i = 0; i < args.size(); i++) {
                Token t = args.get(i);
                if (t.type == TokenType.OFFSET) {
                    int n = mode.argMap[i] - mode.nibbleArgs - 1;
                    if (n < 0 || ((mode.relMap >> n) & 1) == 0) {
                        it.remove();
                        break;
                    }
                } else if (t.type == TokenType.CONDITION_CODE) {
                    int n = mode.argMap[i] - 1;
                    if (((mode.ccMap >> n) & 1) == 0) {
                        it.remove();
                        break;
                    }
                } else if (t.type == TokenType.ERROR) {
                    it.remove();
                    break;
                }
            }
        }
        return modes;
    }

    public List<String> getHexPrefixes() {
        return new ArrayList<String>();
    }

    public String simplifyOutline(String outline) {
        return outline;
    }

    public String getConditionCode(int n) {
        return null;
    }

    public int getConditionIndex(String cc) {
        return -1;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getOscillatorFreq() {
        return oscillatorFreq;
    }

    public void setOscillatorData(int[] oscData) {
        oscillatorFreq = oscData[0];
        carrierTotalOffset = oscData[1];
        carrierOnOffset = oscData[2];
    }

    public int getDataStyle() {
        return dataStyle;
    }

    public void setDataStyle(int dataStyle) {
        this.dataStyle = dataStyle;
    }

    public int getCarrierTotalOffset() {
        return carrierTotalOffset;
    }

    public int getCarrierOnOffset() {
        return carrierOnOffset;
    }

    public String getRegisterPrefix() {
        return "$";
    }

    public LinkedHashMap<String, LinkedHashMap<String, AssemblerOpCode>> getOpMap() {
        return opMap;
    }

    /** The name. */
    private String name = null;

    /** The version. */
    private String version = null;

    /** The op codes. */
    private int[] opCodes = new int[0];

    /** The addresses. */
    private int[] addresses = new int[0];

    /** The min data address. */
    private int minDataAddress = 0x64;

    /** The max data address. */
    private int maxDataAddress = 0x80;

    /** The default RAM address. */
    private int RAMAddress = 0x0100;

    /** Offset of first op code in protocol */
    private int startOffset = 3;

    private int oscillatorFreq = 2000000;

    private int carrierTotalOffset = 0;

    private int carrierOnOffset = 0;

    /** Index to style of PF and PD data */
    private int dataStyle = 0;

    private List<AssemblerOpCode[]> instructions = new ArrayList<AssemblerOpCode[]>();

    protected LinkedHashMap<String, LinkedHashMap<String, AssemblerOpCode>> opMap = new LinkedHashMap<String, LinkedHashMap<String, AssemblerOpCode>>();

    private LinkedHashMap<String, AddressMode> addressModes = new LinkedHashMap<String, AddressMode>();

    private LinkedHashMap<String, List<String>> modesByOutline = new LinkedHashMap<String, List<String>>();

    private LinkedHashMap<Integer, String> absLabels = new LinkedHashMap<Integer, String>();

    private LinkedHashMap<String, Integer> absAddresses = new LinkedHashMap<String, Integer>();

    private LinkedHashMap<Integer, String[]> zeroLabels = new LinkedHashMap<Integer, String[]>();

    private LinkedHashMap<String, Integer> zeroAddresses = new LinkedHashMap<String, Integer>();

    private LinkedHashMap<String, Integer> zeroSizes = new LinkedHashMap<String, Integer>();
}
