package com.ibm.bx.service.impl;

import java.util.List;
import com.ibm.bx.dao.BxQueryDao;
import com.ibm.bx.service.BxQueryManager;

/**
 * @author ibm
 *
 */
public class BxQueryManagerImpl implements BxQueryManager {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4478268876589639578L;

    private BxQueryDao bxQueryDao = null;

    /**
	 * @param bxQueryDao the bxQueryDao to set
	 */
    public void setBxQueryDao(BxQueryDao bxQueryDao) {
        this.bxQueryDao = bxQueryDao;
    }

    public List getBxQueryList(String whereSql) {
        return bxQueryDao.getBxQueryList(whereSql);
    }

    public void deleteBx(String whereSql) {
        bxQueryDao.deleteBx(whereSql);
    }

    public List getBudgetQueryList(String whereSql) {
        return bxQueryDao.getBudgetQueryList(whereSql);
    }

    /**
	 * get a unit's total sum of bx in one year 
	 * @param selectYear
	 * @param unitName
	 * @param flag
	 * @return
	 */
    public double getSumBxOfUnit(String selectYear, String userName, String flag) {
        return bxQueryDao.getSumBxOfUnit(selectYear, userName, flag);
    }
}
