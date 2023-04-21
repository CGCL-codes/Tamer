package pitt.search.semanticvectors;

import java.util.Enumeration;
import pitt.search.semanticvectors.vectors.Vector;
import pitt.search.semanticvectors.vectors.VectorType;

/**
   Classes implementing this interface are used to represent a collection
   of object vectors, including i. methods for accessing individual
   ObjectVectors and ii. an enumeration of all the vectors.
   @author Dominic Widdows
   @see ObjectVector
*/
public interface VectorStore {

    /**
   * The type of all vectors in the vector store.  (Implementations should enforce homogeneity.)
   */
    public VectorType getVectorType();

    /**
   * The dimension of all vectors in the vector store.  (Implementations should enforce homogeneity.)
   */
    public int getDimension();

    /**
   * @param object the object whose vector you want to look up
   * @return a vector (of floats)
   */
    public Vector getVector(Object object);

    /**
   * Returns an enumeration of all the object vectors in the store.
   */
    public Enumeration<ObjectVector> getAllVectors();

    /**
   * Returns a count of the number of vectors in the store.
   */
    public int getNumVectors();
}
