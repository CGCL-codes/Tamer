package com.sun.java.util.jar.pack;

import java.io.*;
import java.util.*;
import com.sun.java.util.jar.pack.Package.Class;
import java.lang.reflect.Modifier;

/**
 * Represents a chunk of bytecodes.
 * @author John Rose
 */
class Code extends Attribute.Holder implements Constants {

    Class.Method m;

    public Code(Class.Method m) {
        this.m = m;
    }

    public Class.Method getMethod() {
        return m;
    }

    public Class thisClass() {
        return m.thisClass();
    }

    public Package getPackage() {
        return m.thisClass().getPackage();
    }

    public ConstantPool.Entry[] getCPMap() {
        return m.getCPMap();
    }

    private static final ConstantPool.Entry[] noRefs = ConstantPool.noRefs;

    int max_stack;

    int max_locals;

    ConstantPool.Entry handler_class[] = noRefs;

    int handler_start[] = noInts;

    int handler_end[] = noInts;

    int handler_catch[] = noInts;

    byte[] bytes;

    Fixups fixups;

    Object insnMap;

    int getLength() {
        return bytes.length;
    }

    int getMaxStack() {
        return max_stack;
    }

    void setMaxStack(int ms) {
        max_stack = ms;
    }

    int getMaxNALocals() {
        int argsize = m.getArgumentSize();
        return max_locals - argsize;
    }

    void setMaxNALocals(int ml) {
        int argsize = m.getArgumentSize();
        max_locals = argsize + ml;
    }

    int getHandlerCount() {
        assert (handler_class.length == handler_start.length);
        assert (handler_class.length == handler_end.length);
        assert (handler_class.length == handler_catch.length);
        return handler_class.length;
    }

    void setHandlerCount(int h) {
        if (h > 0) {
            handler_class = new ConstantPool.Entry[h];
            handler_start = new int[h];
            handler_end = new int[h];
            handler_catch = new int[h];
        }
    }

    void setBytes(byte[] bytes) {
        this.bytes = bytes;
        if (fixups != null) fixups.setBytes(bytes);
    }

    void setInstructionMap(int[] insnMap, int mapLen) {
        this.insnMap = allocateInstructionMap(insnMap, mapLen);
    }

    void setInstructionMap(int[] insnMap) {
        setInstructionMap(insnMap, insnMap.length);
    }

    int[] getInstructionMap() {
        return expandInstructionMap(getInsnMap());
    }

    void addFixups(Collection moreFixups) {
        if (fixups == null) {
            fixups = new Fixups(bytes);
        }
        assert (fixups.getBytes() == bytes);
        fixups.addAll(moreFixups);
    }

    public void trimToSize() {
        if (fixups != null) {
            fixups.trimToSize();
            if (fixups.size() == 0) fixups = null;
        }
        super.trimToSize();
    }

    protected void visitRefs(int mode, Collection refs) {
        int verbose = getPackage().verbose;
        if (verbose > 2) System.out.println("Reference scan " + this);
        Class cls = thisClass();
        Package pkg = cls.getPackage();
        for (int i = 0; i < handler_class.length; i++) {
            refs.add(handler_class[i]);
        }
        if (fixups != null) {
            fixups.visitRefs(refs);
        } else {
            ConstantPool.Entry[] cpMap = getCPMap();
            for (Instruction i = instructionAt(0); i != null; i = i.next()) {
                if (verbose > 4) System.out.println(i);
                int cpref = i.getCPIndex();
                if (cpref >= 0) {
                    refs.add(cpMap[cpref]);
                }
            }
        }
        super.visitRefs(mode, refs);
    }

    static final boolean shrinkMaps = true;

    private Object allocateInstructionMap(int[] insnMap, int mapLen) {
        int PClimit = getLength();
        if (shrinkMaps && PClimit <= Byte.MAX_VALUE - Byte.MIN_VALUE) {
            byte[] map = new byte[mapLen + 1];
            for (int i = 0; i < mapLen; i++) {
                map[i] = (byte) (insnMap[i] + Byte.MIN_VALUE);
            }
            map[mapLen] = (byte) (PClimit + Byte.MIN_VALUE);
            return map;
        } else if (shrinkMaps && PClimit < Short.MAX_VALUE - Short.MIN_VALUE) {
            short[] map = new short[mapLen + 1];
            for (int i = 0; i < mapLen; i++) {
                map[i] = (short) (insnMap[i] + Short.MIN_VALUE);
            }
            map[mapLen] = (short) (PClimit + Short.MIN_VALUE);
            return map;
        } else {
            int[] map = new int[mapLen + 1];
            for (int i = 0; i < mapLen; i++) {
                map[i] = (int) insnMap[i];
            }
            map[mapLen] = (int) PClimit;
            return map;
        }
    }

    private int[] expandInstructionMap(Object map0) {
        int[] imap;
        if (map0 instanceof byte[]) {
            byte[] map = (byte[]) map0;
            imap = new int[map.length - 1];
            for (int i = 0; i < imap.length; i++) {
                imap[i] = map[i] - Byte.MIN_VALUE;
            }
        } else if (map0 instanceof short[]) {
            short[] map = (short[]) map0;
            imap = new int[map.length - 1];
            for (int i = 0; i < imap.length; i++) {
                imap[i] = map[i] - Byte.MIN_VALUE;
            }
        } else {
            int[] map = (int[]) map0;
            imap = new int[map.length - 1];
            for (int i = 0; i < imap.length; i++) {
                imap[i] = map[i];
            }
        }
        return imap;
    }

    Object getInsnMap() {
        if (insnMap != null) {
            return insnMap;
        }
        int[] map = new int[getLength()];
        int fillp = 0;
        for (Instruction i = instructionAt(0); i != null; i = i.next()) {
            map[fillp++] = i.getPC();
        }
        insnMap = allocateInstructionMap(map, fillp);
        return insnMap;
    }

    /** Encode the given BCI as an instruction boundary number.
     *  For completeness, irregular (non-boundary) BCIs are
     *  encoded compactly immediately after the boundary numbers.
     *  This encoding is the identity mapping outside 0..length,
     *  and it is 1-1 everywhere.  All by itself this technique
     *  improved zipped rt.jar compression by 2.6%.
     */
    public int encodeBCI(int bci) {
        if (bci <= 0 || bci > getLength()) return bci;
        Object map0 = getInsnMap();
        int i, len;
        if (shrinkMaps && map0 instanceof byte[]) {
            byte[] map = (byte[]) map0;
            len = map.length;
            i = Arrays.binarySearch(map, (byte) (bci + Byte.MIN_VALUE));
        } else if (shrinkMaps && map0 instanceof short[]) {
            short[] map = (short[]) map0;
            len = map.length;
            i = Arrays.binarySearch(map, (short) (bci + Short.MIN_VALUE));
        } else {
            int[] map = (int[]) map0;
            len = map.length;
            i = Arrays.binarySearch(map, (int) bci);
        }
        assert (i != -1);
        assert (i != 0);
        assert (i != len);
        assert (i != -len - 1);
        return (i >= 0) ? i : len + bci - (-i - 1);
    }

    public int decodeBCI(int bciCode) {
        if (bciCode <= 0 || bciCode > getLength()) return bciCode;
        Object map0 = getInsnMap();
        int i, len;
        if (shrinkMaps && map0 instanceof byte[]) {
            byte[] map = (byte[]) map0;
            len = map.length;
            if (bciCode < len) return map[bciCode] - Byte.MIN_VALUE;
            i = Arrays.binarySearch(map, (byte) (bciCode + Byte.MIN_VALUE));
            if (i < 0) i = -i - 1;
            int key = bciCode - len + Byte.MIN_VALUE;
            for (; ; i--) {
                if (map[i - 1] - (i - 1) <= key) break;
            }
        } else if (shrinkMaps && map0 instanceof short[]) {
            short[] map = (short[]) map0;
            len = map.length;
            if (bciCode < len) return map[bciCode] - Short.MIN_VALUE;
            i = Arrays.binarySearch(map, (short) (bciCode + Short.MIN_VALUE));
            if (i < 0) i = -i - 1;
            int key = bciCode - len + Short.MIN_VALUE;
            for (; ; i--) {
                if (map[i - 1] - (i - 1) <= key) break;
            }
        } else {
            int[] map = (int[]) map0;
            len = map.length;
            if (bciCode < len) return map[bciCode];
            i = Arrays.binarySearch(map, (int) bciCode);
            if (i < 0) i = -i - 1;
            int key = bciCode - len;
            for (; ; i--) {
                if (map[i - 1] - (i - 1) <= key) break;
            }
        }
        return bciCode - len + i;
    }

    public void finishRefs(ConstantPool.Index ix) {
        if (fixups != null) {
            fixups.finishRefs(ix);
            fixups = null;
        }
    }

    Instruction instructionAt(int pc) {
        return Instruction.at(bytes, pc);
    }

    static boolean flagsRequireCode(int flags) {
        return (flags & (Modifier.NATIVE | Modifier.ABSTRACT)) == 0;
    }

    public String toString() {
        return m + ".Code";
    }

    public int getInt(int pc) {
        return Instruction.getInt(bytes, pc);
    }

    public int getShort(int pc) {
        return Instruction.getShort(bytes, pc);
    }

    public int getByte(int pc) {
        return Instruction.getByte(bytes, pc);
    }

    void setInt(int pc, int x) {
        Instruction.setInt(bytes, pc, x);
    }

    void setShort(int pc, int x) {
        Instruction.setShort(bytes, pc, x);
    }

    void setByte(int pc, int x) {
        Instruction.setByte(bytes, pc, x);
    }
}
