package org.paradise.dms.dao.impl;

import java.util.List;
import org.paradise.dms.pojo.RegisterLargeItemType;
import org.springframework.stereotype.Service;
import com.dheaven.framework.dao.BaseHibernateDao;

/**
 * Description: Copyright (c) 2008-2009 Neo. All Rights Reserved.
 * 
 * @version 1.0 Apr 9, 2009 10:25:21 PM 李萌（neolimeng@gmail.com）created
 */
@Service
public class RegisterLargeItemTypeDAOImpl extends BaseHibernateDao<RegisterLargeItemType> {

    @SuppressWarnings("unchecked")
    public List<RegisterLargeItemType> getAllRegisterLargeItemType() {
        return this.find("from RegisterLargeItemType where registerlargeitemtypeind=1");
    }
}
