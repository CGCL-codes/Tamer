package org.springframework.lucene.search.factory;

import java.io.IOException;
import java.util.Iterator;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;

/**
 * Interface representing the contract of the Lucene Hits class. It
 * allows unit tests with this resource.
 *  
 * All the method of the Hits class are present in this interface
 * and, so allow to make all the operations of this class. 
 *  
 * @author Thierry Templier
 * @see Hits
 */
public interface LuceneHits {

    int length();

    Document doc(int n) throws IOException;

    float score(int n) throws IOException;

    int id(int n) throws IOException;

    Iterator iterator();
}
