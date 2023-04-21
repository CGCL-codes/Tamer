package com.iver.cit.gvsig.fmap.edition.writers.gdbms;

import java.sql.Types;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.driver.exceptions.WriteDriverException;
import com.hardcode.gdbms.engine.data.edition.DataWare;
import com.hardcode.gdbms.engine.values.Value;
import com.iver.cit.gvsig.exceptions.visitors.ProcessWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StartWriterVisitorException;
import com.iver.cit.gvsig.exceptions.visitors.StopWriterVisitorException;
import com.iver.cit.gvsig.fmap.core.IRow;
import com.iver.cit.gvsig.fmap.drivers.ITableDefinition;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.writers.AbstractWriter;

public class GdbmsWriter extends AbstractWriter {

    DataWare dataWare;

    Value[] record;

    int numRecord;

    public GdbmsWriter() {
    }

    public void initialize(ITableDefinition tableDef) {
    }

    public void setDataWare(DataWare dataWare) {
        this.dataWare = dataWare;
    }

    public void preProcess() throws StartWriterVisitorException {
        try {
            dataWare.beginTrans();
            numRecord = 0;
        } catch (ReadDriverException e) {
            throw new StartWriterVisitorException(getName(), e);
        }
    }

    public void process(IRowEdited editedRow) throws ProcessWriterVisitorException {
        IRow row = editedRow.getLinkedRow();
        try {
            switch(editedRow.getStatus()) {
                case IRowEdited.STATUS_ADDED:
                    record = row.getAttributes();
                    dataWare.insertFilledRow(record);
                    break;
                case IRowEdited.STATUS_MODIFIED:
                    record = row.getAttributes();
                    for (int i = 0; i < record.length; i++) dataWare.setFieldValue(numRecord, i, record[i]);
                    break;
                case IRowEdited.STATUS_ORIGINAL:
                    break;
                case IRowEdited.STATUS_DELETED:
                    dataWare.deleteRow(numRecord);
                    break;
            }
            numRecord++;
        } catch (WriteDriverException e) {
            throw new ProcessWriterVisitorException(getName(), e);
        } catch (ReadDriverException e) {
            throw new ProcessWriterVisitorException(getName(), e);
        }
    }

    public void postProcess() throws StopWriterVisitorException {
        boolean exception = false;
        try {
            dataWare.commitTrans();
        } catch (WriteDriverException e) {
            exception = true;
        } catch (ReadDriverException e) {
            exception = true;
        }
        if (exception) {
            try {
                dataWare.rollBackTrans();
            } catch (WriteDriverException e1) {
            } catch (ReadDriverException e) {
            }
            throw new StopWriterVisitorException(getName(), null);
        }
    }

    public String getName() {
        return "GDBMS Writer";
    }

    public boolean canWriteGeometry(int gvSIGgeometryType) {
        return false;
    }

    public boolean canWriteAttribute(int sqlType) {
        switch(sqlType) {
            case Types.DOUBLE:
            case Types.FLOAT:
            case Types.INTEGER:
            case Types.BIGINT:
                return true;
            case Types.DATE:
                return true;
            case Types.BIT:
            case Types.BOOLEAN:
                return true;
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.LONGVARCHAR:
                return true;
        }
        return false;
    }

    public boolean canAlterTable() {
        return false;
    }

    public boolean canSaveEdits() {
        return false;
    }
}
