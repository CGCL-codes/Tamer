package org.springframework.aop.target;

import org.springframework.beans.BeansException;

/**
 * TargetSource that lazily accesses a singleton from a BeanFactory.
 *
 * <p>Useful when a proxy reference is needed on initialization but
 * the actual target object should not be initialized until first use.
 * The target bean must me marked as "lazy-init" too, else it would get
 * instantiated by the BeanFactory on startup. For example:
 *
 * <pre>
 * &lt;bean id="serviceTarget" class="example.MyService" lazy-init="true"&gt;
 *   ...
 * &lt;/bean&gt;
 *
 * &lt;bean id="service" class="org.springframework.aop.framework.ProxyFactoryBean"&gt;
 *   &lt;property name="targetSource"&gt;
 *     &lt;bean class="org.springframework.aop.target.LazyInitTargetSource"&gt;
 *       &lt;property name="targetBeanName"&gt;&lt;idref local="serviceTarget"/&gt;&lt;/property&gt;
 *     &lt;/bean&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;</pre>
 *
 * The "serviceTarget" bean will not get initialized until a method on the
 * "service" proxy gets invoked.
 *
 * <p>Sub-classes can extend this class and override the {@link #postProcessTargetObject(Object)} to
 * perform some additional processing with the target object when it is first loaded.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 1.1.4
 * @see #postProcessTargetObject(Object)
 */
public class LazyInitTargetSource extends AbstractBeanFactoryBasedTargetSource {

    private Object target;

    public synchronized Object getTarget() throws BeansException {
        if (this.target == null) {
            this.target = getBeanFactory().getBean(getTargetBeanName());
            postProcessTargetObject(this.target);
        }
        return this.target;
    }

    /**
	 * Sub-classes may override this method to perform additional processing on
	 * the target object when it is first loaded.
	 */
    protected void postProcessTargetObject(Object targetObject) {
    }
}
