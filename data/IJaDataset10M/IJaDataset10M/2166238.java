package ho.module.evilcard.gui;

import ho.core.db.DBManager;
import ho.core.model.HOVerwaltung;
import ho.core.model.match.IMatchHighlight;
import ho.core.model.match.MatchHighlight;
import ho.core.model.match.Matchdetails;
import ho.core.util.HOLogger;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

class DetailsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 5909157286656017836L;

    static final int cols = 10;

    static final int COL_DIRECT_RED_CARDS = 0;

    static final int COL_WARNINGS_TYPE1 = 1;

    static final int COL_WARNINGS_TYPE2 = 2;

    static final int COL_WARNINGS_TYPE3 = 3;

    static final int COL_WARNINGS_TYPE4 = 4;

    static final int COL_MATCH_ID = 5;

    static final int COL_MATCH_HOME = 6;

    static final int COL_MATCH_GUEST = 7;

    static final int COL_MATCH_RESULT = 8;

    static final int COL_EVENT = 9;

    static final String CHECKED = "X";

    static final String UNDEFINED = "";

    private Vector<String> vColumnNames = null;

    private Object[][] data = null;

    private int playerId = 0;

    DetailsTableModel(int playerId) {
        this.playerId = playerId;
        data = new Object[0][cols];
        vColumnNames = new Vector<String>(Arrays.asList(new String[cols]));
        vColumnNames.set(COL_DIRECT_RED_CARDS, HOVerwaltung.instance().getLanguageString("column.RedCards"));
        vColumnNames.set(COL_WARNINGS_TYPE1, HOVerwaltung.instance().getLanguageString("column.WarningType1"));
        vColumnNames.set(COL_WARNINGS_TYPE2, HOVerwaltung.instance().getLanguageString("column.WarningType2"));
        vColumnNames.set(COL_WARNINGS_TYPE3, HOVerwaltung.instance().getLanguageString("column.WarningType3"));
        vColumnNames.set(COL_WARNINGS_TYPE4, HOVerwaltung.instance().getLanguageString("column.WarningType4"));
        vColumnNames.set(COL_MATCH_ID, HOVerwaltung.instance().getLanguageString("ID"));
        vColumnNames.set(COL_MATCH_HOME, HOVerwaltung.instance().getLanguageString("Heim"));
        vColumnNames.set(COL_MATCH_GUEST, HOVerwaltung.instance().getLanguageString("Gast"));
        vColumnNames.set(COL_MATCH_RESULT, HOVerwaltung.instance().getLanguageString("Ergebnis"));
        vColumnNames.set(COL_EVENT, HOVerwaltung.instance().getLanguageString("column.Event"));
        generateData();
    }

    @Override
    public Class<?> getColumnClass(int c) {
        return Object.class;
    }

    public int getColumnCount() {
        return vColumnNames.size();
    }

    @Override
    public String getColumnName(int c) {
        return (String) vColumnNames.get(c);
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public void refresh(int playerId) {
        this.playerId = playerId;
        generateData();
        this.fireTableDataChanged();
    }

    private void generateData() {
        if (playerId > 0) {
            Vector<MatchHighlight> highlights = DBManager.instance().getMatchHighlightsByTypIdAndPlayerId(IMatchHighlight.HIGHLIGHT_KARTEN, playerId);
            int i = 0;
            int rows = highlights.size();
            if (rows <= 0) {
                data = new Object[0][cols];
                return;
            }
            data = new Object[rows][cols];
            for (Iterator<MatchHighlight> iterator = highlights.iterator(); iterator.hasNext(); ) {
                MatchHighlight matchHighlight = iterator.next();
                data[i][COL_MATCH_ID] = Integer.valueOf(matchHighlight.getMatchId());
                data[i][COL_EVENT] = new String("<html>" + matchHighlight.getEventText());
                switch(matchHighlight.getHighlightSubTyp()) {
                    case IMatchHighlight.HIGHLIGHT_SUB_GELB_HARTER_EINSATZ:
                        data[i][COL_WARNINGS_TYPE1] = CHECKED;
                        break;
                    case IMatchHighlight.HIGHLIGHT_SUB_GELB_ROT_HARTER_EINSATZ:
                        data[i][COL_WARNINGS_TYPE2] = CHECKED;
                        break;
                    case IMatchHighlight.HIGHLIGHT_SUB_GELB_UNFAIR:
                        data[i][COL_WARNINGS_TYPE3] = CHECKED;
                        break;
                    case IMatchHighlight.HIGHLIGHT_SUB_GELB_ROT_UNFAIR:
                        data[i][COL_WARNINGS_TYPE4] = CHECKED;
                        break;
                    case IMatchHighlight.HIGHLIGHT_SUB_ROT:
                        data[i][COL_DIRECT_RED_CARDS] = CHECKED;
                        break;
                }
                data[i][COL_MATCH_HOME] = UNDEFINED;
                data[i][COL_MATCH_HOME] = UNDEFINED;
                data[i][COL_MATCH_RESULT] = UNDEFINED;
                i++;
            }
            for (i = 0; i < rows; i++) {
                Matchdetails matchDetail = DBManager.instance().getMatchDetails(((Integer) data[i][COL_MATCH_ID]).intValue());
                data[i][COL_MATCH_HOME] = matchDetail.getHeimName();
                data[i][COL_MATCH_GUEST] = matchDetail.getGastName();
                data[i][COL_MATCH_RESULT] = matchDetail.getHomeGoals() + " - " + matchDetail.getGuestGoals();
            }
        }
    }
}
