package edu.psu.citeseerx.citematch;

import edu.psu.citeseerx.domain.Document;
import org.json.JSONException;

/**
 * Generic specification for citation clustering implementations.
 *
 * @author Isaac Councill
 * @version $Rev: 1112 $ $Date: 2009-05-13 10:57:14 -0400 (Wed, 13 May 2009) $
 */
public interface CitationClusterer {

    /**
     * Filters the citation list for self citations, generates metadata keys,
     * then clusters the Document and Citations within the database backend.
     */
    public void clusterDocument(Document doc) throws JSONException;

    /**
     * Filters the citation list for self citations, generates new keys
     * based on existing metadata, then calls the database API to delete
     * the Document and Citations, then re-cluster them based on the
     * new key and self-citation information.
     */
    public void reclusterDocument(Document doc) throws JSONException;

    /**
     * Does the same thing as the simpler reclusterDocument, but checks to
     * see whether key information has changed before triggering a
     * call to recluster the Document and Citations.
     */
    public void reclusterDocument(Document newDoc, Document oldDoc) throws JSONException;

    /**
     * Deletes the document from its cluster.
     * @param doc
     */
    public void deleteDocumentFromCluster(Document doc);
}
