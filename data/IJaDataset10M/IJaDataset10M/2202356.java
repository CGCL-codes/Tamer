package org.jcrom;

import java.util.List;
import org.jcrom.annotations.JcrChildNode;
import org.jcrom.annotations.JcrName;
import org.jcrom.annotations.JcrPath;
import org.jcrom.annotations.JcrProperty;

/**
 *
 * @author Olafur Gauti Gudmundsson
 */
public class InvalidEntity {

    public InvalidEntity() {
    }

    @JcrPath
    private String path;

    @JcrName
    private String name;

    @JcrProperty
    private String body;

    @JcrProperty
    private float number;

    @JcrChildNode
    private List children;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
