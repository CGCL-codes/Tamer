package org.meruvian.yama.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author vick
 */
@Entity
@Table
@XmlRootElement(name = "item")
public class Item implements Serializable {

    @Id
    @GeneratedValue(generator = "i")
    @GenericGenerator(name = "i", strategy = "uuid")
    @Column
    private String id;

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private Long price;

    @Column
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
