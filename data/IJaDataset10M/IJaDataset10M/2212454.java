package com.brekeke.hiway.ticket.dao;

import com.brekeke.hiway.ticket.entity.CancelledDataCertifcateNum;

/**
 * This interface defined all user security operation  
 * @author LEPING.LI
 * @version 1.0.0
 */
public interface CancelledDataCertifcateNumDAO extends BaseDAO {

    /**
     * 修改废票凭证号
     * @param xdt
     * @return
     */
    public void updateCancelledDataCertifcateNumByID(CancelledDataCertifcateNum cancelledDataCertifcateNum);

    /**
     * 查询废票凭证号
     * @param xdt
     * @return
     */
    public CancelledDataCertifcateNum searchCancelledDataCertifcateNumByID(CancelledDataCertifcateNum cancelledDataCertifcateNum);

    /**
     * 查询废票凭证号
     * @param xdt
     * @return
     */
    public void insertCancelledDataCertifcateNumByID(CancelledDataCertifcateNum cancelledDataCertifcateNum);
}
