package com.be.vo;

public class NavigationMenuVO {

    private long id;

    private String moduleName;

    private long seqNumber;

    private String name;

    private String link;

    private long level;

    private long parent;

    public void setId(long id) {
        this.id = id;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setSeqNumber(long seqNumber) {
        this.seqNumber = seqNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public void setParent(long parent) {
        this.parent = parent;
    }

    public long getId() {
        return id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public long getSeqNumber() {
        return seqNumber;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public long getLevel() {
        return level;
    }

    public long getParent() {
        return parent;
    }

    public String toString() {
        return id + ";" + moduleName + ";" + seqNumber + ";" + name + ";" + link + ";" + level + ";" + parent;
    }

    public void init() {
        id = 0;
        moduleName = "";
        seqNumber = 0;
        name = "";
        link = "";
        level = 0;
        parent = 0;
    }
}
