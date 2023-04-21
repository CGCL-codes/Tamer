package ru.spbu.cuckoo.index.rea;

import ru.spbu.cuckoo.index.common.HashArray;
import ru.spbu.cuckoo.index.exceptions.KeyIndexerException;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.File;

/**
 *
 * @author 1mp0ss1ble
 * Date: 23.05.2007
 */
public class PatriciaTrie implements HashArray {

    private KeyIndexer keyIndexer;

    private RandomAccessFile trieFile;

    public PatriciaTrie(File file) {
        try {
            boolean exists = file.exists();
            this.trieFile = new RandomAccessFile(file, "rw");
            this.keyIndexer = new KeyIndexer();
            if (!exists) createNode("".toCharArray(), new int[] { -1, -1 }, -1, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets value from the trie by the given key
     * @param s key to search
     * @param size value size
     * @return key's value or null if no such key could be found
     */
    public int[] getArray(String s, int size) {
        char[] key = s.toLowerCase().toCharArray();
        try {
            long found = getNearestNodeForKey(key);
            return compare(key, getKey(found)) ? getValue(found) : null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Adds a new <key, value> pair to the Trie and if the pair already
     * exists it will be replaced
     * @param s key to put
     * @param value value to put
     */
    public void putArray(String s, int[] value) {
        if (s == null) throw new NullPointerException("Key cannot be null");
        char[] key = s.toCharArray();
        try {
            long found = getNearestNodeForKey(key);
            if (compare(key, getKey(found))) {
                setValue(found, value);
                return;
            }
            int bitIndex = bitIndex(key, getKey(found));
            long fileLength = trieFile.length();
            createNode(key, value, bitIndex, fileLength);
            addNode(fileLength);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether the trie has the given key
     * @param s string key to check
     * @return true if the given key is in the trie,and false if it's not
     */
    public boolean has(String s) {
        char[] key = s.toCharArray();
        try {
            long found = getNearestNodeForKey(key);
            return compare(key, getKey(found));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private long getNearestNodeForKey(char[] key) throws IOException {
        long path = 0;
        long current = getLeft(path);
        while (true) {
            if (getBitIndex(current) <= getBitIndex(path)) return current;
            path = current;
            if (!isBitSet(key, getBitIndex(current))) current = getLeft(current); else current = getRight(current);
        }
    }

    private void addNode(long toAddPosition) throws IOException {
        long path = 0;
        long current = getLeft(path);
        while (true) {
            if (getBitIndex(current) >= getBitIndex(toAddPosition) || getBitIndex(current) <= getBitIndex(path)) {
                if (!isBitSet(getKey(toAddPosition), getBitIndex(toAddPosition))) {
                    setLeft(toAddPosition, toAddPosition);
                    setRight(toAddPosition, current);
                } else {
                    setLeft(toAddPosition, current);
                    setRight(toAddPosition, toAddPosition);
                }
                if (path == 0 || !isBitSet(getKey(toAddPosition), getBitIndex(path))) setLeft(path, toAddPosition); else setRight(path, toAddPosition);
                return;
            }
            path = current;
            if (!isBitSet(getKey(toAddPosition), getBitIndex(current))) current = getLeft(current); else current = getRight(current);
        }
    }

    private boolean isBitSet(char[] key, int bitIndex) {
        if (key == null) return false;
        return keyIndexer.isBitSet(key, bitIndex);
    }

    private int bitIndex(char[] key, char[] foundKey) throws KeyIndexerException {
        return keyIndexer.bitIndex(key, foundKey);
    }

    private boolean compare(char[] key, char[] found) {
        if (found.length != key.length) return false;
        for (int i = 0; i < key.length; i++) if (key[i] != found[i]) return false;
        return true;
    }

    private void createNode(char[] key, int[] value, int bitIndex, long potision) throws IOException {
        trieFile.seek(potision);
        trieFile.writeInt(bitIndex);
        trieFile.writeInt(key.length);
        trieFile.writeLong(potision);
        trieFile.writeLong(-1);
        trieFile.writeInt(value[0]);
        if (value.length == 1) trieFile.writeInt(-2); else trieFile.writeInt(value[1]);
        for (char aKey : key) trieFile.writeChar(aKey);
    }

    private int getBitIndex(long position) throws IOException {
        trieFile.seek(position);
        return trieFile.readInt();
    }

    private char[] getKey(long position) throws IOException {
        trieFile.seek(position + 4);
        int keyLength = trieFile.readInt();
        char[] key = new char[keyLength];
        trieFile.seek(position + 32);
        for (int i = 0; i < keyLength; i++) key[i] = trieFile.readChar();
        return key;
    }

    private int[] getValue(long position) throws IOException {
        int[] value = new int[2];
        trieFile.seek(position + 24);
        value[0] = trieFile.readInt();
        trieFile.seek(position + 28);
        value[1] = trieFile.readInt();
        return value;
    }

    private void setValue(long position, int[] value) throws IOException {
        trieFile.seek(position + 24);
        trieFile.writeInt(value[0]);
        trieFile.seek(position + 28);
        if (value.length == 1) trieFile.writeInt(-2); else trieFile.writeInt(value[1]);
    }

    private long getLeft(long position) throws IOException {
        trieFile.seek(position + 8);
        return trieFile.readLong();
    }

    private void setLeft(long position, long key) throws IOException {
        trieFile.seek(position + 8);
        trieFile.writeLong(key);
    }

    private long getRight(long position) throws IOException {
        trieFile.seek(position + 16);
        return trieFile.readLong();
    }

    private void setRight(long position, long key) throws IOException {
        trieFile.seek(position + 16);
        trieFile.writeLong(key);
    }
}
