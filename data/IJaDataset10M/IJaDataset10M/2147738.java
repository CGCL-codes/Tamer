package wizworld.navigate.stream;

import java.io.File;
import java.io.IOException;
import wizworld.navigate.database.*;
import wizworld.navigate.geo.*;
import wizworld.navigate.port.PortTable;
import wizworld.util.tableModel.TableModel;

/** Read tidal stream object from table
 * @author (c) Stephen Denham 2002
 * @version 0.1
 * @version 5.0.1 SD Increase maximum distance to stream to 20 miles.
 */
public final class TidalStreamTable extends TableModel {

    /** Number of tidal stream table attributes: port, position, 13 x set/drift, description */
    public static final int TIDAL_STREAM_TABLE_ATTRIBUTES = 16;

    /** Table column */
    public static final int TIDAL_STREAM_PORT_COLUMN = 0;

    /** Table column */
    public static final int TIDAL_STREAM_POSITION_COLUMN = 1;

    /** Table column */
    public static final int TIDAL_STREAM_SET_DRIFT_COLUMN = 2;

    /** Table column */
    public static final int TIDAL_STREAM_DESCRIPTION_COLUMN = 15;

    /** Number of set and drift entries HW-6..HW..HW+6 */
    public static final int SET_AND_DRIFT_ENTRIES = TIDAL_STREAM_DESCRIPTION_COLUMN - TIDAL_STREAM_SET_DRIFT_COLUMN;

    /** Constructor
   * @param	columnHeadings	Column heading labels
   */
    public TidalStreamTable(String[] columnHeadings) {
        super(columnHeadings);
        this.setKeys();
    }

    /** Constructor
   */
    public TidalStreamTable() {
        super();
        String columnHeadings[] = new String[TIDAL_STREAM_TABLE_ATTRIBUTES];
        for (int i = 0; i < columnHeadings.length; i++) {
            columnHeadings[i] = "";
        }
        this.setColumns(columnHeadings);
        this.setKeys();
    }

    public TidalStreamRecord[] getTidalStream(Cartesian position) throws RecordNotFoundException, TidalStreamException {
        final int RECORD_COUNT = 2;
        TidalStreamRecord tidalStreamRecord[] = new TidalStreamRecord[RECORD_COUNT];
        tidalStreamRecord[0] = new TidalStreamRecord();
        tidalStreamRecord[1] = new TidalStreamRecord();
        Object found[][] = new Object[RECORD_COUNT][TIDAL_STREAM_TABLE_ATTRIBUTES];
        found[0] = new Object[TIDAL_STREAM_TABLE_ATTRIBUTES];
        found[1] = new Object[TIDAL_STREAM_TABLE_ATTRIBUTES];
        Object row[] = null;
        final short MAX_STREAM_DISTANCE = 20;
        double maximum = MAX_STREAM_DISTANCE;
        double secondMaximum = maximum;
        Cartesian streamPos = null;
        double range = 0;
        for (int i = 0; i < this.getRowCount(); i++) {
            row = this.getRow(i);
            streamPos = (Cartesian) row[TIDAL_STREAM_POSITION_COLUMN];
            try {
                range = position.toPolar(streamPos).getRange();
            } catch (AngleException e) {
            } catch (RangeException e) {
                throw new TidalStreamException(position.toString() + ": " + e.getMessage());
            }
            if (range <= maximum) {
                if (found[0][0] != null) {
                    for (int j = 0; j < found[0].length; j++) {
                        found[1][j] = found[0][j];
                    }
                    secondMaximum = range;
                }
                for (int j = 0; j < row.length; j++) {
                    found[0][j] = row[j];
                }
                maximum = range;
            } else if (range <= secondMaximum) {
                for (int j = 0; j < row.length; j++) {
                    found[1][j] = row[j];
                }
                secondMaximum = range;
            }
        }
        if (found[0][0] != null) {
            if (found[1][0] == null) {
                for (int j = 0; j < found[0].length; j++) {
                    found[1][j] = found[0][j];
                }
            }
            for (int i = 0; i < RECORD_COUNT; i++) {
                SetDriftAttribute sd = null;
                double setDrift[][] = new double[SET_AND_DRIFT_ENTRIES][SetDriftAttribute.SET_DRIFT_VALUES];
                tidalStreamRecord[i].setPortName((String) (found[i][TIDAL_STREAM_PORT_COLUMN]));
                tidalStreamRecord[i].setPosition((Cartesian) (found[i][TIDAL_STREAM_POSITION_COLUMN]));
                try {
                    for (int n = 0; n < SET_AND_DRIFT_ENTRIES; n++) {
                        sd = (SetDriftAttribute) found[i][TIDAL_STREAM_SET_DRIFT_COLUMN + n];
                        for (int j = 0; j < SetDriftAttribute.SET_DRIFT_VALUES; j++) {
                            setDrift[n][j] = sd.getSetDriftAttribute(j);
                        }
                    }
                } catch (SetDriftAttributeException e) {
                    throw new TidalStreamException(e.getMessage());
                }
                tidalStreamRecord[i].setSetDrift(setDrift);
            }
        } else {
            throw new RecordNotFoundException(Integer.toString(Tables.TIDAL_STREAMS) + ": " + position.toString());
        }
        return tidalStreamRecord;
    }

    /** Translate tidal stream record object to table row
   * @param	tidalStreamRecord	Tidal stream record object
   * @return	Waypoint table row
   * @exception SetDriftAttributeException if invalid set and drift attribute
   */
    public Object[] toRow(TidalStreamRecord tidalStreamRecord) throws SetDriftAttributeException {
        Object row[] = new Object[TIDAL_STREAM_TABLE_ATTRIBUTES];
        row[TIDAL_STREAM_PORT_COLUMN] = tidalStreamRecord.getPortName();
        row[TIDAL_STREAM_POSITION_COLUMN] = tidalStreamRecord.getPosition();
        double setDriftAttributes[][] = tidalStreamRecord.getSetDrift();
        for (int i = 0; i < SET_AND_DRIFT_ENTRIES; i++) {
            row[TIDAL_STREAM_SET_DRIFT_COLUMN + i] = new SetDriftAttribute(setDriftAttributes[i]);
        }
        row[TIDAL_STREAM_DESCRIPTION_COLUMN] = tidalStreamRecord.getDescription();
        return row;
    }

    /** Translate text fields to tidal stream table row
   * @param	fields	Tidal stream row as text fields
   * @return	Tidal stream table row
   * @exception TidalStreamException if cannot translate fields
   * @exception RecordNotFoundException if tidal stream port does not exist
   * @exception	TableNotFoundException	if cannot find ports table
   */
    public Object[] toRow(String[] fields) throws TidalStreamException, RecordNotFoundException, TableNotFoundException {
        Object row[] = null;
        try {
            row = (new TidalStreamStream()).toRow(fields);
        } catch (IOException e) {
            throw new TidalStreamException(e.getMessage());
        }
        if (((PortTable) Tables.getTable(Tables.PORTS)).containsRow(row[TIDAL_STREAM_PORT_COLUMN]) < 0) {
            throw new RecordNotFoundException(Integer.toString(Tables.TIDAL_STREAMS) + ": " + row[TIDAL_STREAM_PORT_COLUMN].toString());
        } else {
            return row;
        }
    }

    /** Translate table row to tidal stream record object
   * @param	row	Table row of tidal stream
   * @return	Tidal stream record object
   * @exception	TidalStreamException	if invalid row
   */
    public TidalStreamRecord toTidalStream(Object row[]) throws TidalStreamException {
        TidalStreamRecord tidalStream = new TidalStreamRecord();
        tidalStream.setPortName((String) row[TIDAL_STREAM_PORT_COLUMN]);
        tidalStream.setPosition((Cartesian) row[TIDAL_STREAM_POSITION_COLUMN]);
        double setDrift[][] = new double[SET_AND_DRIFT_ENTRIES][SetDriftAttribute.SET_DRIFT_VALUES];
        try {
            for (int i = TIDAL_STREAM_SET_DRIFT_COLUMN; i < TIDAL_STREAM_DESCRIPTION_COLUMN; i++) {
                for (int j = 0; j < SetDriftAttribute.SET_DRIFT_VALUES; j++) {
                    setDrift[i - TIDAL_STREAM_SET_DRIFT_COLUMN][j] = ((SetDriftAttribute) row[i]).getSetDriftAttribute(j);
                }
            }
        } catch (SetDriftAttributeException e) {
            throw new TidalStreamException(e.getMessage());
        }
        tidalStream.setSetDrift(setDrift);
        tidalStream.setDescription((String) row[TIDAL_STREAM_DESCRIPTION_COLUMN]);
        return tidalStream;
    }

    /** Check if row has valid foreign keys (foreign key records exist).
   *  Should be overidden
   * @param	row	Row to check
   * @return True if foreign key records valid, else false
   */
    public boolean hasValidForeignKey(Object[] row) {
        try {
            PortTable portTable = ((PortTable) Tables.getTable(Tables.PORTS));
            String portName = row[TidalStreamTable.TIDAL_STREAM_PORT_COLUMN].toString();
            if (portTable.getPort(portName) != null) {
                return true;
            }
        } catch (Exception ex) {
        }
        return false;
    }

    /** Check if port is in tidal stream table
   * @param portName Name of port
   * @return True if found, else false
   */
    public boolean isPortInTidalStream(String portName) {
        for (int i = 0; i < this.getRowCount(); i++) {
            if (this.getRow(i)[TidalStreamTable.TIDAL_STREAM_PORT_COLUMN].toString().compareTo(portName) == 0) {
                return true;
            }
        }
        return false;
    }

    /** Save tidal streams to tab delimited text file
   * @param	file	File to save tidal streams
   * @exception	IOException	if cannot read from file
   */
    public void unloadFile(File file) throws IOException {
        (new TidalStreamStream()).tableModelWriter(this, file);
    }

    /** Read tidal streams from tab delimited text file
   * @param	file	File to load tidal streams
   * @exception	IOException	if cannot write to file
   */
    public void loadFile(File file) throws IOException {
        (new TidalStreamStream()).tableModelReader(this, file);
    }

    /** Set table keys */
    private void setKeys() {
        this.setUniqueKey(TIDAL_STREAM_POSITION_COLUMN);
        this.setPrimeKey(TIDAL_STREAM_POSITION_COLUMN);
    }
}
