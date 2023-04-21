package com.nhncorp.usf.macro.method;

/**
 * Freemarker TemplateMethod에 대한 환정 정보를 담고 있는 클래스
 *
 * @author Web Platform Development Team
 */
public class MethodInfo {

    private String name;

    private String className;

    private String namespace;

    /**
     * 생성자
     */
    public MethodInfo(String name, String className, String namespace) {
        this.name = name;
        this.className = className;
        this.namespace = namespace;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets the namespace.
     *
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }
}
