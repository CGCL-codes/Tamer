package com.ibm.JikesRVM.OSR;

import com.ibm.JikesRVM.*;
import com.ibm.JikesRVM.classloader.*;
import com.ibm.JikesRVM.opt.*;
import com.ibm.JikesRVM.opt.ir.*;
import java.util.*;

/** 
 * OSR_EncodedOSRMap provides the samilar function as GC map
 * in VM_OptMachineCodeMap.
 * 
 * In VM_OptCompiledMethod, an instance of this class will represent
 * all OSR map info for that method.
 *
 * @author: Feng Qian
 */
public class OSR_EncodedOSRMap implements VM_OptGCMapIteratorConstants, OSR_Constants {

    private long[] mapEntries;

    private int lastEntry;

    private int[] osrMaps;

    private int mapSize = 16;

    private int lastIndex = 0;

    private int[] ieMaps;

    public static final boolean registerIsSet(int map, int regnum) throws VM_PragmaInline {
        int bitpos = getRegBitPosition(regnum);
        return (map & (NEXT_BIT >>> bitpos)) > 0;
    }

    private static int setRegister(int map, int regnum) {
        int bitpos = getRegBitPosition(regnum);
        map |= (NEXT_BIT >>> bitpos);
        return map;
    }

    private static final int getRegBitPosition(int regnum) throws VM_PragmaInline {
        return regnum - FIRST_GCMAP_REG + 1;
    }

    public OSR_EncodedOSRMap(OSR_VariableMap varMap) {
        int entries = varMap.getNumberOfElements();
        this.lastEntry = entries - 1;
        if (entries > 0) {
            this.mapEntries = new long[entries];
            this.osrMaps = new int[mapSize];
            translateMap(varMap.list);
            resizeOsrMaps();
        }
    }

    private void translateMap(LinkedList osrlist) {
        int n = osrlist.size();
        OSR_VariableMapElement[] osrarray = new OSR_VariableMapElement[n];
        for (int i = 0; i < n; i++) {
            osrarray[i] = (OSR_VariableMapElement) osrlist.get(i);
        }
        quickSort(osrarray, 0, n - 1);
        OPT_CallSiteTree inliningTree = new OPT_CallSiteTree();
        for (int i = 0; i < n; i++) {
            OPT_Instruction instr = osrarray[i].osr;
            if (instr.position != null) {
                inliningTree.addLocation(instr.position);
            }
        }
        ieMaps = VM_OptEncodedCallSiteTree.getEncoding(inliningTree);
        for (int i = 0; i < n; i++) {
            OSR_VariableMapElement elm = osrarray[i];
            OPT_Instruction instr = elm.osr;
            int iei = inliningTree.find(instr.position).encodedOffset;
            setIEIndex(i, iei);
            LinkedList mVarList = elm.mvars;
            int osrMapIndex = generateOsrMaps(mVarList);
            int mcOffset = instr.getmcOffset();
            setMCOffset(i, mcOffset);
            setOSRMapIndex(i, osrMapIndex);
            setBCIndex(i, instr.getBytecodeIndex());
        }
    }

    private static final void quickSort(OSR_VariableMapElement[] array, int start, int end) {
        if (start < end) {
            int pivot = partition(array, start, end);
            quickSort(array, start, pivot);
            quickSort(array, pivot + 1, end);
        }
    }

    private static final int partition(OSR_VariableMapElement[] array, int start, int end) {
        int left = start;
        int right = end;
        int pivot = start;
        OSR_VariableMapElement pivot_elm = array[pivot];
        int pivot_offset = pivot_elm.osr.getmcOffset();
        while (true) {
            while (array[right].osr.getmcOffset() > pivot_offset) right--;
            while (array[left].osr.getmcOffset() < pivot_offset) left++;
            if (left < right) {
                OSR_VariableMapElement temp = array[left];
                array[left] = array[right];
                array[right] = temp;
            } else {
                return right;
            }
        }
    }

    private int generateOsrMaps(LinkedList mVarList) {
        int regmap = (mVarList.size() > 0) ? NEXT_BIT : 0;
        int mapIndex = addIntToOsrMap(regmap);
        for (int i = 0, m = mVarList.size(); i < m; i++) {
            OSR_MethodVariables mVar = (OSR_MethodVariables) mVarList.get(i);
            _generateMapForOneMethodVariable(mapIndex, mVar, (i == (m - 1)));
        }
        return mapIndex;
    }

    private void _generateMapForOneMethodVariable(int regMapIndex, OSR_MethodVariables mVar, boolean lastMid) {
        int mid = lastMid ? mVar.methId : (mVar.methId | NEXT_BIT);
        addIntToOsrMap(mid);
        LinkedList tupleList = mVar.tupleList;
        int m = tupleList.size();
        int bci = (m == 0) ? mVar.bcIndex : (mVar.bcIndex | NEXT_BIT);
        addIntToOsrMap(bci);
        for (int j = 0; j < m; j++) {
            OSR_LocalRegPair tuple = (OSR_LocalRegPair) tupleList.get(j);
            boolean isLast = (j == m - 1);
            if (tuple.typeCode == LongTypeCode) {
                processLongTuple(tuple, isLast);
            } else {
                processTuple(tuple, isLast, false);
            }
            if (((tuple.typeCode == ClassTypeCode) || (tuple.typeCode == ArrayTypeCode)) && (tuple.valueType == PHYREG)) {
                osrMaps[regMapIndex] = setRegister(osrMaps[regMapIndex], tuple.value);
            }
        }
    }

    private void processLongTuple(OSR_LocalRegPair tuple, boolean isLast) {
        processTuple(tuple, false, true);
        processTuple(tuple._otherHalf, isLast, false);
    }

    private void processTuple(OSR_LocalRegPair tuple, boolean isLast, boolean isFirstLong) {
        int first = (tuple.num << NUM_SHIFT) & NUM_MASK;
        if (!isLast) {
            first |= NEXT_BIT;
        }
        first |= (tuple.kind << KIND_SHIFT);
        first |= (tuple.valueType << VTYPE_SHIFT);
        switch(tuple.typeCode) {
            case BooleanTypeCode:
            case ByteTypeCode:
            case CharTypeCode:
            case ShortTypeCode:
            case IntTypeCode:
                first |= (INT << TCODE_SHIFT);
                break;
            case FloatTypeCode:
                first |= (FLOAT << TCODE_SHIFT);
                break;
            case DoubleTypeCode:
                first |= (DOUBLE << TCODE_SHIFT);
                break;
            case LongTypeCode:
                if (isFirstLong) {
                    first |= (LONG1 << TCODE_SHIFT);
                } else {
                    first |= (LONG2 << TCODE_SHIFT);
                }
                break;
            case AddressTypeCode:
                VM.sysWrite("address type for ");
                if (tuple.kind == LOCAL) {
                    VM.sysWrite("L" + tuple.num);
                } else {
                    VM.sysWrite("S" + tuple.num);
                }
                VM.sysWrite("\n");
                first |= (ADDR << TCODE_SHIFT);
                break;
            case ClassTypeCode:
            case ArrayTypeCode:
                first |= (REF << TCODE_SHIFT);
                break;
        }
        addIntToOsrMap(first);
        addIntToOsrMap(tuple.value);
    }

    private int addIntToOsrMap(int value) {
        if (lastIndex >= mapSize) {
            int oldSize = mapSize;
            mapSize <<= 1;
            int[] oldMaps = osrMaps;
            osrMaps = new int[mapSize];
            System.arraycopy(oldMaps, 0, osrMaps, 0, oldSize);
        }
        osrMaps[lastIndex++] = value;
        return lastIndex - 1;
    }

    private void resizeOsrMaps() {
        if (VM.VerifyAssertions) VM._assert(mapSize == osrMaps.length);
        if (lastIndex < mapSize - 1) {
            int[] newMaps = new int[lastIndex];
            System.arraycopy(osrMaps, 0, newMaps, 0, lastIndex);
            osrMaps = newMaps;
            mapSize = lastIndex;
        }
    }

    public final boolean hasOSRMap(VM_Offset mcOffset) {
        int entry = findOSREntry(mcOffset);
        return (entry != NO_OSR_ENTRY);
    }

    public final int getBytecodeIndexForMCOffset(VM_Offset mcOffset) {
        int entry = findOSREntry(mcOffset);
        return getBCIndex(entry);
    }

    public final int getInlineEncodingForMCOffset(VM_Offset mcOffset) {
        return -1;
    }

    public final int getRegisterMapForMCOffset(VM_Offset mcOffset) {
        int entry = findOSREntry(mcOffset);
        int mapIndex = getOSRMapIndex(entry);
        return osrMaps[mapIndex];
    }

    /**
   * given a MC offset, return an iterator over the
   * elements of this map.
   * NOTE: the map index is gotten from 'findOSRMapIndex'.
   * This has to be changed....
   */
    public final OSR_MapIterator getOsrMapIteratorForMCOffset(VM_Offset mcOffset) {
        int entry = findOSREntry(mcOffset);
        int mapIndex = getOSRMapIndex(entry);
        return new OSR_MapIterator(osrMaps, mapIndex);
    }

    private final int findOSREntry(VM_Offset mcOffset) {
        int l = 0;
        int r = lastEntry;
        while (l <= r) {
            int m = (l + r) >> 1;
            int offset = getMCOffset(m);
            if (offset == mcOffset.toInt()) {
                return m;
            } else if (offset < mcOffset.toInt()) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        if (VM.TraceOnStackReplacement) {
            VM.sysWrite("cannot find map entry for " + mcOffset.toInt() + "\n");
            this.printMap();
        }
        if (VM.VerifyAssertions) VM._assert(VM.NOT_REACHED);
        return NO_OSR_ENTRY;
    }

    private final int getMCOffset(int entry) {
        return (int) ((mapEntries[entry] & OFFSET_MASK) >>> OFFSET_SHIFT);
    }

    private final int getOSRMapIndex(int entry) {
        return (int) ((mapEntries[entry] & OSRI_MASK) >>> OSRI_SHIFT);
    }

    private final int getBCIndex(int entry) {
        return (int) ((mapEntries[entry] & BCI_MASK) >>> BCI_SHIFT);
    }

    private final int getIEIndex(int entry) {
        return (int) ((mapEntries[entry] & IEI_MASK) >>> IEI_SHIFT);
    }

    private final void setMCOffset(int entry, int offset) {
        mapEntries[entry] = (mapEntries[entry] & ~OFFSET_MASK) | (((long) offset) << OFFSET_SHIFT);
    }

    private final void setOSRMapIndex(int entry, int index) {
        mapEntries[entry] = (mapEntries[entry] & ~OSRI_MASK) | (((long) index) << OSRI_SHIFT);
    }

    private final void setBCIndex(int entry, int index) {
        mapEntries[entry] = (mapEntries[entry] & ~BCI_MASK) | (((long) index) << BCI_SHIFT);
    }

    private final void setIEIndex(int entry, int index) {
        mapEntries[entry] = (mapEntries[entry] & ~IEI_MASK) | (((long) index) << IEI_SHIFT);
    }

    public void printMap() {
        if (lastEntry > 0) {
            VM.sysWrite("On-stack-replacement maps:\n");
        }
        for (int i = 0; i <= lastEntry; i++) {
            VM.sysWrite("Entry " + i + " : ");
            int mapIndex = getOSRMapIndex(i);
            VM.sysWrite("  mapIndex " + mapIndex + ", ");
            int mcOffset = getMCOffset(i);
            VM.sysWrite("  mc " + mcOffset + ", ");
            int bcIndex = getBCIndex(i);
            VM.sysWriteln("bc " + bcIndex);
            int regmap = osrMaps[mapIndex] & ~NEXT_BIT;
            VM.sysWrite("regmap: " + Integer.toBinaryString(regmap));
            OSR_MapIterator iterator = new OSR_MapIterator(osrMaps, mapIndex);
            while (iterator.hasMore()) {
                VM.sysWrite("(" + iterator.getValueType() + "," + iterator.getValue() + ")");
                iterator.moveToNext();
            }
            VM.sysWrite("\n");
        }
    }

    public int[] getMCIndexes() {
        int[] indexes = new int[mapEntries.length];
        for (int i = 0, n = mapEntries.length; i < n; i++) {
            indexes[i] = getMCOffset(i);
        }
        return indexes;
    }
}
