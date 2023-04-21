package cn.myapps.core.task.ejb;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.log4j.Logger;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.dts.exp.Export;
import cn.myapps.core.dynaform.dts.exp.Export_Erro_type;
import cn.myapps.core.dynaform.dts.exp.mappingconfig.ejb.MappingConfig;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.superuser.ejb.SuperUserProcess;
import cn.myapps.core.superuser.ejb.SuperUserVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.core.user.ejb.UserVO;
import cn.myapps.util.DateUtil;
import cn.myapps.util.ObjectUtil;
import cn.myapps.util.ProcessFactory;

/**
 * @hibernate.class table="T_TASK"
 * @author nicholas
 */
public class Task extends ValueObject implements Cloneable {

    private static final Logger LOG = Logger.getLogger(Task.class);

    private int type;

    private String id;

    private String name;

    private String description;

    private Date runningTime;

    private int period;

    private int runtimes;

    private String terminateScript;

    private String taskScript;

    private Date modifyTime;

    private String creator;

    private String creatorid;

    private int totalRuntimes;

    private int state;

    private int startupType;

    private Collection daysOfWeek = new ArrayList();

    private int dayOfMonth;

    private int repeatTimes;

    private int frequency;

    private Date lastExecutedDate = null;

    private String applicationid;

    private ModuleVO module;

    private int executedCount;

    private static int docsnum = -1;

    /**
	 * 获取启动类型
	 * 
	 * @hibernate.property column="STARTUPTYPE"
	 * @return 启动类型
	 */
    public int getStartupType() {
        return startupType;
    }

    /**
	 * 设置启动类型
	 * 
	 * @param startupType
	 *            启动类型
	 */
    public void setStartupType(int startupType) {
        this.startupType = startupType;
    }

    /**
	 * 设置任务状态
	 * 
	 * @hibernate.property column="STATE"
	 * @return 任务状态
	 */
    public int getState() {
        return state;
    }

    /**
	 * 设置任务状态
	 * 
	 * @param state
	 *            任务状态
	 */
    public void setState(int state) {
        this.state = state;
    }

    /**
	 * 获取创建人名称
	 * 
	 * @hibernate.property column="CREATOR"
	 * @return 创建人名称
	 */
    public String getCreator() {
        return creator;
    }

    /**
	 * 设置创建人名称
	 * 
	 * @param creator
	 *            创建人名称
	 */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
	 * 获取任务的描述
	 * 
	 * @hibernate.property column="DESCRIPTION" length="1000"
	 * @return 任务的描述
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * 设置任务的描述
	 * 
	 * @param description
	 *            任务的描述
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * 获取开始时间
	 * 
	 * @hibernate.property column="RUNNINGTIME"
	 * @return 开始时间
	 */
    public Date getRunningTime() {
        return runningTime;
    }

    /**
	 * 设置开始时间
	 * 
	 * @param runningTime
	 *            开始时间
	 */
    public void setRunningTime(Date runningTime) {
        this.runningTime = runningTime;
    }

    /**
	 * 获取标识
	 * 
	 * @hibernate.id column="ID" generator-class="assigned"
	 * @return 标识
	 */
    public String getId() {
        return id;
    }

    /**
	 * 设置标识
	 * 
	 * @param id
	 *            标识
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * 获取修改时间
	 * 
	 * @hibernate.property column="MODIFY_TIME"
	 * @return 修改时间
	 */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
	 * 获取修改时间,并给予格式化
	 * 
	 * @return 格式化后的修改时间
	 */
    public String getModifyTiemStr() {
        return DateUtil.getDateStr(modifyTime);
    }

    /**
	 * 设置修改时间
	 * 
	 * @param modifyTime
	 *            修改时间
	 */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = new Date();
    }

    /**
	 * 获取任务名称
	 * 
	 * @hibernate.property column="NAME"
	 * @return 任务名称
	 */
    public String getName() {
        return name;
    }

    /**
	 * 设置任务名称
	 * 
	 * @param name
	 *            任务名称
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * 获取任务内容
	 * 
	 * @hibernate.property column="TASK_SCRIPT" type = "text"
	 * @return 任务内容
	 */
    public String getTaskScript() {
        return taskScript;
    }

    /**
	 * 设置任务内容
	 * 
	 * @param taskScript
	 *            任务内容
	 */
    public void setTaskScript(String taskScript) {
        this.taskScript = taskScript;
    }

    /**
	 * 获取停止条件
	 * 
	 * @hibernate.property column="TERMINATE_SCRIPT" type = "text"
	 * @return 停止条件
	 */
    public String getTerminateScript() {
        return terminateScript;
    }

    /**
	 * 设置停止条件
	 * 
	 * @param terminateScript
	 *            停止条件
	 */
    public void setTerminateScript(String terminateScript) {
        this.terminateScript = terminateScript;
    }

    /**
	 * 获取运行次数
	 * 
	 * @hibernate.property column="RUNTIMES"
	 * @return 运行次数
	 */
    public int getRuntimes() {
        return runtimes;
    }

    /**
	 * 设置运行次数
	 * 
	 * @param runtimes
	 *            运行次数
	 */
    public void setRuntimes(int runtimes) {
        this.runtimes = runtimes;
    }

    /**
	 * 获取总运行次数
	 * 
	 * @hibernate.property column="TOTAL_RUNTIMES"
	 * @return 总运行次数
	 */
    public int getTotalRuntimes() {
        return totalRuntimes;
    }

    /**
	 * 设置总运行次数
	 * 
	 * @param totalRuntimes
	 *            总运行次数
	 */
    public void setTotalRuntimes(int totalRuntimes) {
        this.totalRuntimes = totalRuntimes;
    }

    /**
	 * 获取任务类型
	 * 
	 * @hibernate.property column="TYPE"
	 * @return 任务类型
	 */
    public int getType() {
        return type;
    }

    /**
	 * 设置任务类型
	 * 
	 * @param type
	 *            类型(导出,脚本)
	 */
    public void setType(int type) {
        this.type = type;
    }

    /**
	 * 获取一个月中的第几天
	 * 
	 * @hibernate.property column="DAYOFMONTH"
	 * @return 一个月中的第几天
	 */
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    /**
	 * 设置一个月中的第几天
	 * 
	 * @param dayOfMonth
	 *            一个月中的第几天
	 */
    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    /**
	 * 获取重复类型
	 * 
	 * @hibernate.property column="PERIOD"
	 * @return 重复类型
	 */
    public int getPeriod() {
        return period;
    }

    /**
	 * 设置重复类型
	 * 
	 * @param period
	 *            重复类型
	 */
    public void setPeriod(int period) {
        this.period = period;
    }

    /**
	 * 是否可以执行此任务
	 * 
	 * @return 是否可以执行此任务(ture: 表示可执行 false:表示为可执行)
	 */
    public boolean isExecuteAble() {
        return isExecuteAble(new Date());
    }

    /**
	 * 是否可以执行此任务
	 * 
	 * @param sysDate
	 *            系统时间
	 * @return 如果可以执行则返回true,否则返回false
	 */
    public boolean isExecuteAble(Date sysDate) {
        boolean executeAble = false;
        Calendar calendar = Calendar.getInstance();
        Date nextRunningTime = getNextRunningTime(sysDate);
        long dateTimeDiff = DateUtil.getDiffDateTime(getRunningTime(), sysDate);
        long timeDiff = DateUtil.getDiffTime(nextRunningTime, sysDate);
        LOG.debug("Time Difference: " + timeDiff / 1000 + "(s)");
        try {
            if (lastExecutedDate == null || DateUtil.getDistinceDay(lastExecutedDate, sysDate) >= 1) {
                setExecutedCount(0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        switch(period) {
            case (TaskConstants.REPEAT_TYPE_NONE):
                executeAble = isBetweenOneMinute(dateTimeDiff);
                break;
            case (TaskConstants.REPEAT_TYPE_DAILY):
                executeAble = isBetweenOneMinute(timeDiff);
                break;
            case (TaskConstants.REPEAT_TYPE_WEEKLY):
                executeAble = getDaysOfWeek().contains(new Integer(calendar.get(Calendar.DAY_OF_WEEK))) && isBetweenOneMinute(timeDiff);
                break;
            case (TaskConstants.REPEAT_TYPE_MONTHLY):
                executeAble = getDayOfMonth() == calendar.get(Calendar.DAY_OF_MONTH) && isBetweenOneMinute(timeDiff);
                break;
        }
        if (executeAble) {
            int count = getExecutedCount();
            setExecutedCount(++count);
            lastExecutedDate = sysDate;
        }
        if (getExecutedCount() != 1 && getExecutedCount() >= repeatTimes) {
            return false;
        }
        return executeAble;
    }

    /**
	 * 是否在1分钟以内(大于0小于等于60000毫秒)
	 * 
	 * @param timeDifference
	 *            时间差值
	 * @return 是否在1分钟以内, 如果是返回true, 否则返回false
	 */
    private boolean isBetweenOneMinute(long timeDifference) {
        return timeDifference > 0 && timeDifference <= 60 * 1000;
    }

    /**
	 * 获取下一次运行日期
	 * 
	 * @param sysDate
	 *            当前系统日期
	 * @return 日期
	 */
    private Date getNextRunningTime(Date sysDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sysDate);
        calendar.setTime(getRunningTime());
        calendar.add(Calendar.MINUTE, getExecutedCount() * frequency);
        return calendar.getTime();
    }

    /**
	 * 执行任务
	 */
    public void execute() {
        LOG.info("************ Execute Start *************");
        switch(type) {
            case TaskConstants.TASK_TYPE_SCRIPT:
                try {
                    StringBuffer label = new StringBuffer();
                    label.append("TASK(").append(getId()).append(")." + getName()).append(".TaskScript");
                    SuperUserProcess userProcess = (SuperUserProcess) ProcessFactory.createProcess(SuperUserProcess.class);
                    SuperUserVO superUser = (SuperUserVO) userProcess.doView(creatorid);
                    superUser.setApplicationid(this.applicationid);
                    superUser.setDomainid(this.domainid);
                    IRunner runner = JavaScriptFactory.getInstance(null, getApplicationid());
                    runner.initBSFManager(new Document(), new ParamsTable(), new WebUser(superUser), new ArrayList());
                    runner.run(label.toString(), this.getTaskScript());
                } catch (Exception e) {
                    LOG.warn("Run TaskScript Error: " + e.getMessage());
                }
                break;
        }
        LOG.info("************ Execute End *************");
    }

    /**
	 * 获取一周中的哪几天运行任务
	 * 
	 * @hibernate.list table = "T_TASK_DAYSOFWEEK" lazy = "false"
	 * @hibernate.collection-key column = "TASKID"
	 * @hibernate.collection-index column = "SORTNUMBER"
	 * @hibernate.collection-element column = "DAYOFWEEK" type = "int"
	 * @return 一周中的哪几天运行任务
	 */
    public Collection getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
	 * 设置一周中的哪几天运行任务
	 * 
	 * @param daysOfWeek
	 *            一周中的哪几天
	 */
    public void setDaysOfWeek(Collection daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /**
	 * 获取创建人标识
	 * 
	 * @hibernate.property column="CREATORID"
	 * @return 创建人标识
	 */
    public String getCreatorid() {
        return creatorid;
    }

    /**
	 * 设置创建人标识
	 * 
	 * @param creatorid
	 *            创建人标识
	 */
    public void setCreatorid(String creatorid) {
        this.creatorid = creatorid;
    }

    /**
	 * 获取一天中运行的次数
	 * 
	 * @hibernate.property column="REPEATTIMES"
	 * @return 运行的次数
	 */
    public int getRepeatTimes() {
        return repeatTimes;
    }

    /**
	 * 设置一天中运行的次数
	 * 
	 * @param repeatTimes
	 *            运行的次数
	 */
    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    /**
	 * 获取频率(时间/次数)
	 * 
	 * @hibernate.property column="FREQUENCY"
	 * @return 频率(时间/次数)
	 */
    public int getFrequency() {
        return frequency;
    }

    /**
	 * 设置频率(时间/次数)
	 * 
	 * @param frequency
	 *            频率(时间/次数)
	 */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
	 * 获取任务标识与已一天中已执行次数的映射
	 * 
	 * @return 执行次数
	 */
    public int getExecutedCount() {
        return executedCount;
    }

    /**
	 * 设置任务标识与已一天中已执行次数的映射
	 * 
	 * @param executedCount
	 *            次数
	 */
    public void setExecutedCount(int executedCount) {
        this.executedCount = executedCount;
    }

    /**
	 * 克隆对象
	 * 
	 * @return Object
	 */
    public Object clone() {
        return ObjectUtil.clone(this);
    }

    /**
	 * 获取应用的标识
	 * 
	 * @hibernate.property column="APPLICATIONID"
	 * @return 应用的标识
	 */
    public String getApplicationid() {
        return applicationid;
    }

    /**
	 * 设置应用标识
	 * 
	 * @param 应用标识
	 */
    public void setApplicationid(String applicationid) {
        this.applicationid = applicationid;
    }

    /**
	 * 获取关联模块对象
	 * 
	 * @return cn.myapps.core.deploy.module.ejb.ModuleVO
	 * @hibernate.many-to-one class="cn.myapps.core.deploy.module.ejb.ModuleVO"
	 *                        column="MODULE"
	 */
    public ModuleVO getModule() {
        return module;
    }

    /**
	 * 设置关联模块对象
	 * 
	 * @param module
	 *            模块对象
	 */
    public void setModule(ModuleVO module) {
        this.module = module;
    }
}
