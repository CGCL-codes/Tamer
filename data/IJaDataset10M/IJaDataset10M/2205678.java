package org.apache.poi.hssf.eventusermodel;

import java.util.Vector;
import org.apache.poi.hssf.record.ContinueRecord;
import org.apache.poi.hssf.record.DrawingGroupRecord;
import org.apache.poi.hssf.record.DrawingRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RecordFactory;
import org.apache.poi.hssf.record.RecordFormatException;
import org.apache.poi.hssf.record.RecordInputStream;
import org.apache.poi.hssf.record.TextObjectRecord;
import org.apache.poi.hssf.record.UnknownRecord;

/**
 * A stream based way to get at complete records, with
 *  as low a memory footprint as possible.
 * This handles reading from a RecordInputStream, turning
 *  the data into full records, processing continue records
 *  etc.
 * Most users should use {@link HSSFEventFactory} /
 *  {@link HSSFListener} and have new records pushed to
 *  them, but this does allow for a "pull" style of coding.  
 */
public class HSSFRecordStream {

    private RecordInputStream in;

    /** Have we run out of records on the stream? */
    private boolean hitEOS = false;

    /** Have we returned all the records there are? */
    private boolean complete = false;

    /**
	 * Sometimes we end up with a bunch of
	 *  records. When we do, these should
	 *  be returned before the next normal
	 *  record processing occurs (i.e. before
	 *  we check for continue records and
	 *  return rec)
	 */
    private Vector bonusRecords = null;

    /** 
	 * The next record to return, which may need to have its
	 *  continue records passed to it before we do
	 */
    private Record rec = null;

    /**
	 * The most recent record that we gave to the user
	 */
    private Record lastRec = null;

    /**
	 * The most recent DrawingRecord seen
	 */
    private DrawingRecord lastDrawingRecord = new DrawingRecord();

    public HSSFRecordStream(RecordInputStream inp) {
        this.in = inp;
    }

    /**
	 * Returns the next (complete) record from the 
	 *  stream, or null if there are no more.
	 */
    public Record nextRecord() {
        Record r = null;
        while (r == null && !complete) {
            r = getBonusRecord();
            if (r == null) {
                r = getNextRecord();
            }
        }
        return r;
    }

    /**
	 * If there are any "bonus" records, that should
	 *  be returned before processing new ones, 
	 *  grabs the next and returns it.
	 * If not, returns null;
	 */
    private Record getBonusRecord() {
        if (bonusRecords != null) {
            Record r = (Record) bonusRecords.remove(0);
            if (bonusRecords.size() == 0) {
                bonusRecords = null;
            }
            return r;
        }
        return null;
    }

    /**
	 * Returns the next available record, or null if
	 *  this pass didn't return a record that's
	 *  suitable for returning (eg was a continue record).
	 */
    private Record getNextRecord() {
        Record toReturn = null;
        if (in.hasNextRecord()) {
            in.nextRecord();
            short sid = in.getSid();
            if (sid == 0) return null;
            if ((rec != null) && (sid != ContinueRecord.sid)) {
                toReturn = rec;
            }
            if (sid != ContinueRecord.sid) {
                Record[] recs = RecordFactory.createRecord(in);
                if (recs.length > 1) {
                    bonusRecords = new Vector(recs.length - 1);
                    for (int k = 0; k < (recs.length - 1); k++) {
                        bonusRecords.add(recs[k]);
                    }
                }
                rec = recs[recs.length - 1];
            } else {
                Record[] recs = RecordFactory.createRecord(in);
                ContinueRecord crec = (ContinueRecord) recs[0];
                if ((lastRec instanceof ObjRecord) || (lastRec instanceof TextObjectRecord)) {
                    lastDrawingRecord.processContinueRecord(crec.getData());
                    rec = lastDrawingRecord;
                } else if ((lastRec instanceof DrawingGroupRecord)) {
                    ((DrawingGroupRecord) lastRec).processContinueRecord(crec.getData());
                    rec = lastRec;
                } else {
                    if (rec instanceof UnknownRecord) {
                        ;
                    } else {
                        throw new RecordFormatException("Records should handle ContinueRecord internally. Should not see this exception");
                    }
                }
            }
            lastRec = rec;
            if (rec instanceof DrawingRecord) {
                lastDrawingRecord = (DrawingRecord) rec;
            }
        } else {
            hitEOS = true;
        }
        if (hitEOS) {
            complete = true;
            if (rec != null) {
                toReturn = rec;
                rec = null;
            }
        }
        return toReturn;
    }
}
