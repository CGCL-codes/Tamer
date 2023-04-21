package org.dozer.vo.isaccessible;

public class PrivateConstructorBean {

    private String field1;

    private PrivateConstructorBean() {
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public static PrivateConstructorBean newInstance() {
        return new PrivateConstructorBean();
    }
}
