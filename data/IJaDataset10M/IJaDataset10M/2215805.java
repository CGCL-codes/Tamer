package com.tjoris.midigateway;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

class MidiModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private static final int kCOLUMN_NAME = 0;

    private static final int kCOLUMN_NOTE = 1;

    private static final int kCOLUMN_COUNT = 2;

    private MidiEntry[] fEntries;

    private IMidiAction[] fActionsByNote;

    public MidiModel(final IMidiAction[] actions, final Configuration configuration) {
        fEntries = new MidiEntry[actions.length];
        fActionsByNote = new IMidiAction[256];
        for (int i = 0; i < fEntries.length; ++i) {
            fEntries[i] = new MidiEntry(actions[i], configuration);
            final int note = fEntries[i].getNote();
            if (note != -1) {
                fActionsByNote[note] = actions[i];
            }
        }
    }

    public void storeProperties(final Configuration configuration) {
        for (int i = 0; i < fEntries.length; ++i) {
            final int note = fEntries[i].getNote();
            if (note != -1) {
                configuration.setUserProperty(fEntries[i].getAction().getID(), Integer.toString(note));
            }
        }
    }

    /**
	 * Changes the value of an action.
	 * 
	 * If value is in the range [0..127] it denotes a midi note. If it is in the range
	 * [128..255], it denotes a control change event.
	 * 
	 * @param rowIndex the index of the row that needs to be updated
	 * @param note the new value
	 */
    public void setNote(final int rowIndex, final int note) {
        if ((note >= 0) && (note < fActionsByNote.length)) {
            fActionsByNote[note] = fEntries[rowIndex].getAction();
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                fEntries[rowIndex].setNote(note);
                fireTableCellUpdated(rowIndex, 1);
            }
        });
    }

    public int getRowForNote(final int note) {
        final IMidiAction action = fActionsByNote[note];
        for (int i = 0; i < fEntries.length; ++i) {
            if (action == fEntries[i].getAction()) {
                return i;
            }
        }
        return -1;
    }

    public IMidiAction getAction(final int note) {
        return fActionsByNote[note];
    }

    public String getNameAt(final int rowIndex) {
        if ((rowIndex >= 0) && (rowIndex < fEntries.length)) {
            return fEntries[rowIndex].getName();
        }
        return null;
    }

    public Class getColumnClass(final int columnIndex) {
        return String.class;
    }

    public String getColumnName(final int column) {
        switch(column) {
            case kCOLUMN_NAME:
                return "Action";
            case kCOLUMN_NOTE:
                return "Note";
            default:
                throw new RuntimeException("unknown column: " + column);
        }
    }

    public int getColumnCount() {
        return kCOLUMN_COUNT;
    }

    public int getRowCount() {
        return fEntries.length;
    }

    public Object getValueAt(final int rowIndex, final int columnIndex) {
        switch(columnIndex) {
            case kCOLUMN_NAME:
                return fEntries[rowIndex].getName();
            case kCOLUMN_NOTE:
                return fEntries[rowIndex].getNoteString();
            default:
                throw new RuntimeException("unknown column: " + columnIndex);
        }
    }
}
