package org.hip.vif.search;

import java.io.IOException;
import org.apache.lucene.index.IndexWriter;
import org.hip.kernel.exc.VException;

/**
 * Interface for all domain objects (i.e. contributions) that are indexable,
 * i.e. that have content that should be included into full text search.
 * 
 * @author Benno Luthiger
 * Created on 25.09.2005 
 */
public interface Indexable {

    /**
	 * Add the content of this domain object to the full text search index.
	 * @param inWriter
	 */
    void indexContent(IndexWriter inWriter) throws IOException, VException;
}
