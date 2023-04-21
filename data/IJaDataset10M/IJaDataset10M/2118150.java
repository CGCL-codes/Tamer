package com.twolattes.json;

@Entity
public class AWithNonstandardAccessorNames {

    private String a;

    private String aTag;

    @Value(name = "foo")
    public String getA() {
        return a;
    }

    @Value(name = "foo")
    public void setAWithWeirdName(String a) {
        this.a = a;
    }

    @Value(name = "bar")
    public String getATag() {
        return aTag;
    }

    @Value(name = "bar")
    public void setATag(String aTag) {
        this.aTag = aTag;
    }
}
