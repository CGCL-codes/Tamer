package appengine;

import candy.core.entity.IEntity;

public interface AppEngine_Eav_IEntity {

    Object getKey();

    /**
	 * Eav对象模型转换为AppEngine模型
	 * @param entity
	 */
    void EavToAppEngineEav(final IEntity entity);

    /**
	 * AppEngine模型转换为Eav模型
	 * @param entity
	 */
    void AppEngineEavToEav(final IEntity entity);

    /**
	 * AppEngine模型转换为Eav模型
	 * @param entity
	 */
    IEntity AppEngineEavToEav();
}
