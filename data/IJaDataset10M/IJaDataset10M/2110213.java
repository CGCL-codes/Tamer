package com.multimedia.service.advertisementPages;

import com.multimedia.model.beans.AdvertisementPages;
import common.dao.IGenericDAO;
import common.services.generic.GenericServiceImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author demchuck.dima@gmail.com
 */
@Service(value = "advertisementPagesService")
public class AdvertisementPagesServiceImpl extends GenericServiceImpl<AdvertisementPages, Long> implements IAdvertisementPagesService {

    @Override
    public void init() {
        super.init();
    }

    @Override
    @Resource(name = "advertisementPagesDAO")
    public void setDao(IGenericDAO<AdvertisementPages, Long> dao) {
        super.setDao(dao);
    }
}
