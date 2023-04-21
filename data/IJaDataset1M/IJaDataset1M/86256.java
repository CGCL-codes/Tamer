package fi.iki.kaila.miniproject;

import fi.iki.kaila.kacoma.*;
import java.sql.*;
import java.util.*;

public class Maakunnat extends MiniprojectPage {

    Vector maakunnat = null;

    /**
    * retrieve page and book constants from storage
    * @param database open connection
    * @param book name
    * @param name of page
    */
    public void setPage(Connection con, String library, String book, String page, String lang) throws KaCoMaException {
        String sql, tmp1, tmp2;
        maakunnat = null;
        Hashtable h = null;
        try {
            Statement stmt = con.createStatement();
            sql = "select maakno," + lang + " from maakunnat order by " + lang;
            ResultSet rs = stmt.executeQuery(sql);
            maakunnat = new Vector();
            while (rs.next()) {
                h = new Hashtable();
                h.put("maakno", rs.getString(1));
                h.put("name", rs.getString(2));
                maakunnat.add(h);
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
            throw new KaCoMaException("Maakunnat: " + se.getMessage());
        }
    }

    /**
    * retrieve # of lines in repeat table
    * @param name of repeat element
    * @return # of lines in element
    */
    public int getLines(String repeat) {
        if (maakunnat != null) {
            return maakunnat.size();
        }
        return 0;
    }

    /**
    * get Value of item from current element
    * @param item name
    */
    public String getValue() {
        return null;
    }

    /**
    * retrieve Element from tree data and make it "current"
    */
    public void getElement(String name) {
    }

    /**
    * get Value of item from repetive item in book
    * @param name of Repeat element
    * @param line number (0 .. n-1)
    * @param item name
    */
    public String getValue(String repeat, int line, String name) {
        Hashtable h;
        if (maakunnat != null) {
            if (line < maakunnat.size()) {
                h = (Hashtable) maakunnat.get(line);
                return (String) h.get(name);
            }
        }
        return null;
    }
}
