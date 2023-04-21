package ants.p2p.gui;

import java.util.*;
import javax.swing.table.*;
import org.apache.log4j.*;
import ants.p2p.query.*;
import ants.p2p.utils.indexer.*;
import ants.p2p.utils.encoding.*;

public class QueryTableModel extends AbstractTableModel {

    static Logger _logger = Logger.getLogger(ConnectionAntPanel.class.getName());

    ArrayList resultSet = new ArrayList();

    public String[] columnNames = { ji.JI.i("File Name"), ji.JI.i("Sources"), ji.JI.i("Max % completed"), ji.JI.i("File Size"), ji.JI.i("Last Seen on"), ji.JI.i("File Infos"), ji.JI.i("File Hash") };

    public QueryTableModel(ArrayList resultSet) {
        this.resultSet = resultSet;
    }

    public ArrayList getResultSet() {
        return this.resultSet;
    }

    public void setResultSet(ArrayList resultSet) {
        this.resultSet = resultSet;
        this.fireTableRowsUpdated(0, this.resultSet.size());
    }

    public FileTupleGroup findByHash(String hashKey) {
        synchronized (this.resultSet) {
            Iterator it = this.resultSet.iterator();
            while (it.hasNext()) {
                FileTupleGroup objFileTupe = (FileTupleGroup) it.next();
                if (objFileTupe.getHash().equals(hashKey)) return objFileTupe;
            }
        }
        return null;
    }

    public int getRowCount() {
        return this.resultSet.size();
    }

    public Object getValueAt(int row, int col) {
        FileTupleGroup ftg = (FileTupleGroup) this.resultSet.get(row);
        switch(col) {
            case 0:
                return ((QueryFileTuple) ftg.tuples.get(0)).getFileName();
            case 1:
                int owners = ftg.tuples.size();
                long lastTimeSeen = ftg.getLastTimeSeen();
                return new FileSource(owners, lastTimeSeen);
            case 2:
                PercentageString percent = new PercentageString("0%");
                for (int x = 0; x < ftg.tuples.size(); x++) {
                    QueryFileTuple qft = (QueryFileTuple) ftg.tuples.get(x);
                    if (qft instanceof QueryRemoteFileTuple) continue; else if (qft instanceof QueryCompletedFileTuple) return new PercentageString("100%"); else if (qft instanceof QueryPartialFileTuple && (new PercentageString(((QueryPartialFileTuple) qft).getPercentage())).compareTo(percent) > 0) percent = new PercentageString(((QueryPartialFileTuple) qft).getPercentage());
                }
                return percent;
            case 3:
                return new FileSize(ftg.getSize().longValue());
            case 4:
                return new TableEntryDate(ftg.getLastTimeSeen());
            case 5:
                return ((QueryFileTuple) ftg.tuples.get(0)).getExtendedInfos();
            case 6:
                return ftg.getHash();
            default:
                return null;
        }
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col].toString();
    }

    public Class getColumnClass(int col) {
        try {
            switch(col) {
                case 0:
                    return Class.forName("java.lang.String");
                case 1:
                    return Class.forName("ants.p2p.utils.indexer.FileSource");
                case 2:
                    return Class.forName("ants.p2p.utils.encoding.PercentageString");
                case 3:
                    return Class.forName("ants.p2p.utils.indexer.FileSize");
                case 4:
                    return Class.forName("ants.p2p.utils.encoding.TableEntryDate");
                case 5:
                    return Class.forName("java.lang.String");
                case 6:
                    return Class.forName("java.lang.String");
                default:
                    return null;
            }
        } catch (Exception e) {
            _logger.error("Cannot resolve class", e);
            return null;
        }
    }
}
