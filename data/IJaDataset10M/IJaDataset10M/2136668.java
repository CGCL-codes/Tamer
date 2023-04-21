package com.freshbooks.model;

import java.io.Serializable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("card")
public class Card implements Serializable {

    private static final long serialVersionUID = -6382862130184354619L;

    private String number;

    private String name;

    private Expiration expiration;

    public Card() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Expiration getExpiration() {
        return expiration;
    }

    public void setExpiration(Expiration expiration) {
        this.expiration = expiration;
    }
}
