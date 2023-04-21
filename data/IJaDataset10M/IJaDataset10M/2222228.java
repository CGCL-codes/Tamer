package com.dashboard.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.dashboard.dto.BackgroundMode;
import com.dashboard.dto.SettingsDTO;
import com.dashboard.dto.TravelMode;
import com.dashboard.dto.Units;

/**
 * Data access object for the DigitalDashboard database to access settings records.
 * @author Joe Rains
 *
 */
public class SettingsProvider extends AbstractDashboardProvider implements ISettingsDAO {

    private static final String SELECT_SETTINGS_SQL = "SELECT " + BACKGROUND_MODE_FLD + ", " + TRAVEL_MODE_FLD + ", " + UNITS_FLD + " FROM " + SETTINGS_TBL + " WHERE " + ID_FLD + " = (SELECT MAX(" + ID_FLD + ") FROM " + SETTINGS_TBL + ")";

    /**
	 * The constructor for a SettingsProvider requires Context to access the database.
	 * @param context the calling Activity
	 */
    public SettingsProvider(Context context) {
        super(context);
    }

    @Override
    public SettingsDTO getSettings() throws Exception {
        SettingsDTO settings = null;
        Cursor c = getReadableDatabase().rawQuery(SELECT_SETTINGS_SQL, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            settings = new SettingsDTO();
            settings.setBackgroundMode(BackgroundMode.valueOf(c.getString(0)));
            settings.setTravelMode(TravelMode.valueOf(c.getString(1)));
            settings.setUnits(Units.valueOf(c.getString(2)));
        }
        return settings != null ? settings : SettingsDTO.getDefaultSettings();
    }

    @Override
    public void updateSettings(SettingsDTO settings) throws Exception {
        if (settings == null) throw new NullPointerException("No settings object was given to update");
        StringBuilder errorMessage = new StringBuilder();
        if (settings.getBackgroundMode() == null) errorMessage.append("No BackgroundMode was given to update");
        if (settings.getTravelMode() == null) errorMessage.append("No TravelMode was given to update");
        if (settings.getUnits() == null) errorMessage.append("No Units were given to update");
        if (errorMessage.length() > 0) throw new IllegalArgumentException("Invalid settings: " + errorMessage.toString());
        ContentValues values = new ContentValues(2);
        values.put(BACKGROUND_MODE_FLD, settings.getBackgroundMode().name());
        values.put(TRAVEL_MODE_FLD, settings.getTravelMode().name());
        values.put(UNITS_FLD, settings.getUnits().name());
        getWritableDatabase().update(SETTINGS_TBL, values, null, null);
    }
}
