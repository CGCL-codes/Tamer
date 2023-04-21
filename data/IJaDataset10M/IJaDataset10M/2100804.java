package com.xenonsoft.bridgetown.soa.spi;

import com.xenonsoft.bridgetown.aop.IMethodPointCut;
import com.xenonsoft.bridgetown.aop.IMixinPointcut;
import com.xenonsoft.bridgetown.soa.config.MixinConfig;
import com.xenonsoft.bridgetown.soa.config.ServiceConfig;

/**
 * This is a Java interface for an Java object that can weave
 * beans with aspect oriented method pointcuts.
 * 
 * @author peterp, 29-Sep-2004
 * @version $Id: IAOPWeaverProxyBeanBuilder.java,v 1.2 2005/04/20 02:54:26 peter_pilgrim Exp $
 */
public interface IAOPWeaverProxyBeanBuilder extends IProxyBeanBuilder {

    /**
     * Adds a method pointcut to the collection of pointcuts
     * to the AOP weaver
     * 
     * @param pointcut the method pointcut to add
     */
    public void addMethodPointCut(IMethodPointCut pointcut);

    /**
     * Returns an array of method pointcuts registered
     * with the AOP weaver
     * @return the array of method pointcuts
     */
    public IMethodPointCut[] getMethodPointCutList();

    /**
     * Registers a mixin method interceptor with the proxy bean builder in order
     * to instrument the bean creation. This method is called, obviously, before the
     * proxy bean is first created.
     * 
     * @param mixinConfig the mixin configuraton 
     * @param mixinPointcut the mixin pointcut implementation
     * @param serviceConfig the service bean configuration with the details of the
     * POJO target bean that needs to be instrumented.
     */
    public void registerMixinConfig(MixinConfig mixinConfig, IMixinPointcut mixinPoincut, ServiceConfig serviceConfig);

    /**
     * Returns an array of mixin pointcuts registered
     * with the AOP weaver
     * @return the array of mixin pointcuts
     */
    public IMixinPointcut[] getMixinPointcutList();

    /**
     * Retrieve the mixin pointcut implementation from the stored collection 
     * using the mixin configuration to locate.
     * @param mixConfig the mixin configuration to reference the mixin point 
     * implementation 
     * @return the mixin point implementation
     */
    public IMixinPointcut getMixinConfig(MixinConfig mixinConfig);
}
