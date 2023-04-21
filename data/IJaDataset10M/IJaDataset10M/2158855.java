package org.javanuke.core.model.dto.dbobjects;

import org.javanuke.core.model.dto.DataTransferObjectSupport;

public class Related extends DataTransferObjectSupport implements SimpleDBObjectInterface {

    private Integer id;

    private String name;

    private Integer tid;

    private String url;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer param) {
        this.id = param;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String param) {
        this.name = param;
    }

    public Integer getTid() {
        return this.tid;
    }

    public void setTid(Integer param) {
        this.tid = param;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String param) {
        this.url = param;
    }
}
