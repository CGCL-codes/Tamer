package net.sf.balm.spring.context.config;

import java.lang.reflect.Method;
import org.springframework.context.config.ContextNamespaceHandler;

/**
 * @author dz
 */
public class ContextNamespaceHandlerExt extends ContextNamespaceHandler {

    @Override
    public void init() {
        registerJava5DependentParserExt("component-scan", "net.sf.balm.spring.context.annotation.ComponentScanBeanDefinitionParserExt");
    }

    /**
     * 通过反射调用父类的私有方法
     */
    private void registerJava5DependentParserExt(final String elementName, final String parserClassName) {
        System.out.println("Registeriing " + elementName + ":" + parserClassName);
        try {
            Method method = this.getClass().getSuperclass().getDeclaredMethod("registerJava5DependentParser", String.class, String.class);
            boolean accessible = method.isAccessible();
            if (!accessible) {
                method.setAccessible(true);
            }
            method.invoke(this, elementName, parserClassName);
            method.setAccessible(accessible);
        } catch (Exception e) {
            throw new RuntimeException("Invoke parent private method[#registerJava5DependentParser] failed!", e);
        }
    }
}
