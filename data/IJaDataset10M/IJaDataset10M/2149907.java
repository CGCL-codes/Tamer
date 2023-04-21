package com.yict.csms.resourceplan.dao.impl;

import org.springframework.stereotype.Repository;
import com.yict.common.dao.impl.CommonDao;
import com.yict.csms.resourceplan.dao.IRtgcFlTimeDao;
import com.yict.csms.resourceplan.entity.RtgcFlTime;

@Repository
public class RtgcFlTimeDaoImpl extends CommonDao<RtgcFlTime, Long> implements IRtgcFlTimeDao {

    public RtgcFlTime findbyId(Long id) {
        RtgcFlTime t = null;
        try {
            Object obj = this.getTemplate().get(RtgcFlTime.class, id);
            t = (RtgcFlTime) obj;
        } catch (Exception e) {
            logger.error(e);
        }
        return t;
    }
}
