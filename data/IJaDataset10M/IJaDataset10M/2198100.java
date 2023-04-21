package com.sl.eventlog.dao.user;

import java.util.List;
import com.sl.eventlog.dao.BaseDao;
import com.sl.eventlog.domain.user.User;
import com.sl.eventlog.domain.user.UserOperateLog;

public interface UserOperateLogDao extends BaseDao<UserOperateLog> {

    /**
	 * Get User operate log by user.
	 * @param user The user entity
	 * @return List
	 */
    List<UserOperateLog> getLogsByUser(User user);

    /**
	 * Get User operate log by user name.
	 * @param userName The user name
	 * @return list
	 */
    List<UserOperateLog> getLogsByUserId(long userName);
}
