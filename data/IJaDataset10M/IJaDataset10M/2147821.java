package com.lxitedu.freemarker;

import java.util.LinkedList;
import java.util.List;

public class MainMenu {

    private String name;

    private String url;

    private String desc;

    private List subMenuList = new LinkedList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void addSubMenu(Object o) {
        getSubMenuList().add(o);
    }

    public void romoveSubMenu(Object o) {
        getSubMenuList().remove(o);
    }

    public void setSubMenuList(List subMenuList) {
        this.subMenuList = subMenuList;
    }

    public List getSubMenuList() {
        return subMenuList;
    }
}
