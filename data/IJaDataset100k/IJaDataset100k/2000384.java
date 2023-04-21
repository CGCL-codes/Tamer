package org.imogene.sync.server.metadata;

import java.util.Date;

public class ImogMetadata {

    private String id;

    private Date date;

    private String terminalId;

    private String name;

    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String temrinalId) {
        this.terminalId = temrinalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
