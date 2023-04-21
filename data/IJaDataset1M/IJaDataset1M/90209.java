package com.domain;

import java.io.Serializable;

public class Popedom implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Long popedom_id;

    private Roles roles;

    public Long getPopedom_id() {
        return popedom_id;
    }

    public void setPopedom_id(Long popedom_id) {
        this.popedom_id = popedom_id;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }
}
