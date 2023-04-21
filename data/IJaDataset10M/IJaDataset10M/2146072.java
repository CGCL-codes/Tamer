package com.example.possessed.playerinfo;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import tony.db.DBUtil;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class RandomNumbers extends Panel implements Serializable {

    private String userid;

    public RandomNumbers(String userid) {
        this.userid = userid;
    }

    @Override
    public void attach() {
        Connection conn = DBUtil.getConnection();
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            prep = conn.prepareStatement("select uid,userid,created,text from randomnumbers " + "where userid=? order by userid,created desc limit 10");
            prep.setString(1, userid);
            rs = prep.executeQuery();
            DateFormat fmt = SimpleDateFormat.getDateTimeInstance();
            while (rs.next()) {
                Panel itm = new Panel();
                Label lbl = new Label();
                lbl.setValue(rs.getString("text"));
                lbl.setContentMode(Label.CONTENT_XHTML);
                itm.addComponent(lbl);
                addComponent(itm);
            }
        } catch (SQLException err) {
            err.printStackTrace();
        } finally {
            DBUtil.close(rs);
            DBUtil.close(prep);
            DBUtil.close(conn);
        }
    }
}
