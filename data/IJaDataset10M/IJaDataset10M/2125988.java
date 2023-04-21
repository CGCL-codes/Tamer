package org.telluriumsource.inject;

/**
 * @author: Jian Fang (John.Jian.Fang@gmail.com)
 *
 * Date: Oct 4, 2010
 */
public class BeanInfo {

    public static final String NAME = "name";

    private String name;

    public static final String CLAZZ = "clazz";

    private Class clazz;

    public static final String CONCRETE = "concrete";

    private Class concrete;

    public static final String SINGLETON = "singleton";

    private boolean singleton = true;

    public static final String SCOPE = "scope";

    private Scope scope;

    public static final String INSTANCE = "instance";

    public static final String BEAN_INFO = "beanInfo";

    public BeanInfo() {
    }

    public BeanInfo(String name, Class clazz, Class concrete, boolean singleton, Scope scope) {
        this.name = name;
        this.clazz = clazz;
        this.concrete = concrete;
        this.singleton = singleton;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Class getConcrete() {
        return concrete;
    }

    public void setConcrete(Class concrete) {
        this.concrete = concrete;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String toString() {
        final int typicalLength = 64;
        final String avpSeparator = ": ";
        final String fieldSeparator = ", ";
        StringBuffer sb = new StringBuffer(typicalLength);
        sb.append("[").append(NAME).append(avpSeparator).append(name).append(fieldSeparator).append(CLAZZ).append(avpSeparator).append(clazz.getCanonicalName()).append(fieldSeparator).append(CONCRETE).append(avpSeparator).append(concrete.getCanonicalName()).append(fieldSeparator).append(SINGLETON).append(avpSeparator).append(singleton).append(fieldSeparator).append(SCOPE).append(avpSeparator).append(scope.toString()).append("]");
        return sb.toString();
    }
}
