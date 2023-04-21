package com.nodeshop.dao;

import com.nodeshop.entity.Reship;

/**
 * Dao接口 - 退货
 
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
 
 
 
 
 
 * KEY: nodeshop637C7DD59A103EF33A7394FCC82CDD9F
 
 */
public interface ReshipDao extends BaseDao<Reship, String> {

    /**
	 * 获取最后生成的退货编号
	 * 
	 * @return 退货编号
	 */
    public String getLastReshipSn();
}
