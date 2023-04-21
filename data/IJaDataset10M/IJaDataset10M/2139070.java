package com.szxys.mhub.bizmanager;

import com.szxys.mhub.bizinterfaces.ISubSystemCallBack;

/**
 * 业务系统管理层暴露给上层的接口 （主要由子业务界面层调用）
 * 
 * @author 黄仕龙
 */
public interface IBusinessManager {

    /**
	 * @param userID
	 * @param subSystemID
	 * @param subSysCallBack
	 * @return long
	 */
    public long startSubSystem(int userID, int subSystemID, ISubSystemCallBack subSysCallBack);

    /**
	 * @param userID
	 * @param subSystemID
	 * @return long
	 */
    public long stopSubSystem(int userID, int subSystemID);

    /**
	 * @param userID
	 * @param subSystemID
	 * @param ctrlID
	 * @param paramIn
	 * @param paramOut
	 * @return long
	 */
    public long control(int userID, int subSystemID, int ctrlID, Object[] paramIn, Object[] paramOut);
}
