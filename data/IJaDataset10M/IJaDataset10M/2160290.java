package net.sf.RecordEditor.re.util;

import net.sf.JRecord.External.CopybookLoader;
import net.sf.JRecord.External.ExternalRecord;
import net.sf.JRecord.External.XmlCopybookLoader;
import net.sf.RecordEditor.re.db.Record.ExtendedRecordDB;
import net.sf.RecordEditor.re.db.Record.RecordRec;
import net.sf.RecordEditor.utils.common.ReConnection;
import net.sf.RecordEditor.utils.jdbc.AbsDB;
import net.sf.RecordEditor.utils.jdbc.AbsRecord;

/**
 * This class will insert a XML Copybook into the Record Editor DB
 *
 * @author Bruce Martin
 */
public class XmlCopybookLoaderDB extends XmlCopybookLoader {

    private int lastDBidx = -1;

    private ExtendedRecordDB recordDB = null;

    /**
     * Allocate the Database Interfaces
     *
     * @param dbIdx Database index
     */
    @Override
    protected void allocDBs(final int dbIdx) {
        if (lastDBidx != dbIdx || recordDB == null) {
            this.recordDB = new ExtendedRecordDB();
            this.recordDB.setDoFree(false);
            this.recordDB.setConnection(new ReConnection(dbIdx));
        } else if (lastDBidx != dbIdx) {
            this.recordDB.fullClose();
            this.recordDB.setDoFree(true);
            this.recordDB.setDoFree(false);
            this.recordDB.setConnection(new ReConnection(dbIdx));
        }
        lastDBidx = dbIdx;
    }

    protected void freeDBs(int pDbIdx) {
        if (recordDB != null) {
            recordDB.close();
            recordDB.setDoFree(true);
            recordDB.setDoFree(false);
        }
    }

    /**
     * Update the record details. This method will read current
     * DB details (if there are any) and apply standard updates to
     * the record
     *
     * @param copyBook Copybook name
     * @param rec Record Details
     * @param updateRequired Wether a update of existing record is required
      */
    protected void updateRecord(String copyBook, ExternalRecord rec, boolean updateRequired) {
        RecordRec oldRec;
        recordDB.resetSearch();
        recordDB.setSearchArg("COPYBOOK", AbsDB.opEquals, "'" + copyBook + "'");
        recordDB.open();
        if ((oldRec = recordDB.fetch()) != null) {
            ExternalRecord oldValues = oldRec.getValue();
            rec.setRecordId(oldValues.getRecordId());
            rec.setNew(false);
            if (updateRequired) {
                rec.setUpdateStatus(AbsRecord.UPDATED);
            } else {
                String oldFont = oldValues.getFontName();
                if (oldFont == null) {
                    oldFont = "";
                }
                if (!getFontName().toUpperCase().equals(oldFont.toUpperCase())) {
                    oldValues.setFontName(getFontName());
                    rec = oldValues;
                    rec.setUpdateStatus(AbsRecord.UPDATED);
                }
            }
        } else {
            super.updateRecord(copyBook, rec, updateRequired);
        }
        recordDB.freeConnection();
    }
}
