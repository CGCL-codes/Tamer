package com.naszetatry.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.naszetatry.beans.Comment;

public class CommentLogic implements DatabaseConstants {

    private List<Comment> list;

    /**
	 * Domy�lny konstruktor.
	 */
    public CommentLogic() {
        loadComments();
    }

    /**
	 * Pobiera komentarze dla danego szlaku.
	 * @param pathId ID szlaku
	 * @return lista komentarzy dla danego szlaku
	 */
    public List<Comment> getPathComments(int pathId) {
        List<Comment> comments = new ArrayList<Comment>();
        Iterator<Comment> it = list.iterator();
        while (it.hasNext()) {
            Comment c = it.next();
            if (c.getPath() == pathId) comments.add(c);
        }
        return comments;
    }

    /**
	 * Pobiera wszystkie komentarze dla danego punktu na mapie.
	 * @param vertexId ID punktu na mapie
	 * @return lista komentarzy do danego punktu na mapie
	 */
    public List<Comment> getVertexComments(int vertexId) {
        List<Comment> comments = new ArrayList<Comment>();
        Iterator<Comment> it = list.iterator();
        while (it.hasNext()) {
            Comment c = it.next();
            if (c.getVertex() == vertexId) comments.add(c);
        }
        return comments;
    }

    /**
	 * Pobiera wszystkie komentarze danego u�ytkownika.
	 * @param userId ID u�ytkownika
	 * @return lista komentarzy danego u�ytkownika
	 */
    public List<Comment> getUserComments(int userId) {
        List<Comment> comments = new ArrayList<Comment>();
        Iterator<Comment> it = list.iterator();
        while (it.hasNext()) {
            Comment c = it.next();
            if (c.getUser() == userId) comments.add(c);
        }
        return comments;
    }

    /**
	 * Zapisuje komentarz do bazy danych.
	 * @param cmnt
	 */
    public void saveComment(Comment cmnt) {
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        try {
            connect = DriverManager.getConnection(DB);
            preparedStatement = connect.prepareStatement("INSERT INTO `mac_naszetatry`.`comment` (`user`, `vertex`, `path`, `date`, `text`) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, cmnt.getUser());
            preparedStatement.setInt(2, cmnt.getVertex());
            preparedStatement.setInt(3, cmnt.getPath());
            preparedStatement.setTimestamp(4, cmnt.getDate());
            preparedStatement.setString(5, cmnt.getText());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connect.close();
                preparedStatement.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        loadComments();
    }

    private void loadComments() {
        list = new ArrayList<Comment>();
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connect = DriverManager.getConnection(DB);
            preparedStatement = connect.prepareStatement("SELECT * FROM `mac_naszetatry`.`comment`");
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Comment baseComment = new Comment();
                initComment(baseComment, rs);
                list.add(baseComment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                connect.close();
                preparedStatement.close();
                if (rs != null) rs.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void initComment(Comment cmnt, ResultSet rs) throws SQLException {
        cmnt.setId(rs.getInt("idComment"));
        cmnt.setPath(rs.getInt("path"));
        cmnt.setVertex(rs.getInt("vertex"));
        cmnt.setUser(rs.getInt("user"));
        cmnt.setDate(rs.getTimestamp("date"));
        cmnt.setText(rs.getString("text"));
    }

    /**
	 * Test
	 * @param str
	 */
    @Deprecated
    public static void main(String[] str) {
        CommentLogic cl = new CommentLogic();
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        c1.setDate(new Timestamp(new Date().getTime()));
        c1.setPath(2);
        c1.setUser(1);
        c1.setText("Test comment 1.");
        c2.setDate(new Timestamp(new Date().getTime()));
        c2.setVertex(2);
        c2.setUser(1);
        c2.setText("Test comment 2.");
        cl.saveComment(c1);
        cl.saveComment(c2);
    }
}
