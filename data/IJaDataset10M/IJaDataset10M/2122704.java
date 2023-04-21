package ru.org.linux.beans.show;

import java.sql.*;
import ru.org.linux.beans.*;
import ru.org.linux.db.*;

public class VoteBox {

    private MySQLReader mrd = new MySQLReader();

    private ResultSet rs;

    public VoteBox() {
        String query = "SELECT votes.id as 'vote_id', " + "messages.header " + "FROM votes " + "LEFT JOIN messages ON messages.id=votes.mess_id";
        rs = mrd.select(query);
    }

    public Vote getNext() {
        ResultSet rs1;
        try {
            if (rs.next()) {
                Vote v = new Vote();
                v.header = rs.getString("header");
                v.id = rs.getInt("vote_id");
                rs1 = mrd.select("SELECT COUNT(*) as 'vote_cnt' " + "FROM vote_replies " + "LEFT JOIN vote_cases ON vote_cases.case_id=vote_replies.case_id " + "LEFT JOIN votes ON vote_cases.vote_id=votes.id");
                if (rs1.next()) {
                    v.vote_cnt = rs1.getInt(1);
                }
                return v;
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
