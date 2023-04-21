package blizzard.mpq;

import java.util.Hashtable;

public class MPQHashTable {

    private static final byte[] HASH_KEY = new byte[] { '(', 'H', 'A', 'S', 'H', ' ', 'T', 'A', 'B', 'L', 'E', ')' };

    private long maxBlockIndex;

    private MPQHashEntry[] hashTable;

    /** MPQHashEntry.toString() -> Integer index in hashTable[] */
    private Hashtable entryHash;

    public long getMaxBlockIndex() {
        return maxBlockIndex;
    }

    public int getEntryIndex(MPQHashEntry entry) {
        return ((Integer) entryHash.get(entry.toString())).intValue();
    }

    public MPQHashTable(byte[] buffer, int tableSize) {
        MPQEncryption.decryptBuffer(HASH_KEY, buffer, tableSize * 4);
        maxBlockIndex = 0;
        for (int i = 0; i < tableSize; i++) {
            long locale = MPQHashEntry.extractLocateInfo(buffer, i * MPQHashEntry.MPQ_HASH_ENTRY_SIZE);
            if (locale != 0xFFFFFFFFL && (locale & 0x00FF0000L) != 0) {
                new Exception().printStackTrace();
            }
            long blockIndex = MPQHashEntry.extractBlockIndex(buffer, i * MPQHashEntry.MPQ_HASH_ENTRY_SIZE);
            if (blockIndex < MPQHashEntry.MPQ_HASH_ENTRY_DELETED && blockIndex > maxBlockIndex) maxBlockIndex = blockIndex;
        }
        hashTable = new MPQHashEntry[tableSize];
        entryHash = new Hashtable(tableSize);
        for (int i = 0; i < tableSize; i++) {
            hashTable[i] = new MPQHashEntry(buffer, i * MPQHashEntry.MPQ_HASH_ENTRY_SIZE);
            entryHash.put(hashTable[i].toString(), new Integer(i));
        }
    }

    public MPQHashEntry getHashEntry(String fileName) {
        long index = MPQEncryption.decryptHashIndex(hashTable.length, fileName);
        long name1 = MPQEncryption.decryptFileName1(fileName);
        long name2 = MPQEncryption.decryptFileName2(fileName);
        MPQHashEntry entryName = new MPQHashEntry(name1, name2);
        int entryOffset = (int) index;
        while (!hashTable[entryOffset].isFree()) {
            MPQHashEntry candidate = hashTable[entryOffset];
            if (entryName.equalsName(candidate) && !candidate.isDeleted()) {
                return candidate;
            }
            if (++entryOffset >= hashTable.length) entryOffset = 0;
            if (entryOffset == (int) index) break;
        }
        return null;
    }

    public MPQHashEntry getHashEntryEx(String fileName, long locale) {
        MPQHashEntry entry = getHashEntry(fileName);
        if (entry != null) {
            int entryOffset = ((Integer) entryHash.get(entry.toString())).intValue();
            MPQHashEntry firstEntry = entry;
            MPQHashEntry nameEntry = new MPQHashEntry(entry.getEncryptedName1(), entry.getEncryptedName2());
            MPQHashEntry neutralEntry = null;
            MPQHashEntry languageEntry = null;
            while (!entry.isFree()) {
                if (entry.equalsName(nameEntry)) {
                    if (entry.isLanguageNeutral()) {
                        neutralEntry = entry;
                    }
                    if (entry.getLocale() == locale) {
                        languageEntry = entry;
                    }
                    if (neutralEntry != null && languageEntry != null) break;
                }
                if (++entryOffset >= hashTable.length) entryOffset = 0;
                entry = hashTable[entryOffset];
                if (entry == firstEntry) return null;
            }
            if (locale != MPQHashEntry.MPQ_LANGUAGE_NEUTRAL && languageEntry != null) return languageEntry;
            return neutralEntry;
        }
        return null;
    }
}
