package com.uside.ido.service.sys;

import java.util.List;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uside.core.constant.SysConstant;
import com.uside.core.exception.DataAccessException;
import com.uside.core.exception.ServiceException;
import com.uside.ido.dao.sys.CityDAO;
import com.uside.ido.entity.sys.Area;
import com.uside.ido.entity.sys.City;

/**
 * <p>
 * 类功能描述:
 * </p>
 * 
 * @author 王桂元(Mike.Wang)
 * @创建日期：2009-6-12 下午01:22:01
 * @copyright (c) 2009-2009 王桂元. 保留所有权利.
 */
@Service
@Transactional
@RemoteProxy(name = "cityService", creator = SpringCreator.class, creatorParams = { @Param(name = "beanName", value = "cityService") })
public class CityService {

    @Autowired
    private CityDAO cityDAO;

    public List<City> listAllCity() throws ServiceException, DataAccessException {
        List<City> cityList = cityDAO.listCity(null, null);
        if (SysConstant.cityMap.isEmpty()) {
            System.out.println("系统缓存地市信息开始..............");
            if (cityList != null && cityList.size() > 0) {
                for (int i = 0; i < cityList.size(); i++) {
                    City city = cityList.get(i);
                    String[] citys = new String[] { city.getZhCityName(), city.getEnCityName(), city.getProvince().getId() };
                    SysConstant.cityMap.put(city.getId(), citys);
                }
            }
            System.out.println("系统缓存地市信息完成..............");
        }
        return cityList;
    }

    @RemoteMethod
    public List<City> listCityByProvince(String provId) throws ServiceException, DataAccessException {
        return cityDAO.listCity(provId, null);
    }
}
