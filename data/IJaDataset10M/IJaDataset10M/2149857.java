package org.conserve.tools.protection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.conserve.connection.ConnectionWrapper;
import org.conserve.tools.Defaults;
import org.conserve.tools.Tools;

/**
 * Keeps track of all the objects that are referenced (directly or indirectly) by a certain object, as well as all
 * objects that reference them.
 * 
 * @author Erik Berglund
 * 
 */
public class DependentSet {

    private boolean protectedEntry;

    private List<ProtectionEntry> cacheKey = new ArrayList<ProtectionEntry>();

    private List<List<ProtectionEntry>> cacheValue = new ArrayList<List<ProtectionEntry>>();

    /**
	 * Create and populate a new set of entries that are dependent on the given entry.
	 * 
	 * @param tableName
	 * @param id
	 * @param cw
	 * @throws SQLException
	 */
    public DependentSet(String tableName, Long id, ConnectionWrapper cw) throws SQLException {
        ProtectionEntry candidate = new ProtectionEntry(tableName, id);
        List<ProtectionEntry> candidates = new ArrayList<ProtectionEntry>();
        candidates.add(candidate);
        List<ProtectionEntry> entries = new ArrayList<ProtectionEntry>(candidates);
        populateList(entries, candidates, cw);
        cullEntries(entries, cw);
        if (entries.contains(candidate)) {
            protectedEntry = false;
        } else {
            protectedEntry = true;
        }
    }

    public boolean isProtected() {
        return protectedEntry;
    }

    /**
	 * Remove all entries that are referenced from outside the list.
	 * 
	 * @param entries
	 * @throws SQLException
	 */
    private void cullEntries(List<ProtectionEntry> entries, ConnectionWrapper cw) throws SQLException {
        ProtectionEntry toDelete = null;
        do {
            toDelete = null;
            outerLoop: for (ProtectionEntry entry : entries) {
                List<ProtectionEntry> referencers = this.listAllDependingEntries(entry, cw);
                for (ProtectionEntry ref : referencers) {
                    if (ref.getPropertyId() == null) {
                        toDelete = entry;
                        break outerLoop;
                    }
                    if (!ref.equals(entry)) {
                        if (!entries.contains(ref)) {
                            toDelete = entry;
                            break outerLoop;
                        }
                    }
                }
            }
            if (toDelete != null) {
                entries.remove(toDelete);
            }
        } while (toDelete != null);
    }

    private void populateList(List<ProtectionEntry> entries, List<ProtectionEntry> candidates, ConnectionWrapper cw) throws SQLException {
        List<ProtectionEntry> nuCandidates = new ArrayList<ProtectionEntry>();
        for (ProtectionEntry pe : candidates) {
            List<ProtectionEntry> tmp = listAllDependentEntries(pe, cw);
            for (ProtectionEntry tmpEntr : tmp) {
                if (!candidates.contains(tmpEntr) && !entries.contains(tmpEntr)) {
                    nuCandidates.add(tmpEntr);
                }
            }
        }
        if (nuCandidates.size() > 0) {
            entries.addAll(nuCandidates);
            populateList(entries, nuCandidates, cw);
        }
    }

    /**
	 * Get a list of all objects that are references by the given entry.
	 * 
	 * @param entry
	 * @param cw
	 * @return
	 * @throws SQLException
	 */
    private List<ProtectionEntry> listAllDependentEntries(ProtectionEntry entry, ConnectionWrapper cw) throws SQLException {
        ArrayList<ProtectionEntry> res = new ArrayList<ProtectionEntry>();
        StringBuffer statement = new StringBuffer(100);
        statement.append("SELECT PROPERTY_TABLE, PROPERTY_ID FROM ");
        statement.append(Defaults.HAS_A_TABLENAME);
        statement.append(" WHERE OWNER_TABLE = ? AND OWNER_ID = ?");
        PreparedStatement ps = cw.prepareStatement(statement.toString());
        ps.setString(1, entry.getPropertyTableName());
        ps.setLong(2, entry.getPropertyId());
        Tools.logFine(ps);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProtectionEntry nuEntry = new ProtectionEntry(rs.getString(1), rs.getLong(2));
                res.add(nuEntry);
            }
        } finally {
            ps.close();
        }
        return res;
    }

    /**
	 * Get a list of all objects that references the given entry.
	 * 
	 * @param entry
	 * @param cw
	 * @return
	 * @throws SQLException
	 */
    private List<ProtectionEntry> listAllDependingEntries(ProtectionEntry entry, ConnectionWrapper cw) throws SQLException {
        List<ProtectionEntry> res = null;
        int index = cacheKey.indexOf(entry);
        if (index < 0) {
            res = new ArrayList<ProtectionEntry>();
            StringBuffer statement = new StringBuffer(100);
            statement.append("SELECT OWNER_TABLE, OWNER_ID FROM ");
            statement.append(Defaults.HAS_A_TABLENAME);
            statement.append(" WHERE PROPERTY_TABLE = ? AND PROPERTY_ID = ?");
            PreparedStatement ps = cw.prepareStatement(statement.toString());
            ps.setString(1, entry.getPropertyTableName());
            ps.setLong(2, entry.getPropertyId());
            Tools.logFine(ps);
            try {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ProtectionEntry nuEntry = new ProtectionEntry(rs.getString(1), rs.getLong(2));
                    res.add(nuEntry);
                }
            } finally {
                ps.close();
            }
            cacheKey.add(entry);
            cacheValue.add(res);
        } else {
            res = cacheValue.get(index);
        }
        return res;
    }
}
