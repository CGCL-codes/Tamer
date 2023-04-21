package frost.gui.model;

import frost.util.gui.translation.*;

public class BoardInfoTableModel extends SortedTableModel implements LanguageListener {

    private Language language = null;

    protected static final String columnNames[] = new String[8];

    protected static final Class columnClasses[] = { String.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class };

    public BoardInfoTableModel() {
        super();
        language = Language.getInstance();
        refreshLanguage();
    }

    private void refreshLanguage() {
        columnNames[0] = language.getString("BoardInfoFrame.table.board");
        columnNames[1] = language.getString("BoardInfoFrame.table.state");
        columnNames[2] = language.getString("BoardInfoFrame.table.messages");
        columnNames[3] = language.getString("BoardInfoFrame.table.messagesToday");
        columnNames[4] = language.getString("BoardInfoFrame.table.messagesFlagged");
        columnNames[5] = language.getString("BoardInfoFrame.table.messagesStarred");
        columnNames[6] = language.getString("BoardInfoFrame.table.messagesUnread");
        columnNames[7] = language.getString("BoardInfoFrame.table.lastMsgDate");
        fireTableStructureChanged();
    }

    public void languageChanged(final LanguageEvent event) {
        refreshLanguage();
    }

    @Override
    public boolean isCellEditable(final int row, final int col) {
        return false;
    }

    @Override
    public String getColumnName(final int column) {
        if (column >= 0 && column < columnNames.length) {
            return columnNames[column];
        }
        return null;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Class getColumnClass(final int column) {
        if (column >= 0 && column < columnClasses.length) {
            return columnClasses[column];
        }
        return null;
    }
}
