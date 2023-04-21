package wizworld.navigate.port;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import wizworld.navigate.database.*;
import wizworld.navigate.stream.TidalStreamTable;
import wizworld.util.tableModel.TableModel;

/** Reads port object from table
 * @author (c) Stephen Denham 2002
 * @version 0.1 - SD - Check row count in table relationships.
 * @version 5.0.3 - Add port id formatter.
 */
public final class PortTable extends wizworld.util.tableModel.TableModel {

    /** Number of attributes */
    protected static final int PORT_ATTRIBUTES = 22;

    /** Useful field/attribute index */
    public static final int PORT_NAME = 0;

    /** Useful field/attribute index */
    public static final int PORT_ID = 1;

    /** Useful field/attribute index */
    public static final int PORT_ZONE = 2;

    /** Useful field/attribute index */
    public static final int PORT_ML = 3;

    /** Useful field/attribute index */
    public static final int PORT_M2G = 4;

    /** Useful field/attribute index */
    public static final int PORT_M2H = 5;

    /** Useful field/attribute index */
    public static final int PORT_S2G = 6;

    /** Useful field/attribute index */
    public static final int PORT_S2H = 7;

    /** Useful field/attribute index */
    public static final int PORT_K1G = 8;

    /** Useful field/attribute index */
    public static final int PORT_K1H = 9;

    /** Useful field/attribute index */
    public static final int PORT_O1G = 10;

    /** Useful field/attribute index */
    public static final int PORT_O1H = 11;

    /** Useful field/attribute index */
    public static final int PORT_F4 = 12;

    /** Useful field/attribute index */
    public static final int PORT_FF4 = 13;

    /** Useful field/attribute index */
    public static final int PORT_F6 = 14;

    /** Useful field/attribute index */
    public static final int PORT_FF6 = 15;

    /** Useful field/attribute index */
    public static final int PORT_SC_ML = 16;

    /** Useful field/attribute index */
    public static final int PORT_SC_M2 = 17;

    /** Useful field/attribute index */
    public static final int PORT_SC_S2 = 18;

    /** Useful field/attribute index */
    public static final int PORT_LAG = 19;

    /** Useful field/attribute index */
    public static final int PORT_MLSP = 20;

    /** Useful field/attribute index */
    public static final int PORT_ML_OFFSET = 21;

    /** Constructor
   * @param	columnHeadings	Column heading labels
   */
    public PortTable(String[] columnHeadings) {
        super(columnHeadings);
        this.setKeys();
    }

    /** Constructor
   */
    public PortTable() {
        super();
        String columnHeadings[] = new String[PORT_ATTRIBUTES];
        for (int i = 0; i < columnHeadings.length; i++) {
            columnHeadings[i] = "";
        }
        this.setColumns(columnHeadings);
        this.setKeys();
    }

    /** Fetches a port object from the port table
   * @param	portName Name of port
   * @return Port object
   * @exception RecordNotFoundException	if cannot read port from table
   */
    public Port getPort(String portName) throws RecordNotFoundException {
        Object row[] = this.getRow(portName);
        if (row != null) {
            return this.toPort(row);
        } else {
            throw new RecordNotFoundException(Integer.toString(Tables.PORTS) + ": " + portName);
        }
    }

    /** Get table relationships
   * @return List of table models that this table model has a foreign key in
   */
    public TableModel[] getTableRelationships() {
        try {
            TableModel tables[] = new TableModel[1];
            tables[0] = Tables.getTable(Tables.TIDAL_STREAMS);
            if (tables[0] != null && tables[0].getRowCount() > 0) {
                return tables;
            } else {
                return new TableModel[0];
            }
        } catch (TableNotFoundException noTable) {
            return new TableModel[0];
        }
    }

    /** Check if row keys are foreign keys
   * @param	row	Row to check
   * @return True if foreign key, else false
   */
    public boolean isForeignKey(int row) {
        try {
            TidalStreamTable tidalStreamTable = (TidalStreamTable) Tables.getTable(Tables.TIDAL_STREAMS);
            String portName = this.getRow(row)[PortTable.PORT_NAME].toString();
            return tidalStreamTable.isPortInTidalStream(portName);
        } catch (TableNotFoundException ex) {
            return false;
        }
    }

    /** Replace foreign key value
   * @param row Row number of current key
   * @param newRow Values for new row
   * @return True if key value changed
   */
    public boolean replaceForeignKey(int row, Object[] newRow) {
        try {
            String portName = this.getRow(row)[PortTable.PORT_NAME].toString();
            TidalStreamTable tidalStreamTable = (TidalStreamTable) Tables.getTable(Tables.TIDAL_STREAMS);
            boolean changed = false;
            Object tidalStreamRow[];
            for (int i = 0; i < tidalStreamTable.getRowCount(); i++) {
                if (tidalStreamTable.getRow(i)[TidalStreamTable.TIDAL_STREAM_PORT_COLUMN].toString().compareTo(portName) == 0) {
                    tidalStreamRow = tidalStreamTable.getRow(i);
                    tidalStreamRow[TidalStreamTable.TIDAL_STREAM_PORT_COLUMN] = newRow[TidalStreamTable.TIDAL_STREAM_PORT_COLUMN];
                    tidalStreamTable.replaceRow(tidalStreamRow, false);
                    changed = true;
                }
            }
            if (changed) {
                tidalStreamTable.save();
            }
            return changed;
        } catch (Exception e) {
            return true;
        }
    }

    /** Make port name Pnnnnn, where nnnnn is table index
   * @return Port name
   */
    public String getName() {
        return "P" + (new DecimalFormat("00000")).format(this.getRowCount());
    }

    /** Translate text fields to port table row
   * @param	fields	Port object as text fields
   * @return	Port table row
   * @exception PortException if cannot translate fields
   */
    public Object[] toRow(String[] fields) throws PortException {
        try {
            return (new PortStream()).toRow(fields);
        } catch (IOException ioe) {
            throw new PortException(ioe.getMessage());
        }
    }

    /** Translate port object to table row
   * @param	port	Port object
   * @return	Port table row
   * @exception PortException if cannot translate object
   */
    public Object[] toRow(Port port) throws PortException {
        Object row[] = new Object[PORT_ATTRIBUTES];
        try {
            row[PORT_NAME] = (String) port.getPortName();
            row[PORT_ID] = (String) port.getPortId();
            row[PORT_ZONE] = new Zone(Double.toString(port.getTimeZone()));
            row[PORT_ML] = new HeightHarmonicConstant(port.getMlZ0());
            row[PORT_M2G] = new AngleHarmonicConstant(port.getM2g());
            row[PORT_M2H] = new HeightHarmonicConstant(port.getM2h());
            row[PORT_S2G] = new AngleHarmonicConstant(port.getS2g());
            row[PORT_S2H] = new HeightHarmonicConstant(port.getS2h());
            row[PORT_K1G] = new AngleHarmonicConstant(port.getK1g());
            row[PORT_K1H] = new HeightHarmonicConstant(port.getK1h());
            row[PORT_O1G] = new AngleHarmonicConstant(port.getO1g());
            row[PORT_O1H] = new HeightHarmonicConstant(port.getO1h());
            row[PORT_F4] = new AngleHarmonicConstant(port.getf4());
            row[PORT_FF4] = new DiurnalHeightHarmonicConstant(port.getfF4());
            row[PORT_F6] = new AngleHarmonicConstant(port.getf6());
            row[PORT_FF6] = new DiurnalHeightHarmonicConstant(port.getfF6());
            row[PORT_SC_ML] = new PortCorrections(port.getSc());
            row[PORT_SC_M2] = new PortCorrections(port.getM2());
            row[PORT_SC_S2] = new PortCorrections(port.getS2());
            row[PORT_LAG] = new Integer((int) port.getLag());
            row[PORT_MLSP] = new HeightHarmonicConstant(Double.toString(port.getMlSp()));
            row[PORT_ML_OFFSET] = new PortCorrections(port.getMl());
        } catch (IOException e) {
            throw new PortException(port.getPortName() + ": " + e.getMessage());
        } catch (PortCorrectionsException e) {
            throw new PortException(port.getPortName() + ": " + e.getMessage());
        }
        return row;
    }

    /** Translate table row to port object
   * @param	row	Table row of waypoint
   * @return	Port object
   */
    public Port toPort(Object row[]) {
        Port port = new Port();
        port.setPortName((String) row[PORT_NAME]);
        port.setPortId((String) row[PORT_ID]);
        port.setTimeZone(((Zone) row[PORT_ZONE]).get());
        port.setMlZ0(((HeightHarmonicConstant) row[PORT_ML]).get());
        port.setM2g(((AngleHarmonicConstant) row[PORT_M2G]).get());
        port.setM2h(((HeightHarmonicConstant) row[PORT_M2H]).get());
        port.setS2g(((AngleHarmonicConstant) row[PORT_S2G]).get());
        port.setS2h(((HeightHarmonicConstant) row[PORT_S2H]).get());
        port.setK1g(((AngleHarmonicConstant) row[PORT_K1G]).get());
        port.setK1h(((HeightHarmonicConstant) row[PORT_K1H]).get());
        port.setO1g(((AngleHarmonicConstant) row[PORT_O1G]).get());
        port.setO1h(((HeightHarmonicConstant) row[PORT_O1H]).get());
        port.setf4(((AngleHarmonicConstant) row[PORT_F4]).get());
        port.setfF4(((DiurnalHeightHarmonicConstant) row[PORT_FF4]).get());
        port.setf6(((AngleHarmonicConstant) row[PORT_F6]).get());
        port.setfF6(((DiurnalHeightHarmonicConstant) row[PORT_FF6]).get());
        port.setSc(((PortCorrections) row[PORT_SC_ML]).getPortCorrections());
        port.setM2(((PortCorrections) row[PORT_SC_M2]).getPortCorrections());
        port.setS2(((PortCorrections) row[PORT_SC_S2]).getPortCorrections());
        port.setLag(((Integer) row[PORT_LAG]).intValue());
        port.setMlSp(((HeightHarmonicConstant) row[PORT_MLSP]).get());
        port.setMl(((PortCorrections) row[PORT_ML_OFFSET]).getPortCorrections());
        return port;
    }

    /** Save ports to tab delimited text file
   * @param	file	File to save ports
   * @exception	IOException	if cannot write to file
   */
    public void unloadFile(File file) throws IOException {
        (new PortStream()).tableModelWriter(this, file);
    }

    /** Read ports from tab delimited text file
   * @param	file	File to load ports
   * @exception	IOException	if cannot read file
   */
    public void loadFile(File file) throws IOException {
        (new PortStream()).tableModelReader(this, file);
    }

    /** Set table keys */
    private void setKeys() {
        this.setUniqueKey(PORT_NAME);
        this.setUniqueKey(PORT_ID);
    }

    /** Format port id to 0000([a..z])
   * @param portId Port id string
   * @return Formatted port id
   * @exception NumberFormatException if cannot format
   */
    public static String toPortId(String portId) throws NumberFormatException {
        String id = portId;
        int len = id.length();
        char suffix = id.charAt(len - 1);
        DecimalFormat formatId = new DecimalFormat("0000");
        if (suffix >= 'a' && suffix <= 'z') {
            id = formatId.format(Double.valueOf(id.substring(0, len - 1)).doubleValue());
            id = id + suffix;
        } else {
            id = formatId.format(Double.valueOf(id).doubleValue());
        }
        return id;
    }
}
