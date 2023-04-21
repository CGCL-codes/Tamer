package com.objectwave.persist.broker;

import com.objectwave.persist.*;
import com.objectwave.persist.sqlConstruction.SQLSelect;
import java.sql.SQLException;

/**
 *  This PrimaryKeyStrategy involves selecting a sequence value
 *  from a table.  The column name selected is constructed with
 *  the following format {0}_uid_seq, where {0} is the table name
 *  of the persistent object that requires a PK.
 *
 * @author  Trever M. Shick with help from dhoag
 * @version  $Id: TableSequence.java,v 2.2 2005/02/13 15:38:33 dave_hoag Exp $
 */
public class TableSequence implements PrimaryKeyStrategy {

    protected String sequenceTableName = "DUAL";

    protected String sequenceColumnSuffix = "_UID_SEQ.NEXTVAL";

    /**
	 *  Bean style mutator to allow client code to alter which
	 *  table that the sequence value is selected from.  If
	 *  <i>newTableName</i> is null or a 0 length string, the
	 *  parameter is ignored and the value is not changed.
	 *
	 * @param  newTableName The new table name from which to select
	 *  the sequence value from.
	 */
    public void setSequenceTableName(String newTableName) {
        if (newTableName == null || newTableName.length() == 0) {
            return;
        } else {
            this.sequenceTableName = newTableName;
        }
    }

    /**
	 *  Bean style mutator to allow client code to alter which
	 *  column that the sequence value is selected from.  If
	 *  <i>newColumnName</i> is null or a 0 length string, the
	 *  parameter is ignored and the value is not changed.
	 *
	 * @param  newSuffix The new SequenceColumnSuffix value
	 */
    public void setSequenceColumnSuffix(String newSuffix) {
        if (newSuffix == null || newSuffix.length() == 0) {
            return;
        } else {
            this.sequenceColumnSuffix = newSuffix;
        }
    }

    /**
	 *  Returns the name of the table which is queried to get the
	 *  next sequence number for a given table.  With Oracle, this
	 *  is generally "DUAL" since sequence values are often accessed
	 *  via performing : SELECT my_seq.nextval FROM DUAL;
	 *
	 * @return  The name of the table from which a sequence value is
	 *  selected.
	 */
    public String getSequenceTableName() {
        return sequenceTableName;
    }

    /**
	 *  Returns the suffix of the column which is queried to get the
	 *  next sequence number for a given table.  With Oracle, this
	 *  is generally "_uid_seq.NEXTVAL" since sequence values are often accessed
	 *  via performing : SELECT my_uid_seq.nextval FROM DUAL; "my" is the
	 *  table name of the persistent object for which a sequence is being
	 *  gathered.
	 *
	 * @return  The name of the table from which a sequence value is
	 *  selected.
	 */
    public String getSequenceColumnSuffix() {
        return sequenceColumnSuffix;
    }

    /**
	 * @param  broker - The current broker being used.
	 * @param  pObj - The peristent object to get a primary key for
	 * @return  an object that represents the next primary key value for
	 *  the given persistent object pObj
	 * @exception  SQLException
	 * @exception  QueryException
	 */
    public Object nextPrimaryKey(final RDBBroker broker, final RDBPersistence pObj) throws SQLException, QueryException {
        String sequenceColumnName = pObj.getTableName(pObj) + sequenceColumnSuffix;
        SQLSelect sql = new SQLSelect(getSequenceTableName());
        sql.addColumnList(sequenceColumnName);
        int nextPk = broker.getConnection().nextPrimaryKey(sql);
        return new Integer(nextPk);
    }
}
