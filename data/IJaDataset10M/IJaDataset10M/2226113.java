package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import util.MysqlConn;
import bean.User;

/**
 * @author Baicai 在显示商品信息的地方查询关联的用户信息
 */
public class GoodsViewUser {

    /**
	 * 根据用户id获取用户的部分信息
	 * 
	 * @param i
	 * @return User
	 */
    public static User getGoodsUser(int i) {
        try {
            User user = new User();
            MysqlConn.setSqlString("select userinfo_name,user_score,user_phone from user where user_id='" + i + "'");
            ResultSet resultSet = MysqlConn.queryDB();
            resultSet.next();
            user.setUserId(i);
            user.setUserinfoName(resultSet.getString("userinfo_name"));
            user.setUserScore(Integer.parseInt(resultSet.getString("user_score")));
            user.setUserPhone(resultSet.getString("user_phone"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MysqlConn.free();
        }
        return null;
    }
}
