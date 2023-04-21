package com.hy.erp.inventory.pojo.interfaces;

import com.hy.enterprise.framework.service.business.IServiceBusinessBean;
import com.hy.erp.inventory.pojo.DisposalWay;
import com.hy.framework.lang.annotation.ImplementsBy;

/**
 * 
 * <ul>
 * <li>开发作者：汤莉</li>
 * <li>设计日期：2010-11-13；时间：上午11:53:14</li>
 * <li>类型名称：DisposalWay</li>
 * <li>设计目的：质检不合格后货物的处理方式</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
@ImplementsBy(DisposalWay.class)
public interface IDisposalWay extends IServiceBusinessBean {

    public static String NAME = "NAME";

    public static String DESCRIPTION = "DESCRIPTION";
}
