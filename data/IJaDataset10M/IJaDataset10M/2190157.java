package com.okko.db.model;

public class ProductCategory extends BaseObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7683908047242593269L;

    private Long productCategoryId;

    private Product product;

    private Category category;

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
