package org.openofficesearch.util;

import java.sql.SQLException;
import org.openofficesearch.data.Posting;
import org.openofficesearch.data.Document;
import java.util.Iterator;

/**
 * Interface for classes that can hold and iterate over postings<br />
 * Created: 2005
 * @author Connor Garvey
 * @version 0.1.1
 * @since 0.0.1
 */
public interface PostingList {

    /**
   * Adds a posting to the list
   * @param posting The posting to be added
   * @throws SQLException Thrown if a problem occurs while working with the
   *    database
   */
    public void add(Posting posting) throws SQLException;

    /**
   * Adds a posting to a document
   * @param posting The posting to be added
   * @param document The document to which the posting will be added
   */
    public void add(Posting posting, Document document);

    /**
   * Gets postings by the document with which they are associated
   * @param document The document to which the posting is associated
   * @return The matching document postings
   */
    public DocumentPostings getByDocument(Document document);

    /**
   * Gets an iterator over the postings
   * @return The iterator
   */
    public Iterator<Posting> postingIterator();

    /**
   * Gets an iterator over the document postings
   * @return The document postings iterator
   */
    public Iterator<DocumentPostings> documentPostingsIterator();

    /**
   * Gets the number of documents
   * @return The number of documents in the list
   */
    public int documentCount();
}
