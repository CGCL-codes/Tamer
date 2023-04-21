package creiter.gpxTcxWelder.gui.models;

import creiter.gpxTcxWelder.data.TcxFile;
import creiter.gpxTcxWelder.data.xml.tcx.PositionT;
import creiter.gpxTcxWelder.data.xml.tcx.TrackpointT;
import javax.swing.table.AbstractTableModel;
import javax.xml.datatype.XMLGregorianCalendar;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 *
 * @author christian
 */
public class TcxFileTableModel extends AbstractTableModel {

    private TcxFile tcxFile;

    private DateTimeZone timeZone;

    public static final int COL_TIME = 0;

    public static final int COL_DISTANCE = 1;

    public static final int COL_HEARTRATE = 2;

    public static final int COL_POSITION_LATITUDE = 3;

    public static final int COL_POSITION_LONGITUDE = 4;

    public static final int COL_POSITION_ALTITUDE = 5;

    public TcxFileTableModel(TcxFile tcxFile, DateTimeZone timeZone) {
        this.tcxFile = tcxFile;
        this.timeZone = timeZone;
    }

    public DateTimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(DateTimeZone timeZone) {
        this.timeZone = timeZone;
        for (int i = 0; i < getRowCount(); i++) {
            fireTableCellUpdated(i, COL_TIME);
        }
    }

    public int getRowCount() {
        return tcxFile.getTrackpointCount();
    }

    public int getColumnCount() {
        return 6;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case COL_TIME:
                return getTrackpointDate(rowIndex);
            case COL_HEARTRATE:
                return getTrackpointHeartrate(rowIndex);
            case COL_DISTANCE:
                return getTrackpointDistance(rowIndex);
            case COL_POSITION_LATITUDE:
                return getTrackpointLatitude(rowIndex);
            case COL_POSITION_LONGITUDE:
                return getTrackpointLongitude(rowIndex);
            case COL_POSITION_ALTITUDE:
                return getTrackpointAltitude(rowIndex);
            default:
                return "1101291157 - No data for column " + columnIndex;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case COL_TIME:
                return DateTime.class;
            case COL_HEARTRATE:
                return Integer.class;
            case COL_DISTANCE:
                return Double.class;
            case COL_POSITION_LATITUDE:
                return Double.class;
            case COL_POSITION_LONGITUDE:
                return Double.class;
            case COL_POSITION_ALTITUDE:
                return Double.class;
            default:
                return Object.class;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case COL_TIME:
                return java.util.ResourceBundle.getBundle("creiter/gpxTcxWelder/gui/models/TcxFileTableModel").getString("ColumnNames.TIME");
            case COL_HEARTRATE:
                return java.util.ResourceBundle.getBundle("creiter/gpxTcxWelder/gui/models/TcxFileTableModel").getString("ColumnNames.HEARTRATE");
            case COL_DISTANCE:
                return java.util.ResourceBundle.getBundle("creiter/gpxTcxWelder/gui/models/TcxFileTableModel").getString("ColumnNames.DISTANCE");
            case COL_POSITION_LATITUDE:
                return java.util.ResourceBundle.getBundle("creiter/gpxTcxWelder/gui/models/TcxFileTableModel").getString("ColumnNames.LATITUDE");
            case COL_POSITION_LONGITUDE:
                return java.util.ResourceBundle.getBundle("creiter/gpxTcxWelder/gui/models/TcxFileTableModel").getString("ColumnNames.LONGITUDE");
            case COL_POSITION_ALTITUDE:
                return java.util.ResourceBundle.getBundle("creiter/gpxTcxWelder/gui/models/TcxFileTableModel").getString("ColumnNames.ALTITUDE");
            default:
                return "1101291148 - Unknown column name for column with index " + columnIndex;
        }
    }

    private TrackpointT getTrackpoint(int rowIndex) {
        return tcxFile.getTrackpoints().get(rowIndex);
    }

    private DateTime getTrackpointDate(int rowIndex) {
        TrackpointT tp = getTrackpoint(rowIndex);
        if (tp.getTime() == null) {
            return null;
        }
        XMLGregorianCalendar xmlCalendarUtc = tp.getTime().normalize();
        DateTime timeUtc = new DateTime(xmlCalendarUtc.getYear(), xmlCalendarUtc.getMonth(), xmlCalendarUtc.getDay(), xmlCalendarUtc.getHour(), xmlCalendarUtc.getMinute(), xmlCalendarUtc.getSecond(), xmlCalendarUtc.getMillisecond(), DateTimeZone.UTC);
        return new DateTime(timeUtc, timeZone);
    }

    private Integer getTrackpointHeartrate(int rowIndex) {
        TrackpointT tp = getTrackpoint(rowIndex);
        if (tp.getHeartRateBpm() != null) {
            return Integer.valueOf(tp.getHeartRateBpm().getValue());
        } else {
            return null;
        }
    }

    private Double getTrackpointDistance(int rowIndex) {
        TrackpointT tp = getTrackpoint(rowIndex);
        if (tp.getDistanceMeters() != null) {
            return tp.getDistanceMeters();
        } else {
            return null;
        }
    }

    private Double getTrackpointLatitude(int rowIndex) {
        TrackpointT tp = getTrackpoint(rowIndex);
        PositionT position = tp.getPosition();
        if (position != null) {
            return Double.valueOf(position.getLatitudeDegrees());
        } else {
            return null;
        }
    }

    private Double getTrackpointLongitude(int rowIndex) {
        TrackpointT tp = getTrackpoint(rowIndex);
        PositionT position = tp.getPosition();
        if (position != null) {
            return Double.valueOf(position.getLongitudeDegrees());
        } else {
            return null;
        }
    }

    private Double getTrackpointAltitude(int rowIndex) {
        TrackpointT tp = getTrackpoint(rowIndex);
        return tp.getAltitudeMeters();
    }

    public void refreshData() {
        fireTableDataChanged();
    }
}
