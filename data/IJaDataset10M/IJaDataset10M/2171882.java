package cz.cvut.fel.mvod.gui.table;

import cz.cvut.fel.mvod.common.Voter;
import cz.cvut.fel.mvod.global.GlobalSettingsAndNotifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Model tabulky seznamu účastníků.
 * @author jakub
 */
public class VotersTableModel extends AbstractTableModel<Voter> {

    private static final TableColumnInformation NAME = new TableColumnInformation(0, GlobalSettingsAndNotifier.singleton.messages.getString("nameFormInput"), String.class, false);

    private static final TableColumnInformation SURNAME = new TableColumnInformation(1, GlobalSettingsAndNotifier.singleton.messages.getString("surnameFormInput"), String.class, false);

    private static final TableColumnInformation USERNAME = new TableColumnInformation(2, GlobalSettingsAndNotifier.singleton.messages.getString("usernameFormInput"), String.class, false);

    private List<Voter> rows = new ArrayList<Voter>();

    public VotersTableModel() {
        super(new TableColumnInformation[3]);
        COLUMNS[0] = NAME;
        COLUMNS[1] = SURNAME;
        COLUMNS[2] = USERNAME;
    }

    /**
	 * {@inheritDoc  }
	 */
    public int getRowCount() {
        return rows.size();
    }

    /**
	 * {@inheritDoc  }
	 */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == NAME.INDEX) {
            return rows.get(rowIndex).getFirstName();
        } else if (columnIndex == SURNAME.INDEX) {
            return rows.get(rowIndex).getLastName();
        } else if (columnIndex == USERNAME.INDEX) {
            return rows.get(rowIndex).getUserName();
        }
        throw new IndexOutOfBoundsException("No such column.");
    }

    /**
	 * {@inheritDoc  }
	 */
    public Voter getValueAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex > rows.size()) {
            throw new IndexOutOfBoundsException("No such row.");
        }
        return rows.get(rowIndex);
    }

    /**
	 * Nastaví obsah tabulky.
	 * @param voters to show
	 */
    public void setVoters(Collection<Voter> voters) {
        rows.clear();
        rows.addAll(voters);
        fireTableDataChanged();
    }

    /**
	 * Přidá účastníka do tbulky.
	 * @param voter nový účastník
	 */
    public void addVoter(Voter voter) {
        rows.add(voter);
    }

    /**
	 * {@inheritDoc  }
	 */
    public Voter remove(int index) {
        Voter voter = rows.remove(index);
        fireTableDataChanged();
        return voter;
    }
}
