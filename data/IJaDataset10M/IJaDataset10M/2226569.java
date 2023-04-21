package com.hy.enterprise.framework.persistence.api;

import com.hy.enterprise.framework.lang.IHierarchialbe;
import com.hy.mydesktop.shared.persistence.domain.util.Persistable;

/**
 * <ul>
 * <li>设计作者：刘川</li>
 * <li>设计日期：2009-8-12</li>
 * <li>设计时间：下午06:17:30</li>
 * <li>设计目的：具有持久化层次型结构能力的接口</li>
 * </ul>
 * <ul>
 * <b>修订历史</b>
 * <li>1、</li>
 * </ul>
 */
public interface HierarchyPersistable extends IHierarchialbe, Persistable {

    /**
	 * <ul>
	 * <li>设计作者：刘川</li>
	 * <li>设计日期：2009-8-12</li>
	 * <li>设计时间：下午09:32:31</li>
	 * <li>设计目的：获取节点左值</li>
	 * </ul>
	 * <ul>
	 * <b>修订历史</b>
	 * <li>1、</li>
	 * </ul>
	 * 
	 * @return
	 */
    public Integer getLeftValue();

    /**
	 * <ul>
	 * <li>设计作者：刘川</li>
	 * <li>设计日期：2009-8-12</li>
	 * <li>设计时间：下午09:31:41</li>
	 * <li>设计目的：获取节点右值</li>
	 * </ul>
	 * <ul>
	 * <b>修订历史</b>
	 * <li>1、</li>
	 * </ul>
	 * 
	 * @return
	 */
    public Integer getRightValue();

    /**
	 * <ul>
	 * <li>设计作者：刘川</li>
	 * <li>设计日期：2009-8-12</li>
	 * <li>设计时间：下午09:32:12</li>
	 * <li>设计目的：设置节点左值</li>
	 * </ul>
	 * <ul>
	 * <b>修订历史</b>
	 * <li>1、</li>
	 * </ul>
	 * 
	 * @param leftValue
	 */
    public void setLeftValue(Integer leftValue);

    /**
	 * <ul>
	 * <li>设计作者：刘川</li>
	 * <li>设计日期：2009-8-12</li>
	 * <li>设计时间：下午09:31:28</li>
	 * <li>设计目的：设置节点右值</li>
	 * </ul>
	 * <ul>
	 * <b>修订历史</b>
	 * <li>1、</li>
	 * </ul>
	 * 
	 * @param rightValue
	 */
    public void setRightValue(Integer rightValue);
}
