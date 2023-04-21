package org.hlj.commons.helper;

import org.hlj.commons.common.CommonUtil;
import org.hlj.param.bean.EntityBean;

/**
 * 继承Entity实体的操作助手类的基础实现
 * @author WD
 * @since JDK5
 * @version 1.0 2009-11-23
 */
public abstract class BaseEntityHelper<E extends EntityBean> implements EntityHelper<E> {

    private E entity;

    /**
	 * 检查实体对象 为空返回一个空的对象
	 * @param E 实体实体
	 * @return 实体实体
	 */
    public final E check(E E) {
        return CommonUtil.isEmpty(E) ? getInstance() : E;
    }

    /**
	 * 获得一个空的实体对象 此实体只做null的代替,不做赋值操作
	 * @return 实体对象
	 */
    public final synchronized E getInstance() {
        return CommonUtil.isEmpty(entity) ? entity = newInstance() : entity;
    }

    /**
	 * 根据ID构造一个实体
	 * @param id 主键ID
	 * @return 实体
	 */
    public final E newInstance(int id) {
        E e = newInstance();
        e.setId(id);
        return e;
    }

    /**
	 * 根据ID构造一个实体
	 * @param id 主键ID
	 * @param name 名称
	 * @return 实体
	 */
    public final E newInstance(int id, String name) {
        E e = newInstance(id);
        e.setName(name);
        return e;
    }
}
