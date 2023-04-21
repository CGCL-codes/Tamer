package com.mycila.testing.plugin.spring;

import com.mycila.testing.core.api.TestContext;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.support.GenericXmlContextLoader;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static com.mycila.testing.core.introspect.Filters.excludeOverridenMethods;
import static com.mycila.testing.core.introspect.Filters.fieldsAnnotatedBy;
import static com.mycila.testing.core.introspect.Filters.methodsAnnotatedBy;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class MycilaContextLoader extends GenericXmlContextLoader {

    private final TestContext mycilaContext;

    public MycilaContextLoader(TestContext mycilaContext) {
        this.mycilaContext = mycilaContext;
    }

    public String[] contextLocations() {
        Set<String> contextLocations = new HashSet<String>();
        Class<?> clazz = mycilaContext.introspector().testClass();
        do {
            SpringContext ctx = clazz.getAnnotation(SpringContext.class);
            if (ctx != null) {
                contextLocations.addAll(Arrays.asList(ctx.locations()));
            }
            clazz = clazz.getSuperclass();
        } while (!clazz.equals(Object.class));
        return contextLocations.toArray(new String[contextLocations.size()]);
    }

    @Override
    protected void customizeContext(GenericApplicationContext context) {
        SpringContext ctx = mycilaContext.introspector().testClass().getAnnotation(SpringContext.class);
        if (ctx != null && ctx.classes() != null && ctx.classes().length > 0) setupJavaConfig(context, ctx);
        for (Field field : mycilaContext.introspector().selectFields(fieldsAnnotatedBy(Bean.class))) {
            Bean annotation = field.getAnnotation(Bean.class);
            context.registerBeanDefinition(getBeanName(field, annotation), createBeanDefinition(field, FieldAccessFactoryBean.class, annotation.scope()));
        }
        for (Method method : mycilaContext.introspector().selectMethods(excludeOverridenMethods(methodsAnnotatedBy(Bean.class)))) {
            Bean annotation = method.getAnnotation(Bean.class);
            context.registerBeanDefinition(getBeanName(method, annotation), createBeanDefinition(method, MethodAccessFactoryBean.class, annotation.scope()));
        }
        context.registerBeanDefinition("org.springframework.test.context.TestContext", createBeanDefinition(mycilaContext.attributes().get("org.springframework.test.context.TestContext"), ObjectFactoryBean.class));
    }

    private void setupJavaConfig(GenericApplicationContext context, SpringContext ctx) {
        context.setParent(new org.springframework.context.annotation.AnnotationConfigApplicationContext(ctx.classes()));
    }

    protected String getBeanName(Member member, Bean annotation) {
        String name = annotation.name();
        return name == null || name.length() == 0 ? member.getName() : name;
    }

    private AbstractBeanDefinition createBeanDefinition(Object object, Class beanClass) {
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addIndexedArgumentValue(0, object);
        GenericBeanDefinition beanDef = new GenericBeanDefinition();
        beanDef.setBeanClass(beanClass);
        beanDef.setScope(Bean.Scope.SINGLETON.value());
        beanDef.setAutowireCandidate(true);
        beanDef.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_AUTODETECT);
        beanDef.setConstructorArgumentValues(args);
        return beanDef;
    }

    private AbstractBeanDefinition createBeanDefinition(AccessibleObject access, Class beanClass, Bean.Scope scope) {
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addIndexedArgumentValue(0, mycilaContext.introspector().instance());
        args.addIndexedArgumentValue(1, access);
        GenericBeanDefinition beanDef = new GenericBeanDefinition();
        beanDef.setBeanClass(beanClass);
        beanDef.setScope(scope.value());
        beanDef.setAutowireCandidate(true);
        beanDef.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_AUTODETECT);
        beanDef.setConstructorArgumentValues(args);
        return beanDef;
    }
}
