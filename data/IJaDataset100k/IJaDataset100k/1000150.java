package jazsync.jazsync;

import java.util.ArrayList;
import java.util.Arrays;
import org.jarsync.ChecksumPair;

/**
 * Chaining hash table used to store block checksums loaded from metafile
 * @author Tomáš Hlavnička
 */
public class ChainingHash {

    private ArrayList<ArrayList> hashArray;

    private int arraySize;

    private int index;

    /**
     * Initializing chaining hash table of <code>size</code>
     * @param size Size of the hash table
     */
    public ChainingHash(int size) {
        arraySize = size;
        hashArray = new ArrayList<ArrayList>(arraySize);
        for (int i = 0; i < arraySize; i++) {
            hashArray.add(i, new ArrayList<ChecksumPair>());
        }
    }

    /**
     * Hashing function
     * @param pKey Key object that will be hashed into the table
     * @return Index in hash table where the object will be put
     */
    public int hashFunction(ChecksumPair pKey) {
        return pKey.hashCode() % arraySize;
    }

    /**
     * Method inserting object into the table
     * @param pKey Object we are inserting
     */
    public void insert(ChecksumPair pKey) {
        int hashValue = hashFunction(pKey);
        hashArray.get(hashValue).add(pKey);
    }

    /**
     * Method used to delete an object from hash table
     * @param pKey Object we want to delete from table
     */
    public void delete(ChecksumPair pKey) {
        int hashValue = hashFunction(pKey);
        hashArray.get(hashValue).remove(pKey);
    }

    /**
     * Method used to find an object in hash table using only weakSum
     * @param pKey Object that we are looking for
     * @return Object if found, null if not
     */
    public ChecksumPair find(ChecksumPair pKey) {
        int hashValue = hashFunction(pKey);
        ChecksumPair p = null;
        for (int i = 0; i < hashArray.get(hashValue).size(); i++) {
            p = (ChecksumPair) hashArray.get(hashValue).get(i);
            if (p.getWeak() == pKey.getWeak()) {
                index = i;
                return p;
            }
        }
        return null;
    }

    /**
     * Method used to find an object in hash table using weakSum and strongSum
     * @param pKey Object that we are looking for
     * @return Object if found, null if not
     */
    public ChecksumPair findMatch(ChecksumPair pKey) {
        int hashValue = hashFunction(pKey);
        ChecksumPair p = (ChecksumPair) hashArray.get(hashValue).get(index);
        if (p.getWeak() == pKey.getWeak() && Arrays.equals(p.getStrong(), pKey.getStrong())) {
            return p;
        } else {
            p = null;
        }
        for (int i = 0; i < hashArray.get(hashValue).size(); i++) {
            p = (ChecksumPair) hashArray.get(hashValue).get(i);
            if (p.getWeak() == pKey.getWeak() && Arrays.equals(p.getStrong(), pKey.getStrong())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Simple method used to write out the content of hash table
     */
    public void displayTable() {
        for (int l = 0; l < hashArray.size(); l++) {
            for (int i = 0; i < hashArray.get(l).size(); i++) {
                System.out.println(l + ". list: " + ((ChecksumPair) (hashArray.get(l).get(i))).toString());
            }
        }
    }
}
