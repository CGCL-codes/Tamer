package com.Team4.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.Team4.model.Item;

public class Items {

    public Items() {
    }

    private Connection getConnection() throws Exception {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/cs420");
        return ds.getConnection();
    }

    /**
	 * Authenticates a user.
	 * 
	 * @param userId -
	 *            The user ID.
	 * @param password -
	 *            The password.
	 * @return true if the user ID and password are valid.
	 * @throws Exception
	 */
    public ArrayList getRandomItems() throws Exception {
        ArrayList items = new ArrayList();
        Connection con = getConnection();
        Item item = null;
        try {
            PreparedStatement ps = con.prepareStatement("select id,name,disc,price,quantity from item_info limit 10");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));
                item.setDisc(rs.getString("disc"));
                item.setPrice(rs.getString("price"));
                item.setQuantity(rs.getInt("quantity"));
                items.add(item);
            }
            rs.close();
            ps.close();
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return items;
    }
}
