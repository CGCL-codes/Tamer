package com.aaronprj.school.mbooks.uivo;

import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import com.aaronprj.common.web.uivo.BaseEntity;

@XmlRootElement
public class UITestObj extends BaseEntity {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String discription;

    private double price;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
