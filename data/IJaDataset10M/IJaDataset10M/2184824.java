package org.javanuke.core.model.dto.dbobjects;

import org.javanuke.core.model.dto.DataTransferObjectSupport;

public class Access extends DataTransferObjectSupport implements SimpleDBObjectInterface {

    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String param) {
        this.title = param;
    }
}
