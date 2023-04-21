package com.daffodilwoods.daffodildb.server.sql99.fulltext.dml;

import com.daffodilwoods.fulltext.common._FullTextDML;
import com.daffodilwoods.fulltext.common._FullTextDocumentParser;
import com.daffodilwoods.fulltext.common._FullTextModifications;
import com.daffodilwoods.fulltext.common._FullTextAdapter;
import com.daffodilwoods.database.resource.DException;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces._DatabaseUser;
import com.daffodilwoods.daffodildb.utils.field.FieldBase;
import com.daffodilwoods.daffodildb.utils.FieldUtility;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.Datatype;
import com.daffodilwoods.fulltext.common._Tokenizer;
import java.util.TreeSet;
import java.util.TreeMap;
import com.daffodilwoods.fulltext.common._Token;
import java.util.Iterator;
import com.daffodilwoods.database.utility.*;

/**
 * <p>Title: DaffodilFullTextDML </p>
 * <p>Description: This Class acts as a interface between updation of actual
 * full-text indexed enabled table and corresponding updation on the internal
 * tables required for full-text search.</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class DaffodilFullTextDML implements _FullTextDML {

    /**
   * Reference variable for getting tokenizer
   */
    private _FullTextDocumentParser documentParser;

    /**
   * Dml object corresponding to TOken table
   */
    private _FullTextModifications tokenModification;

    /**
   * Dml Object corresponding to Location Table.
   */
    private _FullTextModifications locationModification;

    /**
   * Index of Full-text enabled column in table
   */
    private int[] columnIndex;

    /**
   * variable to retrive objects used in full-text process.
   */
    private _FullTextIndexInformation fullTextIndexInformation;

    public DaffodilFullTextDML(_FullTextDocumentParser documentParser0, _FullTextAdapter adapter) throws DException {
        documentParser = documentParser0;
        tokenModification = adapter.getTokenTable();
        locationModification = adapter.getLocationTable();
        columnIndex = adapter.getColumnIndex();
    }

    /**
   * Inserts a value of full-text enabled column into token and location
   * tables for a particular values passed.
   * For a particular word i.e. term is added into token table only once for
   * same document. For mutiple terms in the same document, only the enteries
   * are made into location table with exact location of word in the given
   * parsed string. Tokens are hashed using the comparator. For
   * token table insertion last rowid of token table is retrived to insert next
   * succesive rowid into pk column of token table.
   * @param user name of the user for which insert is made
   * @param columnValue parsed string from which tokens to be extracted.
   * @param pk document id i.e the rowid of the full-text enabled table
   * @throws DException
   */
    public void insert(_DatabaseUser user, Object columnValue, Object pk) throws DException {
        FieldBase[] objs = (FieldBase[]) columnValue;
        TreeMap map = new TreeMap(new Comparator());
        long startlocation = 0;
        for (int i = 0; i < objs.length; i++) {
            if (!objs[i].isNull()) {
                _Tokenizer tokenizer = documentParser.getTokenizer(objs[i], startlocation);
                _Token token = null;
                long[] loc;
                while (tokenizer.hasMoreToken()) {
                    token = tokenizer.nextToken();
                    _Token tkn = (_Token) map.get(token);
                    if (tkn == null) {
                        map.put(token, token);
                    } else {
                        loc = token.getLocation();
                        tkn.addLocation(loc);
                    }
                }
                if (token != null) {
                    loc = token.getLocation();
                    startlocation = loc[loc.length - 1] + 1;
                }
            }
        }
        Iterator iter = map.keySet().iterator();
        while (iter.hasNext()) {
            _Token token = (_Token) iter.next();
            Object lastPK = getLastPK();
            tokenModification.insert(user, new Object[] { lastPK, token.getTerm(), pk });
            long[] locations = token.getLocation();
            for (int j = 0; j < locations.length; j++) {
                locationModification.insert(user, new Object[] { lastPK, FieldUtility.getField(Datatype.LONG, new Long(locations[j])), pk });
            }
        }
    }

    /**
   * This method is specifically required for TOKEN table.
   * This method finds out the last row-id of the token table.
   * and calculates the rowId (i.e. lastrowid+1) to be used as PK for token
   * table.
   * @return pk value to be inserted into token table
   * @throws DException
   */
    private synchronized Object getLastPK() throws DException {
        Object lastPK = tokenModification.getLastPK();
        Long result = new Long(((Long) ((FieldBase) lastPK).getObject()).longValue() + 1);
        return FieldUtility.getField(Datatype.LONG, result);
    }

    /**
   * Updation = Deletion of old version of record to be updated
   *          + Insertion of record with new version of record
   * @param user
   * @param oldColumnValue
   * @param newColumnValue
   * @param pk
   * @throws DException
   */
    public void update(_DatabaseUser user, Object oldColumnValue, Object newColumnValue, Object pk) throws DException {
        Object[] newValues = (Object[]) newColumnValue;
        Object[] oldValues = (Object[]) oldColumnValue;
        boolean isSame = true;
        for (int i = 0; i < newValues.length; i++) {
            if (!newValues[i].equals(oldValues[i])) {
                isSame = false;
                break;
            }
        }
        if (isSame) return;
        delete(user, pk);
        insert(user, newColumnValue, pk);
    }

    /**
   * Deletes the records from the token and location table
   * for the document-id passed as argument.
   * @param user
   * @param pk
   * @throws DException
   */
    public void delete(_DatabaseUser user, Object pk) throws DException {
        tokenModification.delete(user, pk);
        locationModification.delete(user, pk);
    }

    /**
   * Determines the full-text enabled column Index
   * @return
   */
    public int[] getColumnIndex() {
        return this.columnIndex;
    }

    /**
   * Dml Object stores one instance of index informations
   * @param fullTextIndexInformation0
   */
    public void setFullTextIndexInformation(_FullTextIndexInformation fullTextIndexInformation0) {
        this.fullTextIndexInformation = fullTextIndexInformation0;
    }

    /**
   * Determines the indexes information creted for token table/location table
   * @return
   */
    public _FullTextIndexInformation getFullTextIndexInformation() {
        return this.fullTextIndexInformation;
    }
}
