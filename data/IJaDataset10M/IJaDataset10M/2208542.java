package pitt.search.semanticvectors;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IndexInput;
import pitt.search.semanticvectors.vectors.Vector;
import pitt.search.semanticvectors.vectors.VectorType;
import pitt.search.semanticvectors.vectors.VectorFactory;

/**
   This class provides methods for reading a VectorStore from disk. <p>

   The serialization currently presumes that the object (in the ObjectVectors)
   should be serialized as a String. <p>

   The implementation uses Lucene's I/O package, which proved much faster
   than the native java.io.DataOutputStream.
   
   Attempts to be thread-safe but this is not fully tested.
   
   @see ObjectVector
 **/
public class VectorStoreReaderLucene implements CloseableVectorStore {

    private static final Logger logger = Logger.getLogger(VectorStoreReaderLucene.class.getCanonicalName());

    private String vectorFileName;

    private File vectorFile;

    private Directory directory;

    private int dimension;

    private VectorType vectorType;

    @Override
    public VectorType getVectorType() {
        return vectorType;
    }

    @Override
    public int getDimension() {
        return dimension;
    }

    private ThreadLocal<IndexInput> threadLocalIndexInput;

    public IndexInput getIndexInput() {
        return threadLocalIndexInput.get();
    }

    public VectorStoreReaderLucene(String vectorFileName) throws IOException {
        this.vectorFileName = vectorFileName;
        this.vectorFile = new File(vectorFileName);
        try {
            String parentPath = this.vectorFile.getParent();
            if (parentPath == null) parentPath = "";
            this.directory = FSDirectory.open(new File(parentPath));
            this.threadLocalIndexInput = new ThreadLocal<IndexInput>() {

                @Override
                protected IndexInput initialValue() {
                    try {
                        return directory.openInput(vectorFile.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            };
            readHeadersFromIndexInput();
        } catch (IOException e) {
            logger.warning("Cannot open file: " + this.vectorFileName + "\n" + e.getMessage());
            throw e;
        }
    }

    /**
   * Only for testing!  This does not create an FSDirectory so calling "close()" gives NPE.
   * TODO(widdows): Fix this, and ownership of FSDirectory or RAMDirectory!
   */
    protected VectorStoreReaderLucene(ThreadLocal<IndexInput> threadLocalIndexInput) throws IOException {
        this.threadLocalIndexInput = threadLocalIndexInput;
        readHeadersFromIndexInput();
    }

    /**
   * Sets internal dimension and vector type, and public flags to match.
   * 
   * @throws IOException
   */
    public void readHeadersFromIndexInput() throws IOException {
        String header = threadLocalIndexInput.get().readString();
        Flags.parseFlagsFromString(header);
        this.dimension = Flags.dimension;
        this.vectorType = VectorType.valueOf(Flags.vectortype.toUpperCase());
    }

    public void close() {
        this.closeIndexInput();
        try {
            this.directory.close();
        } catch (IOException e) {
            logger.severe("Failed to close() directory resources: have they already been destroyed?");
            e.printStackTrace();
        }
    }

    public void closeIndexInput() {
        try {
            this.getIndexInput().close();
        } catch (IOException e) {
            logger.info("Cannot close resources from file: " + this.vectorFile + "\n" + e.getMessage());
        }
    }

    public Enumeration<ObjectVector> getAllVectors() {
        try {
            getIndexInput().seek(0);
            getIndexInput().readString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new VectorEnumeration(getIndexInput());
    }

    /**
   * Given an object, get its corresponding vector <br>
   * This implementation only works for string objects so far <br>
   * 
   * @param desiredObject - the string you're searching for
   * @return vector from the VectorStore, or null if not found.
   */
    public Vector getVector(Object desiredObject) {
        try {
            String stringTarget = desiredObject.toString();
            getIndexInput().seek(0);
            getIndexInput().readString();
            while (getIndexInput().getFilePointer() < getIndexInput().length() - 1) {
                String objectString = getIndexInput().readString();
                if (objectString.equals(stringTarget)) {
                    VerbatimLogger.info("Found vector for '" + stringTarget + "'\n");
                    Vector vector = VectorFactory.createZeroVector(Flags.vectortype, Flags.dimension);
                    vector.readFromLuceneStream(getIndexInput());
                    return vector;
                } else {
                    getIndexInput().seek(getIndexInput().getFilePointer() + VectorFactory.getLuceneByteSize(vectorType, dimension));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        VerbatimLogger.info("Didn't find vector for '" + desiredObject + "'\n");
        return null;
    }

    /**
   * Trivial (costly) implementation of getNumVectors that iterates and counts vectors.
   */
    public int getNumVectors() {
        Enumeration<ObjectVector> allVectors = this.getAllVectors();
        int i = 0;
        while (allVectors.hasMoreElements()) {
            allVectors.nextElement();
            ++i;
        }
        return i;
    }

    /**
   * Implements the hasMoreElements() and nextElement() methods
   * to give Enumeration interface from store on disk.
   */
    public class VectorEnumeration implements Enumeration<ObjectVector> {

        IndexInput indexInput;

        public VectorEnumeration(IndexInput indexInput) {
            this.indexInput = indexInput;
        }

        public boolean hasMoreElements() {
            return (indexInput.getFilePointer() < indexInput.length());
        }

        public ObjectVector nextElement() {
            String object = null;
            Vector vector = VectorFactory.createZeroVector(vectorType, dimension);
            try {
                object = indexInput.readString();
                vector.readFromLuceneStream(indexInput);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ObjectVector(object, vector);
        }
    }
}
