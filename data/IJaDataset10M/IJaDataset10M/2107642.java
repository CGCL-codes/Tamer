package org.vegbank.nvcrs.web;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import org.vegbank.nvcrs.util.*;

public class Proposals {

    ArrayList records;

    Database database;

    int curRecord = 0;

    int recordsPerPage = 10;

    int curPage = 1;

    int totalPages = 0;

    public String id;

    public String author;

    public String type;

    public String level;

    public String submit_before_y;

    public String submit_before_m;

    public String submit_before_d;

    public String submit_after_y;

    public String submit_after_m;

    public String submit_after_d;

    public String approve_before_y;

    public String approve_before_m;

    public String approve_before_d;

    public String approve_after_y;

    public String approve_after_m;

    public String approve_after_d;

    public String summary;

    public boolean b_id;

    public boolean b_author;

    public boolean b_type;

    public boolean b_submit_before;

    public boolean b_submit_after;

    public boolean b_approve_before;

    public boolean b_approve_after;

    public boolean b_level;

    public boolean b_summary;

    public boolean b_orderBy;

    public String orderBy;

    public Proposals() {
        records = new ArrayList();
        database = new Database();
    }

    public void clearRecords() throws Exception {
        records.clear();
    }

    public void clearConditions() {
        b_id = false;
        b_author = false;
        b_type = false;
        b_submit_before = false;
        b_submit_after = false;
        b_approve_before = false;
        b_approve_after = false;
        b_level = false;
        b_summary = false;
        b_orderBy = false;
    }

    public void addProposal(Proposal p) throws Exception {
        records.add(p);
    }

    public Proposal getProposal(int index) throws Exception {
        return (Proposal) records.get(index);
    }

    public int getProposalCount() {
        return records.size();
    }

    public int getCurrentRecordIndex() {
        return curRecord;
    }

    public void setCurrentRecordIndex(int index) {
        curRecord = index;
    }

    public int getRecordsPerPage() {
        return recordsPerPage;
    }

    public void setRecordsPerPage(int num) {
        recordsPerPage = num;
    }

    public void findRecords() throws Exception {
        String sql = "";
        if (b_author) sql = "author ilike '%" + author + "%'";
        if (b_type) {
            if (!sql.equals("")) sql += " AND ";
            sql += "type_names ilike '%" + type + "%'";
        }
        if (b_level) {
            if (!sql.equals("")) sql += " AND ";
            sql += "level ilike '%" + level + "%'";
        }
        if (b_submit_before) {
            String d = submit_before_m.trim() + "-" + submit_before_d.trim() + "-" + submit_before_y.trim();
            if (!sql.equals("")) sql += " AND ";
            sql += "submitted_date < '" + d + "'";
        }
        if (b_submit_after) {
            String d = submit_after_m.trim() + "-" + submit_after_d.trim() + "-" + submit_after_y.trim();
            if (!sql.equals("")) sql += " AND ";
            sql += "submitted_date > '" + d + "'";
        }
        if (b_approve_before) {
            String d = approve_before_m.trim() + "-" + approve_before_d.trim() + "-" + approve_before_y.trim();
            if (!sql.equals("")) sql += " AND ";
            sql += "approved_date < '" + d + "'";
        }
        if (b_approve_after) {
            String d = approve_after_m.trim() + "-" + approve_after_d.trim() + "-" + approve_after_y.trim();
            if (!sql.equals("")) sql += " AND ";
            sql += "approved_date > '" + d + "'";
        }
        if (sql.equals("")) throw new Exception("No query condition specified");
        sql = "select * from approved where " + sql;
        if (b_orderBy) sql += " order by " + orderBy;
        Connection con = null;
        try {
            con = database.getConnection();
            PreparedStatement prepStmt = con.prepareStatement(sql);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                String aid = rs.getString("APPROVED_ID");
                String pid = rs.getString("PROPOSAL_ID");
                String author = rs.getString("author");
                String typeNames = rs.getString("type_names");
                String submitDate = rs.getString("submitted_date");
                String approveDate = rs.getString("approved_date");
                String level = rs.getString("level");
                String summary = rs.getString("summary");
                String title = rs.getString("title");
                String seq = rs.getString("seqnumber");
                Proposal p = new Proposal(aid, pid, author, typeNames, level, submitDate, approveDate, summary, title, seq);
                addProposal(p);
            }
            prepStmt.close();
            con.close();
            curRecord = 0;
            updatePages();
        } catch (Exception e) {
            if (con != null && !con.isClosed()) con.close();
            throw e;
        }
    }

    public void findAllRecords() throws Exception {
        String sql = "select * from approved ";
        if (b_orderBy) sql += " order by " + orderBy;
        Connection con = null;
        try {
            con = database.getConnection();
            PreparedStatement prepStmt = con.prepareStatement(sql);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                String aid = rs.getString("APPROVED_ID");
                String pid = rs.getString("PROPOSAL_ID");
                String author = rs.getString("author");
                String typeNames = rs.getString("type_names");
                String submitDate = rs.getString("submitted_date");
                String approveDate = rs.getString("approved_date");
                String level = rs.getString("level");
                String summary = rs.getString("summary");
                String title = rs.getString("title");
                String seq = rs.getString("seqnumber");
                Proposal p = new Proposal(aid, pid, author, typeNames, level, submitDate, approveDate, summary, title, seq);
                addProposal(p);
            }
            prepStmt.close();
            con.close();
            curRecord = 0;
            updatePages();
        } catch (Exception e) {
            if (con != null && !con.isClosed()) con.close();
            throw e;
        }
    }

    public void addProposalToDatabase(Proposal p) throws Exception {
        Connection con = null;
        try {
            con = database.getConnection();
            String updateStatement = "insert into approved ( proposal_id,author,type_names,level,submitted_date,approved_date,";
            updateStatement += "summary,title,seqnumber) values(";
            updateStatement += p.getProposalId();
            updateStatement += ",'" + p.getAuthor() + "'";
            updateStatement += ",'" + p.getTypeNames() + "'";
            updateStatement += ",'" + p.getLevel() + "'";
            updateStatement += ",'" + p.getDateSubmit() + "'";
            updateStatement += ",'" + p.getDateApprove() + "'";
            updateStatement += ",'" + p.getSummary() + "'";
            updateStatement += ",'" + p.getTitle() + "'";
            updateStatement += ",'" + p.getSequenceNumber() + "'";
            updateStatement += " )";
            PreparedStatement prepStmt = con.prepareStatement(updateStatement);
            try {
                int rowCount = prepStmt.executeUpdate();
            } catch (Exception e) {
                prepStmt.close();
                con.close();
                throw new Exception(updateStatement + "\nSystem error:" + e.getMessage());
            }
            prepStmt.close();
            con.close();
        } catch (Exception e) {
            if (con != null && !con.isClosed()) con.close();
            throw e;
        }
    }

    public void updatePages() {
        totalPages = records.size() / recordsPerPage;
        int remains = records.size() - totalPages * recordsPerPage;
        if (totalPages == 0) {
            if (remains != 0) totalPages = 1;
        } else {
            if (remains > 0) totalPages += 1;
        }
    }

    public void goTop() {
        curRecord = 0;
        curPage = 1;
    }

    public void goBottom() {
        int num = records.size();
        curRecord = num - recordsPerPage;
        if (curRecord < 0) curRecord = 0;
        curPage = totalPages;
    }

    public void goNext() {
        int num = records.size();
        curRecord += recordsPerPage;
        if (curRecord >= num) curRecord = 0;
        curPage++;
    }

    public void goPrevious() {
        int num = records.size();
        curRecord -= recordsPerPage;
        if (curRecord < 0) curRecord = 0;
        curPage--;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPage() {
        return curPage;
    }
}
