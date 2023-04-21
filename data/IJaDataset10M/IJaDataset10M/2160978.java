package org.fao.waicent.kids.editor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegisterUser {

    boolean status;

    boolean insertstatus;

    String fname, lname, emailid, password, country;

    public RegisterUser() {
    }

    public boolean Insert(Connection con) {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            int id = getUserID(con);
            String sql = "INSERT INTO USERINFO(ID,FNAME,LNAME,EMAIL,PASSWORD,COUNTRY) VALUES('" + id + "' , '" + fname + "', '" + lname + "' ,'" + emailid + "' , '" + password + "' , '" + country + "')";
            int x = stmt.executeUpdate(sql);
            if (x >= 1) {
                con.commit();
                insertstatus = true;
            } else {
                insertstatus = false;
            }
        } catch (Exception ex) {
            System.out.println("Insert" + ex.getMessage());
            insertstatus = false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (Exception ef) {
                System.out.println("finally" + ef.getMessage());
            }
        }
        return this.insertstatus;
    }

    public boolean CheckUser(String sql, Connection con) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception ex) {
            System.out.println("checkuser" + ex.getMessage());
            status = true;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception ef) {
                System.out.println("checkuser finally" + ef.getMessage());
            }
        }
        return (this.status);
    }

    public boolean getSuccess() {
        return this.status;
    }

    public void setFirstName(String v) {
        fname = v;
    }

    public void setLastName(String v) {
        lname = v;
    }

    public void setEmailId(String v) {
        emailid = v;
    }

    public void setPassword(String v) {
        password = v;
    }

    public void setCountry(String v) {
        country = v;
    }

    private static int getUserID(Connection con) {
        Statement stmt = null;
        ResultSet rs = null;
        int id = 1;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT MAX(ID) FROM USERINFO");
            if (rs.next()) {
                id = rs.getInt(1);
            }
            if (id == 0) {
                id = 1;
            } else {
                id++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception ef) {
                System.out.println(ef.getMessage());
            }
        }
        return (id);
    }
}
