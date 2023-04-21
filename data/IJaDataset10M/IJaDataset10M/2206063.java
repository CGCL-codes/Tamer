package org.hibernate.search.test.query.facet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;

/**
 * @author Hardy Ferentschik
 */
@Entity
@Indexed
public class Fruit {

    @Id
    @GeneratedValue
    private int id;

    @Field
    private String name;

    @Field(index = Index.UN_TOKENIZED)
    @NumericField
    private Double price;

    private Fruit() {
    }

    public Fruit(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }
}
