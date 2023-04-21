package com.rlsoftwares.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Rodrigo
 */
public class Message implements Serializable {

    private Integer owner;

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }
}
