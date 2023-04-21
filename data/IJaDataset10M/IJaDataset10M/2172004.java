package com.dashboard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.dashboard.dto.HistoryDTO;

/**
 * Data access object for the DigitalDashboard database to access history records.
 * @author Joe Rains
 *
 */
public class HistoryProvider extends AbstractDashboardProvider implements IHistoryDAO {

    private static final String SELECT_HISTORY_SQL = "SELECT " + MAX_SPEED_FLD + ", " + DISTANCE_FLD + " FROM " + HISTORY_TBL + " WHERE " + ID_FLD + " = (SELECT MAX(" + ID_FLD + ") FROM " + HISTORY_TBL + ")";

    /**
	 * The constructor for a HistoryProvider requires Context to access the database.
	 * @param context the calling Activity
	 */
    public HistoryProvider(Context context) {
        super(context);
    }

    @Override
    public HistoryDTO getHistory() throws Exception {
        HistoryDTO history = null;
        Cursor c = getReadableDatabase().rawQuery(SELECT_HISTORY_SQL, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            history = new HistoryDTO();
            history.setMaxSpeed(c.getInt(0));
            history.setDistance(c.getInt(1));
        }
        return history;
    }

    @Override
    public void updateHistory(HistoryDTO history) throws Exception {
        if (history == null) throw new NullPointerException("No history object was given to update");
        StringBuilder errorMessage = new StringBuilder();
        if (history.getMaxSpeed() < 0) errorMessage.append("The max speed cannot be a negative number [" + history.getMaxSpeed() + "]");
        if (history.getDistance() < 0) {
            if (errorMessage.length() > 0) errorMessage.append("; ");
            errorMessage.append("The distance traveled cannot be a negative number [" + history.getDistance() + "]");
        }
        if (errorMessage.length() > 0) throw new IllegalArgumentException("Invalid history: " + errorMessage.toString());
        HistoryDTO currentHistory = getHistory();
        history.setDistance(history.getDistance() + currentHistory.getDistance());
        if (history.getMaxSpeed() < currentHistory.getMaxSpeed()) history.setMaxSpeed(currentHistory.getMaxSpeed());
        ContentValues values = new ContentValues(2);
        values.put(MAX_SPEED_FLD, history.getMaxSpeed());
        values.put(DISTANCE_FLD, history.getDistance());
        getWritableDatabase().insert(HISTORY_TBL, DISTANCE_FLD, values);
    }

    @Override
    public void resetHistory() throws Exception {
        ContentValues values = new ContentValues(2);
        values.put(MAX_SPEED_FLD, 0);
        values.put(DISTANCE_FLD, 0);
        getWritableDatabase().insert(HISTORY_TBL, DISTANCE_FLD, values);
    }
}
