package com.companyname.common.system.service;

import java.sql.Timestamp;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.companyname.common.base.web.service.AbstractListService;
import com.companyname.common.system.manager.TaskRunningInfoManager;
import com.companyname.common.system.view.foreveryone.TaskRunningInfoList4EveryOne;

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
public class FindTaskRunningInfosService extends AbstractListService {

    static Logger logger = Logger.getLogger(FindTaskRunningInfosService.class);

    private TaskRunningInfoManager taskRunningInfoManager;

    public void setTaskRunningInfoManager(TaskRunningInfoManager taskRunningInfoManager) {
        this.taskRunningInfoManager = taskRunningInfoManager;
    }

    /** 获取列表的models */
    public List getList(HttpServletRequest request) {
        logger.debug("1. 获取查询条件.....");
        String taskCode = this.getValueFromRequestParameter(request, "taskCode");
        String taskName = this.getValueFromRequestParameter(request, "taskName");
        String result = this.getValueFromRequestParameter(request, "result");
        Timestamp startTime = this.getTimestampFromRequestParameter(request, "startTime", "yyyy-MM-dd");
        startTime.setHours(0);
        startTime.setMinutes(0);
        startTime.setSeconds(0);
        Timestamp endTime = this.getTimestampFromRequestParameter(request, "endTime", "yyyy-MM-dd");
        endTime.setHours(0);
        endTime.setMinutes(0);
        endTime.setSeconds(0);
        endTime.setDate(endTime.getDate() + 1);
        String orderBy = this.getValueFromRequestParameter(request, "orderBy", "startTime");
        String sortType = this.getValueFromRequestParameter(request, "sortType", "desc");
        logger.debug("2. 获取查询条件完毕,开始查询.....");
        return this.taskRunningInfoManager.findTaskRunningInfos(taskCode, taskName, result, startTime, endTime, orderBy, sortType);
    }

    /** model -> viewer */
    public Object getViewer(HttpServletRequest request) {
        return new TaskRunningInfoList4EveryOne();
    }
}
