package com.kongur.network.erp.manager.system.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kongur.network.erp.common.Paginable;
import com.kongur.network.erp.dao.basic.BscCodeValueDAO;
import com.kongur.network.erp.domain.basic.BscCodeValueDO;
import com.kongur.network.erp.manager.system.BscCodeValueManager;

@Service("bscCodeValueManager")
public class BscCodeValueManagerImpl implements BscCodeValueManager {

    @Autowired
    private BscCodeValueDAO bscCodeValueDAO;

    @Override
    public Long insertBscCodeValue(BscCodeValueDO bscCodeValueDO) {
        return bscCodeValueDAO.insertBscCodeValue(bscCodeValueDO);
    }

    @Override
    public Integer updateBscCodeValue(BscCodeValueDO bscCodeValueDO) {
        return bscCodeValueDAO.updateBscCodeValue(bscCodeValueDO);
    }

    @Override
    public BscCodeValueDO selectBscCodeValueById(Long id) {
        return bscCodeValueDAO.selectBscCodeValueById(id);
    }

    @Override
    public Paginable<BscCodeValueDO> selectBscCodeValueForPagin(Paginable<BscCodeValueDO> page) {
        return bscCodeValueDAO.selectBscCodeValueForPagin(page);
    }

    @Override
    public int UpdateCodeValueStatus(Map<String, Object> params) {
        return bscCodeValueDAO.UpdateCodeValueStatus(params);
    }

    @Override
    public List<BscCodeValueDO> getCodeValueByCodeType(String codeType) {
        return bscCodeValueDAO.getCodeValueByCodeType(codeType);
    }

    @Override
    public List<BscCodeValueDO> selectBscCodeValue() {
        return bscCodeValueDAO.selectBscCodeValue();
    }
}
