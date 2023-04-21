package com.mycila.plugin.spi.model;

import com.mycila.plugin.Provider;
import com.mycila.plugin.spi.aop.ProxyConfig;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ProviderProxyFactory {

    private final Binding<?> binding;

    private final Provider<?> provider;

    public ProviderProxyFactory(Binding<?> binding, Provider<?> provider) {
        this.binding = binding;
        this.provider = provider;
    }

    public Object build() {
        if (binding.isProvided()) return provider;
        Class<?> type = binding.getType().getRawType();
        return ProxyConfig.create().withTargetClass(type).withConfigExposed().preferCglib().addInterceptor(new MethodInterceptor() {

            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                return invocation.getMethod().invoke(provider.get(), invocation.getArguments());
            }
        }).buildProxy();
    }

    public static ProviderProxyFactory create(Binding<?> binding, Provider<?> provider) {
        return new ProviderProxyFactory(binding, provider);
    }
}
