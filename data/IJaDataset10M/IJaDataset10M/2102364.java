package org.t2framework.lucy.behavior;

import java.util.Map;
import org.t2framework.commons.Constants;
import org.t2framework.commons.Disposable;
import org.t2framework.commons.Disposer;
import org.t2framework.commons.annotation.ConfigurationTarget;
import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.commons.meta.Config;
import org.t2framework.commons.meta.MethodDesc;
import org.t2framework.commons.util.CollectionsUtil;
import org.t2framework.lucy.Lifecycle;
import org.t2framework.lucy.Lucy;
import org.t2framework.lucy.annotation.core.DestroyMethod;
import org.t2framework.lucy.config.meta.impl.DestroyMethodConfig;
import org.t2framework.lucy.config.meta.impl.LifeCycleMethodConfig;
import org.t2framework.lucy.spi.AbstractBehavior;

/**
 * <#if locale="en">
 * <p>
 * DestroyMethodBehavior is a behavior class.It will invoke target method for
 * {@link DestroyMethod} as annotation configuration, and
 * {@link DestroyMethodConfig} as xml configuration.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author taichi
 */
@ConfigurationTarget({ DestroyMethod.class, DestroyMethodConfig.class })
public class DestroyMethodBehavior extends AbstractBehavior {

    protected Map<MethodDesc, Object> destroyedCache = CollectionsUtil.newHashMap();

    @SuppressWarnings("unchecked")
    public DestroyMethodBehavior(BeanDesc beanDesc, MethodDesc md, Config cd) {
        super(beanDesc, md, cd);
    }

    @Override
    public <T> T execute(final T t, Lucy lucy) {
        final MethodDesc methodDesc = getMethodDesc();
        if (methodDesc != null) {
            if (this.destroyedCache.containsKey(methodDesc) == false) {
                Disposer.add(new Disposable() {

                    @Override
                    public void dispose() {
                        Config cd = getConfig();
                        if (cd == null || cd.hasAnnotation()) {
                            methodDesc.invoke(t, Constants.EMPTY_ARRAY);
                        } else {
                            methodDesc.invoke(t, ((LifeCycleMethodConfig) cd).getArgs());
                        }
                        DestroyMethodBehavior.this.destroyedCache.put(methodDesc, t);
                    }
                });
            }
        }
        return t;
    }

    @Override
    public <T> void destroy() {
        this.destroyedCache.clear();
    }

    @Override
    public Lifecycle getLifecycle() {
        return Lifecycle.COMPONENT_DESTROY;
    }
}
