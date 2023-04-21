package com.googlecode.cbrates.transport;

import java.io.Serializable;
import org.w3c.dom.Element;

/**
 *
 * @author Alex Askerov
 */
public abstract class Valute implements Serializable {

    private String id;

    protected String name;

    private int nominal;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNominal() {
        return nominal;
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    abstract void fromXmlValuteElement(Element valuteElement);
}
