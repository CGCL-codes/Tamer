package com.tim.sptsystem.pojo;

/**@author huangjie
 *@date Jan 14, 2009
 *@filename Test.java
 *@packagename com.tim.sptsystem.pojo
 **/
public class Test implements java.io.Serializable {

    private String id;

    private String test;

    public Test() {
    }

    public Test(String test) {
        this.test = test;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTest() {
        return this.test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
