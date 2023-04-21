package com.joebertj.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.joebertj.Branch;
import com.joebertj.Level;
import com.joebertj.Mode;
import com.joebertj.helper.DatabaseConnection;
import java.sql.ResultSet;
import java.util.List;
import java.util.ListIterator;

public class StatisticsService {

    private Connection conn;

    private PreparedStatement pstmt;

    private float rate;

    private int level;

    private int branchId;

    private int userId;

    private int mode;

    private int count;

    public StatisticsService() {
        conn = DatabaseConnection.getConnection();
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getRedeemCount() {
        count = getRedeemCount(level, branchId, userId, mode);
        return count;
    }

    public int getRedeemCount(int level, int branchId, int userId, int mode) {
        int count = 0;
        try {
            pstmt = conn.prepareStatement("select count(pid) from redeem");
            ResultSet rs = pstmt.executeQuery();
            if (rs.first()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getRedeemCountByInterestRate(float rate, int level, int branchId, int userId, int mode) {
        int count = 0;
        String query = "select count(redeem.pid) from pawn " + "left join redeem on pawn.pid=redeem.pid " + "where 100*interest/loan = ? " + "and redeem.pid is not null";
        if (mode == Mode.DAILY) {
            query += " and redeem_date=curdate()";
        } else if (mode == Mode.MONTHLY) {
            query += " and year(redeem_date)=year(curdate()) and month(redeem_date)=month(curdate())";
        } else if (mode == Mode.YEARLY) {
            query += " and year(redeem_date)=year(curdate())";
        }
        if (level == Level.ADMIN) {
            try {
                pstmt = conn.prepareStatement(query);
                pstmt.setFloat(1, rate);
                ResultSet rs = pstmt.executeQuery();
                if (rs.first()) {
                    count = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (level == Level.OWNER) {
            String branches = "";
            List<Branch> list = new BranchService().getBranches(userId);
            ListIterator<Branch> li = list.listIterator();
            int[] id = new int[list.size()];
            int i = 0;
            while (li.hasNext()) {
                Branch b = (Branch) li.next();
                id[i] = b.getId();
                i++;
            }
            i = 0;
            while (li.hasNext()) {
                if (i > 0) {
                    branches += " or ";
                }
                branches = "pawn.branch = ?";
                i++;
            }
            if (branches.length() > 0) {
                query += " and (" + branches + ")";
            }
            try {
                pstmt = conn.prepareStatement(query);
                pstmt.setFloat(1, rate);
                for (i = 0; i < id.length; i++) {
                    pstmt.setInt(2 + i, id[i]);
                }
                ResultSet rs = pstmt.executeQuery();
                if (rs.first()) {
                    count = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (level == Level.ENCODER || level == Level.MANAGER) {
            query += " and pawn.branch=?";
            try {
                pstmt = conn.prepareStatement(query);
                pstmt.setFloat(1, rate);
                pstmt.setInt(2, branchId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.first()) {
                    count = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public int getRedeemCountByInterestRate() {
        count = getRedeemCountByInterestRate(rate, level, branchId, userId, mode);
        return count;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
