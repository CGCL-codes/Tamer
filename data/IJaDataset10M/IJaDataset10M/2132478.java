package edu.calstatela.coolstatela.workflow;

import java.sql.*;
import java.util.*;

public class BookmarkAccessor extends DBBean {

    PreparedStatement insertOne;

    PreparedStatement updateOne;

    PreparedStatement deleteOne;

    PreparedStatement getAll;

    PreparedStatement getOne;

    PreparedStatement getWhole;

    PreparedStatement send;

    public BookmarkAccessor() {
        try {
            Connection c = DriverManager.getConnection(url, user, pass);
            insertOne = c.prepareStatement("INSERT INTO userPreference (title,  url, description,  saveDate, changeDate, user, lastUser, status) values (?,?,?,?,?,?,?,'New Arrival')");
            deleteOne = c.prepareStatement("DELETE FROM userPreference WHERE preID = ? ");
            updateOne = c.prepareStatement("UPDATE userPreference SET  changeDate=? , user=? , lastUser=?, status=? , Comment=? WHERE preID = ? ");
            getAll = c.prepareStatement("SELECT storyID, slug ,subslug, creationdata ,userID FROM story WHERE  story.userID = ? ");
            getOne = c.prepareStatement("SELECT * FROM userPreference where preID = ? ");
            getWhole = c.prepareStatement("SELECT * FROM userPreference");
            send = c.prepareStatement("UPDATE userPreference SET  user=?  WHERE preID = ? ");
        } catch (Exception e) {
        }
    }

    public void close() {
        try {
            getAll.close();
            insertOne.close();
            updateOne.close();
            deleteOne.close();
            getOne.close();
        } catch (SQLException e) {
        }
    }

    public Bookmark find(int id) {
        Bookmark bookmark = null;
        try {
            getOne.setInt(1, id);
            ResultSet rs = getOne.executeQuery();
            while (rs.next()) {
                bookmark = new Bookmark(rs.getInt("preID"), rs.getString("title"), rs.getString("url"), rs.getString("description"), rs.getDate("saveDate"), rs.getInt("lastUser"), rs.getDate("changeDate"), rs.getString("status"), rs.getString("Comment"));
            }
            rs.close();
        } catch (SQLException e) {
            exceptionRaised = true;
            exceptionMsg = e.getMessage();
        }
        return bookmark;
    }

    public List findAll(int userId) {
        List<Bookmark> bookmarks;
        bookmarks = new ArrayList<Bookmark>();
        try {
            getAll.setInt(1, userId);
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                bookmarks.add(new Bookmark(rs.getInt("storyID"), rs.getString("slug"), rs.getString("subslug"), rs.getString("creationdata"), rs.getInt("userID")));
            }
            rs.close();
        } catch (SQLException e) {
            exceptionRaised = true;
            exceptionMsg = e.getMessage();
        }
        return bookmarks;
    }

    public List findWhole() {
        List<Bookmark> bookmarks;
        bookmarks = new ArrayList<Bookmark>();
        try {
            ResultSet rs = getWhole.executeQuery();
            while (rs.next()) {
                bookmarks.add(new Bookmark(rs.getInt("preID"), rs.getString("title"), rs.getString("url"), rs.getString("description"), rs.getDate("saveDate"), rs.getInt("user")));
            }
            rs.close();
        } catch (SQLException e) {
            exceptionRaised = true;
            exceptionMsg = e.getMessage();
        }
        return bookmarks;
    }

    public boolean delete(int id) {
        try {
            deleteOne.setInt(1, id);
            deleteOne.execute();
        } catch (SQLException e) {
            exceptionRaised = true;
            exceptionMsg = e.getMessage();
        }
        return false;
    }

    public boolean insert(String bookmarkName, String url) {
        try {
            System.out.println(">>>in accessor>" + bookmarkName + "---" + url + "---");
            insertOne.setString(1, bookmarkName);
            insertOne.setString(2, url);
            insertOne.setString(3, bookmarkName);
            java.util.GregorianCalendar x = new java.util.GregorianCalendar();
            java.sql.Date today = new java.sql.Date(x.getTimeInMillis());
            insertOne.setDate(4, today);
            insertOne.setDate(5, today);
            insertOne.setInt(6, 1);
            insertOne.setInt(7, 1);
            insertOne.execute();
            return true;
        } catch (SQLException e) {
            exceptionRaised = true;
            exceptionMsg = e.getMessage();
            return false;
        }
    }

    public boolean update(Bookmark b) {
        try {
            java.sql.Date doe = new java.sql.Date(b.getDateMod().getTime());
            updateOne.setDate(1, doe);
            updateOne.setInt(2, b.getUser());
            updateOne.setInt(3, b.getPrEditor());
            updateOne.setString(4, b.getStatus());
            updateOne.setString(5, b.getComment());
            updateOne.setInt(6, b.getId());
            updateOne.execute();
            return true;
        } catch (SQLException e) {
            exceptionRaised = true;
            exceptionMsg = e.getMessage();
            return false;
        }
    }

    public boolean send(int pid) {
        try {
            send.setInt(1, pid);
            send.execute();
            return true;
        } catch (SQLException e) {
            exceptionRaised = true;
            exceptionMsg = e.getMessage();
            return false;
        }
    }
}
