package com.gm.security.dao.impl;

import org.springframework.stereotype.Repository;
import com.gm.common.orm.mybatis.BaseEntityDao;
import com.gm.security.dao.PrivilegeInfoDao;
import com.gm.security.model.PrivilegeInfo;

@Repository("privilegeInfoDao")
public class PrivilegeInfoDaoImpl extends BaseEntityDao<PrivilegeInfo, java.lang.Long> implements PrivilegeInfoDao {

    @Override
    public String getMybatisMapperNamesapce() {
        return "com.gm.security.model.PrivilegeInfo";
    }

    public void saveOrUpdate(PrivilegeInfo privilegeInfo) {
        if (privilegeInfo.getPrivilegeId() == null) save(privilegeInfo); else update(privilegeInfo);
    }
}
