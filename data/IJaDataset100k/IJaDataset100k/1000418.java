package lu.ng.search.lucene;

import java.util.Map;
import org.apache.lucene.search.BooleanClause;

/**
 * Contract for implementing a specific parsed object.
 * 
 * @author <a href="mailto:georgosn@gmail.com">georgosn</a> but it is taken from
 *         an early version of jetspeed where the author was <a
 *         href="mailto:morciuch@apache.org">Mark Orciuch</a>
 * @version $Id: ParsedObject.java 187671 2004-10-06 16:40:06Z jford $
 */
public interface ParsedObject {

    /**
     * Getter for property className.
     * 
     * @return Value of property className.
     */
    String getClassName();

    /**
     * Returns parsed object content (cannot be null).
     * 
     * @return String , the text content
     */
    String getContent();

    /**
     * Returns parsed object description (cannot be null).
     * 
     * @return String , the description of the object
     */
    String getDescription();

    /**
     * Returns parsed object searchable fields.
     * 
     * @return Map&lt;Object , Object &gt; , the map of field names and values
     *         for all the searchable fields of the object
     */
    Map<Object, Object> getFields();

    /**
     * Provides the flags to be used among the fields of the object when
     * searching.
     * 
     * @return BooleanClause.Occure[] the array of flags to match the searchable
     *         fields
     */
    BooleanClause.Occur[] getFlags();

    /**
     * Returns parsed object key (cannot be null).
     * 
     * @return String , the key of the object
     */
    String getKey();

    /**
     * Returns parsed object keywords.
     * 
     * @return String[] , the array of keywords for the object
     */
    String[] getKeywords();

    /**
     * Returns parsed object type (cannot be null).
     * 
     * @return the type of the object
     */
    String getObjectType();

    /**
     * Getter for property score.
     * 
     * @return Value of property score.
     */
    float getScore();

    /**
     * Returns parsed object title (cannot be null).
     * 
     * @return String, the title of the object
     */
    String getTitle();

    /**
     * The array of searchable fields for the ParsedObject.
     * 
     * @return String[] the array of field names that can be searched
     */
    String[] searchableFields();

    /**
     * Setter for property className.
     * 
     * @param className
     *            New value of property className.
     */
    void setClassName(String className);

    /**
     * Sets parsed object content (cannot be null).
     * 
     * @param content
     *            , the text content
     */
    void setContent(String content);

    /**
     * Sets parsed object description (cannot be null).
     * 
     * @param description
     *            , the description of the object
     */
    void setDescription(String description);

    /**
     * Sets parsed object searchable fields.
     * 
     * @param fields
     *            , the map of field names and values for all the searchable
     *            fields of the object
     */
    void setFields(Map<Object, Object> fields);

    /**
     * Sets parsed object key (cannot be null).
     * 
     * @param key
     *            , the key of the object
     */
    void setKey(String key);

    /**
     * Sets parsed object keywords.
     * 
     * @param keywords
     *            , the array of keywords for the object
     */
    void setKeywords(String[] keywords);

    /**
     * Sets parsed object type (cannot be null).
     * 
     * @param type
     *            , the type of the object
     */
    void setObjectType(String type);

    /**
     * Setter for property score.
     * 
     * @param score
     *            New value of property score.
     */
    void setScore(float score);

    /**
     * Sets parsed object title (cannot be null).
     * 
     * @param title
     *            , the title of the object
     */
    void setTitle(String title);

    /**
     * Returns the array of field names in the ParsedObject.
     * 
     * @return the array of all field names of the parsedObject
     */
    String[] whatFields();
}
