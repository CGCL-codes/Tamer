package bodi.stamp.registration;

public class ProductType {

    private int productTypeID;

    private String productTypeName;

    private int productParentID;

    public ProductType(int productTypeID, String productTypeName) {
        super();
        this.productTypeID = productTypeID;
        this.productTypeName = productTypeName;
    }

    public int getProductParentID() {
        return productParentID;
    }

    public void setProductParentID(int productParentID) {
        this.productParentID = productParentID;
    }

    public int getProductTypeID() {
        return productTypeID;
    }

    public void setProductTypeID(int productTypeID) {
        this.productTypeID = productTypeID;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
}
