package dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import core.Document;
import dao.DocumentDAO;

/**
 * Contains the implementation of methods specified by the interface DocumentDAO
 * with the use of jdbc technology.
 * Handles connection to the database and data access to its tables (via SQL queries).
 * 
 * @author damo, anti
 * @version 0.1
 */
public class DocumentJdbcDAO implements DocumentDAO {

    private Connection connection;

    private Statement statement;

    private PreparedStatement getDocumentById, getDocumentByUrl, insertDocument, updateDocument, deleteDocument, findLastId;

    /**
	 * Class constructor.
	 * Creates the connection to the database using autenthication information from the
	 * Defaults class. Also creates all stored (prepared) SQL queries (statements).
	 */
    public DocumentJdbcDAO(Connection c) {
        try {
            connection = c;
            statement = connection.createStatement();
            getDocumentById = connection.prepareStatement("SELECT * FROM Documents WHERE id=?");
            getDocumentByUrl = connection.prepareStatement("SELECT * FROM Documents WHERE url=?");
            insertDocument = connection.prepareStatement("INSERT INTO Documents (id, url, title) VALUES (?,?,?)");
            updateDocument = connection.prepareStatement("UPDATE Documents SET url=?,title=? WHERE id=?");
            deleteDocument = connection.prepareStatement("DELETE FROM Documents WHERE id=?");
            findLastId = connection.prepareStatement("SELECT MAX(id) FROM Documents");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Document> getAllDocuments() {
        List<Document> list = new ArrayList<Document>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM Documents");
            while (rs.next()) list.add(new Document(rs.getInt(1), rs.getString(2), rs.getString(3)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Document getDocumentById(int id) {
        Document doc = null;
        try {
            getDocumentById.setInt(1, id);
            ResultSet rs = getDocumentById.executeQuery();
            while (rs.next()) doc = new Document(rs.getInt(1), rs.getString(2), rs.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public Document getDocumentByUrl(String url) {
        Document doc = null;
        try {
            getDocumentByUrl.setString(1, url);
            ResultSet rs = getDocumentByUrl.executeQuery();
            while (rs.next()) doc = new Document(rs.getInt(1), rs.getString(2), rs.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public int insertDocument(Document doc) {
        int last = 0;
        try {
            ResultSet lastId = findLastId.executeQuery();
            while (lastId.next()) last = lastId.getInt(1);
            last++;
            insertDocument.setInt(1, last);
            insertDocument.setString(2, doc.getUrl());
            insertDocument.setString(3, doc.getTitle());
            insertDocument.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return last;
    }

    public void updateDocument(Document doc) {
        try {
            updateDocument.setString(1, doc.getUrl());
            updateDocument.setString(2, doc.getTitle());
            updateDocument.setInt(3, doc.getId());
            updateDocument.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDocumentById(int id) {
        try {
            deleteDocument.setInt(1, id);
            deleteDocument.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void flushTable() {
        try {
            statement.executeUpdate("DELETE FROM Documents");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
