package org.gary.base.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.gary.base.dao.XxxImageDao;
import org.gary.base.service.XxxImageService;
import org.springframework.stereotype.Component;

@Component("xxxImageService")
public class XxxImageServiceImpl implements XxxImageService {

    private XxxImageDao xxxImageDao;

    public void deleteByImageId(String target, int imageId) {
        xxxImageDao.deleteByImageId(target, imageId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] deleteByXxxId(String target, int xxxId) {
        List imageIdsList = xxxImageDao.deleteByXxxId(target, xxxId);
        Integer[] ids = null;
        if (imageIdsList != null) {
            ids = new Integer[imageIdsList.size()];
            for (int i = 0; i < ids.length; i++) {
                ids[i] = Integer.parseInt(imageIdsList.get(i).toString());
            }
        }
        return ids;
    }

    @Resource
    public void setXxxImageDao(XxxImageDao xxxImageDao) {
        this.xxxImageDao = xxxImageDao;
    }

    public XxxImageDao getXxxImageDao() {
        return xxxImageDao;
    }
}
