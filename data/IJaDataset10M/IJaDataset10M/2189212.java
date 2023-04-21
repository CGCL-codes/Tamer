package org.springframework.aop.framework;

/**
 * Interface to be implemented by objects that can create
 * AOP proxies based on AdvisedSupport objects.
 *
 * <p>Proxies should observe the following contract:
 * <ul>
 * <li>They should implement all interfaces that the configuration
 * indicates should be proxied.
 * <li>They should implement the Advised interface.
 * <li>They should implement the equals method to compare proxied
 * interfaces, advice, and target.
 * <li>They should be serializable if all advisors and target
 * are serializable.
 * <li>They should be thread-safe if advisors and target
 * are thread-safe.
 * </ul>
 *
 * <p>Proxies may or may not allow advice changes to be made.
 * If they do not permit advice changes (for example, because
 * the configuration was frozen) a proxy should throw an 
 * AopConfigException on an attempted advice change.
 *
 * @author Rod Johnson
 */
public interface AopProxyFactory {

    /**
	 * Return an AopProxy for the given AdvisedSupport object.
	 * @param advisedSupport the AOP configuration
	 * @return an AOP proxy
	 * @throws AopConfigException if the configuration is invalid
	 */
    AopProxy createAopProxy(AdvisedSupport advisedSupport) throws AopConfigException;
}
