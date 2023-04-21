package org.apache.lucene.search;

/**
 * Expert: Returned by low-level sorted search implementations.
 *
 * <p>Created: Feb 12, 2004 8:58:46 AM 
 * 
 * @author  Tim Jones (Nacimiento Software)
 * @since   lucene 1.4
 * @version $Id: TopFieldDocs.java 354819 2005-12-07 17:48:37Z yonik $
 * @see Searcher#search(Query,Filter,int,Sort)
 */
public class TopFieldDocs extends TopDocs {

    /** The fields which were used to sort results by. */
    public SortField[] fields;

    /** Creates one of these objects.
	 * @param totalHits  Total number of hits for the query.
	 * @param scoreDocs  The top hits for the query.
	 * @param fields     The sort criteria used to find the top hits.
	 * @param maxScore   The maximum score encountered.
	 */
    TopFieldDocs(int totalHits, ScoreDoc[] scoreDocs, SortField[] fields, float maxScore) {
        super(totalHits, scoreDocs, maxScore);
        this.fields = fields;
    }
}
