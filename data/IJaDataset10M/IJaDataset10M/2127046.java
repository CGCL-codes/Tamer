package org.eiichiro.jazzmaster.examples.petstore.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Product implements java.io.Serializable {

    private String productID;

    private String categoryID;

    private String name;

    private String description;

    private String imageURL;

    public Product(String productID, String categoryID, String name, String description, String imageURL) {
        this.productID = productID;
        this.categoryID = categoryID;
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }

    public Product() {
    }

    @Id
    public String getProductID() {
        return this.productID;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
