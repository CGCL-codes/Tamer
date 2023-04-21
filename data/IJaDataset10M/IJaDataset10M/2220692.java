package com.nullfish.lib.vfs.tag_db.command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.nullfish.lib.vfs.VFile;

public class FileCopiedCommand extends AbstractTagDataBaseCommand {

    static final String PRE_QUERY = "DELETE FROM file_tag ft1 " + "WHERE " + "ft1.file = ? " + "AND EXISTS (" + "SELECT * FROM file_tag ft2 WHERE ft2.file = ? AND ft2.tag = ft1.tag)";

    private static final String QUERY = "INSERT INTO file_tag (file, directory, tag) " + "SELECT ?, ?, tag FROM file_tag " + "WHERE file = ?";

    private VFile from;

    private VFile to;

    public FileCopiedCommand(VFile from, VFile to) {
        this.from = from;
        this.to = to;
    }

    protected PreparedStatement getStatement(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(QUERY);
        stmt.setString(1, to.getSecurePath());
        stmt.setString(2, to.getParent() != null ? to.getParent().getSecurePath() : null);
        stmt.setString(3, from.getSecurePath());
        return stmt;
    }

    protected PreparedStatement getPreStatement(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(PRE_QUERY);
        stmt.setString(1, to.getSecurePath());
        stmt.setString(2, from.getSecurePath());
        return stmt;
    }
}
