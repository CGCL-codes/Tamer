package educate.sis.bursary;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.db.UniqueID;
import lebah.util.DateTool;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.00
 */
public class SponsorData {

    public static Sponsor getSponsor(String sponsorId) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("sponsor_id", sponsorId);
            r.add("sponsor_id");
            r.add("sponsor_code");
            r.add("sponsor_name");
            sql = r.getSQLSelect("sponsor");
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return getSponsorData(rs); else return null;
        } finally {
            if (db != null) db.close();
        }
    }

    public static Vector getSponsorList() throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            Vector list = new Vector();
            SQLRenderer r = new SQLRenderer();
            r.add("sponsor_id");
            r.add("sponsor_code");
            r.add("sponsor_name");
            sql = r.getSQLSelect("sponsor", "sponsor_name");
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) list.addElement(getSponsorData(rs));
            return list;
        } finally {
            if (db != null) db.close();
        }
    }

    public static Sponsor getSponsorData(ResultSet rs) throws Exception {
        Sponsor sponsor = new Sponsor();
        String id = rs.getString("sponsor_id");
        String code = rs.getString("sponsor_code");
        String name = rs.getString("sponsor_name");
        sponsor.setId(id);
        sponsor.setCode(code);
        sponsor.setName(name);
        return sponsor;
    }

    public static void addSponsor(Sponsor sponsor) throws Exception {
        Db db = null;
        String sql = "";
        try {
            sponsor.setId(Long.toString(UniqueID.get()));
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("sponsor_id", sponsor.getId());
            r.add("sponsor_code", sponsor.getCode());
            r.add("sponsor_name", sponsor.getName());
            sql = r.getSQLInsert("sponsor");
            stmt.executeUpdate(sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static void deleteSponsor(String id) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            sql = "delete from sponsor where sponsor_id = '" + id + "'";
            stmt.executeUpdate(sql);
        } finally {
            if (db != null) db.close();
        }
    }

    public static Vector getStudentSponsorInfoList(String sponsorId) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("sp.sponsor_id", sponsorId);
            r.add("sp.student_id");
            r.add("sp.date_start");
            r.add("sp.date_end");
            r.add("sp.sponsor_amount_yearly");
            r.add("sp.sponsor_amount_total");
            r.add("s.name");
            r.add("s.icno");
            sql = r.getSQLSelect("student_sponsor sp, student s", "s.name");
            ResultSet rs = stmt.executeQuery(sql);
            Vector list = new Vector();
            while (rs.next()) {
                String studentId = rs.getString("student_id");
                Date dateStart = rs.getDate("date_start");
                Date dateEnd = rs.getDate("date_end");
                float amountYearly = rs.getFloat("sponsor_amount_yearly");
                float amountTotal = rs.getFloat("sponsor_amount_total");
                String name = rs.getString("name");
                String icno = rs.getString("icno");
                StudentSponsorInfo info = new StudentSponsorInfo();
                info.setStudentId(studentId);
                info.setSponsorId(sponsorId);
                info.setDateStart(dateStart);
                info.setDateEnd(dateEnd);
                info.setAmountYearly(amountYearly);
                info.setAmountTotal(amountTotal);
                info.setName(name);
                info.setIcno(icno);
                list.addElement(info);
            }
            return list;
        } finally {
            if (db != null) db.close();
        }
    }

    public void addStudent(StudentSponsorInfo info) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            SQLRenderer r = new SQLRenderer();
            r.add("sponsor_id", info.getSponsorId());
            r.add("student_id", info.getStudentId());
            r.add("sponsor_amount_yearly", info.getAmountYearly());
            r.add("sponsor_amount_total", info.getAmountTotal());
            sql = r.getSQLInsert("student_sponsor");
            stmt.executeUpdate(sql);
        } finally {
            if (db != null) db.close();
        }
    }
}
