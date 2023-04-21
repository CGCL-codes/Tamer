package de.blitzcoder.cddatabase.gui.trackeditor;

import de.blitzcoder.cddatabase.data.Track;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author blitzcoder
 */
public class TrackTableModel implements TableModel {

    public static final String[] COLUMNS = { "Num", "Name", "Artist", "Länge" };

    private Track[] tracks;

    private LinkedList<TableModelListener> listenerList = new LinkedList<TableModelListener>();

    public TrackTableModel(Track[] tracks) {
        this.tracks = tracks;
    }

    public TrackTableModel(int trackCount) {
        this.tracks = new Track[trackCount];
        for (int i = 0; i < trackCount; i++) {
            tracks[i] = new Track("");
        }
    }

    public Track[] getTracks() {
        return tracks;
    }

    @Override
    public int getRowCount() {
        return tracks.length;
    }

    public int getColumnCount() {
        return COLUMNS.length;
    }

    public String getColumnName(int columnIndex) {
        return COLUMNS[columnIndex];
    }

    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return Integer.class;
            case 1:
            case 2:
                return String.class;
            case 3:
                return String.class;
        }
        throw new IllegalArgumentException("Unknown Column");
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rowIndex;
            case 1:
                return tracks[rowIndex].getName();
            case 2:
                return tracks[rowIndex].getArtist();
            case 3:
                return Track.convertSecondsToString(tracks[rowIndex].getDuration());
            default:
                return "ERROR";
        }
    }

    public void addTrack() {
        tracks = Arrays.copyOf(tracks, tracks.length + 1);
        tracks[tracks.length - 1] = new Track("New Track");
        fireEvent(new TableModelEvent(this, tracks.length, tracks.length, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    public void removeTrack() {
        if (tracks.length > 0) {
            tracks = Arrays.copyOf(tracks, tracks.length - 1);
            fireEvent(new TableModelEvent(this, tracks.length, tracks.length, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
        }
    }

    public Track[] getAllTracks() {
        return tracks;
    }

    private void fireEvent(TableModelEvent evt) {
        ListIterator<TableModelListener> it = listenerList.listIterator();
        while (it.hasNext()) {
            it.next().tableChanged(evt);
        }
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 1:
                tracks[rowIndex].setName((String) aValue);
                break;
            case 2:
                tracks[rowIndex].setArtist((String) aValue);
                break;
            case 3:
                try {
                    tracks[rowIndex].setDuration((String) aValue);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Zeit bitte im Format xx:xx oder xx:xx:xx eingeben!");
                    throw new NumberFormatException("Wrong format");
                }
                break;
            default:
                throw new IllegalArgumentException("This column is not editable");
        }
        fireEvent(new TableModelEvent(this, rowIndex, rowIndex, columnIndex, TableModelEvent.UPDATE));
    }

    public void addTableModelListener(TableModelListener l) {
        listenerList.addLast(l);
    }

    public void removeTableModelListener(TableModelListener l) {
        listenerList.remove(l);
    }
}
