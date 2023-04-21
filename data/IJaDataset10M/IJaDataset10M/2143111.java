package com.rb.ft.config;

import java.util.List;

/**
 * @类功能说明: A interface Configuration class is a common interface which defined 
 * 				all common functions that used in FT system
 * @类修改者:   robin  
 * @修改日期:   2011-9-6
 * @修改说明:   basic change
 * @作者:       robin
 * @创建时间:   2011-7-13 下午01:48:40
 * @版本:       1.0.0
 */
public interface IFtCommon<T> {

    /**
	 * @方法说明: check the object is unique or not in the database
	 * @参数:     @param list
	 * @参数:     @param obj
	 * @参数:     @return
	 * @return    boolean
	 * @throws
	 */
    public boolean doCheckUnique(List<T> list, T obj);
}
