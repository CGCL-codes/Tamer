package com.figo.ThreadManager;

import java.util.Date;

/**
* 所有任务接口
* 其他任务必须继承访类
*
* @author obullxl
*/
public abstract class Task implements Runnable {

    private Date generateTime = null;

    private Date submitTime = null;

    private Date beginExceuteTime = null;

    private Date finishTime = null;

    private long taskId;

    public Task() {
        this.generateTime = new Date();
    }

    /**
    * 任务执行入口
    */
    public void run() {
    }

    /**
    * 是否用到数据库
    *
    * @return
    */
    protected abstract boolean useDb();

    /**
    * 是否需要立即执行
    *
    * @return
    */
    protected abstract boolean needExecuteImmediate();

    /**
    * 任务信息
    *
    * @return String
    */
    public abstract String info();

    public Date getGenerateTime() {
        return generateTime;
    }

    public Date getBeginExceuteTime() {
        return beginExceuteTime;
    }

    public void setBeginExceuteTime(Date beginExceuteTime) {
        this.beginExceuteTime = beginExceuteTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}
