package com.air.common.service.imp;

import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import com.air.common.dao.BasicUserInfoDao;
import com.air.common.exception.DuplicateEntityException;
import com.air.common.exception.NoSuchUserException;
import com.air.common.exception.WrongPasswordException;
import com.air.common.mo.BasicUserInfo;
import com.air.common.service.IBasicUserInfoService;
import com.air.common.util.BasicDBDictionary;
import com.air.common.util.CommonUtil;
import com.air.common.util.QueryCondition;
import com.air.common.util.QueryExpression;

public class BasicUserInfoServiceImp extends BaseServiceImp implements IBasicUserInfoService, InitializingBean {

    @Autowired
    BasicUserInfoDao basicUserInfoMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.baseDao = basicUserInfoMapper;
    }

    @Override
    public BasicUserInfo validateUser(String loginName, String password) throws Exception {
        QueryCondition condition = new QueryCondition();
        condition.addQueryCondition("loginName", loginName, QueryExpression.EQUALS);
        condition.addQueryCondition("status", BasicDBDictionary._common_status_available, QueryExpression.EQUALS);
        List<BasicUserInfo> result = baseDao.queryByCondition(condition);
        if (result == null || result.size() == 0) {
            throw new NoSuchUserException();
        }
        if (result.size() > 1) {
            logger.error("FOUND " + result.size() + " User Entities (login_name=" + loginName + ")");
            throw new DuplicateEntityException();
        }
        BasicUserInfo user = result.get(0);
        if (!user.getPassword().equals(CommonUtil.getMD5Str(password))) {
            throw new WrongPasswordException();
        }
        return user;
    }
}
