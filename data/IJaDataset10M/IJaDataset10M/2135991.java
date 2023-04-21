package medi.db.getter;

import javatools.db.DbException;
import javatools.db.DbIterator;
import javatools.db.DbRow;
import medi.db.AbstractProvider;

/**
 *
 * @author  Antonio Petrelli
 */
public class GenreGetter extends medi.db.getter.AbstractGetter {

    /** Creates a new instance of DataGetter */
    public GenreGetter(AbstractProvider prv) {
        super(prv);
    }

    public String getRepresentationString(Object[] ID) throws DbException {
        DbIterator rowIt;
        DbRow tempRow;
        String tempString;
        rowIt = prv.getOneGenreReduced((Integer) ID[0]).iterator();
        tempString = null;
        if (rowIt.hasNextRow()) {
            tempRow = rowIt.nextRow();
            tempString = (String) tempRow.getValue(0);
        }
        return tempString;
    }
}
