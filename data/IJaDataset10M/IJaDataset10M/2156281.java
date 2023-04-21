package org.sucri.floxs.ext.data;

import org.sucri.floxs.ext.JsonConfig;
import org.sucri.floxs.ext.Function;

/**
 * User: Wen Yu
 * Date: Jul 17, 2007
 * Time: 10:40:06 PM

 *  name : String
       The name by which the field is referenced within the Record. This is referenced by, for example the dataIndex
       property in column definition objects passed to Ext.grid.ColumnModel
 * mapping : String
      (Optional) A path specification for use by the Ext.data.Reader implementation that is creating the Record to
       access the data value from the data object. If an Ext.data.JsonReader is being used, then this is a string
       containing the javascript expression to reference the data relative to the record item's root. If an
       Ext.data.XmlReader is being used, this is an Ext.DomQuery path to the data item relative to the record element.
       If the mapping expression is the same as the field name, this may be omitted.
 * type : String
      (Optional) The data type for conversion to displayable value. Possible values are
         o auto (Default, implies no conversion)
         o string
         o int
         o float
         o boolean
         o date
 * sortType : Mixed
      (Optional) A member of Ext.data.SortTypes.
 * sortDir : String
      (Optional) Initial direction to sort. "ASC" or "DESC"
 * convert : Function
      (Optional) A function which converts the value provided by the Reader into an object that will be stored in the
       Record. It is passed the following parameters:
         o v : Mixed
            The data value as read by the Reader.
 * dateFormat : String
      (Optional) A format String for the Date.Date.parseDate function.

 var TopicRecord = Ext.data.Record.create(
     {name: 'title', mapping: 'topic_title'},
     {name: 'author', mapping: 'username'},
     {name: 'totalPosts', mapping: 'topic_replies', type: 'int'},
     {name: 'lastPost', mapping: 'post_time', type: 'date'},
     {name: 'lastPoster', mapping: 'user2'},
     {name: 'excerpt', mapping: 'post_text'}
 );

 var myNewRecord = new TopicRecord({
     title: 'Do my job please',
     author: 'noobie',
     totalPosts: 1,
     lastPost: new Date(),
     lastPoster: 'Animal',
     excerpt: 'No way dude!'
 });
 myStore.add(myNewRecord); 
 */
public class Record extends JsonConfig {

    public Record() {
        super();
    }

    public Record(String n, String m) {
        super();
        setName(n);
        setMapping(m);
    }

    public Record(String n, String m, eType t) {
        super();
        setName(n);
        setMapping(m);
        setType(t);
    }

    enum eType {

        auto, string, integer, floating, bool, date
    }

    public static final String SORT_ASC = "ASC";

    public static final String SORT_DESC = "DESC";

    private static final String NAME = "name";

    private static final String MAPPING = "mapping";

    private static final String TYPE = "type";

    private static final String SORT_DIR = "sortDir";

    private static final String CONVERT = "convert";

    private static final String DATE_FORMAT = "dateFormat";

    public void setName(String s) {
        mProperties.put(NAME, s);
    }

    public void setMapping(String s) {
        mProperties.put(MAPPING, s);
    }

    public void setSortAsc() {
        mProperties.put(SORT_DIR, SORT_ASC);
    }

    public void setSortDesc() {
        mProperties.put(SORT_DIR, SORT_DESC);
    }

    public void setConvert(Function s) {
        mProperties.put(CONVERT, s.toString());
    }

    public void setDateFormat(String s) {
        mProperties.put(DATE_FORMAT, s);
    }

    public void setType(eType s) {
        mProperties.put(TYPE, toString(s));
    }

    public void setType(String s) {
        setType(eType.string);
    }

    public void setType(Integer s) {
        setType(eType.integer);
    }

    public void setType(Long s) {
        setType(eType.integer);
    }

    public void setType(Float s) {
        setType(eType.floating);
    }

    public void setType(Double s) {
        setType(eType.floating);
    }

    public void setType(Boolean s) {
        setType(eType.bool);
    }

    public void setType(java.sql.Date s) {
        setType(eType.date);
    }

    public void setType(java.util.Date s) {
        setType(eType.date);
    }

    public void setType(Class s) {
        if (s.equals(String.class)) setType(eType.string); else if (s.equals(Integer.class) || s.equals(Long.class)) setType(eType.integer); else if (s.equals(Float.class) || s.equals(Double.class)) setType(eType.floating); else if (s.equals(java.sql.Date.class) || s.equals(java.util.Date.class)) setType(eType.date); else if (s.equals(Boolean.class)) setType(eType.bool);
    }

    public String toString() {
        return (mProperties != null ? mProperties.toString() : null);
    }

    private static String toString(eType e) {
        if (e == null) return null; else if (e.equals(eType.auto)) return "auto"; else if (e.equals(eType.string)) return "string"; else if (e.equals(eType.integer)) return "int"; else if (e.equals(eType.floating)) return "float"; else if (e.equals(eType.bool)) return "boolean"; else if (e.equals(eType.date)) return "date";
        return null;
    }
}
