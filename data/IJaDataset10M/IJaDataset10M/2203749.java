package self.micromagic.app;

import java.util.List;
import java.util.LinkedList;
import self.micromagic.eterna.sql.ResultIterator;
import self.micromagic.eterna.sql.ResultRow;
import self.micromagic.eterna.sql.ResultMetaData;
import self.micromagic.eterna.sql.ResultReaderManager;
import self.micromagic.eterna.sql.impl.ResultMetaDataImpl;
import self.micromagic.eterna.sql.impl.AbstractResultIterator;
import self.micromagic.eterna.sql.impl.ResultRowImpl;
import self.micromagic.eterna.digester.ConfigurationException;
import self.micromagic.eterna.security.Permission;

/**
 * @deprecated
 * @see self.micromagic.util.CustomResultIterator
 */
public class CustomResultIterator extends AbstractResultIterator implements ResultIterator {

    private int recordCount = 0;

    private ResultReaderManager rrm;

    private Permission permission;

    /**
    * @deprecated
    * @see #CustomResultIterator(ResultReaderManager, Permission)
    */
    public CustomResultIterator(List readerList) {
        super(readerList);
        this.result = new LinkedList();
    }

    public CustomResultIterator(ResultReaderManager rrm, Permission permission) throws ConfigurationException {
        super(rrm.getReaderList());
        this.rrm = rrm;
        this.permission = permission;
        this.result = new LinkedList();
    }

    public ResultRow createRow(Object[] values) throws ConfigurationException {
        return this.createRow(values, true);
    }

    public ResultRow createRow(Object[] values, boolean autoAdd) throws ConfigurationException {
        if (this.rrm == null) {
            throw new ConfigurationException("Must use [CustomResultIterator(ResultReaderManager, Permission)] " + "to constructor this object.");
        }
        ResultRow row = new ResultRowImpl(values, this, this.rrm, this.permission);
        if (autoAdd) {
            this.result.add(row);
        }
        return row;
    }

    /**
    * @deprecated
    * @see #createRow(Object[])
    */
    public void addRow(ResultRow row) {
        this.result.add(row);
    }

    public void addedOver() {
        this.resultItr = this.result.iterator();
        this.recordCount = this.result.size();
    }

    public ResultMetaData getMetaData() {
        return new ResultMetaDataImpl(this.readerList, null);
    }

    public int getRealRecordCount() {
        return this.recordCount;
    }

    public int getRecordCount() {
        return this.recordCount;
    }

    public boolean isRealRecordCountAvailable() {
        return true;
    }

    public boolean isHasMoreRecord() {
        return false;
    }
}
