package org.eweb4j.mvc.config.bean;

import org.eweb4j.util.xml.AttrTag;

/**
 * MVC组件用来存取配置信息的Bean
 * 
 * @author cfuture.aw
 * @since v1.a.0
 */
public class ResultConfigBean {

    @AttrTag
    private String name = "success";

    @AttrTag
    private String type = "forward";

    @AttrTag
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String value) {
        this.location = value;
    }

    @Override
    public String toString() {
        return "ResultConfigBean [name=" + name + ", type=" + type + ", location=" + location + "]";
    }
}
