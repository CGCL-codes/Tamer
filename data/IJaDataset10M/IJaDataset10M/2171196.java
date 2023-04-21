package com.anasoft.os.daofusion.sample.hellodao.client.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Base class for data transfer objects (DTOs).
 * <p>
 * DTOs represent persistent entity data sent from the server
 * that is applicable for the client to display and manage.
 */
public abstract class AbstractDto implements IsSerializable {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
