package org.javanuke.core.model.dto.dbobjects;

import org.javanuke.core.model.dto.DataTransferObjectSupport;

public class EncyclopediaText extends DataTransferObjectSupport implements SimpleDBObjectInterface {

    private Integer counter;

    private Integer encyclopediaId;

    private Integer id;

    private byte[] text;

    private String title;

    public Integer getCounter() {
        return this.counter;
    }

    public void setCounter(Integer param) {
        this.counter = param;
    }

    public Integer getEncyclopediaId() {
        return this.encyclopediaId;
    }

    public void setEncyclopediaId(Integer param) {
        this.encyclopediaId = param;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer param) {
        this.id = param;
    }

    public byte[] getText() {
        return this.text;
    }

    public void setText(byte[] param) {
        this.text = param;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String param) {
        this.title = param;
    }
}
