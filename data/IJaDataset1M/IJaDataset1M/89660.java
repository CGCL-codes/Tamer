package com.air.admin.service.imp;

import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.air.admin.dao.AdminComponentDao;
import com.air.admin.mo.AdminComponent;
import com.air.admin.service.IAdminComponentService;
import com.air.common.exception.NoSuchEntityException;
import com.air.common.service.imp.BaseServiceImp;
import com.air.common.util.BasicDBDictionary;
import com.air.common.util.QueryCondition;
import com.air.common.util.QueryExpression;

@Service("adminComponentService")
public class AdminComponentServiceImp extends BaseServiceImp implements IAdminComponentService, InitializingBean {

    @Autowired
    AdminComponentDao adminComponentMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.baseDao = adminComponentMapper;
    }

    @Override
    public AdminComponent findComponentByCode(String code) throws NoSuchEntityException {
        QueryCondition condition = new QueryCondition();
        condition.addQueryCondition("status", BasicDBDictionary._common_status_available, QueryExpression.EQUALS);
        condition.addQueryCondition("code", code, QueryExpression.EQUALS);
        List<AdminComponent> component = adminComponentMapper.queryByCondition(condition);
        if (component == null || component.size() == 0) {
            throw new NoSuchEntityException("Can't find admin component with code=" + code);
        }
        return component.get(0);
    }
}
