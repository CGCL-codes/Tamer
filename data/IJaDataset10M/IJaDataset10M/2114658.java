package org.blueoxygen.workshop.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import org.blueoxygen.cimande.DefaultPersistence;

@Entity
@Table(name = "workshop_item")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Item extends DefaultPersistence {

    private String code;

    private String name;

    private String description;

    private int price;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
