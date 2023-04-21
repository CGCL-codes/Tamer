package org.slasoi.businessManager.common.ws.types;

public class Product {

    private long id;

    private String name;

    private String description;

    private String brand;

    private String[] category;

    private ProductOffer[] productOffers;

    private RatingType rating;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String[] getCategory() {
        return category;
    }

    public void setCategory(String[] category) {
        this.category = category;
    }

    public ProductOffer[] getProductOffers() {
        return productOffers;
    }

    public void setProductOffers(ProductOffer[] productOffers) {
        this.productOffers = productOffers;
    }

    public RatingType getRating() {
        return rating;
    }

    public void setRating(RatingType rating) {
        this.rating = rating;
    }
}
