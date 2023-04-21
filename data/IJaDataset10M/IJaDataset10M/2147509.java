package br.com.radiceti.falke.samples.contacts;

import java.io.Serializable;
import java.util.UUID;

public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String name;

    private String phone;

    public Contact() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return name + " - " + phone;
    }
}
