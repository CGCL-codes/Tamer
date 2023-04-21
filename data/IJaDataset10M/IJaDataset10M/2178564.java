package dict.prepare;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Hashtable;
import dict.common.DictEntryRef;
import dict.common.DictType;

/** Base class for importing dictionary files into various output formats.
  * Creates a map of dictionary entry references from the dictionary file.
  * @author Daniel Stoinski
  * @version $Revision$
  */
public abstract class ImportBase implements IDictParserHandler {

    /** Returns characted encoding for the input dictionary file.
    * @param aDictType  dictionary type, one of the constants defined
    *                   in DictType
    * @return encoding for the dictionary type.
    */
    private static String getEncoding(int aDictType) {
        switch(aDictType) {
            case DictType.DICTCC:
                return "windows-1250";
            case DictType.UDDL:
                return "utf-8";
            case DictType.THES:
                return "iso-8859-2";
            default:
                return ImportBase.getEncoding(DictType.DEFAULT);
        }
    }

    /** The map of dictionary references.
    * The keys are keywords from the dictionary, the values are ArrayLists
    * of DictEntryRef objects.
    */
    private final Hashtable m_index;

    /** The parser for the dictionary type.
    */
    private final ParserBase m_parser;

    /** The encoding of the dictionary file.
    * <OL>
    *   <LI>DictType.DICTCC - windows-1250</LI>
    *   <LI>DictType.UDDL - utf-8</LI>
    *   <LI>DictType.THES - iso-8859-2</LI>
    * </OL>
    */
    private final String m_enc;

    /** The type of the dictionary.
    */
    private final int m_type;

    /** Initializes an empty object.
    * @param aType the name of the dictionary type, accroding to
    *              DictType.fromString().
    * @see DictType#fromString(String)
    */
    public ImportBase(String aType) {
        this.m_index = new Hashtable();
        this.m_type = DictType.fromString(aType);
        this.m_enc = ImportBase.getEncoding(this.m_type);
        this.m_parser = ParserBase.getInstance(this, this.m_type);
    }

    /** Adds the given keyword and description to the map.
    * If the map already kontains a key equaling the given index keyword,
    * then the DictEntryRef object created for the given from and to position
    * will be appended to the array of objects for this key. Else a new key
    + is created in the map and the DictEntryRef object is put as the only
    * and first element of the erray for the key.
    * @param index  the index keyword used as a key in the map.
    * @param from   the position of the begin of the keyword explanation
    *               in the dictionary file. Used for instantiating a new
    *               DictEntryRef object.
    * @param to     the position of the end of the keyword explanation
    *               in the dictionary file. Used for instantiating a new
    *               DictEntryRef object.
    * @param fileno index of the dictionary file, for which the index entry
    *               reference has to be created.
    * @return always true, meaning we don't want to break the import process.
    */
    public final boolean addIndex(String index, int fileno, long from, long to) {
        ArrayList al;
        DictEntryRef der;
        Iterator it;
        if (index != null && index.length() != 0) {
            al = (ArrayList) this.m_index.get(index);
            if (al == null) al = new ArrayList();
            al.add(new DictEntryRef(from, to, fileno));
            this.m_index.put(index, al);
        }
        return true;
    }

    /** Parses the given dictionary file.
    * @param aDictionaryFile the of the dictionary file.
    * @param aFileNo         the index of the dictionary file, for which the
    *                        references have to be created.
    * @throws IOException on read errors.
    */
    protected final void parsesingle(String aDictionaryFile, int aFileNo) throws IOException {
        FileInputStream is;
        is = null;
        if (aDictionaryFile != null) {
            try {
                is = new FileInputStream(aDictionaryFile);
                this.m_parser.read(is, aFileNo);
            } finally {
                if (is != null) try {
                    is.close();
                } catch (Exception excp) {
                }
            }
        }
    }

    /** Returns the index created during the import process.
    * The keywords in the returned map are the dictionary kewords. The values
    * are ArrayList of DictEntryRef objects containing references to the
    * explanations of the keywords within the dictionary file.
    * @return the index of keywords.
    */
    public final Hashtable get() {
        return this.m_index;
    }

    /** Returns the dictionary encoding.
    * @return the dictionary encoding.
    */
    protected final String getEnc() {
        return this.m_enc;
    }

    /** Returns the dictionary type.
    * @return the dictionary type.
    */
    protected final int getType() {
        return this.m_type;
    }

    /** Does the whole job.
    * @throws Exception on read errors.
    */
    public abstract void start() throws Exception;
}
