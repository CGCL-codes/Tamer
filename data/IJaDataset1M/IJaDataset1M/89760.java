package net.sourceforge.configured.rules.standard;

import java.lang.reflect.Field;
import net.sourceforge.configured.BeanGraphAutowiringServiceException;
import net.sourceforge.configured.annotation.Provider;
import net.sourceforge.configured.rules.BeanResolutionInfo;
import net.sourceforge.configured.rules.resolver.SimplePostProcessingObjectResolver;
import net.sourceforge.configured.rules.standard.typeresolver.ServiceProviderInfo;
import net.sourceforge.configured.rules.standard.typeresolver.ServiceProviderRegistry;
import net.sourceforge.configured.rules.template.StandardTemplateDependencyResolutionRule;
import net.sourceforge.configured.spring.rules.SpringServiceProviderDependencyResolutionRule;
import net.sourceforge.configured.utils.ConfiguredAnnotationUtils;

/**
 * Partially resolves the dependency by resolving appropriate the ServiceProvider
 * implementation, if it can find one.
 * <p/>
 * 
 * This rule delegates to a {@link ServiceProviderRegistry}, which 
 * finds a concrete class of the abstract type. The object is then
 * obtained by rerunning with the rulebase on this dependency with
 * the additional information added by this rule. 
 * 
 * @author dboyce
 * @see SpringServiceProviderDependencyResolutionRule
 */
public class ServiceProviderResolutionRule extends StandardTemplateDependencyResolutionRule {

    protected ServiceProviderRegistry concreteTypeResolver;

    @Override
    protected boolean canIdentifyDependency(BeanResolutionInfo contextHolder, Class<?> declaringClass, String fieldName, Class<?> fieldType) {
        return findServiceProvider(declaringClass, fieldName, fieldType) != null;
    }

    @Override
    protected void resolveDependency(BeanResolutionInfo contextHolder, Class<?> declaringClass, String fieldName, Class<?> fieldType) {
        ServiceProviderInfo info = findServiceProvider(declaringClass, fieldName, fieldType);
        if (info == null) {
            throw new IllegalStateException("only call this method if canIdentifyDependency returns true");
        }
        Class<?> concreteType = concreteTypeResolver.getImplementation(info);
        contextHolder.setObjectResolver(new SimplePostProcessingObjectResolver(declaringClass, fieldName, concreteType));
    }

    protected ServiceProviderInfo findServiceProvider(Class<?> declaringClass, String fieldName, Class<?> fieldType) {
        ServiceProviderInfo info = getServiceProviderInfo(declaringClass, fieldName, fieldType);
        if (testMode) {
            info.setTestServiceProvider(true);
            if (concreteTypeResolver.canResolve(info)) {
                return info;
            }
            info.setTestServiceProvider(false);
        }
        if (concreteTypeResolver.canResolve(info)) {
            return info;
        } else {
            return null;
        }
    }

    protected ServiceProviderInfo getServiceProviderInfo(Class<?> declaringClass, String fieldName, Class<?> fieldType) {
        try {
            Field field = ConfiguredAnnotationUtils.getDeclareFieldQuietly(declaringClass, fieldName);
            Provider provider = field != null ? field.getAnnotation(Provider.class) : null;
            if (provider == null) {
                return new ServiceProviderInfo(fieldType);
            } else {
                return new ServiceProviderInfo(fieldType, provider.name());
            }
        } catch (Exception e) {
            throw new BeanGraphAutowiringServiceException(e);
        }
    }

    public ServiceProviderRegistry getConcreteTypeResolver() {
        return concreteTypeResolver;
    }

    public void setConcreteTypeResolver(ServiceProviderRegistry concreteTypeResolver) {
        this.concreteTypeResolver = concreteTypeResolver;
    }
}
