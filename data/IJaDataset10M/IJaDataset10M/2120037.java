package pdfdb.data.db;

import java.sql.*;
import pdfdb.structure.*;

/** Provides access to indexes to the database layer and allows public
 * indexing classes limited ability to save indexes.
 * @author ug22cmg */
public class IndexProvider {

    /** Performs the database insert of an index.
     * @param conn The connection to use.
     * @param index The index to insert.
     * @param region The region to insert into.
     * @throws java.sql.SQLException If an error occurs. */
    private static void addIndex(Connection conn, Index index, Region region) throws SQLException {
        String sql = "INSERT INTO Indexes(WordId, RegionId, " + "WordPosition) VALUES(?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            statement.setInt(1, index.getWord().getWordId());
            statement.setInt(2, region.getRegionId());
            statement.setInt(3, index.getPosition());
            statement.executeUpdate();
        } finally {
            DatabaseConnection.close(statement);
        }
    }

    /** Public accessor to add the specified set of words into the specified
     *  region.
     * @param words The words to add.
     * @param region The region to add to.
     * @throws java.sql.SQLException If an error occurs. */
    public static void addIndexes(String[] words, Region region) throws SQLException {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getNewConnection();
            addIndexes(conn, words, region);
        } finally {
            DatabaseConnection.close(conn);
        }
    }

    /** Add indexes using a connection.
     * @param conn The connection to use.
     * @param words The words to add.
     * @param region The region to add to.
     * @throws java.sql.SQLException If an error occurs. */
    static void addIndexes(Connection conn, String[] words, Region region) throws SQLException {
        if (region == null) {
            throw new IllegalArgumentException();
        }
        if (words == null) {
            return;
        }
        int len = words.length;
        for (int i = 0; i < len; i++) {
            String tag = words[i];
            if (WordProvider.isWordUsed(tag)) {
                Word word = WordProvider.convertToWord(conn, tag);
                if (word != null) {
                    Index index = new Index(-1, i, word);
                    IndexProvider.addIndex(conn, index, region);
                }
            }
        }
    }

    /** Gets indexes for a specified region.
     * @param conn The connection to use.
     * @param region The region to get indexes for.
     * @return The array of indexes.
     * @throws java.sql.SQLException If an error occurs. */
    static Index[] getIndexesForRegion(Connection conn, Region region) throws SQLException {
        String sql = "SELECT IndexId, Words.WordId as Wid, Tag, " + "WordPosition FROM Indexes, Words WHERE " + "(Indexes.WordId = Words.WordId) AND (Indexes.RegionId = ?)";
        ResultSet rs = null;
        Index[] indexes = null;
        int i = 0;
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, region.getRegionId());
            rs = statement.executeQuery();
            rs.setFetchDirection(ResultSet.TYPE_SCROLL_INSENSITIVE);
            rs.afterLast();
            rs.previous();
            indexes = new Index[rs.getRow()];
            rs.beforeFirst();
            while (rs.next()) {
                int indexId = rs.getInt("IndexId");
                int pos = rs.getInt("WordPosition");
                Word word = new Word(rs.getInt("Wid"), rs.getString("Tag"));
                indexes[i] = new Index(indexId, pos, word);
                i++;
            }
            return indexes;
        } finally {
            DatabaseConnection.close(statement, rs);
        }
    }
}
