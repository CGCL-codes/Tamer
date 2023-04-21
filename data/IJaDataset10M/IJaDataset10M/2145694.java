package mecca.util;

import java.sql.Statement;
import mecca.db.Db;
import mecca.db.SQLRenderer;

public class PasswordReset {

    public static String pwd(String user, String pass) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.update("user_login", user);
            r.add("user_password", PasswordService.encrypt(pass != null && !"".equals(pass) ? pass : user));
            sql = r.getSQLUpdate("users");
            stmt.executeUpdate(sql);
            return sql;
        } finally {
            if (db != null) db.close();
        }
    }

    public static String pwd(String user) throws Exception {
        return pwd(user, "");
    }
}
