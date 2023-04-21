package org.jwaim.core.storage.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jwaim.core.interfaces.Module;

/**
 * Simple util methods which help building
 * database functionality 
 */
final class DatabaseUtils {

    private static final class PriorityComparator implements Comparator<Module> {

        public int compare(Module o1, Module o2) {
            return o1.getPriority() - o2.getPriority();
        }
    }

    static final <T extends Module> void sortInstancedByPriority(List<T> ret) {
        Collections.sort(ret, new PriorityComparator());
    }

    static final String generateIn(Collection<Integer> collection) {
        if (collection == null) return null;
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (Integer i : collection) {
            if (!first) b.append(','); else first = false;
            b.append(i.intValue());
        }
        if (collection.size() == 1) {
            return " = " + b.toString() + " ";
        } else return " in (" + b.toString() + ") ";
    }

    static final void executeUpdate(Collection<String> queries, DBConnector connector) throws IOException {
        Connection con = null;
        Statement st = null;
        try {
            con = connector.getDB();
            con.setAutoCommit(false);
            st = con.createStatement();
            for (String s : queries) st.executeUpdate(s);
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new IOException(e.getMessage());
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ignore) {
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ignore) {
                }
            }
        }
    }
}
