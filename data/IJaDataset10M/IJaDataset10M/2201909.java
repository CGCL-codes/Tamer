package com.sin.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FbFriend implements IsSerializable {

    private Long id;

    private Long version;

    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
