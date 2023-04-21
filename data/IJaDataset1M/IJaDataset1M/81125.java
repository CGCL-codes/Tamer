package org.opengpx.lib.geocache.helpers;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.text.format.DateFormat;
import com.db4o.ObjectSet;
import org.opengpx.lib.CacheDatabase;
import org.opengpx.lib.geocache.FieldNote;

/**
 * 
 * @author Martin Preishuber
 *
 */
public class FieldNoteList {

    private CacheDatabase mCacheDatabase = CacheDatabase.getInstance();

    /**
	 * 
	 */
    public FieldNoteList() {
        if (!this.mCacheDatabase.isFieldNoteDatabaseOpen()) this.mCacheDatabase.openFieldNoteDatabase();
    }

    /**
	 * 
	 * @param logType
	 * @return
	 */
    public ArrayList<String> getCacheCodes(final FieldNote.LogType logType) {
        final List<FieldNote> fieldNotes = this.mCacheDatabase.getFieldNotes(false, logType);
        final ArrayList<String> cacheCodes = new ArrayList<String>();
        for (final FieldNote fieldNote : fieldNotes) {
            cacheCodes.add(fieldNote.gcId);
        }
        return cacheCodes;
    }

    /**
	 * 
	 * @param fieldNotes
	 * @return
	 */
    public static int getDistinctiveDateCount(final Context context, final ObjectSet<FieldNote> fieldNotes) {
        final ArrayList<String> datesFound = new ArrayList<String>();
        final java.text.DateFormat dateFormat = DateFormat.getDateFormat(context);
        for (FieldNote fieldNote : fieldNotes) {
            final String dateString = dateFormat.format(fieldNote.noteTime);
            if (!datesFound.contains(dateString)) datesFound.add(dateString);
        }
        return datesFound.size();
    }
}
