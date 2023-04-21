package com.liferay.portlet.shopping.service.impl;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.service.impl.PrincipalBean;
import com.liferay.portlet.shopping.service.persistence.ShoppingItemFieldUtil;
import com.liferay.portlet.shopping.service.spring.ShoppingItemFieldService;
import java.util.List;

/**
 * <a href="ShoppingItemFieldServiceImpl.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class ShoppingItemFieldServiceImpl extends PrincipalBean implements ShoppingItemFieldService {

    public List getItemFields(String itemId) throws PortalException, SystemException {
        return ShoppingItemFieldUtil.findByItemId(itemId);
    }
}
