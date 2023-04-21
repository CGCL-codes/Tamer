package com.myjavalab.spring;

import com.myjavalab.util.PrintUtil;

public interface Animal {

    public void say();
}

class Cat implements Animal {

    private String name;

    public void say() {
        PrintUtil.prt("cat..");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
