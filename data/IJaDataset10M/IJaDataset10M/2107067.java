package com.jalyoo.recon.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.jalyoo.recon.db.ConnectionPool;
import com.jalyoo.recon.flexservice.beans.UserMessageObj;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class TransactionDAO {

    public void insert(List<TransactionObj> transactions) {
        Connection conn = (Connection) ConnectionPool.getConnection();
        PreparedStatement s = null;
        int len = transactions.size();
        try {
            s = (PreparedStatement) conn.prepareStatement("INSERT INTO transactions " + "(user_id, msg_id,activity,msg_create_time,last_update_time,money)" + " VALUES(?,?,?,?,?,?)");
            for (int i = 0; i < len; i++) {
                TransactionObj trans = (TransactionObj) transactions.get(i);
                s.clearParameters();
                s.setString(1, trans.getRobotAcct());
                s.setString(2, trans.getMessageId());
                s.setString(3, trans.getActivity());
                Date date = new Date(trans.getDate().getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formatedDt = df.format(date.getTime());
                s.setString(4, formatedDt);
                formatedDt = df.format(Calendar.getInstance().getTimeInMillis());
                s.setString(5, formatedDt);
                s.setDouble(6, trans.getMoney());
                s.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * This method update the transaction type only
	 * @param user
	 * @param tran
	 * @param type
	 * @return UserMessageObj contain result and the messages
	 */
    public UserMessageObj updateTransType(UserObj user, TransactionObj tran, TransTypesObj type) {
        return null;
    }

    public TransactionObj getMonthlyTotal(UserObj user) {
        Connection conn = (Connection) ConnectionPool.getConnection();
        TransactionObj trans = new TransactionObj();
        PreparedStatement s = null;
        String userId = user.getUserId();
        try {
            s = (PreparedStatement) conn.prepareStatement("SELECT user_id,sum(money) " + "FROM transactions t" + " where msg_create_time >(ADDDATE(CURRENT_DATE,1-DAYOFMONTH(CURRENT_DATE))) " + "and user_id =(select robot_acct from recon_user ru where ru.user_id =?) group by user_id;");
            s.setString(1, userId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                trans.setRobotAcct(rs.getString(1));
                trans.setMoney(rs.getDouble(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return trans;
    }

    public List<TransactionObj> getLastTransaction(UserObj user) {
        List<TransactionObj> results = new ArrayList<TransactionObj>();
        Connection conn = (Connection) ConnectionPool.getConnection();
        PreparedStatement s = null;
        String userName = user.getRobotAcct();
        try {
            s = (PreparedStatement) conn.prepareStatement("SELECT user_id,msg_id, msg_create_time," + "last_update_time " + "FROM transactions where user_id =? and " + "msg_create_time = (select max(msg_create_time) " + "from transactions where user_id=?) limit 1");
            s.setString(1, userName);
            s.setString(2, userName);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                TransactionObj trans = new TransactionObj();
                trans.setRobotAcct(rs.getString(1));
                trans.setMessageId(rs.getString(2));
                trans.setDate(rs.getTimestamp(3));
                results.add(trans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    /**
	 * This Method return the data between the range that user input
	 * @param user
	 * @param fromDate
	 * @param toDate
	 * @return TransactionObj List
	 */
    public List<TransactionObj> getCustomDateTransactions(UserObj user, java.util.Date fromDate, java.util.Date toDate) {
        List<TransactionObj> results = new ArrayList<TransactionObj>();
        Connection conn = (Connection) ConnectionPool.getConnection();
        PreparedStatement s = null;
        String userId = user.getUserId();
        try {
            String sql = "SELECT u.user_id,activity,money,msg_create_time,tp.id,tp.type_name " + "FROM transactions t " + "LEFT join recon_user u on t.user_id = u.robot_acct " + "LEFT join trans_types tp on t.trans_type = tp.id " + "WHERE u.user_id =? and msg_create_time >=? and msg_create_time <=? " + "ORDER BY msg_create_time";
            s = (PreparedStatement) conn.prepareStatement(sql);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            s.setString(1, userId);
            s.setString(2, df.format(fromDate));
            s.setString(3, df.format(toDate));
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                TransactionObj trans = new TransactionObj();
                trans.setRobotAcct(rs.getString(1));
                trans.setActivity(rs.getString(2));
                trans.setMoney(rs.getDouble(3));
                trans.setDate(rs.getTimestamp(4));
                trans.setTransType(rs.getString(6));
                results.add(trans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    /**
	 * Return one week data
	 * @param user
	 * @return
	 */
    public List<TransactionObj> getThisWeekTransactions(UserObj user) {
        return getWeeklyTransactions(user, 0);
    }

    public List<TransactionObj> getOneWeekTransactions(UserObj user) {
        return getWeeklyTransactions(user, 1);
    }

    public List<TransactionObj> getBioWeeksTransactions(UserObj user) {
        return getWeeklyTransactions(user, 2);
    }

    public List<TransactionObj> getTriWeeksTransactions(UserObj user) {
        return getWeeklyTransactions(user, 3);
    }

    public List<TransactionObj> getWeeklyTransactions(UserObj user, int count) {
        List<TransactionObj> results = new ArrayList<TransactionObj>();
        Connection conn = (Connection) ConnectionPool.getConnection();
        PreparedStatement s = null;
        String userId = user.getUserId();
        try {
            String sql = "SELECT u.user_id,activity,money,msg_create_time,tp.id,tp.type_name " + "FROM transactions t " + "LEFT join recon_user u on t.user_id = u.robot_acct " + "LEFT join trans_types tp on t.trans_type = tp.id " + "WHERE u.user_id =? and msg_create_time >(SUBDATE(CURRENT_DATE, DAYOFWEEK(CURRENT_DATE)-1+7*?)) " + "ORDER BY msg_create_time";
            s = (PreparedStatement) conn.prepareStatement(sql);
            s.setString(1, userId);
            s.setInt(2, count);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                TransactionObj trans = new TransactionObj();
                trans.setRobotAcct(rs.getString(1));
                trans.setActivity(rs.getString(2));
                trans.setMoney(rs.getDouble(3));
                trans.setDate(rs.getTimestamp(4));
                trans.setTransType(rs.getString(6));
                results.add(trans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public List<TransactionObj> getWeeklyDaySum(UserObj user, int count) {
        List<TransactionObj> results = new ArrayList<TransactionObj>();
        Connection conn = (Connection) ConnectionPool.getConnection();
        PreparedStatement s = null;
        String userId = user.getUserId();
        try {
            String sql = "SELECT u.user_id,sum(money) money,date(msg_create_time) as cdate " + "FROM transactions t " + "LEFT join recon_user u on t.user_id = u.robot_acct " + "WHERE u.user_id =? and msg_create_time >(SUBDATE(CURRENT_DATE, DAYOFWEEK(CURRENT_DATE)-1+7*?)) " + "GROUP BY cdate ORDER BY cdate";
            s = (PreparedStatement) conn.prepareStatement(sql);
            s.setString(1, userId);
            s.setInt(2, count);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                TransactionObj trans = new TransactionObj();
                trans.setRobotAcct(rs.getString(1));
                trans.setMoney(rs.getDouble(2));
                trans.setDate(rs.getTimestamp(3));
                results.add(trans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
