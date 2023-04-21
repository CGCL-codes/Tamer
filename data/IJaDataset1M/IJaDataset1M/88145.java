package org.xiangxji.util.framework.asynlog;

/**
 * 默认任务接口定义
 * @author xiangxji
 *
 */
public interface ISchedule<T> extends Runnable {

    /**
	 * 初始化
	 */
    public void init();

    /**
	 * 停止任务
	 */
    public void stopSchedule();
}
