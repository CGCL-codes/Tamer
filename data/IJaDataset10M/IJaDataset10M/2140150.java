package org.susan.java.design.prototype;

public class N2Product implements N2ProductPrototype {

    private String productId;

    private String name;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "产品编号=" + this.productId + ",产品名称=" + this.name;
    }

    public N2ProductPrototype cloneProduct() {
        N2Product product = new N2Product();
        product.setProductId(this.productId);
        product.setName(this.name);
        return product;
    }
}
