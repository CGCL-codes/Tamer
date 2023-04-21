package org.ztemplates.test.reflection;

import java.util.ArrayList;
import java.util.List;

public class TestClass2 {

    private String value;

    private List<String> stringList = new ArrayList<String>();

    public List<String> getStringList() {
        return stringList;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
