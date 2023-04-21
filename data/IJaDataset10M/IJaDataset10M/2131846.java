package riktree;

import java.lang.Integer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

/**
   This class provides methods for reading a VectorStore into memory
   as an optimization if batching many searches. <p>

   The serialization currently presumes that the object (in the ObjectVectors)
   should be serialized as a String. <p>

   The class is constructed by creating a VectorStoreReader class,
   iterating through vectors and reading them into memory.
	 @see VectorStoreReader
   @see ObjectVector
**/
public class VectorStoreSparseRAM implements VectorStore {

    private Hashtable<String, short[]> sparseVectors;

    int seedLength;

    Random random = new Random();

    public VectorStoreSparseRAM() {
        this.sparseVectors = new Hashtable<String, short[]>();
    }

    ;

    public Enumeration getKeys() {
        return this.sparseVectors.keys();
    }

    public void CreateRandomVectors(int numVectors, int seedLength) {
        this.seedLength = seedLength;
        random = new Random();
        System.err.println("Creating store of sparse vectors  ...");
        for (int i = 0; i < numVectors; ++i) {
            short[] sparseVector = VectorUtils.generateRandomVector(seedLength, random);
            this.sparseVectors.put(Integer.toString(i), sparseVector);
        }
        System.err.println("Created " + sparseVectors.size() + " sparse random vectors.");
    }

    public void generateAndPutNewVector(String key, int seedLength) {
        short[] sparseVector = VectorUtils.generateRandomVector(seedLength, random);
        this.sparseVectors.put(key, sparseVector);
    }

    public void putVector(String key, short[] sparseVector) {
        this.sparseVectors.put(key, sparseVector);
    }

    /**
   * Given an object, get its corresponding vector <br>
   * This implementation only works for string objects so far <br>
   * @param desiredObject - the string you're searching for
	 * @return vector from the VectorStore, or null if not found. 
   */
    public float[] getVector(Object desiredObject) {
        short[] sparseVector = this.sparseVectors.get(desiredObject);
        if (sparseVector != null) {
            return VectorUtils.sparseVectorToFloatVector(sparseVector, ObjectVector.vecLength);
        } else {
            return null;
        }
    }

    /**
	 * Returns the sparse vector without going through the float[] interface.
	 */
    public short[] getSparseVector(Object desiredObject) {
        return this.sparseVectors.get(desiredObject);
    }

    public int getNumVectors() {
        return this.sparseVectors.size();
    }

    public Enumeration<ObjectVector> getAllVectors() {
        return new SparseVectorEnumeration(this);
    }

    /**
   * Implements the hasMoreElements() and nextElement() methods
   * to give Enumeration interface from sparse vector store.
   */
    public class SparseVectorEnumeration implements Enumeration {

        VectorStoreSparseRAM sparseVectorStore;

        Enumeration keys;

        public SparseVectorEnumeration(VectorStoreSparseRAM sparseVectorStore) {
            this.sparseVectorStore = sparseVectorStore;
            this.keys = sparseVectorStore.getKeys();
        }

        public boolean hasMoreElements() {
            return this.keys.hasMoreElements();
        }

        public ObjectVector nextElement() {
            Object key = this.keys.nextElement();
            float[] vector = this.sparseVectorStore.getVector(key);
            return new ObjectVector(key, vector);
        }
    }
}
