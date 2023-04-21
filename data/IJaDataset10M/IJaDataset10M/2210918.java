package ru.denn.eddah;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class responsible to work with Oracle database.
 * Only specific for Oracle RDBMS functions has implemented.
 */
final class DAOOracle extends DAO {

    DAOOracle(Connection connection) {
        super(connection);
    }

    void createArticle(Article article) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String query = "select Article_Seq.nextval from Dual";
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            rs.next();
            article.setId(rs.getString(1));
            rs.close();
            stmt.close();
            query = "insert into Articles " + "(Article_ID, Article_Text, Article_Title, Article_Author) " + "values (?, ?, ?, ?)";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, article.getId());
            stmt.setBytes(2, article.getText().getBytes());
            stmt.setBytes(3, article.getTitle().getBytes());
            stmt.setBytes(4, article.getAuthor().getBytes());
            stmt.executeUpdate();
            stmt.close();
            createKeywords(article);
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage());
        } finally {
            try {
                rs.close();
            } catch (Exception ex) {
            }
            try {
                stmt.close();
            } catch (Exception ex) {
            }
        }
    }
}
