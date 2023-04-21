package org.swiftdao.entity;

import java.io.Serializable;

/**
 * 这个接口是所有有主键实体类要实现的接口，包括单一主键和复合主键。
 * 不设主键的实体类直接实现<code>Persistable</code>接口。
 * @param <T>
 * @author Wang Yuxing
 *
 */
public interface KeyedPersistable<T extends Serializable> extends Persistable {

    /**
	 * 获得该实体的主键。
	 *
	 * @return
	 */
    T getId();

    /**
	 * 设置实体主键，使用ORM进行持久化时不要直接调用此方法设置id。
	 *
	 * @param id
	 */
    void setId(T id);
}
