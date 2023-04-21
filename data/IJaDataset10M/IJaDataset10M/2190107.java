package com.hy.enterprise.architecture.foundation;

import com.hy.enterprise.architecture.ArchitectureException;

/**
 * <ul>
 * <li>设计作者：刘川</li>
 * <li>设计日期：2009-8-13</li>
 * <li>设计时间：下午11:24:28</li>
 * <li>设计目的：</li>
 * </ul>
 * <ul>
 * <b>修订历史</b>
 * <li>1、</li>
 * </ul>
 */
public class ArchitectureFoundationException extends ArchitectureException {

    private static final long serialVersionUID = -707181360951731524L;

    /**
	 * 构造函数
	 */
    public ArchitectureFoundationException() {
        super();
    }

    /**
	 * 构造函数
	 * 
	 * @param message
	 */
    public ArchitectureFoundationException(String message) {
        super(message);
    }

    /**
	 * 构造函数
	 * 
	 * @param message
	 * @param nested
	 */
    public ArchitectureFoundationException(String message, Throwable nested) {
        super(message, nested);
    }

    /**
	 * 构造函数
	 * 
	 * @param nested
	 */
    public ArchitectureFoundationException(Throwable nested) {
        super(nested);
    }
}
