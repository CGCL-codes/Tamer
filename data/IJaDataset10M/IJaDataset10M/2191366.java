package com.dsp.services.impl;

import java.io.Serializable;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.core.util.GenericDAO;
import com.core.util.GenericServiceHibernate;
import com.dsp.bean.Province;
import com.dsp.dao.ProvinceDAO;
import com.dsp.services.ProvinceService;

@Service("provinceService")
public class ProvinceServiceImpl extends GenericServiceHibernate<Province, Long> implements ProvinceService {

    private ProvinceDAO provinceDAO;

    @Resource(name = "provinceDAO")
    public void setDao(GenericDAO<Province, Serializable> dao) {
        super.setDao(dao);
    }

    /**
	 * @return the provinceDAO
	 */
    public ProvinceDAO getProvinceDAO() {
        return provinceDAO;
    }

    /**
	 * @param provinceDAO the provinceDAO to set
	 */
    public void setProvinceDAO(ProvinceDAO provinceDAO) {
        this.provinceDAO = provinceDAO;
    }
}
