package com.liferay.portlet.messageboards.service.persistence;

import com.liferay.util.dao.hibernate.Transformer;

/**
 * <a href="MBCategoryHBMUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class MBCategoryHBMUtil implements Transformer {

    public static com.liferay.portlet.messageboards.model.MBCategory model(MBCategoryHBM mbCategoryHBM) {
        return (com.liferay.portlet.messageboards.model.MBCategory) mbCategoryHBM;
    }

    public static MBCategoryHBMUtil getInstance() {
        return _instance;
    }

    public Comparable transform(Object obj) {
        return model((MBCategoryHBM) obj);
    }

    private static MBCategoryHBMUtil _instance = new MBCategoryHBMUtil();
}
