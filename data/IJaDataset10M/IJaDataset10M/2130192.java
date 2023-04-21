package pub.db.command;

import java.sql.*;

public class AddComment {

    private pub.db.PubConnection conn;

    private String _table_name;

    private int _row_id;

    private String _comment_type;

    private String _comment;

    private int _entered_by;

    private int _comment_id;

    public AddComment() {
        _table_name = null;
        _row_id = 0;
        _comment_type = "COMMENT";
        _comment = null;
        _entered_by = 0;
        _comment_id = 0;
    }

    public void setConnection(pub.db.PubConnection conn) {
        this.conn = conn;
    }

    public void setTableName(String table_name) {
        _table_name = table_name;
    }

    public void setTableRowId(int row_id) {
        _row_id = row_id;
    }

    public void setCommentType(String type) {
        _comment_type = type;
    }

    public void setComment(String comment) {
        _comment = comment;
    }

    public void setEnteredBy(int user_id) {
        _entered_by = user_id;
    }

    public void initialize() {
    }

    public void execute() {
        try {
            String query = "INSERT into pub_comment" + "(table_name, table_id, comment_type, " + " comment, entered_by, " + " date_entered) " + "VALUES (?, ?, ?, ?, ?, curdate())";
            PreparedStatement stmt = conn.prepareStatement(query);
            try {
                stmt.setString(1, _table_name);
                stmt.setInt(2, _row_id);
                stmt.setString(3, _comment_type);
                stmt.setString(4, _comment);
                stmt.setInt(5, _entered_by);
                stmt.executeUpdate();
                setCommentId(conn);
            } finally {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCommentId(pub.db.PubConnection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        try {
            ResultSet rs = stmt.executeQuery("select LAST_INSERT_ID() from pub_comment limit 1");
            if (rs.next()) {
                _comment_id = rs.getInt(1);
            } else {
                throw new RuntimeException("impossible situation detected");
            }
        } finally {
            stmt.close();
        }
    }

    public int getCommentId() {
        return _comment_id;
    }
}
