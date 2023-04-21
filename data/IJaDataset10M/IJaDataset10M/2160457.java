package com.windupurnomo.si.perpustakaan.dao;

import com.windupurnomo.si.perpustakaan.connection.DBConnection;
import com.windupurnomo.si.perpustakaan.domain.Book;
import com.windupurnomo.si.perpustakaan.helper.ErrorMessage;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Windu Purnomo
 */
public class BookDaoImpl implements BookDao {

    private Connection conn;

    private PreparedStatement pStatement;

    private ResultSet resultSet;

    private DBConnection dBConnection;

    public BookDaoImpl(DBConnection dBConnection) {
        this.dBConnection = dBConnection;
    }

    @Override
    public boolean saveBook(Book book) {
        conn = dBConnection.getConnection();
        boolean flag = false;
        try {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO BOOK (CODE, ISBN, TITLE, AUTHOR, " + "EDITION, ID_TYPE, DATE_IN, YEAR, ID_DURATION, ID_FINE, LOCATION) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, book.getCode());
            pStatement.setString(2, book.getIsbn());
            pStatement.setString(3, book.getTitle());
            pStatement.setString(4, book.getAuthor());
            pStatement.setInt(5, book.getEdition());
            pStatement.setInt(6, book.getBookType().getId());
            pStatement.setDate(7, new Date(book.getDateIn().getTime()));
            pStatement.setInt(8, book.getYear());
            pStatement.setInt(9, book.getDuration().getId());
            pStatement.setInt(10, book.getFine().getId());
            pStatement.setInt(11, book.getLocation());
            pStatement.executeUpdate();
            conn.setAutoCommit(true);
            flag = true;
        } catch (Exception ex) {
            ErrorMessage.errorDao("saveBook", ex.getMessage());
            flag = false;
        } finally {
            try {
                if (conn != null) conn.close();
                if (pStatement != null) pStatement.close();
            } catch (SQLException se) {
                ErrorMessage.errorDao("finally saveBook", se.getMessage());
            }
            return flag;
        }
    }

    @Override
    public boolean updateBook(Book book) {
        conn = dBConnection.getConnection();
        boolean flag = false;
        try {
            conn.setAutoCommit(false);
            String sql = "UPDATE BOOK SET CODE = ?, ISBN = ?, TITLE = ?, AUTHOR = ?, " + "EDITION = ?, ID_TYPE = ?, DATE_IN = ?, YEAR = ?, " + "ID_DURATION = ?, ID_FINE = ?, LOCATION = ? WHERE ID = ?";
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, book.getCode());
            pStatement.setString(2, book.getIsbn());
            pStatement.setString(3, book.getTitle());
            pStatement.setString(4, book.getAuthor());
            pStatement.setInt(5, book.getEdition());
            pStatement.setInt(6, book.getBookType().getId());
            pStatement.setDate(7, new Date(book.getDateIn().getTime()));
            pStatement.setInt(8, book.getYear());
            pStatement.setInt(9, book.getDuration().getId());
            pStatement.setInt(10, book.getFine().getId());
            pStatement.setInt(11, book.getLocation());
            pStatement.setInt(12, book.getId());
            pStatement.executeUpdate();
            conn.setAutoCommit(true);
            flag = true;
        } catch (Exception ex) {
            ErrorMessage.errorDao("updateBook", ex.getMessage());
            flag = false;
        } finally {
            try {
                if (conn != null) conn.close();
                if (pStatement != null) pStatement.close();
            } catch (SQLException se) {
                ErrorMessage.errorDao("finally updateBook", se.getMessage());
            }
            return flag;
        }
    }

    @Override
    public boolean deleteBook(int idBook) {
        conn = dBConnection.getConnection();
        boolean flag = false;
        try {
            conn.setAutoCommit(false);
            String sql = "DELETE FROM BOOK WHERE ID = ?";
            pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, idBook);
            pStatement.executeUpdate();
            conn.setAutoCommit(true);
            flag = true;
        } catch (Exception ex) {
            ErrorMessage.errorDao("deleteBook", ex.getMessage());
            flag = false;
        } finally {
            try {
                if (conn != null) conn.close();
                if (pStatement != null) conn.close();
            } catch (SQLException se) {
                ErrorMessage.errorDao("finally deleteBook", se.getMessage());
            }
            return flag;
        }
    }

    @Override
    public ArrayList<Book> getAllBook() {
        conn = dBConnection.getConnection();
        ArrayList<Book> books = new ArrayList<Book>();
        try {
            conn.setAutoCommit(false);
            String sql = "SELECT * FROM BOOK";
            pStatement = conn.prepareStatement(sql);
            resultSet = pStatement.executeQuery();
            conn.setAutoCommit(true);
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("ID"));
                book.setCode(resultSet.getString("CODE"));
                book.setIsbn(resultSet.getString("ISBN"));
                book.setTitle(resultSet.getString("TITLE"));
                book.setAuthor(resultSet.getString("AUTHOR"));
                book.setEdition(resultSet.getInt("EDITION"));
                book.setBookType(new BookTypeDaoImpl(dBConnection).getBookTypeById(resultSet.getInt("ID_TYPE")));
                book.setDateIn(resultSet.getDate("DATE_IN"));
                book.setYear(resultSet.getInt("YEAR"));
                book.setDuration(new DurationDaoImpl(dBConnection).getDurationById(resultSet.getInt("ID_DURATION")));
                book.setFine(new FineDaoImpl(dBConnection).getFineById(resultSet.getInt("ID_FINE")));
                book.setLocation(resultSet.getInt("LOCATION"));
                books.add(book);
            }
        } catch (Exception ex) {
            ErrorMessage.errorDao("getAllBook", ex.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
                if (pStatement != null) pStatement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException se) {
                ErrorMessage.errorDao("finally getAllBook", se.getMessage());
            }
            return books;
        }
    }

    @Override
    public Book getBookById(int idBook) {
        conn = dBConnection.getConnection();
        Book book = new Book();
        try {
            conn.setAutoCommit(false);
            String sql = "SELECT * FROM BOOK WHERE ID = ?";
            pStatement = conn.prepareStatement(sql);
            pStatement.setInt(1, idBook);
            resultSet = pStatement.executeQuery();
            conn.setAutoCommit(true);
            while (resultSet.next()) {
                book.setId(resultSet.getInt("ID"));
                book.setCode(resultSet.getString("CODE"));
                book.setIsbn(resultSet.getString("ISBN"));
                book.setTitle(resultSet.getString("TITLE"));
                book.setAuthor(resultSet.getString("AUTHOR"));
                book.setEdition(resultSet.getInt("EDITION"));
                book.setBookType(new BookTypeDaoImpl(dBConnection).getBookTypeById(resultSet.getInt("ID_TYPE")));
                book.setDateIn(new java.util.Date(resultSet.getDate("DATE_IN").getTime()));
                book.setYear(resultSet.getInt("YEAR"));
                book.setDuration(new DurationDaoImpl(dBConnection).getDurationById(resultSet.getInt("ID_DURATION")));
                book.setFine(new FineDaoImpl(dBConnection).getFineById(resultSet.getInt("ID_FINE")));
                book.setLocation(resultSet.getInt("LOCATION"));
            }
        } catch (Exception ex) {
            ErrorMessage.errorDao("getBookById", ex.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
                if (pStatement != null) pStatement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException se) {
                ErrorMessage.errorDao("finally getBookById", se.getMessage());
            }
            return book;
        }
    }

    @Override
    public ArrayList<Book> searchBook(String keyWord) {
        conn = dBConnection.getConnection();
        ArrayList<Book> books = new ArrayList<Book>();
        keyWord = "%" + keyWord + "%";
        try {
            conn.setAutoCommit(false);
            String sql = "SELECT * FROM BOOK WHERE CODE LIKE ? OR ISBN LIKE ? " + "OR TITLE LIKE ? OR AUTHOR LIKE ? OR EDITION LIKE ?";
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, keyWord);
            pStatement.setString(2, keyWord);
            pStatement.setString(3, keyWord);
            pStatement.setString(4, keyWord);
            pStatement.setString(5, keyWord);
            resultSet = pStatement.executeQuery();
            conn.setAutoCommit(true);
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("ID"));
                book.setCode(resultSet.getString("CODE"));
                book.setIsbn(resultSet.getString("ISBN"));
                book.setTitle(resultSet.getString("TITLE"));
                book.setAuthor(resultSet.getString("AUTHOR"));
                book.setEdition(resultSet.getInt("EDITION"));
                book.setBookType(new BookTypeDaoImpl(dBConnection).getBookTypeById(resultSet.getInt("ID_TYPE")));
                book.setDateIn(resultSet.getDate("DATE_IN"));
                book.setYear(resultSet.getInt("YEAR"));
                book.setDuration(new DurationDaoImpl(dBConnection).getDurationById(resultSet.getInt("ID_DURATION")));
                book.setFine(new FineDaoImpl(dBConnection).getFineById(resultSet.getInt("ID_FINE")));
                book.setLocation(resultSet.getInt("LOCATION"));
                books.add(book);
            }
        } catch (Exception ex) {
            ErrorMessage.errorDao("getAllBook", ex.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
                if (pStatement != null) pStatement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException se) {
                ErrorMessage.errorDao("finally getAllBook", se.getMessage());
            }
            return books;
        }
    }
}
