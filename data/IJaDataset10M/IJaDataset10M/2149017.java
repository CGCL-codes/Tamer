package com.asoft.common.system.task;

import java.sql.Timestamp;
import org.apache.log4j.Logger;
import com.asoft.common.system.define.TaskRunningResult;
import com.asoft.common.system.manager.TaskRunningInfoManager;
import com.asoft.common.system.model.TaskRunningInfo;
import com.asoft.common.system.util.AsoftSystemException;

/**
 * <p>Title: 系统任务</p>
 * <p>Description: 任务管理 Task </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: asoft</p>
 * @ $Author: amon.lei $
 * @ $Date: 2007-2-20 $
 * @ $Revision: 1.0 $
 * @ created in 2007-2-20
 *
 */
public abstract class SysTask {

    static Logger logger = Logger.getLogger(SysTask.class);

    /** 任务代码 */
    private String code;

    /** 任务名称 */
    private String name;

    /** 任务运行情况 */
    private TaskRunningInfoManager taskRunningInfoManager;

    /** 启动 */
    public void run() {
        logger.info("执行任务 " + this.toString());
        long startTime = System.currentTimeMillis();
        int runningRs;
        String errorInfo;
        try {
            this.exec();
            runningRs = TaskRunningResult.SUCCESSFUL;
            errorInfo = "";
        } catch (Exception x) {
            runningRs = TaskRunningResult.FAILED;
            errorInfo = x.toString();
            logger.debug("长度：" + errorInfo);
            logger.error("执行任务" + this.toString() + "失败:");
            x.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long runningTime = endTime - startTime;
        logger.debug("执行时间: " + runningTime / 1000 + " s");
        TaskRunningInfo tri = new TaskRunningInfo(this.getCode(), this.getName(), runningTime, runningRs, errorInfo, new Timestamp(startTime), new Timestamp(endTime));
        this.taskRunningInfoManager.save(tri);
    }

    public String toString() {
        return "[" + this.getCode() + " : " + this.getName() + "]";
    }

    /** 执行任务 */
    public abstract void exec() throws Exception;

    /** getters and setters */
    public String getCode() {
        if (this.code == null) {
            throw new AsoftSystemException("未定义任务代码");
        }
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        if (this.name == null) {
            throw new AsoftSystemException("未定义任务名称");
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskRunningInfoManager getTaskRunningInfoManager() {
        return taskRunningInfoManager;
    }

    public void setTaskRunningInfoManager(TaskRunningInfoManager taskRunningInfoManager) {
        this.taskRunningInfoManager = taskRunningInfoManager;
    }
}
