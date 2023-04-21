package org.rg.lucene;

import java.rmi.RemoteException;
import java.util.*;
import java.io.*;
import org.apache.solr.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.*;
import org.apache.lucene.search.ScoreDoc;
import org.rg.common.datatypes.regression.TermFreqVector;
import org.rg.common.datatypes.regression.TermScore;

public class Documents implements Serializable {

    private static final Log log = LogFactory.getLog("Documents");

    static final long serialVersionUID = 1L;

    public static final int DEFAULT_BUFFER = 100;

    private ContentPort port;

    private Map<String, String[]> cache = new HashMap<String, String[]>();

    private Map<String, String[]> fieldgroup = null;

    private int buffer = DEFAULT_BUFFER;

    private Map<String, Integer> cursors = new HashMap<String, Integer>();

    private int size;

    private long time;

    public Documents() {
        size = 0;
        time = 0;
    }

    public ScoreDoc[] getScoreDocs() throws RemoteException {
        return port.getScoreDocs();
    }

    public Documents(ContentPort port, Map<String, String[]> selector, int size, long time) {
        this(port, selector, size, time, DEFAULT_BUFFER);
    }

    public Documents(ContentPort port, Map<String, String[]> selector, int size, long time, int buffer) {
        this.port = port;
        this.fieldgroup = selector;
        Iterator<String> iter = selector.keySet().iterator();
        while (iter.hasNext()) {
            String field = iter.next();
            cache.put(field, new String[0]);
            cursors.put(field, 0);
        }
        this.size = size;
        this.time = time;
        this.buffer = buffer;
    }

    /**
	 * Get the size of documents
	 * @return the size of documents
	 */
    public int size() {
        return size;
    }

    /**
	 * Get the time (ms) to perform the search (note, it's the time lucene uses to search, not including fetch content)
	 * @return the time (ms) lucene spent
	 */
    public long time() {
        return time;
    }

    /**
	 * This is the basic method to retrieve field, note lucene treat everything as string.
	 * @param field is the field to be retrieved 
	 * @param index is the index of the document
	 * @return the raw (uninterpreted) field content
	 * @throws LuceneException
	 */
    public String get(String field, int index) throws LuceneException {
        if ((index >= size) || (index < 0)) throw new LuceneException("RecordSet index is out of bounds");
        if (!cursors.containsKey(field)) throw new LuceneException("Invalid field");
        int cursor = cursors.get(field);
        if ((index < cursor) || (index > (cursor + cache.get(field).length - 1))) {
            try {
                Map<String, String[]> ret = port.get(fieldgroup.get(field), index, buffer);
                cache.putAll(ret);
                Iterator<String> iter = ret.keySet().iterator();
                while (iter.hasNext()) cursors.put(iter.next(), index);
                cursor = cursors.get(field);
            } catch (Exception e) {
                log.error("Exception getting field named " + field, e);
                e.printStackTrace();
            }
        }
        if (index - cursor < cache.get(field).length) return cache.get(field)[index - cursor]; else {
            log.info("index:" + index + " cursor:" + cursor + " cache:" + cache.get(field).length);
            throw new LuceneException("Something wrong in lucene");
        }
    }

    /**
	 * Method return the term frequency vectors, valid only for content field
	 * @param field is the field to be retrieved
	 * @param index is the index of the document
	 * @return the raw TermFreqVector
	 * @throws LuceneException
	 */
    public TermFreqVector getContentVectors(int index, String field) throws LuceneException, RemoteException {
        return port.getTermVector(index, field);
    }

    /**
	 * Get document at given index and fieldselector
	 * @param index index of the document
	 * @param selector selected fields
	 * @return Document document containing selected fields
	 */
    public Document getDocument(int index, FieldSelector selector) throws LuceneException, RemoteException {
        return port.getDocument(index, selector);
    }

    /**
	 * Method returns the lucene score for a document at given index
	 * @param index index of the document
	 * @return lucene score
	 */
    public float getLuceneScore(int index) throws LuceneException, RemoteException {
        return port.getLuceneScore(index);
    }

    /**
	 * Method return excerpt of given document based on query match, give token stream and content
	 * Token stream and content will be extracted from the lucene document if they are null
	 * @param index lucene document index
	 * @param wtTerms TermScore if it is not null, Snippet is calculated around these terms based on 
	 * corresponding weights
	 * @param content content content of the page, if content is null, it pull content from the lucene index
	 * @return Snippet or Excerpt (best fragment satisfies the query)
	 */
    public String getExcerpt(int index, TermScore ts, String content) throws LuceneException, RemoteException {
        return port.getExcerpt(index, ts, content);
    }

    /**
	 * This is a wrapper for field retrieval, it just calls the basic get method and converts into float
	 * @param field is the field to be retrieved
	 * @param index is the index of the document
	 * @return the float representation of the field raw content
	 * @throws LuceneException
	 */
    public float getFloat(String field, int index) throws LuceneException {
        try {
            return NumberUtils.SortableStr2float(get(field, index));
        } catch (Exception e) {
            log.error("Exception getting float named " + field, e);
            e.printStackTrace();
            throw new LuceneException("Conversion Float Exception");
        }
    }

    /**
	 * This is a wrapper for field retrieval, it just calls the basic get method and converts into long
	 * @param field is the field to be retrieved
	 * @param index is the index of the document
	 * @return the long representation of the field raw content
	 * @throws LuceneException
	 */
    public long getLong(String field, int index) throws LuceneException {
        try {
            return NumberTools.stringToLong(get(field, index));
        } catch (Exception e) {
            log.error("Exception getting long named " + field, e);
            e.printStackTrace();
            throw new LuceneException("Conversion Long Exception");
        }
    }

    /**
	 * This is a wrapper for field retrieval, it just calls the basic get method and converts into Date
	 * @param field is the field to be retrieved
	 * @param index is the index of the document
	 * @return the Date representation of the field raw content
	 * @throws LuceneException
	 */
    public Date getDate(String field, int index) throws LuceneException {
        String tmp = null;
        try {
            tmp = get(field, index);
            if (tmp != null) return DateTools.stringToDate(tmp); else return new Date(0L);
        } catch (Exception e) {
            log.error("Exception getting date named " + field, e);
            e.printStackTrace();
            throw new LuceneException("Conversion Date Exception : " + tmp);
        }
    }
}
