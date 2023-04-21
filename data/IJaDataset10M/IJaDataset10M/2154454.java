package org.easy.eao;

import java.util.Set;

/**
 * EAO 实例创建者接口
 * 
 * @author Frank
 */
public interface EaoBuilder {

    /**
	 * 创建 EAO 接口代理类实例
	 * 
	 * @param clazz
	 *            EAO 接口类型
	 * @param actions
	 *            需要使用的存取动作类型集合
	 * @return EAO 接口的动态代理实例
	 */
    Object builder(Class<?> clazz, Set<Class<Action<?>>> actions);
}
