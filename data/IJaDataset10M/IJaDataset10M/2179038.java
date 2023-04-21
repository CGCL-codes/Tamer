package mecca.lcms;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Vector;
import mecca.db.Db;
import mecca.db.DbDelegator;
import mecca.db.SQLRenderer;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MemberDb implements MemberData {

    public Collection getMembers() throws Exception {
        Vector list = new Vector();
        DbDelegator delegator = new DbDelegator();
        Vector data = new Vector();
        data.addElement("member_id");
        list = delegator.selectDistinct("learner_sco", data, null, "member_id");
        return list;
    }

    public Member getMember(String id) throws Exception {
        Member member = null;
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("user_login", id);
            r.add("user_name");
            sql = r.getSQLSelect("users");
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                member = new Member();
                member.setId(id);
                member.setLogin(id);
                member.setName(rs.getString("user_name"));
            }
        } finally {
            if (db != null) db.close();
        }
        return member;
    }

    public boolean add(Member member) {
        return true;
    }

    public boolean update(Member member) {
        return true;
    }

    public boolean delete(Member member) {
        return true;
    }

    public boolean isLoginExists(String login) {
        return true;
    }

    public boolean isIdExists(String id) {
        return true;
    }

    public String getMemberId(String login) {
        return "";
    }

    public Member findByLogin(String login) {
        Member member = new Member();
        return member;
    }
}
