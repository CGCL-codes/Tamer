package com.hy.erp.inventory.pojo.interfaces;

import com.hy.enterprise.framework.service.business.IServiceBusinessBean;
import com.hy.erp.inventory.pojo.Country;
import com.hy.framework.lang.annotation.ImplementsBy;

/**
 * 
 * <ul>
 * <li>开发作者：李冰</li>
 * <li>设计日期：2010-10-11；时间：上午09:25:13</li>
 * <li>类型名称：ICountry</li>
 * <li>设计目的：</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
@ImplementsBy(Country.class)
public interface ICountry extends IServiceBusinessBean {

    /**
	 * 国家名称缩写
	 */
    public static String ABBREVIATION = "ABBREVIATION";

    /**
	 * 国家描述信息
	 */
    public static String DESCRIPTION = "DESCRIPTION";

    /**
	 * 国家ISO编码
	 */
    public static String ISCODE = "ISCODE";

    /**
	 * 国家全名
	 */
    public static String NAME = "NAME";

    /**
	 * 名称代码
	 */
    public static String NAMECODE = "NAMECODE";

    /**
	 * 
	 * 位置类型
	 */
    public static String LOCATIONTYPE = "locationType";
}
