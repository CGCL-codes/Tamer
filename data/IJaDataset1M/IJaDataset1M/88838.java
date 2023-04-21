package com.sun.java.help.search;

import java.io.*;
import java.util.*;

public class FullBtreeDict extends BtreeDict {

    protected BtreeDictParameters params;

    private boolean update = false;

    private final class Entry {

        public byte[] key;

        public int id;

        public int block = -1;

        public Entry(byte[] key, int length, int id) {
            this.key = new byte[length + 1];
            System.arraycopy(key, 0, this.key, 0, length);
            this.key[length] = 0;
            this.id = id;
        }

        public boolean smallerThan(Entry other) {
            for (int i = 0; i < Math.min(key.length, other.key.length); i++) if (key[i] != other.key[i]) return (key[i] & 0xFF) < (other.key[i] & 0xFF);
            return false;
        }
    }

    protected class FullDictBlock extends DictBlock {

        public void setFree(int free) {
            this.free = free - firstEntry();
            this.data[free] = this.data[free + 1] = 0;
        }

        public void setNumberOfEntries(int n) {
            setIntegerAt(0, n);
        }

        protected void setChildIndex(int index, int value) {
            setIntegerAt(4 * (lastPtrIndex - index + 1), value);
        }

        public void setEntryID(int i, int id) {
            setIntegerAt(i + 2, id);
        }

        private boolean insert(Entry entry) {
            byte[] inkey = entry.key;
            final int inputKeyLen = inkey.length - 1;
            final int freeSpace = free();
            int entryPtr = firstEntry();
            int nCharsEqual = 0;
            int prevNCEqual = 0;
            int compression = 0;
            for (int entryIndex = 0; ; ) {
                if (entryPtr == freeSpace) return insert(entry, entryPtr, nCharsEqual, 0, numberOfEntries()); else if (compression == nCharsEqual) {
                    int keyLen = entryKeyLength(entryPtr);
                    int keyPtr = entryKey(entryPtr), i;
                    prevNCEqual = nCharsEqual;
                    for (i = 0; i < keyLen && inkey[nCharsEqual] == data[keyPtr + i]; i++) ++nCharsEqual;
                    if (i == keyLen) {
                        if (nCharsEqual == inputKeyLen) {
                            setEntryID(entryPtr, entry.id);
                            return true;
                        }
                    } else if ((inkey[nCharsEqual] & 0xFF) < (data[keyPtr + i] & 0xFF)) return insert(entry, entryPtr, prevNCEqual, nCharsEqual, entryIndex);
                } else if (compression < nCharsEqual) return insert(entry, entryPtr, nCharsEqual, compression, entryPtr == freeSpace ? numberOfEntries() : entryIndex);
                do {
                    entryPtr = nextEntry(entryPtr);
                    ++entryIndex;
                } while (entryCompression(entryPtr) > nCharsEqual);
                compression = entryCompression(entryPtr);
            }
        }

        public void makeEntry(int entry, byte[] key, int id, int length, int compr) {
            data[entry] = (byte) length;
            data[entry + 1] = (byte) compr;
            setEntryID(entry, id);
            System.arraycopy(key, compr, data, entryKey(entry), length);
        }

        private boolean insert(Entry ent, final int entryPtr, final int compr1, final int compr2, final int index) {
            final byte[] key = ent.key;
            final int keyLen = key.length - 1 - compr1;
            final int freeSpace = free();
            final int demand = ENTHEADERLEN + keyLen;
            int increase = 0;
            if (entryPtr < freeSpace) if (entryCompression(entryPtr) < compr2) increase = compr2 - entryCompression(entryPtr);
            int limit = isLeaf ? DATALEN - 2 : 4 * (lastPtrIndex - numberOfEntries() - 1);
            if (freeSpace + demand - increase <= limit) {
                if (entryPtr < freeSpace) {
                    final int toMove = increase > 0 ? entryPtr + ENTHEADERLEN + increase : entryPtr;
                    System.arraycopy(data, toMove, data, toMove + demand - increase, freeSpace - toMove);
                    if (increase > 0) {
                        data[entryPtr] -= increase;
                        data[entryPtr + 1] += increase;
                        System.arraycopy(data, entryPtr, data, entryPtr + demand, ENTHEADERLEN);
                    }
                }
                makeEntry(entryPtr, key, ent.id, keyLen, compr1);
                if (isLeaf == false) {
                    int from = 4 * (lastPtrIndex - numberOfEntries() + 1);
                    System.arraycopy(data, from, data, from - 4, 4 * (numberOfEntries() - index));
                    setChildIndex(index + 1, ent.block);
                }
                setFree(freeSpace + demand - increase);
                setNumberOfEntries(numberOfEntries() + 1);
                return true;
            } else return false;
        }

        public int insertInternal(Entry entry) {
            byte[] inkey = entry.key;
            final int inputKeyLen = inkey.length - 1;
            int entryPtr = firstEntry();
            final int freeSpace = free();
            int nCharsEqual = 0;
            int compression = 0;
            for (int entryIndex = 0; ; ) {
                if (entryPtr == freeSpace) return numberOfEntries(); else if (compression == nCharsEqual) {
                    int i;
                    int keyLen = entryKeyLength(entryPtr);
                    int keyPtr = entryKey(entryPtr);
                    for (i = 0; i < keyLen && inkey[nCharsEqual] == data[keyPtr + i]; i++) ++nCharsEqual;
                    if (i == keyLen) {
                        if (nCharsEqual == inputKeyLen) {
                            setEntryID(entryPtr, entry.id);
                            return -1;
                        }
                    } else if ((inkey[nCharsEqual] & 0xFF) < (data[keyPtr + i] & 0xFF)) return entryIndex;
                } else if (compression < nCharsEqual) return entryPtr >= freeSpace ? numberOfEntries() : entryIndex;
                do {
                    entryPtr = nextEntry(entryPtr);
                    ++entryIndex;
                } while (entryCompression(entryPtr) > nCharsEqual);
                compression = entryCompression(entryPtr);
            }
        }

        private Entry split(FullDictBlock newbl) {
            byte[] buffer = new byte[MaxKeyLength];
            int freeSpace = free();
            int half = freeSpace / 2;
            int index = 0;
            newbl.isLeaf = isLeaf;
            int ent;
            for (ent = firstEntry(); ent < half; ent = nextEntry(ent)) {
                restoreKeyInBuffer(ent, buffer);
                ++index;
            }
            final int entriesToMove = numberOfEntries() - index - 1;
            restoreKeyInBuffer(ent, buffer);
            int len = entryKeyLength(ent) + entryCompression(ent);
            Entry result = new Entry(buffer, len, entryID(ent));
            result.block = newbl.number;
            int newFree = ent;
            ent = nextEntry(ent);
            restoreKeyInBuffer(ent, buffer);
            len = entryKeyLength(ent) + entryCompression(ent);
            int nptr = firstEntry();
            newbl.makeEntry(nptr, buffer, entryID(ent), len, 0);
            ent = nextEntry(ent);
            System.arraycopy(data, ent, newbl.data, newbl.nextEntry(nptr), freeSpace - ent);
            newbl.setNumberOfEntries(entriesToMove);
            newbl.setFree(newbl.nextEntry(nptr) + freeSpace - ent);
            if (isLeaf == false) {
                int from = 4 * (lastPtrIndex - numberOfEntries() + 1);
                System.arraycopy(data, from, newbl.data, from + 4 * (index + 1), 4 * (entriesToMove + 1));
            }
            setFree(newFree);
            setNumberOfEntries(index);
            return result;
        }

        public void initInternal(int leftBlock, Entry entry) {
            isLeaf = false;
            setNumberOfEntries(1);
            setChildIndex(0, leftBlock);
            setChildIndex(1, entry.block);
            final int ent = firstEntry();
            makeEntry(ent, entry.key, entry.id, entry.key.length - 1, 0);
            setFree(nextEntry(ent));
        }
    }

    protected FullBtreeDict() {
    }

    public FullBtreeDict(BtreeDictParameters params, boolean update) throws Exception {
        init(params, update, new BlockFactory() {

            public Block makeBlock() {
                return new FullDictBlock();
            }
        });
        blocks = new int[300000];
        this.params = params;
        this.update = update;
    }

    public void close(int freeID) throws Exception {
        params.setFreeID(freeID);
        if (update) params.updateSchema();
        super.close();
    }

    public void store(String key, int id) throws Exception {
        byte[] bytes = key.getBytes("UTF8");
        Entry entry = insert((FullDictBlock) accessBlock(root), new Entry(bytes, bytes.length, id));
        if (entry != null) {
            FullDictBlock nbl = getNewBlock();
            nbl.initInternal(root, entry);
            blocks[entry.id] = root = nbl.number;
            params.setRoot(root);
        }
    }

    private void setModified(Block bl) {
        blockManager.setModified(bl.number);
    }

    private FullDictBlock getNewBlock() throws Exception {
        FullDictBlock nbl = (FullDictBlock) blockManager.getNewBlock();
        setModified(nbl);
        return nbl;
    }

    private Entry insert(FullDictBlock bl, Entry ent) throws Exception {
        if (bl.isLeaf) return insertHere(bl, ent); else {
            int index = bl.insertInternal(ent);
            if (index != -1) try {
                lock(bl);
                ent = insert((FullDictBlock) child(bl, index), ent);
                return ent == null ? null : insertHere(bl, ent);
            } finally {
                unlock(bl);
            } else return null;
        }
    }

    private Entry insertHere(FullDictBlock bl, Entry ent) throws Exception {
        setModified(bl);
        if (bl.insert(ent)) return null; else {
            FullDictBlock nbl = getNewBlock();
            Entry middle = bl.split(nbl);
            nbl.setBlockNumbers(blocks);
            if ((middle.smallerThan(ent) ? nbl : bl).insert(ent) == false) throw new Exception("entry didn't fit into a freshly split block");
            return middle;
        }
    }

    public static void main(String[] args) {
        try {
            Schema schema = new Schema(null, args[0], true);
            BtreeDictParameters tmapParams = new BtreeDictParameters(schema, "TMAP");
            if (tmapParams.readState() == false) {
                tmapParams.setBlockSize(2048);
                tmapParams.setRoot(0);
                tmapParams.setFreeID(1);
            }
            FullBtreeDict dict = new FullBtreeDict(tmapParams, true);
            int freeID = tmapParams.getFreeID();
            LineNumberReader in = new LineNumberReader(new BufferedReader(new FileReader(args[1])));
            String line;
            while ((line = in.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line, " ");
                while (tokens.hasMoreTokens()) {
                    String token = tokens.nextToken();
                    if (token.equals("storing")) dict.store(tokens.nextToken(), freeID++); else if (token.equals("fetching")) dict.fetch(tokens.nextToken());
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Debug code
   */
    private boolean debug = false;

    private void debug(String msg) {
        if (debug) {
            System.err.println("FullBtreeDict: " + msg);
        }
    }
}
