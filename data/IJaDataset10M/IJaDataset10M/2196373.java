package net.tourbook.statistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import net.tourbook.data.TourPerson;
import net.tourbook.data.TourType;
import net.tourbook.database.TourDatabase;
import net.tourbook.ui.SQLFilter;
import net.tourbook.ui.TourTypeFilter;
import net.tourbook.ui.UI;
import net.tourbook.util.ArrayListToArray;

public class DataProviderTourTime extends DataProvider {

    private static DataProviderTourTime _instance;

    private ArrayList<Long> _tourIds;

    private Long _selectedTourId;

    private TourDataTime _tourDataTime;

    private DataProviderTourTime() {
    }

    public static DataProviderTourTime getInstance() {
        if (_instance == null) {
            _instance = new DataProviderTourTime();
        }
        return _instance;
    }

    public Long getSelectedTourId() {
        return _selectedTourId;
    }

    /**
	 * Retrieve chart data from the database
	 * 
	 * @param person
	 * @param tourTypeFilter
	 * @param lastYear
	 * @param numberOfYears
	 * @return
	 */
    TourDataTime getTourTimeData(final TourPerson person, final TourTypeFilter tourTypeFilter, final int lastYear, final int numberOfYears, final boolean refreshData) {
        if (_activePerson == person && _activeTourTypeFilter == tourTypeFilter && _lastYear == lastYear && _numberOfYears == numberOfYears && refreshData == false) {
            return _tourDataTime;
        }
        _activePerson = person;
        _activeTourTypeFilter = tourTypeFilter;
        _lastYear = lastYear;
        _numberOfYears = numberOfYears;
        initYearNumbers();
        int colorOffset = 0;
        if (tourTypeFilter.showUndefinedTourTypes()) {
            colorOffset = StatisticServices.TOUR_TYPE_COLOR_INDEX_OFFSET;
        }
        final ArrayList<TourType> tourTypeList = TourDatabase.getActiveTourTypes();
        final TourType[] tourTypes = tourTypeList.toArray(new TourType[tourTypeList.size()]);
        final SQLFilter sqlFilter = new SQLFilter();
        final String sqlString = "SELECT " + "TourId," + "StartYear," + "StartMonth," + "StartDay," + "StartHour," + "StartMinute," + "TourDistance," + "TourAltUp," + "TourRecordingTime," + "TourDrivingTime," + "TourTitle," + "TourType_typeId," + "TourDescription," + "startWeek," + "jTdataTtag.TourTag_tagId" + UI.NEW_LINE + (" FROM " + TourDatabase.TABLE_TOUR_DATA + UI.NEW_LINE) + (" LEFT OUTER JOIN " + TourDatabase.JOINTABLE_TOURDATA__TOURTAG + " jTdataTtag") + (" ON tourID = jTdataTtag.TourData_tourId") + (" WHERE StartYear IN (" + getYearList(lastYear, numberOfYears) + ")" + UI.NEW_LINE) + sqlFilter.getWhereClause() + (" ORDER BY StartYear, StartMonth, StartDay, StartHour, StartMinute");
        try {
            final ArrayList<String> dbTourTitle = new ArrayList<String>();
            final ArrayList<String> dbTourDescription = new ArrayList<String>();
            final ArrayList<Integer> dbTourYear = new ArrayList<Integer>();
            final ArrayList<Integer> dbTourMonths = new ArrayList<Integer>();
            final ArrayList<Integer> dbAllYearsDOY = new ArrayList<Integer>();
            final ArrayList<Integer> dbTourStartTime = new ArrayList<Integer>();
            final ArrayList<Integer> dbTourEndTime = new ArrayList<Integer>();
            final ArrayList<Integer> dbTourStartWeek = new ArrayList<Integer>();
            final ArrayList<Integer> dbDistance = new ArrayList<Integer>();
            final ArrayList<Integer> dbAltitude = new ArrayList<Integer>();
            final ArrayList<Integer> dbTourRecordingTime = new ArrayList<Integer>();
            final ArrayList<Integer> dbTourDrivingTime = new ArrayList<Integer>();
            final ArrayList<Long> dbTypeIds = new ArrayList<Long>();
            final ArrayList<Integer> dbTypeColorIndex = new ArrayList<Integer>();
            _tourIds = new ArrayList<Long>();
            final HashMap<Long, ArrayList<Long>> dbTagIds = new HashMap<Long, ArrayList<Long>>();
            long lastTourId = -1;
            ArrayList<Long> tagIds = null;
            final Connection conn = TourDatabase.getInstance().getConnection();
            final PreparedStatement statement = conn.prepareStatement(sqlString);
            sqlFilter.setParameters(statement, 1);
            final ResultSet result = statement.executeQuery();
            while (result.next()) {
                final long tourId = result.getLong(1);
                final Object dbTagId = result.getObject(15);
                if (tourId == lastTourId) {
                    if (dbTagId instanceof Long) {
                        tagIds.add((Long) dbTagId);
                    }
                } else {
                    _tourIds.add(tourId);
                    final int tourYear = result.getShort(2);
                    final int tourMonth = result.getShort(3) - 1;
                    final int startHour = result.getShort(5);
                    final int startMinute = result.getShort(6);
                    final int startTime = startHour * 3600 + startMinute * 60;
                    final int recordingTime = result.getInt(9);
                    _calendar.set(tourYear, tourMonth, result.getShort(4), startHour, startMinute);
                    final int tourDOY = _calendar.get(Calendar.DAY_OF_YEAR) - 1;
                    dbTourYear.add(tourYear);
                    dbTourMonths.add(tourMonth);
                    dbAllYearsDOY.add(getYearDOYs(tourYear) + tourDOY);
                    dbTourStartTime.add(startTime);
                    dbTourEndTime.add((startTime + recordingTime));
                    dbDistance.add((int) (result.getInt(7) / UI.UNIT_VALUE_DISTANCE));
                    dbAltitude.add((int) (result.getInt(8) / UI.UNIT_VALUE_ALTITUDE));
                    dbTourRecordingTime.add(recordingTime);
                    dbTourDrivingTime.add(result.getInt(10));
                    dbTourTitle.add(result.getString(11));
                    final String description = result.getString(13);
                    dbTourDescription.add(description == null ? UI.EMPTY_STRING : description);
                    dbTourStartWeek.add(result.getInt(14));
                    if (dbTagId instanceof Long) {
                        tagIds = new ArrayList<Long>();
                        tagIds.add((Long) dbTagId);
                        dbTagIds.put(tourId, tagIds);
                    }
                    int tourTypeColorIndex = 0;
                    final Long dbTypeIdObject = (Long) result.getObject(12);
                    if (dbTypeIdObject != null) {
                        final long dbTypeId = result.getLong(12);
                        for (int typeIndex = 0; typeIndex < tourTypes.length; typeIndex++) {
                            if (tourTypes[typeIndex].getTypeId() == dbTypeId) {
                                tourTypeColorIndex = colorOffset + typeIndex;
                                break;
                            }
                        }
                    }
                    dbTypeColorIndex.add(tourTypeColorIndex);
                    dbTypeIds.add(dbTypeIdObject == null ? TourDatabase.ENTITY_IS_NOT_SAVED : dbTypeIdObject);
                }
                lastTourId = tourId;
            }
            conn.close();
            int yearDays = 0;
            for (final int doy : _yearDays) {
                yearDays += doy;
            }
            _tourDataTime = new TourDataTime();
            _tourDataTime.tourIds = ArrayListToArray.toLong(_tourIds);
            _tourDataTime.typeIds = ArrayListToArray.toLong(dbTypeIds);
            _tourDataTime.typeColorIndex = ArrayListToArray.toInt(dbTypeColorIndex);
            _tourDataTime.tagIds = dbTagIds;
            _tourDataTime.allDaysInAllYears = yearDays;
            _tourDataTime.yearDays = _yearDays;
            _tourDataTime.years = _years;
            _tourDataTime.tourYearValues = ArrayListToArray.toInt(dbTourYear);
            _tourDataTime.tourMonthValues = ArrayListToArray.toInt(dbTourMonths);
            _tourDataTime.tourDOYValues = ArrayListToArray.toInt(dbAllYearsDOY);
            _tourDataTime.weekValues = ArrayListToArray.toInt(dbTourStartWeek);
            _tourDataTime.tourTimeStartValues = ArrayListToArray.toInt(dbTourStartTime);
            _tourDataTime.tourTimeEndValues = ArrayListToArray.toInt(dbTourEndTime);
            _tourDataTime.tourDistanceValues = ArrayListToArray.toInt(dbDistance);
            _tourDataTime.tourAltitudeValues = ArrayListToArray.toInt(dbAltitude);
            _tourDataTime.tourRecordingTimeValues = dbTourRecordingTime;
            _tourDataTime.tourDrivingTimeValues = dbTourDrivingTime;
            _tourDataTime.tourTitle = dbTourTitle;
            _tourDataTime.tourDescription = dbTourDescription;
        } catch (final SQLException e) {
            UI.showSQLException(e);
        }
        return _tourDataTime;
    }

    void setSelectedTourId(final Long selectedTourId) {
        _selectedTourId = selectedTourId;
    }
}
