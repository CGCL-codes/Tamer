package org.t2framework.lucy.config.meta;

import org.t2framework.commons.meta.Config;

/**
 * 
 * <#if locale="en">
 * <p>
 * InjectConfig is a specialized configuration meta class for inject tag.
 * </p>
 * <#else>
 * <p>
 * {@link InjectConfig}は設定ファイルからのインジェクトを表します．
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public interface InjectConfig extends Config {

    void setParameterTypes(Class<?>[] paramTypes);

    Class<?>[] getParameterTypes();

    boolean isAutoinjected();
}
