package com.liferay.portal.upgrade.util;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.upgrade.StagnantRowException;

/**
 * <a href="LazyPKUpgradeColumnImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class LazyPKUpgradeColumnImpl extends PKUpgradeColumnImpl {

    public LazyPKUpgradeColumnImpl(String name) {
        super(name, true);
    }

    public LazyPKUpgradeColumnImpl(String name, Integer oldColumnType) {
        super(name, oldColumnType, true);
    }

    public Object getNewValue(Object oldValue) throws Exception {
        ValueMapper valueMapper = getValueMapper();
        Long newValue = null;
        try {
            newValue = (Long) valueMapper.getNewValue(oldValue);
        } catch (StagnantRowException sre) {
            newValue = new Long(CounterLocalServiceUtil.increment());
            valueMapper.mapValue(oldValue, newValue);
        }
        return newValue;
    }
}
