package net.dawnmist.sims2tracker.data;

import com.db4o.activation.ActivationPurpose;
import com.db4o.collections.ActivatableArrayList;
import com.db4o.collections.ActivatableHashSet;
import com.db4o.collections.ActivatableList;
import com.db4o.collections.ActivatableSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import net.dawnmist.sims2tracker.Sims2Tracker;

/**
 * Neighbourhood is a singleton.
 * @author Janeene Beeforth <sourceforge@dawnmist.net>
 */
public class Neighbourhood extends ActivatableObject {

    private static Neighbourhood sNeighbourhood;

    private static NotesTableModel sNotesModel;

    private String mName;

    private ActivatableSet<Expansion> mExpansions;

    private ActivatableList<String> mPlayerNotes;

    /** TODO: List of challenges */
    private Neighbourhood() {
        mName = "";
        mExpansions = new ActivatableHashSet<Expansion>(EnumSet.copyOf(Expansion.getActualExpansions()));
        mPlayerNotes = new ActivatableArrayList<String>();
    }

    public static Neighbourhood getNeighbourhood() {
        if (sNeighbourhood == null) {
            sNeighbourhood = new Neighbourhood();
            if (sNotesModel == null) {
                sNotesModel = new NotesTableModel();
            }
        }
        return sNeighbourhood;
    }

    public static Neighbourhood setNeighbourhood(Neighbourhood hood) {
        if (sNeighbourhood != null) {
            sNeighbourhood.unbind();
            Sims2Tracker.getDataModel().deleteObject(sNeighbourhood);
        }
        sNeighbourhood = hood;
        return sNeighbourhood;
    }

    public String getName() {
        activate(ActivationPurpose.READ);
        return mName;
    }

    public void setName(String name) {
        activate(ActivationPurpose.WRITE);
        mName = name;
        sNotesModel.changedNotes();
    }

    public EnumSet<Expansion> getExpansions() {
        activate(ActivationPurpose.READ);
        return EnumSet.copyOf(mExpansions);
    }

    public void setExpansions(EnumSet<Expansion> expansions) {
        activate(ActivationPurpose.WRITE);
        mExpansions.clear();
        mExpansions.addAll(expansions);
        sNotesModel.changedNotes();
    }

    public void addExpansion(Expansion expansion) {
        activate(ActivationPurpose.WRITE);
        mExpansions.add(expansion);
        sNotesModel.changedNotes();
    }

    public void removeExpansion(Expansion expansion) {
        activate(ActivationPurpose.WRITE);
        mExpansions.remove(expansion);
        sNotesModel.changedNotes();
    }

    public List<String> getPlayerNotes() {
        activate(ActivationPurpose.READ);
        return Collections.unmodifiableList(mPlayerNotes);
    }

    public void setPlayerNotes(List<String> playerNotes) {
        activate(ActivationPurpose.WRITE);
        mPlayerNotes.clear();
        mPlayerNotes.addAll(playerNotes);
        sNotesModel.changedNotes();
    }

    public void addPlayerNote(String note) {
        activate(ActivationPurpose.WRITE);
        mPlayerNotes.add(note);
        sNotesModel.changedNotes();
    }

    public void removePlayerNote(int rowNum) {
        activate(ActivationPurpose.WRITE);
        mPlayerNotes.remove(rowNum);
        sNotesModel.changedNotes();
    }

    public void updatePlayerNote(int rowNum, String newNote) {
        activate(ActivationPurpose.WRITE);
        mPlayerNotes.set(rowNum, newNote);
        sNotesModel.changedNotes();
    }

    public static TableModel getNotesModel() {
        if (sNotesModel == null) {
            sNotesModel = new NotesTableModel();
        }
        return sNotesModel;
    }

    /**
     * Don't let an accidental clone.
     * @return this
     */
    @Override
    protected Neighbourhood clone() {
        return this;
    }

    @Override
    public void unbind() {
        activate(ActivationPurpose.READ);
        bind(null);
        mPlayerNotes.bind(null);
        mExpansions.bind(null);
    }

    public void saveToNewFile() {
        this.unbind();
        Sims2Tracker.getDataModel().saveObject(this);
    }

    private static class NotesTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            return getNeighbourhood().getPlayerNotes().size();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return getNeighbourhood().getPlayerNotes().get(rowIndex);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            String note = (String) aValue;
            getNeighbourhood().updatePlayerNote(rowIndex, note);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public String getColumnName(int column) {
            String name = "Notes:";
            return name;
        }

        public void changedNotes() {
            fireTableDataChanged();
        }
    }
}
