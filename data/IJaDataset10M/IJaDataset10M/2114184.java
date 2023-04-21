package mips.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Assembler {

    public static final int OTHER_FORMAT = -1;

    public static final int R_FORMAT = 0;

    public static final int I_FORMAT = 1;

    public static final int J_FORMAT = 2;

    public static final String OTHER_OPCODE = "111111";

    public static Map<String, Integer> instrMap;

    public static Map<String, Integer> regMap;

    public static Map<String, String> functMap;

    static {
        instrMap = new HashMap<String, Integer>();
        instrMap.put("and", new Integer(R_FORMAT));
        instrMap.put("or", new Integer(R_FORMAT));
        instrMap.put("add", new Integer(R_FORMAT));
        instrMap.put("sub", new Integer(R_FORMAT));
        instrMap.put("slt", new Integer(R_FORMAT));
        instrMap.put("lw", new Integer(I_FORMAT));
        instrMap.put("sw", new Integer(I_FORMAT));
        instrMap.put("beq", new Integer(I_FORMAT));
        instrMap.put("j", new Integer(J_FORMAT));
        regMap = new TreeMap<String, Integer>();
        regMap.put("$zero", 0);
        regMap.put("$at", 1);
        regMap.put("$v0", 2);
        regMap.put("$v1", 3);
        regMap.put("$a0", 4);
        regMap.put("$a1", 5);
        regMap.put("$a2", 6);
        regMap.put("$a3", 7);
        regMap.put("$t0", 8);
        regMap.put("$t1", 9);
        regMap.put("$t2", 10);
        regMap.put("$t3", 11);
        regMap.put("$t4", 12);
        regMap.put("$t5", 13);
        regMap.put("$t6", 14);
        regMap.put("$t7", 15);
        regMap.put("$s0", 16);
        regMap.put("$s1", 17);
        regMap.put("$s2", 18);
        regMap.put("$s3", 19);
        regMap.put("$s4", 20);
        regMap.put("$s5", 21);
        regMap.put("$s6", 22);
        regMap.put("$s7", 23);
        regMap.put("$t8", 24);
        regMap.put("$t9", 25);
        regMap.put("$k0", 26);
        regMap.put("$k1", 27);
        regMap.put("$gp", 28);
        regMap.put("$sp", 29);
        regMap.put("$fp", 30);
        regMap.put("$ra", 31);
        functMap = new HashMap<String, String>();
        functMap.put("and", "100100");
        functMap.put("or", "100101");
        functMap.put("add", "100000");
        functMap.put("sub", "100010");
        functMap.put("slt", "101010");
    }

    public static SortedMap<Integer, String> parse(String asmFilePath) {
        SortedMap<Integer, Instruction> memoryMapInst = new TreeMap<Integer, Instruction>();
        SortedMap<Integer, String> memoryMapCode = new TreeMap<Integer, String>();
        BufferedReader bufferedreader = null;
        Instruction inst = null;
        HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();
        try {
            if (asmFilePath == null) {
                System.out.println("Error: Assembly file path is not provided.");
                return null;
            } else {
                ClassLoader cl = Assembler.class.getClassLoader();
                bufferedreader = new BufferedReader(new InputStreamReader(cl.getResourceAsStream(asmFilePath)));
            }
            String line = null;
            int address = 0;
            while ((line = bufferedreader.readLine()) != null) {
                line = line.trim();
                if (!line.equals("") && (line.charAt(0) != '#')) {
                    inst = parseInstruction_1(line);
                    if (inst != null) {
                        memoryMapInst.put(address, inst);
                        if (inst.getLabel() != null) {
                            symbolTable.put(inst.getLabel(), address);
                        }
                        address += 4;
                    }
                }
            }
            if (bufferedreader != null) bufferedreader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<Integer> keys = memoryMapInst.keySet();
        Iterator<Integer> it = keys.iterator();
        int address;
        while (it.hasNext()) {
            address = it.next();
            inst = memoryMapInst.get(address);
            parseInstruction_2(address, inst, symbolTable);
            memoryMapCode.put(address, inst.getMachineCode());
        }
        return memoryMapCode;
    }

    public static boolean supportedInstr(String s) {
        return instrMap.containsKey(s.toLowerCase());
    }

    public static int getRegNum(String regName) {
        int regNum = -1;
        if (regMap.containsKey(regName)) {
            regNum = regMap.get(regName);
        }
        return regNum;
    }

    private static Instruction parseInstruction_1(String s) {
        Instruction inst = new Instruction();
        int index1, index2;
        String str;
        index1 = s.indexOf(':');
        if (index1 < 0) {
            inst.setLabel(null);
            index1 = 0;
        } else {
            str = s.substring(0, index1);
            inst.setLabel(str.trim());
            index1++;
        }
        index2 = s.indexOf('#');
        if (index2 < 0) {
            inst.setComment(null);
            index2 = s.length();
        } else {
            str = s.substring(index2);
            inst.setComment(str.trim());
        }
        str = s.substring(index1, index2).trim();
        inst.setInstr(str);
        String[] params = new String[3];
        String[] results = str.split(",?[\\s\\(\\)]+");
        String token, name = null;
        int type = -1;
        for (int i = 0; i < results.length; i++) {
            token = results[i].toLowerCase();
            if (i == 0) {
                name = token;
                inst.setName(name);
                if (supportedInstr(token)) {
                    type = instrMap.get(token.toLowerCase());
                    inst.setType(type);
                } else {
                    inst.setType(OTHER_FORMAT);
                }
            } else {
                params[i - 1] = token;
            }
        }
        inst.setParams(params);
        return inst;
    }

    private static void parseInstruction_2(int addr, Instruction inst, Map<String, Integer> symbolTable) {
        String machineCode = null;
        String opcode = null, rs = null, rt = null, rd = null;
        String shamt = null, funct = null, immediate = null, address = null;
        int type = inst.getType();
        String params[] = inst.getParams();
        String name = inst.getName();
        switch(type) {
            case R_FORMAT:
                {
                    opcode = "000000";
                    rs = Signal.numToBinStr(BigInteger.valueOf(regMap.get(params[1].toLowerCase())), 5);
                    rt = Signal.numToBinStr(BigInteger.valueOf(regMap.get(params[2].toLowerCase())), 5);
                    rd = Signal.numToBinStr(BigInteger.valueOf(regMap.get(params[0].toLowerCase())), 5);
                    shamt = "00000";
                    funct = functMap.get(name);
                    machineCode = opcode + rs + rt + rd + shamt + funct;
                }
                break;
            case Assembler.I_FORMAT:
                {
                    if (name.equals("lw") || name.equals("sw")) {
                        if (name.equals("lw")) {
                            opcode = "100011";
                        } else {
                            opcode = "101011";
                        }
                        rs = Signal.numToBinStr(BigInteger.valueOf(regMap.get(params[2].toLowerCase())), 5);
                        rt = Signal.numToBinStr(BigInteger.valueOf(regMap.get(params[0].toLowerCase())), 5);
                        immediate = Signal.numToBinStr(new BigInteger(params[1]), 16);
                        machineCode = opcode + rs + rt + immediate;
                    } else if (name.equals("beq")) {
                        opcode = "000100";
                        rs = Signal.numToBinStr(BigInteger.valueOf(regMap.get(params[0].toLowerCase())), 5);
                        rt = Signal.numToBinStr(BigInteger.valueOf(regMap.get(params[1].toLowerCase())), 5);
                        int target = symbolTable.get(params[2]);
                        int pc = addr;
                        int offset = (target - (pc + 4)) / 4;
                        immediate = Signal.numToBinStr(BigInteger.valueOf(offset), 16);
                        machineCode = opcode + rs + rt + immediate;
                    }
                    break;
                }
            case Assembler.J_FORMAT:
                opcode = "000010";
                int target = symbolTable.get(params[0]) / 4;
                address = Signal.numToBinStr(BigInteger.valueOf(target), 26);
                machineCode = opcode + address;
                break;
            default:
                opcode = OTHER_OPCODE;
                machineCode = opcode + "11111111111111111111111111";
                System.out.println("Wrong instruction type");
        }
        inst.setMachineCode(machineCode);
    }

    public static Map<Integer, String> getRegValMap(Properties props) {
        Map<Integer, String> regInitValueMap = new HashMap<Integer, String>();
        Set keys = props.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            int regNum = Assembler.getRegNum(key);
            if (regNum >= 0) {
                String value = (String) props.get(key);
                regInitValueMap.put(regNum, value);
            }
        }
        return regInitValueMap;
    }

    public static void main(String[] args) {
        String[] params = new String[3];
        String str = "add $s3, $s3, $s2";
        String[] results = str.split(",?[\\s\\(\\)]+");
        String token, name = null;
        int type = -1;
        for (int i = 0; i < results.length; i++) {
            token = results[i].toLowerCase();
            if (i == 0) {
                name = token;
                if (supportedInstr(token)) {
                    type = instrMap.get(token.toLowerCase());
                } else {
                    System.out.println("Error!");
                }
            } else {
                params[i - 1] = token;
            }
        }
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        for (int i = 0; i < 3; i++) {
            System.out.println("Param" + i + ": " + params[i]);
        }
    }
}
