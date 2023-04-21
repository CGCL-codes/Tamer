package com.air.admin.dao;

import com.air.common.dao.BaseDao;

public interface AdminComponentDao extends BaseDao {

    public <T> T selectByCode(String code);
}
