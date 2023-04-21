package com.rb.lt.config;

/**
 * @类功能说明: A interface Configuration class is a common interface which defined 
 * 				all common functions that used in LT system
 * @类修改者:   robin  
 * @修改日期:   2011-9-6
 * @修改说明:   add lottery functions
 * @作者:       robin
 * @创建时间:   2011-9-6 上午11:01:12
 * @版本:       1.0.0
 */
public interface ILtCommon {

    /**
	 * @方法说明: 
	 * @参数:     @param betting format(gameID([3|1|0|-][*]),)
	 * @参数:     @param passway format m * n
	 * @参数:     @return
	 * @return    int
	 * @throws
	 */
    public int getNoteNumber(String betting, String passway);

    /**
	 * @方法说明: combine function C(4, 2) = 6
	 * @参数:     @param d
	 * @参数:     @param z
	 * @参数:     @return
	 * @return    int
	 * @throws
	 */
    public int C(int d, int z);
}
