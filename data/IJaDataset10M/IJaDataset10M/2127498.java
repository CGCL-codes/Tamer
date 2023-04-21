package org.ttalbott.mytelly;

import java.util.*;
import java.sql.*;
import java.io.*;
import org.apache.oro.text.perl.*;

/**
 *
 * @author  Tom Talbott
 * @version
 */
public class SQLProgramData implements ProgramData {

    private static final String m_programs = "Programs";

    private static final String m_keyname = "KEY";

    private static final String[][] m_columns = { { m_keyname, "INT IDENTITY" }, { ProgramData.CHANNEL, "VARCHAR" }, { ProgramData.START, "VARCHAR(18)" }, { ProgramData.STOP, "VARCHAR(18)" }, { ProgramData.TITLE, "VARCHAR" }, { ProgramData.SUBTITLE, "VARCHAR" }, { ProgramData.DESC, "VARCHAR" }, { ProgramData.CATEGORY, "VARCHAR" }, { ProgramData.STEREO, "BIT" }, { ProgramData.PREVIOUSLYSHOWN, "BIT" }, { ProgramData.TELETEXT, "BIT" } };

    private static final String m_flags = "Flags";

    private static final String m_flagskey = "Table";

    private static final String FLAGS = "FLAGS";

    private static final String[][] m_flagsCols = { { m_flagskey, "VARCHAR" }, { FLAGS, "INT" } };

    private static final String[] m_flagsPrimKey = { m_flagskey };

    private static final String[] m_primaryKey = { m_keyname };

    private static final int STOPFIXED = 0x00000001;

    private SQLData m_sqlData = null;

    private Perl5Util m_regexp = new Perl5Util();

    private ProgramList m_programList = null;

    private ProgramList m_sortedProgramList = null;

    private Vector m_categories = null;

    private int m_cacheKey = -99;

    private TreeMap m_cacheProgs = new TreeMap();

    /** Creates new SQLProgramData */
    public SQLProgramData() throws Exception {
        m_sqlData = SQLData.getInstance();
        init();
    }

    public SQLProgramData(boolean debug) throws Exception {
        m_sqlData = SQLData.getInstance();
        setDebug(debug);
        init();
    }

    public void setDebug(boolean debug) {
        m_sqlData.setDebug(debug);
    }

    public ProgramList getPrograms() {
        if (m_programList == null) {
            final String[] cols = { m_keyname, ProgramData.CHANNEL, ProgramData.START, ProgramData.TITLE, ProgramData.SUBTITLE };
            ResultSet rs;
            SQLProgramList pl = new SQLProgramList();
            try {
                rs = m_sqlData.selectRows(m_programs, cols, null, null, null);
                SQLProgItem prog;
                while (rs.next()) {
                    prog = new SQLProgItem();
                    prog.key = rs.getInt(m_keyname);
                    prog.Channel = rs.getString(SQLData.fixColName(ProgramData.CHANNEL));
                    prog.Start = rs.getString(SQLData.fixColName(ProgramData.START));
                    prog.Title = rs.getString(SQLData.fixColName(ProgramData.TITLE));
                    prog.SubTitle = rs.getString(SQLData.fixColName(ProgramData.SUBTITLE));
                    if (MyTellyMainFrame.getConfig().getPruneProgramsPrior() < 1) {
                        pl.add(prog);
                    } else {
                        Calendar start = Utilities.makeCal(prog.Start);
                        Calendar now = Calendar.getInstance();
                        now.set(Calendar.HOUR, now.get(Calendar.HOUR) - MyTellyMainFrame.getConfig().getPruneProgramsPrior());
                        if (start.after(now)) pl.add(prog);
                    }
                }
                m_programList = pl;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return m_programList;
    }

    public ProgramList getProgramsSortedByTime() {
        if (m_sortedProgramList == null) {
            ProgramList pl = getPrograms();
            pl.sortAndRemoveDups();
            m_sortedProgramList = pl;
        }
        return m_sortedProgramList;
    }

    public ProgramList getProgramsSortedByChannel() {
        ProgramList pl = getPrograms();
        pl.sortByChannel();
        return pl;
    }

    public int getProgramCount() {
        ProgramList progs = getProgramsSortedByTime();
        return progs.getLength();
    }

    public int getCachedDays() {
        int cachedDays = 0;
        try {
            ProgramList progs = getProgramsSortedByTime();
            SQLProgItem lastItem = (SQLProgItem) progs.item(progs.getLength() - 1);
            Calendar last = Utilities.makeCal(lastItem.Start);
            long diffMillis = last.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            cachedDays = new Long(diffMillis / (24 * 60 * 60 * 1000)).intValue() + 1;
        } catch (Exception e) {
            System.err.println("SQLProgramData:getCachedDays returning ZERO due to exception:" + e);
        }
        return cachedDays;
    }

    public void fixStopTimes() {
        ProgramList progs = getProgramsSortedByChannel();
        int progItems = progs.getLength();
        SQLProgItem item;
        String channel;
        String start;
        TreeMap stopValue = new TreeMap();
        if (progItems > 1) {
            SQLProgItem prevItem = (SQLProgItem) progs.item(0);
            String prevChannel = getData(prevItem, ProgramData.CHANNEL);
            String prevStop = getData(prevItem, ProgramData.STOP);
            for (int i = 1; i < progItems; i++) {
                item = (SQLProgItem) progs.item(i);
                channel = getData(item, ProgramData.CHANNEL);
                if (prevStop == null || prevStop.length() == 0) {
                    if (channel.compareTo(prevChannel) == 0) {
                        start = getData(item, ProgramData.START);
                    } else {
                        start = getData(prevItem, ProgramData.START);
                        Calendar calStart = Utilities.makeCal(start);
                        calStart.set(Calendar.HOUR_OF_DAY, 0);
                        calStart.set(Calendar.MINUTE, 0);
                        calStart.add(Calendar.DAY_OF_YEAR, 1);
                        start = Utilities.dateToXMLTVDate(calStart.getTime());
                    }
                    stopValue.put(ProgramData.STOP, start);
                    try {
                        updateProgram(prevItem, stopValue);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                prevItem = item;
                prevChannel = channel;
                prevStop = getData(item, ProgramData.STOP);
            }
        }
    }

    public ProgramList search(ProgramList progs, String regexp, String field, Progress progress) throws MalformedPerl5PatternException {
        SQLProgramList result = new SQLProgramList();
        SQLProgItem prog;
        int numprogs = progs.getLength();
        int per = 0;
        int oldPer = 0;
        if (regexp != null) {
            for (int i = 0; i < numprogs; i++) {
                prog = (SQLProgItem) progs.item(i);
                if (match(prog, regexp, field)) {
                    result.add(prog);
                }
                if (progress != null) {
                    if (progress.isCancelled()) {
                        break;
                    } else {
                        per = i * 100 / numprogs;
                        if (per / 10 != oldPer / 10) {
                            progress.update(null, per + 10);
                            oldPer = per;
                        }
                    }
                }
            }
        }
        return result;
    }

    public boolean hasAdvancedSearch() {
        return true;
    }

    public ProgramList advancedSearch(ProgramList progs, String m_searchText, String m_channelText, String m_category, boolean m_distinct, boolean m_titlesOnly, String hiddenIndex, Progress progress) {
        if (progress != null) progress.setIndeterminate(true);
        String where = "(";
        String stringSearch = "";
        String groupBy = null;
        StringBuffer searchItems = new StringBuffer();
        searchItems.append("*");
        Vector cols = new Vector();
        cols.add(ProgramData.SUBTITLE);
        cols.add(ProgramData.TITLE);
        Vector keywords = new Vector();
        if (m_searchText != null && m_searchText.length() > 0) {
            StringTokenizer tokenizer = new StringTokenizer(m_searchText, "|");
            while (tokenizer.hasMoreTokens()) {
                String m_temp = tokenizer.nextToken();
                m_temp = m_temp.trim();
                if (m_temp.length() > 0) keywords.add(m_temp);
            }
        }
        if (m_titlesOnly) {
            Iterator it = keywords.iterator();
            while (it.hasNext()) {
                String next = ((String) it.next()).toUpperCase();
                if (next.charAt(0) == '"') {
                    next = next.substring(1, next.length() - 1);
                    where = where + " UPPER(" + ProgramData.TITLE + ") = " + "'" + next + "' OR UPPER(" + ProgramData.SUBTITLE + ") = " + "'" + next + "'";
                } else {
                    where = where + " UPPER(" + ProgramData.TITLE + ") LIKE " + "'%" + next + "%' OR UPPER(" + ProgramData.SUBTITLE + ") LIKE " + "'%" + next + "%'";
                }
                if (it.hasNext()) where = where + " OR ";
            }
        } else {
            cols.add(ProgramData.DESC);
            Iterator it = keywords.iterator();
            while (it.hasNext()) {
                String next = ((String) it.next()).toUpperCase();
                if (next.charAt(0) == '"') {
                    next = next.substring(1, next.length() - 1);
                    where = where + " UPPER( " + ProgramData.TITLE + ") = " + "'" + next + "' OR UPPER(" + ProgramData.SUBTITLE + ") = " + "'" + next + "' OR UPPER(" + ProgramData.DESC + ") = " + "'" + next + "' ";
                } else {
                    where = where + " UPPER( " + ProgramData.TITLE + ") LIKE " + "'%" + next + "%' OR UPPER(" + ProgramData.SUBTITLE + ") LIKE " + "'%" + next + "%' OR UPPER(" + ProgramData.DESC + ") LIKE " + "'%" + next + "%' ";
                }
                if (it.hasNext()) where = where + " OR ";
            }
        }
        if (m_channelText.length() > 0) {
            cols.add(ProgramData.CHANNEL);
            if (where.length() > 1) where = where + " ) AND ( ";
            where = where + " UPPER(" + ProgramData.CHANNEL + ") LIKE " + "'%" + m_channelText.toUpperCase() + "%'";
        }
        if (!m_category.equals(ProgramData.ALL_CATS)) {
            cols.add(ProgramData.CATEGORY);
            if (where.length() > 1) where = where + ") AND (";
            where = where + ProgramData.CATEGORY + "= '" + m_category + "'";
        }
        cols.add(ProgramData.START);
        cols.add(m_keyname);
        if (cols.size() > 0 && where.length() > 1) {
            where = where + ')';
            final Object[] objCols = cols.toArray();
            final String[] strCols = new String[cols.size()];
            for (int i = 0; i < strCols.length; i++) {
                strCols[i] = (String) objCols[i];
            }
            ResultSet rs = null;
            try {
                if (MyTellyMainFrame.getConfig().getDebug()) System.out.println("where:" + where);
                rs = m_sqlData.selectRows(m_programs, strCols, where, ProgramData.START, groupBy);
                int ii = 0;
                while (rs.next()) {
                    if (ii++ != 0) searchItems.append('*');
                    searchItems.append(rs.getString(m_keyname));
                }
                if (searchItems.length() < 3) {
                    System.err.println("NO ITEMS MATCHED SEARCH CRITERIA!");
                    return new SQLProgramList();
                }
                searchItems.append("*");
                stringSearch = searchItems.toString();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else System.err.println("NO SEARCH DONE, NO COLS IN SELECT or EMPTY WHERE!");
        SQLProgramList result = new SQLProgramList();
        SQLProgItem prog;
        int numprogs = progs.getLength();
        int per = 0;
        int oldPer = 0;
        ArrayList firstOccurance = new ArrayList(100);
        for (int i = 0; i < numprogs; i++) {
            prog = (SQLProgItem) progs.item(i);
            boolean matched = stringSearch.indexOf("*" + prog.key + "*") > -1 && (hiddenIndex == null || hiddenIndex.indexOf("*" + prog.Title.toUpperCase() + "*") < 0);
            if (matched && m_channelText.length() > 0) {
                matched = prog.Channel.indexOf(m_channelText.toUpperCase()) > -1;
            }
            if (matched) {
                if (m_distinct) {
                    if (firstOccurance.contains(prog.Title + prog.SubTitle)) ; else {
                        firstOccurance.add(prog.Title + prog.SubTitle);
                        result.add(prog);
                    }
                } else result.add(prog);
            }
            if (progress != null) {
                if (progress.isCancelled()) break; else {
                    per = i * 100 / numprogs;
                    if (per / 10 != oldPer / 10) {
                        progress.update(null, per + 10);
                        oldPer = per;
                    }
                }
            }
        }
        return result;
    }

    public boolean match(SQLProgItem prog, String regexp, String field) throws MalformedPerl5PatternException {
        boolean result = false;
        String[] cols = { "*" };
        String where = m_keyname + " = " + Long.toString(prog.key);
        try {
            ResultSet rs = m_sqlData.selectRows(m_programs, cols, where, null, null);
            String value;
            int colType;
            while (rs.next()) {
                ResultSetMetaData m = rs.getMetaData();
                int colCount = m.getColumnCount();
                for (int i = 1; i <= colCount; i++) {
                    if (field == null || m_columns[i - 1][0].equalsIgnoreCase(field)) {
                        value = rs.getString(i);
                        if (value != null) result = m_regexp.match(regexp, value);
                        if (result) break;
                    }
                }
                if (result) break;
            }
        } catch (SQLException e) {
        }
        return result;
    }

    public String getData(ProgItem prog, String tag) {
        String ret = null;
        if (prog instanceof SQLProgItem) {
            SQLProgItem pi = (SQLProgItem) prog;
            if (tag.equalsIgnoreCase(ProgramData.START)) {
                ret = pi.Start;
            } else if (tag.equalsIgnoreCase(ProgramData.CHANNEL)) {
                ret = pi.Channel;
            } else if (tag.equalsIgnoreCase(ProgramData.TITLE)) {
                ret = pi.Title;
            } else if (tag.equalsIgnoreCase(ProgramData.SUBTITLE)) {
                ret = pi.SubTitle;
            } else {
                if (pi.key != m_cacheKey) {
                    UpdateCache(pi);
                }
                ret = (String) m_cacheProgs.get(tag);
            }
        }
        return ret;
    }

    private void UpdateCache(SQLProgItem prog) {
        String where = m_keyname + " = " + prog.key;
        m_cacheProgs.clear();
        String[] col = { "*" };
        try {
            ResultSet rs = m_sqlData.selectRows(m_programs, col, where, null, null);
            int colLength = m_columns.length;
            while (rs.next()) {
                TreeMap rsMap = new TreeMap();
                String data;
                for (int i = 0; i < colLength; i++) {
                    m_cacheProgs.put(m_columns[i][0], rs.getString(i + 1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        m_cacheKey = prog.key;
    }

    public Vector getCategories(ProgItem prog) {
        String value = getData(prog, ProgramData.CATEGORY);
        Vector cats = new Vector();
        if (value != null) {
            StringTokenizer tokenizer = new StringTokenizer(value, ";");
            while (tokenizer.hasMoreTokens()) {
                cats.add(tokenizer.nextToken());
            }
        }
        return cats;
    }

    public boolean getBoolItem(ProgItem prog, String tag) {
        String[] col = { tag };
        String where = m_keyname + " = " + Long.toString(((SQLProgItem) prog).key);
        try {
            ResultSet rs = m_sqlData.selectRows(m_programs, col, where, null, null);
            while (rs.next()) {
                boolean data = rs.getBoolean(SQLData.fixColName(tag));
                return data;
            }
        } catch (SQLException e) {
        }
        return false;
    }

    public boolean getPreviouslyShown(ProgItem prog) {
        return getBoolItem(prog, ProgramData.PREVIOUSLYSHOWN);
    }

    public boolean getStereo(ProgItem prog) {
        return getBoolItem(prog, ProgramData.STEREO);
    }

    public boolean getClosedCaption(ProgItem prog) {
        return getBoolItem(prog, ProgramData.TELETEXT);
    }

    public void init() throws Exception {
        try {
            m_sqlData.createTable(m_programs, m_columns, m_primaryKey);
            m_sqlData.createTable(m_flags, m_flagsCols, m_flagsPrimKey);
        } catch (SQLException e) {
            if (!e.getSQLState().equals("S0001")) throw e;
        }
    }

    public void resetPrograms() throws Exception {
        try {
            m_sqlData.deleteAll(m_programs);
            m_sqlData.deleteAll(m_flags);
            m_sqlData.compactDB();
        } catch (SQLException e) {
            if (!e.getSQLState().equals("S0002")) throw e;
            System.out.println(e.getSQLState());
            e.printStackTrace();
        }
    }

    private int getFlags() {
        String[] col = { FLAGS };
        String where = m_flagskey + " = " + (String) SQLData.fixType(m_programs, m_flagsCols[0][1]);
        ;
        try {
            ResultSet rs = m_sqlData.selectRows(m_flags, col, where, null, null);
            while (rs.next()) {
                int data = rs.getInt(SQLData.fixColName(FLAGS));
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean isStopTimesFixed() {
        int flags = getFlags();
        if (flags != -1) return (flags & STOPFIXED) > 0;
        return false;
    }

    public void setStopTimesFixed() {
        int flags = getFlags();
        if (flags == -1) {
            String[] sCols = new String[2];
            String[] sValues = new String[2];
            sCols[0] = m_flagsCols[0][0];
            sValues[0] = (String) SQLData.fixType(m_programs, m_flagsCols[0][1]);
            sCols[1] = m_flagsCols[1][0];
            sValues[1] = (String) SQLData.fixType(new Integer(STOPFIXED).toString(), m_flagsCols[1][1]);
            try {
                m_sqlData.insertRow(m_flags, sCols, sValues);
            } catch (SQLException e) {
                try {
                    m_sqlData.createTable(m_flags, m_flagsCols, m_flagsPrimKey);
                    m_sqlData.insertRow(m_flags, sCols, sValues);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } else {
            String[] sCols = new String[1];
            String[] sValues = new String[1];
            sValues[0] = (String) SQLData.fixType(new Integer(STOPFIXED | flags).toString(), m_flagsCols[0][1]);
            String where = m_flagskey + " = " + m_programs;
            try {
                m_sqlData.updateRow(m_flags, sCols, sValues, where);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addProgram(Map programValues) throws SQLException {
        int vLen = m_columns.length;
        Vector cols = new Vector();
        Vector values = new Vector();
        for (int i = 0; i < vLen; i++) {
            String value = (String) programValues.get(m_columns[i][0]);
            if (value != null) {
                values.add(SQLData.fixType(value, m_columns[i][1]));
                cols.add(m_columns[i][0]);
            }
        }
        int colSize = cols.size();
        String[] sCols = new String[colSize];
        String[] sValues = new String[colSize];
        for (int i = 0; i < colSize; i++) {
            sCols[i] = (String) cols.get(i);
            sValues[i] = (String) values.get(i);
        }
        m_sqlData.insertRow(m_programs, sCols, sValues);
    }

    public void updateProgram(ProgItem item, Map newValues) throws SQLException {
        int vLen = m_columns.length;
        Vector cols = new Vector();
        Vector values = new Vector();
        for (int i = 0; i < vLen; i++) {
            String value = (String) newValues.get(m_columns[i][0]);
            if (value != null) {
                values.add(SQLData.fixType(value, m_columns[i][1]));
                cols.add(m_columns[i][0]);
            }
        }
        int colSize = cols.size();
        String[] sCols = new String[colSize];
        String[] sValues = new String[colSize];
        for (int i = 0; i < colSize; i++) {
            sCols[i] = (String) cols.get(i);
            sValues[i] = (String) values.get(i);
        }
        String where = m_keyname + " = " + Long.toString(((SQLProgItem) item).key);
        m_sqlData.updateRow(m_programs, sCols, sValues, where);
    }

    public ProgramList getEmptyProgramList() {
        return new SQLProgramList();
    }
}

class SQLProgItem extends java.lang.Object implements ProgItem {

    int key = -1;

    String Channel = null;

    String Start = null;

    String Title = null;

    String SubTitle = null;

    public String toString() {
        String retValue;
        retValue = Title + "-" + SubTitle + " " + Channel + " " + Start + "(" + key + ")";
        return retValue;
    }
}

class SQLProgramList extends ProgramListImp {

    public SQLProgramList() {
    }

    public void sort() {
        Collections.sort(m_items, new SQLProgItemComparitor());
    }

    public void sortAndRemoveDups() {
        TreeSet tempSet = new TreeSet(new SQLProgItemComparitor());
        tempSet.addAll(m_items);
        m_items.clear();
        m_items.addAll(tempSet);
    }

    public void sortByChannel() {
        TreeSet tempSet = new TreeSet(new SQLProgItemChannelComparitor());
        tempSet.addAll(m_items);
        m_items.clear();
        m_items.addAll(tempSet);
    }
}

class SQLProgItemComparitor extends java.lang.Object implements Comparator, Serializable {

    public int compare(java.lang.Object obj, java.lang.Object obj1) {
        int ret = 0;
        SQLProgItem lProgItem = (SQLProgItem) obj;
        SQLProgItem rProgItem = (SQLProgItem) obj1;
        if (lProgItem != null && rProgItem != null) {
            ret = lProgItem.Start.compareTo(rProgItem.Start);
            if (ret == 0) {
                String lChannelDesc = Programs.getChannelDesc(lProgItem.Channel);
                String rChannelDesc = Programs.getChannelDesc(rProgItem.Channel);
                int lEndNum = lChannelDesc.indexOf(' ');
                int rEndNum = rChannelDesc.indexOf(' ');
                if (lEndNum > 0 && rEndNum > 0) {
                    String lChannelNum = lChannelDesc.substring(0, lEndNum);
                    String rChannelNum = rChannelDesc.substring(0, rEndNum);
                    try {
                        ret = Integer.valueOf(lChannelNum).compareTo(Integer.valueOf(rChannelNum));
                    } catch (NumberFormatException e) {
                        ret = lChannelNum.compareTo(rChannelNum);
                    }
                    if (ret == 0) {
                        ret = lChannelDesc.substring(lEndNum).compareTo(rChannelDesc.substring(rEndNum));
                    }
                } else ret = lChannelDesc.compareToIgnoreCase(rChannelDesc);
            }
        }
        return ret;
    }
}

class SQLProgItemChannelComparitor extends java.lang.Object implements Comparator, Serializable {

    public int compare(java.lang.Object obj, java.lang.Object obj1) {
        int ret = 0;
        SQLProgItem lProgItem = (SQLProgItem) obj;
        SQLProgItem rProgItem = (SQLProgItem) obj1;
        if (lProgItem != null && rProgItem != null) {
            String lChannelDesc = Programs.getChannelDesc(lProgItem.Channel);
            String rChannelDesc = Programs.getChannelDesc(rProgItem.Channel);
            int lEndNum = lChannelDesc.indexOf(' ');
            int rEndNum = rChannelDesc.indexOf(' ');
            if (lEndNum > 0 && rEndNum > 0) {
                String lChannelNum = lChannelDesc.substring(0, lEndNum);
                String rChannelNum = rChannelDesc.substring(0, rEndNum);
                try {
                    ret = Integer.valueOf(lChannelNum).compareTo(Integer.valueOf(rChannelNum));
                } catch (NumberFormatException e) {
                    ret = lChannelNum.compareTo(rChannelNum);
                }
                if (ret == 0) {
                    ret = lChannelDesc.substring(lEndNum).compareTo(rChannelDesc.substring(rEndNum));
                    if (ret == 0) {
                        ret = lProgItem.Start.compareTo(rProgItem.Start);
                    }
                }
            } else ret = lChannelDesc.compareToIgnoreCase(rChannelDesc);
        }
        return ret;
    }
}
