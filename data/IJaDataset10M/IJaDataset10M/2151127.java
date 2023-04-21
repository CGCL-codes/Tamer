package com.yict.csms.company.dao.impl;

import org.springframework.stereotype.Repository;
import com.yict.common.dao.impl.CommonDao;
import com.yict.csms.company.entity.ContractRate;

/**
 * 
 * 
 * @author Patrick.Deng
 * 
 */
@Repository
public class ContractRateDaoImpl extends CommonDao<ContractRate, Long> {

    public ContractRate findById(Long id) {
        return (ContractRate) this.getTemplate().get(ContractRate.class, id);
    }
}
