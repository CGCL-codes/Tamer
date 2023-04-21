package com.hy.erp.inventory.pojo.interfaces;

import com.hy.enterprise.framework.service.business.IServiceBusinessBean;
import com.hy.erp.inventory.pojo.UnitGroup;
import com.hy.framework.lang.annotation.ImplementsBy;

@ImplementsBy(UnitGroup.class)
public interface IUnitGroup extends IServiceBusinessBean {

    public static String NAME = "NAME";

    public static String DESCRIPTION = "DESCRIPTION";
}
