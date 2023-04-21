package com.pgol;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.net.*;

public class CollectorBase {

    public CollectorBase() {
    }

    static Connection con;

    static Connection removerCon;

    static {
        try {
            Class c = Class.forName("com.mysql.jdbc.Driver");
            java.util.Properties p = new java.util.Properties();
            p.load(c.getResourceAsStream("/config.txt"));
            System.out.println(p.getProperty("url"));
            String url = "jdbc:mysql://localhost/agonline";
            con = DriverManager.getConnection(url, "root", "liliping");
            removerCon = DriverManager.getConnection(url, "root", "liliping");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void remove(int sid, String referer, String imageUrl) {
        try {
            String sql = ("DELETE FROM source WHERE sid =" + sid);
            System.out.println("Delete sql = " + sql);
            System.out.println("Referer=" + referer);
            System.out.println("imageUrl" + imageUrl);
            Statement st = con.createStatement();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class Channel {

        String channel;

        int cid;

        Channel(int cid, String channel) {
            this.cid = cid;
            this.channel = channel;
        }
    }

    public static java.util.List<Channel> queryChannel() {
        java.util.List<Channel> ret = new java.util.ArrayList<Channel>();
        try {
            String sql = ("Select cid, channel From Channel Order by channel");
            System.out.println("sql=" + sql);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Channel c = new Channel(rs.getInt(1), rs.getString(2));
                ret.add(c);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    static int isValid;

    public static void main(String args[]) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
                removerCon.close();
            } catch (Exception e) {
            }
        }
    }

    public static void main1(String args[]) throws Exception {
        String dir = ".";
        if (args.length > 0) dir = args[0];
        String sql = "Select base, img, sid From source ORDER BY SID";
        System.out.println("sql=" + sql);
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            final String imageUrl = rs.getString(2);
            final String referer = rs.getString(1);
            final int sid = rs.getInt(3);
            isValid = 0;
            Thread timingThread = new Thread() {

                public void run() {
                    try {
                        final WebClient wc = new WebClient();
                        wc.addRequestHeader("Referer", referer);
                        WebResponse wr = wc.getPage(imageUrl).getWebResponse();
                        if (wr.getContentType().equals("text/html")) {
                            isValid = 0;
                        } else {
                            isValid = 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        isValid = 0;
                    }
                }
            };
            timingThread.start();
            System.out.println("sid=" + sid);
            timingThread.join(10000);
            if (timingThread.isAlive() || isValid == 0) {
                timingThread.stop();
                remove(sid, referer, imageUrl);
            }
        }
        rs.close();
        st.close();
    }
}
