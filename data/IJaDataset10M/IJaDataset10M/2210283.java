package com.nhncorp.cubridqa.model;

import java.io.File;
import java.util.StringTokenizer;
import com.nhncorp.cubridqa.utils.XstreamHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * A parent class of other data models.
 * @ClassName: Resource
 * @date 2009-9-3
 * @version V1.0 
 * Copyright (C) www.nhn.com
 */
@XStreamAlias(value = "resource")
public class Resource {

    protected String id;

    protected String name;

    public String path;

    public Resource() {
    }

    public static Resource getInstance(String absPath) {
        Resource instance = new Resource();
        if (absPath != null && !absPath.equals("")) {
            instance = (Resource) XstreamHelper.fromXml(absPath);
        }
        return instance;
    }

    public Resource(String absPath) {
        setName(absPath.replaceAll("\\\\", "/"));
    }

    public String getFileName() {
        return name.substring(name.lastIndexOf("/") + 1);
    }

    public String getRealName() {
        StringTokenizer stringTokenizer = new StringTokenizer(name, ".");
        String string = "";
        if (stringTokenizer.hasMoreTokens()) {
            string = stringTokenizer.nextToken();
        }
        return string.substring(string.lastIndexOf("/") + 1, string.length());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Case) {
            Case c = (Case) obj;
            return name != null && name.equals(c.getName());
        }
        return false;
    }

    public File getFile() {
        return new File(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getTITLE() {
        return "Resource";
    }
}
