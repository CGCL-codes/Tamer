package com.hy.enterprise.framework.persistence.api;

/**
 * <ul>
 * <li>设计作者：刘川</li>
 * <li>设计日期：2009-8-14</li>
 * <li>设计时间：上午11:05:11</li>
 * <li>设计目的：表现层次结构抽象数据访问对象的接口</li>
 * </ul>
 * <ul>
 * <b>修订历史</b>
 * <li>1、</li>
 * </ul>
 */
public interface IHierarchyDao<PersistentEntity extends HierarchyPersistable> extends IDao<PersistentEntity> {

    /**
	 * <ul>
	 * <li>设计作者：刘川</li>
	 * <li>设计日期：2009-8-24</li>
	 * <li>设计时间：下午06:33:22</li>
	 * <li>设计目的：获取指定树型结构节点实体的父节点实体</li>
	 * </ul>
	 * <ul>
	 * <b>修订历史</b>
	 * <li>1、</li>
	 * </ul>
	 * 
	 * @param persistentEntity
	 * @return
	 */
    public PersistentEntity getParentPersistentEntity(PersistentEntity persistentEntity);
}
