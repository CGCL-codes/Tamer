package com.myechelon.util;

import com.myechelon.subject.Subject;
import com.memnon.sql.PK;
import com.memnon.sql.ConnectionManager;
import java.util.StringTokenizer;
import java.sql.ResultSet;
import java.sql.SQLException;
import Controller;

public class SubjectUtils {

    public SubjectUtils() {
    }

    public Subject createSubject(String pathToSubject, ConnectionManager conmgr) {
        Logger log = new Logger(getClass(), "Create subjects in " + pathToSubject);
        Subject ret = null;
        try {
            if (pathToSubject != null) {
                StringTokenizer st = new StringTokenizer(pathToSubject, "/");
                String name = st.nextToken();
                ResultSet rs = conmgr.getConnection().createStatement().executeQuery("SELECT subjectPK FROM subject WHERE subjectFK ISNULL AND name='" + name + "'");
                Subject top = new Subject();
                if (rs.next()) {
                    top.setPrimaryKey(new PK(rs.getInt(1)));
                    Controller.get(top);
                    if (log.shouldLog()) log.log("Top subject '" + top.getName() + "' loaded");
                } else {
                    top.setName(name);
                    Controller.insert(top);
                    if (log.shouldLog()) log.log("Created new top subject " + top.getName());
                }
                Subject s = top;
                while (st.hasMoreTokens()) {
                    name = st.nextToken();
                    String sql = "SELECT subjectPk FROM subject WHERE subjectFK=" + s.getPrimaryKey().getPK() + " AND name='" + name + "'";
                    rs = conmgr.getConnection().createStatement().executeQuery(sql);
                    if (rs.next()) {
                        s = new Subject();
                        s.setPrimaryKey(new PK(rs.getInt(1)));
                        Controller.get(s);
                        if (log.shouldLog()) log.log("Loaded subject " + s.getName());
                    } else {
                        Subject c = new Subject();
                        c.setName(name);
                        c.setMother(s);
                        Controller.insert(c);
                        if (log.shouldLog()) log.log("Created subject " + c.getName());
                        s = c;
                    }
                    rs.close();
                }
                Controller.commit(s);
                log.dispose();
                ret = s;
            }
        } catch (SQLException e) {
            if (log.shouldLog()) log.log(e);
        }
        log.dispose();
        return ret;
    }
}
