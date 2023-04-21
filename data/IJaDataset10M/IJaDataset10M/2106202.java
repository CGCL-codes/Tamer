package org.neodatis.odb.test.arraycollectionmap;

import java.util.ArrayList;
import java.util.List;

public class ObjectWithListOfInteger {

    private String name;

    private List listOfIntegers;

    public ObjectWithListOfInteger(String name) {
        this.name = name;
        listOfIntegers = new ArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getListOfIntegers() {
        return listOfIntegers;
    }

    public void setListOfIntegers(List listOfIntegers) {
        this.listOfIntegers = listOfIntegers;
    }
}
