package com.yict.csms.resourceplan.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.yict.common.entity.PageEntity;
import com.yict.common.service.impl.BaseServiceImpl;
import com.yict.csms.resourceplan.dao.ICarQcNeedhourDao;
import com.yict.csms.resourceplan.dao.ICarStableNeedhourDao;
import com.yict.csms.resourceplan.dao.ICarTimeDao;
import com.yict.csms.resourceplan.entity.CarQcNeedhour;
import com.yict.csms.resourceplan.entity.CarStableNeedhour;
import com.yict.csms.resourceplan.entity.CarTime;
import com.yict.csms.resourceplan.service.ICarTimeService;

@Service("carTimeService")
public class CarTimeServiceImpl extends BaseServiceImpl<CarTime, Long, ICarTimeDao> implements ICarTimeService {

    @Resource(name = "carTimeDao")
    public void setBaseDao(ICarTimeDao baseDao) {
        super.setBaseDao(baseDao);
    }

    @Resource(name = "carQcNeedhourDao")
    private ICarQcNeedhourDao carQcNeedhourDao;

    @Resource(name = "carStableNeedhourDao")
    private ICarStableNeedhourDao carStableNeedhourDao;

    public List<CarTime> search(Map<String, Object> queryMap, PageEntity page) {
        List<CarTime> list = new ArrayList<CarTime>();
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder("from CarTime t where 1=1 ");
        String carDate = (String) queryMap.get("rtgcfldate");
        if (carDate != null && carDate.length() > 0) {
            map.put("carDate", carDate);
            hql.append(" and t.cardate like to_date(:carDate,'yyyy-mm-dd')");
        }
        String rtgcFlTime = (String) queryMap.get("rtgcfltime");
        if (rtgcFlTime != null && rtgcFlTime.length() > 0) {
            map.put("rtgcFlTime", rtgcFlTime);
            hql.append(" and t.cartime = :rtgcFlTime");
        }
        int count = this.getBaseDao().queryCount("select count(*) " + hql.toString(), map);
        if (count > 0) {
            if (page == null) {
                list = this.getBaseDao().list(hql.toString(), map, 0, count);
            }
            if (page != null) {
                list = this.getBaseDao().list(hql.toString(), map, (page.getToPage() - 1) * page.getPageSize(), page.getPageSize());
            }
        }
        return list;
    }

    @Transactional(readOnly = false)
    @Override
    public boolean save(List<CarTime> carTimes, List<CarQcNeedhour> qcNeedHours, List<CarStableNeedhour> carStableNeedHours) throws Exception {
        boolean bool = true;
        try {
            for (CarTime carTime : carTimes) {
                if (carTime.getTimeid() != null) {
                    this.getBaseDao().update(carTime);
                } else {
                    this.getBaseDao().save(carTime);
                }
                for (CarQcNeedhour carQcNeedhour : qcNeedHours) {
                    if (carQcNeedhour.getCarTime() == null) {
                        carQcNeedhour.setCarTime(carTime);
                    }
                    if (carQcNeedhour.getNeedhourid() == null) {
                        carQcNeedhourDao.save(carQcNeedhour);
                    } else {
                        carQcNeedhourDao.update(carQcNeedhour);
                    }
                }
                for (CarStableNeedhour carStableNeedhour : carStableNeedHours) {
                    if (carStableNeedhour.getCarTime() == null) {
                        carStableNeedhour.setCarTime(carTime);
                    }
                    if (carStableNeedhour.getNeedhourid() == null) {
                        carStableNeedhourDao.save(carStableNeedhour);
                    } else {
                        carStableNeedhourDao.update(carStableNeedhour);
                    }
                }
            }
        } catch (Exception e) {
            bool = false;
            throw new Exception(e);
        }
        return bool;
    }
}
