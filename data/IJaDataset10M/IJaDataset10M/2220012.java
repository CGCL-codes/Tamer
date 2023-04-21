package org.agoracms.model;

import javax.persistence.Entity;

@Entity
public class SpecialRank extends Rank {

    private static final long serialVersionUID = 7546592114263327635L;

    protected String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
