package com.performance.dao;

import com.jxva.dao.BaseDao;
import com.performance.model.ColorRing;

/**
 * 
 * @author  The Jxva Framework Foundation
 * @since   1.0
 * @version 2010-02-10 10:00:25 by Automatic Generate Toolkit
 */
public class ColorRingDao extends BaseDao {

    public ColorRing getColorRing(int colorRingId) {
        return dao.get(ColorRing.class, colorRingId);
    }

    public int save(ColorRing colorRing) {
        return dao.save(colorRing);
    }

    public int update(ColorRing colorRing) {
        return dao.update(colorRing);
    }

    public int delete(ColorRing colorRing) {
        return dao.delete(colorRing);
    }

    public int delete(int colorRingId) {
        return dao.delete(ColorRing.class, colorRingId);
    }

    public int saveOrUpdate(ColorRing colorRing) {
        return dao.saveOrUpdate(colorRing);
    }
}
