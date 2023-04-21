package info.javafans.cdn.dao;

import info.javafans.cdn.database.DbHelper;
import info.javafans.cdn.domain.Activity;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Project: CollegeDatingNetwork <br />
 * ClassName: ActivityDao <br />
 * Description: <br />
 */
public class ActivityDao {

    public static Logger logger = Logger.getLogger(ActivityDao.class);

    public static List<Activity> selectLatestActivity() {
        String sql = "select * from activity order by activityCount desc limit 0,5";
        List<Activity> activityList = null;
        activityList = DbHelper.queryBeanList(Activity.class, sql, null);
        for (Activity a : activityList) {
            a.setGroup(GroupDao.selectGroupById(a.getGroupId()));
            a.setSponsor(MemberDao.selectMemberById(a.getSponsorId()));
        }
        return activityList;
    }

    /**
	 * 按id查询活动
	 * selectActivityById
	 * @param id
	 * @return
	 */
    public static Activity selectActivityById(int id) {
        String sql = "select * from activity where id = ?";
        Activity a = DbHelper.queryBean(Activity.class, sql, id);
        a.setGroup(GroupDao.selectGroupById(a.getGroupId()));
        a.setSponsor(MemberDao.selectMemberById(a.getSponsorId()));
        return a;
    }

    /**
	 * 获取活动列表
	 * selectActivityList
	 * @return
	 */
    public static List<Activity> selectActivityList() {
        String sql = "select * from activity";
        List<Activity> activityList = DbHelper.queryBeanList(Activity.class, sql, null);
        for (Activity a : activityList) {
            a.setGroup(GroupDao.selectGroupById(a.getGroupId()));
            a.setSponsor(MemberDao.selectMemberById(a.getSponsorId()));
        }
        return activityList;
    }
}
