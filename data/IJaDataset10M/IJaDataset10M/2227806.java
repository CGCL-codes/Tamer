package com.craigstjean.interrogate.classreader;

/**
 * @author Craig St. Jean
 * @since Oct 9, 2009 12:42:55 AM
 */
public class MethodDeclaration {

    private String name;

    private String desc;

    private String signature;

    public MethodDeclaration(String name, String desc, String signature) {
        this.name = name;
        this.desc = desc;
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
