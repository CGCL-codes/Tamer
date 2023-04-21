package com.rb.lottery.analysis.manager;

import com.rb.lottery.analysis.common.SystemConstants;
import com.rb.lottery.analysis.exception.LotteryException;

/**
 * @类功能说明: 自定义异常处理类
 * @类修改者: robin
 * @修改日期: 2011-10-31
 * @修改说明: 单例模式
 * @作者: robin
 * @创建时间: 2011-10-25 上午11:06:56
 * @版本: 1.0.0
 */
public class ExceptionManager {

    private static ExceptionManager exceptionManager = null;

    private LotteryException exception;

    public ExceptionManager() {
    }

    public ExceptionManager(LotteryException exception) {
        this.exception = exception;
    }

    public static ExceptionManager getInstance() {
        if (exceptionManager == null) {
            exceptionManager = new ExceptionManager();
        }
        return exceptionManager;
    }

    /**
	 * @return exception
	 */
    public LotteryException getException() {
        return exception;
    }

    /**
	 * @param exception
	 *            exception
	 */
    public void setException(LotteryException exception) {
        this.exception = exception;
    }

    public void processException() {
        if (exception == null || exception.getCode() == 0) {
            return;
        }
        int code = exception.getCode();
        String message = exception.getMessage();
    }
}
