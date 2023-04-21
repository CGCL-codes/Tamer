package com.brekeke.hiway.iccard.dao;

import com.brekeke.hiway.iccard.dto.StationMasterDto;
import com.brekeke.hiway.iccard.entity.StationMaster;
import com.brekeke.hiway.iccard.dao.BaseDAO;

/**
 * 站IC卡管理DAO
 * @author LEPING.LI
 * @version 1.0.0
 * 班次设置、库存极限、
 */
public interface StationMasterDAO extends BaseDAO {

    /**
	 * 根据站代号查询站IC管理对象
	 * @param smd
	 * @return
	 */
    public StationMaster selectSMasterByTscode(StationMasterDto smd);
}
