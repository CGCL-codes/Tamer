package cb.recommender.base.recommender;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * Lucene doesn't index files, it indexes Document objects. To index and then
 * search files, you first need write code that converts your files into
 * Document objects. A Document object is a collection of Field objects
 * (name/value pairs). So, for each file, instantiate a Document, then populate
 * it with Fields. 
 * 
 * @author valadao
 * 
 */
public class IdealizeDocument {

    /**
	 * The org.apache.lucene.document.Document could not be inherited because
	 * it's a final class so, a doc variable should be stored to
	 * "simulate the heritage"
	 */
    protected Document doc;

    public static enum FieldType {

        KEYWORD, TEXT
    }

    /**
	 * In order for Mahout to create vectors from a Lucene index, the first and
	 * foremost thing that must be done is that the index must contain Term
	 * Vectors. A term vector is a document centric view of the terms and their
	 * frequencies (as opposed to the inverted index, which is a term centric
	 * view) and is not on by default.
	 */
    public IdealizeDocument(String id) {
        doc = new Document();
        doc.add(getNewKeywordField("id", id));
    }

    /**
	 * Basic constructor.
	 */
    public IdealizeDocument() {
        doc = new Document();
    }

    /**
	 * A Document is a set of fields. Each field has a name and a textual value.
	 * A field may be stored with the document, in which case it is returned
	 * with search hits on the document. Thus each document should typically
	 * contain one or more stored fields which uniquely identify it.
	 * 
	 * @return the org.apache.lucene.document.Document
	 */
    public Document getDocument() {
        return doc;
    }

    /**
	 * A field is a section of a Document. Each field has two parts, a name and
	 * a value. Values may be free text, provided as a String or as a Reader, or
	 * they may be atomic keywords, which are not further processed. Such
	 * keywords may be used to represent dates, urls, etc. Fields are optionally
	 * stored in the index, so that they may be returned with hits on the
	 * document. @see org.apache.lucene.document.Field
	 * 
	 * @param fieldname
	 *            the name of the field
	 * @return the value of that field
	 */
    public String getField(String fieldname) {
        return doc.get(fieldname);
    }

    /**
	 * A keyword is a simple field, with a simple value (ex.: to some field
	 * 'color' the value could be 'blue')
	 * 
	 * A Field object contains a name (a String) and a value (a String or a
	 * Reader), and three booleans that control whether or not the value will be
	 * indexed for searches, tokenized prior to indexing, and stored in the
	 * index so it can be returned with the search.
	 * 
	 * @Field.Index.NOT_ANALYZED: A simple field is a keyword, so there's no
	 *                            need to be indexed analyzed like a text.
	 * @Field.Store.YES: It has to be stored to be able to retrieve it's values
	 *                   (e.g. to filter something).
	 * 
	 *                   Stored fields are handy for immediately having the
	 *                   original text available from a search, such as a
	 *                   database primary key or filename. Indexed field
	 *                   information is stored extremely efficiently, such that
	 *                   the same term in the same field name across multiple
	 *                   documents is only stored once, with pointers to the
	 *                   documents that contain it.
	 * 
	 * @Field.TermVector.YES: Every field has to enable TERM_VECTOR in order for
	 *                        Mahout to create vectors from a Lucene index.
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
    public void setKeywordField(String fieldname, String fieldValue) {
        doc.add(getNewKeywordField(fieldname, fieldValue));
    }

    /**
	 * A "analyzed field" is a text, having lots of words in its value. The text
	 * should be analyzed, i.e., tokenized and filtered (typically dropping out
	 * punctuation and commonly occurring words)
	 * 
	 * A Field object contains a name (a String) and a value (a String or a
	 * Reader), and three booleans that control whether or not the value will be
	 * indexed for searches, tokenized prior to indexing, and stored in the
	 * index so it can be returned with the search.
	 * 
	 * @Field.Index.ANALYZED: Text values has to be analyzed to enable fast and
	 *                        efficient searching and indexing
	 * @Field.Store.NO: A priori, there's no need to store the text value, only
	 *                  to index it.
	 * 
	 *                  Stored fields can dramatically increase the index size,
	 *                  so use them wisely. So, if the value should be
	 *                  retrieved, a java.io.Reader could be passed to retrieve
	 *                  the value directly from the source of the Document (ex.:
	 *                  a file, a database, a HTML page, etc).
	 * @Field.TermVector.YES: Every field has to enable TERM_VECTOR in order for
	 *                        Mahout to create vectors from a Lucene index.
	 * 
	 * @param fieldName
	 * @param fieldText
	 * @return
	 */
    public void setTextField(String fieldname, String fieldText) {
        doc.add(getNewTextField(fieldname, fieldText));
    }

    /**
	 * Set a new field of a given type.
	 * @param fieldName
	 * @param fieldValue
	 * @param type
	 */
    public void setField(String fieldName, String fieldValue, FieldType type) {
        switch(type) {
            case KEYWORD:
                setKeywordField(fieldName, fieldValue);
                break;
            case TEXT:
                setTextField(fieldName, fieldValue);
                break;
        }
    }

    /**
	 * Get a new Keyword Field.
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
    protected Field getNewKeywordField(String fieldName, String fieldValue) {
        return new Field(fieldName, fieldValue, Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.YES);
    }

    /**
	 * Get a new Text Field.
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
    protected Field getNewTextField(String fieldName, String fieldValue) {
        return new Field(fieldName, fieldValue, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES);
    }
}
