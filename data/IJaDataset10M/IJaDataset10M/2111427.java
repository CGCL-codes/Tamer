package com.apelon.dtswf.db.assignment.dao;

import com.apelon.common.util.db.dao.SQL2KGeneralDAO;
import com.apelon.common.util.db.dao.CacheGeneralDAO;
import com.apelon.common.util.db.DBSystemConfig;
import com.apelon.dtswf.data.impl.NoteItemImpl;
import com.apelon.dtswf.db.assignment.WorkflowLookup;
import java.sql.*;
import java.util.ArrayList;

/**
 * This is an extension of SQL2KGeneralDAO. This implements DTSWFGeneralDAO for SQL2000 Database.
 *
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 * @author Apelon Inc.
 * @since 3.2
 */
public class DTSWFCacheGeneralDAO extends CacheGeneralDAO implements DTSWFGeneralDAO {

    public DTSWFCacheGeneralDAO(DBSystemConfig daoConfig) {
        super(daoConfig);
    }

    /**
   * Returns current id for the identity column of the table
   *
   * @param conn      DB Connection
   * @param tableName Table name
   * @return long id or 0 if it is not found
   * @throws java.sql.SQLException Any SQL Error
   */
    public long getCurrentId(Connection conn, String tableName) throws SQLException {
        long id = 0;
        String sql = "SELECT LAST_IDENTITY() FROM " + tableName;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            if (rset.next()) {
                id = rset.getLong(1);
            }
        } finally {
            closeStmt(stmt);
        }
        return id;
    }

    /**
   * Inserts a note by a user for an assignment
   *
   * @param conn        DB Connection
   * @param asgmtId     Assignment Id for Note
   * @param userId      User Id of Note
   * @param createdTS Note create timestamp
   * @param note        Note text
   * @return long Assignment Note Id or 0 if note is not inserted
   * @throws java.sql.SQLException Any SQL Error
   */
    public long insertAssignmentNote(Connection conn, long asgmtId, int userId, Timestamp createdTS, String note) throws SQLException {
        if (note == null) {
            return 0;
        }
        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        long asgmtNoteId = 0;
        PreparedStatement insertPS = null;
        try {
            String sql = getStatement("ASSIGNMENT", "INSERT_NOTE");
            insertPS = conn.prepareStatement(sql);
            insertPS.setLong(1, asgmtId);
            insertPS.setInt(2, userId);
            insertPS.setTimestamp(3, createdTS);
            byte[] bytes = note.getBytes();
            if (bytes == null) {
                insertPS.setNull(4, Types.LONGVARBINARY);
            } else {
                insertPS.setBytes(4, bytes);
            }
            insertPS.executeUpdate();
            insertPS.close();
            asgmtNoteId = getCurrentId(conn, "DTSWF_ASSIGNMENT_NOTE");
        } finally {
            closeStmt(insertPS);
            conn.setAutoCommit(autoCommit);
        }
        return asgmtNoteId;
    }

    /**
   * Returns note items for an assignment id
   * @param conn  DB Connection
   * @param lookup Workflow lookup
   * @param asgmtId long Assignment ID
   * @return Array of note items if found, else empty array
   * @throws java.sql.SQLException Any SQL Error
   */
    public NoteItemImpl[] getAssignmentNotes(Connection conn, WorkflowLookup lookup, long asgmtId) throws SQLException {
        String sql = getStatement("ASSIGNMENT", "SELECT_NOTES");
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, asgmtId);
        try {
            ResultSet rset = ps.executeQuery();
            ArrayList list = new ArrayList();
            while (rset.next()) {
                NoteItemImpl item = new NoteItemImpl(lookup.getUserName(rset.getInt(1)), new Date(rset.getTimestamp(2).getTime()), new String(rset.getBytes(3)));
                list.add(item);
            }
            if (list.size() > 0) {
                NoteItemImpl[] items = new NoteItemImpl[list.size()];
                list.toArray(items);
                return items;
            }
        } finally {
            closeStmt(ps);
        }
        return new NoteItemImpl[0];
    }

    /**
   * Closes the open statements
   * @throws java.sql.SQLException
   */
    public void close() throws SQLException {
    }

    private void closeStmt(Statement stmt) throws SQLException {
        if (stmt != null) {
            stmt.close();
        }
    }
}
