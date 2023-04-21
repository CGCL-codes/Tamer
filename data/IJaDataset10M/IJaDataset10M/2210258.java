package edu.thu.tsinghua.iw.app.model.flow;

/**
 * ����
 * @author Panda
 *
 */
public class Param implements Cloneable {

    private String name;

    private String value;

    private String realValue;

    public Param() {
    }

    public Param(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRealValue() {
        return realValue;
    }

    public void setRealValue(String realValue) {
        this.realValue = realValue;
    }

    public String toString() {
        return "[" + name + ":" + realValue + "]";
    }

    public Param clone() throws CloneNotSupportedException {
        return (Param) super.clone();
    }
}
