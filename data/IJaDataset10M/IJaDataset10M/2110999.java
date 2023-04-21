package com.gm.demo.service.impl;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gm.demo.model.CusContact;
import com.gm.demo.dao.CusContactDao;
import com.gm.demo.service.CusContactManager;
import com.gm.common.orm.mybatis.BaseEntityDao;
import com.gm.common.orm.mybatis.BaseEntity;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.gm.common.orm.mybatis.BaseEntity;
import com.gm.common.service.Manager;
import com.gm.common.orm.mybatis.EntityDao;
import com.gm.common.service.BaseManager;

@Service("cusContactManager")
@Transactional
public class CusContactManagerImpl extends BaseManager<CusContact, java.lang.Long> implements CusContactManager {

    private CusContactDao cusContactDao;

    @Resource
    public void setCusContactDao(CusContactDao dao) {
        this.cusContactDao = dao;
    }

    public CusContactDao getEntityDao() {
        return this.cusContactDao;
    }
}
