package com.piyosailing.jClubHouse.beans.files;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import com.piyosailing.ParameterIncorrectException;
import com.piyosailing.jClubHouse.IConst;
import com.piyosailing.jClubHouse.beans.*;

public class FileCategory extends BaseBean {

    private int id = 0;

    private int idType = 0;

    private String name = "";

    private String typeName = "";

    public int getId() {
        return id;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int value) {
        idType = value;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

    public FileCategory() {
    }

    public FileCategory(int itemId) throws SQLException, ParameterIncorrectException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet myResultSet = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(getStatement("FileCategory.GET_BY_ID"));
            statement.setInt(1, itemId);
            myResultSet = statement.executeQuery();
            if (myResultSet.next()) loadFromResultSet(myResultSet); else if (itemId != 0) throw new ParameterIncorrectException("FileCategory with id " + itemId + " not found");
        } catch (SQLException se) {
            throw se;
        } finally {
            if (myResultSet != null) myResultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
    }

    public FileCategory(ResultSet myResultSet) throws SQLException {
        loadFromResultSet(myResultSet);
    }

    private void loadFromResultSet(ResultSet myResultSet) throws SQLException {
        id = myResultSet.getInt("id");
        idType = myResultSet.getInt("idType");
        name = myResultSet.getString("name");
        typeName = myResultSet.getString("typeName");
    }

    public void store() throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet myResultSet = null;
        try {
            conn = getConnection();
            if (id == 0) statement = conn.prepareStatement(getStatement("FileCategory.ADD"), Statement.RETURN_GENERATED_KEYS); else statement = conn.prepareStatement(getStatement("FileCategory.EDIT"));
            statement.setInt(1, idType);
            statement.setString(2, name);
            if (id != 0) {
                statement.setInt(3, id);
            }
            statement.executeUpdate();
            if (id == 0) {
                myResultSet = statement.getGeneratedKeys();
                if (myResultSet.next()) {
                    id = myResultSet.getInt(1);
                }
            }
        } catch (SQLException se) {
            throw se;
        } finally {
            if (myResultSet != null) myResultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
    }

    public static List getAll() throws SQLException {
        List itemsList = new ArrayList();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet myResultSet = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(getStatement("FileCategory.GET_ALL"));
            myResultSet = statement.executeQuery();
            while (myResultSet.next()) {
                itemsList.add(new FileCategory(myResultSet));
            }
        } catch (SQLException se) {
            throw se;
        } finally {
            if (myResultSet != null) myResultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        return itemsList;
    }

    public static List getAll(String parLang, int parTypeId) throws SQLException {
        List itemsList = new ArrayList();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet myResultSet = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(getStatement("FileCategory.GET_BY_LANG_AND_TYPE"));
            statement.setString(1, parLang);
            statement.setInt(2, parTypeId);
            myResultSet = statement.executeQuery();
            while (myResultSet.next()) {
                itemsList.add(new FileCategory(myResultSet));
            }
        } catch (SQLException se) {
            throw se;
        } finally {
            if (myResultSet != null) myResultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        return itemsList;
    }

    public List getAvailability() throws SQLException {
        List itemsList = new ArrayList();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet myResultSet = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(getStatement("FileCategory.GET_TRANSLATIONS_BY_ID"));
            statement.setInt(1, id);
            myResultSet = statement.executeQuery();
            while (myResultSet.next()) {
                itemsList.add(new FileCatTranslation(myResultSet));
            }
        } catch (SQLException se) {
            throw se;
        } finally {
            if (myResultSet != null) myResultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
        return itemsList;
    }

    public void removeAllTranslation() throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet myResultSet = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(getStatement("FileCategory.REMOVE_ALL_TRANSLATION"));
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException se) {
            throw se;
        } finally {
            if (myResultSet != null) myResultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
    }

    public void addTranslation(FileCatTranslation translation) throws SQLException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet myResultSet = null;
        try {
            conn = getConnection();
            statement = conn.prepareStatement(getStatement("FileCategory.ADD_TRANSLATION"));
            statement.setInt(1, translation.getId());
            statement.setString(2, translation.getLangCode());
            statement.setString(3, translation.getName());
            statement.executeUpdate();
        } catch (SQLException se) {
            throw se;
        } finally {
            if (myResultSet != null) myResultSet.close();
            if (statement != null) statement.close();
            if (conn != null) conn.close();
        }
    }
}
