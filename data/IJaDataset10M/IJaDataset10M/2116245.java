package com.hy.enterprise.architecture.common;

import com.hy.enterprise.architecture.ArchitectureException;

/**
 * <ul>
 * <li>设计作者：刘川</li>
 * <li>设计日期：2009-8-13</li>
 * <li>设计时间：下午11:23:53</li>
 * <li>设计目的：</li>
 * </ul>
 * <ul>
 * <b>修订历史</b>
 * <li>1、</li>
 * </ul>
 */
public class ArchitectureCommonException extends ArchitectureException {

    private static final long serialVersionUID = 6311022330597945013L;

    /**
	 * 构造函数
	 */
    public ArchitectureCommonException() {
        super();
    }

    /**
	 * 构造函数
	 * 
	 * @param message
	 */
    public ArchitectureCommonException(String message) {
        super(message);
    }

    /**
	 * 构造函数
	 * 
	 * @param message
	 * @param nested
	 */
    public ArchitectureCommonException(String message, Throwable nested) {
        super(message, nested);
    }

    /**
	 * 构造函数
	 * 
	 * @param nested
	 */
    public ArchitectureCommonException(Throwable nested) {
        super(nested);
    }
}
