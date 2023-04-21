package com.companyname.common.system.dao;

import java.util.List;
import java.sql.Timestamp;
import com.companyname.common.base.dao.DAO;
import com.companyname.common.system.model.TaskRunningInfo;
import com.companyname.common.system.util.CompanyNameSystemException;

/**
 * <p>Title: 任务运行情况</p>
 * <p>Description: 任务运行情况 TaskRunningInfo </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: 公司名</p>
 * @ $Author: 作者名 $
 * @ $Date: 创建日期 $
 * @ $Revision: 1.0 $
 * @ created in 创建日期
 *
 */
public interface TaskRunningInfoDAO extends DAO {

    /**
         * 描述:任务运行情况
         *
         * @ param:
         * @ param:orderBy 排序属性（for hql）
         * @ param:sortType 排序方式（asc or desc）
         * @ Exception:
         * @ return model集合
         */
    public List findTaskRunningInfos(String taskCode, String taskName, String result, Timestamp startTime, Timestamp endTime, String orderBy, String sortType);

    public List findTasks();
}
