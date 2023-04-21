package cn.edu.bit.ss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import cn.edu.bit.dto.WebPage;

@Component
public class UpdateDAO {

    private String userName;

    private String password;

    private String url;

    private Connection conn;

    private PreparedStatement pstmt;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public UpdateDAO(@Qualifier("userName") String userName, @Qualifier("password") String password, @Qualifier("url") String url) {
        this.userName = userName;
        this.password = password;
        this.url = url;
        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(WebPage webPage) {
        try {
            if (conn.isClosed()) {
                conn = DriverManager.getConnection(url, userName, password);
            }
            pstmt = conn.prepareStatement("UPDATE HugeTable SET title=?,content=? WHERE url=?");
            pstmt.setString(1, webPage.getTitle());
            pstmt.setString(2, webPage.getContent());
            pstmt.setString(3, webPage.getURL());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dispose() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return userName + password + url;
    }

    public ArrayList<String> getSomeUrls(int offset, int limit) {
        ResultSet rs;
        try {
            if (conn.isClosed()) {
                conn = DriverManager.getConnection(url, userName, password);
            }
            pstmt = conn.prepareStatement("SELECT url FROM HugeTable ORDER BY id  LIMIT ?,?");
            rs = pstmt.executeQuery();
            ArrayList<String> strQueue = new ArrayList<String>(10);
            while (rs.next()) {
                strQueue.add(rs.getString(1));
            }
            return strQueue;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
