package com.liferay.portal.service.persistence;

/**
 * <a href="BatchSessionEnabled.java.html"><b><i>View Source</i></b></a>
 *
 * @author Raymond Augé
 * @author Brian Wing Shun Chan
 *
 */
public class BatchSessionEnabled extends ThreadLocal<Boolean> {

    public Boolean initialValue() {
        return Boolean.FALSE;
    }
}
