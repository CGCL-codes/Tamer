package com.mkk.kenji1016.dao.impl;

import com.mkk.kenji1016.dao.FamilyDao;
import com.mkk.kenji1016.domain.FamilyImage;
import java.util.List;

/**
 * User: mkk
 * Date: 11-9-21
 * Time: 下午10:30
 */
public class HibernateFamilyDao extends AbstractDao implements FamilyDao {

    public void persistFamilyImages(List<FamilyImage> imgs) {
        hibernateTemplate.saveOrUpdateAll(imgs);
    }
}
