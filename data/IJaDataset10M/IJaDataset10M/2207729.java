package bean;

public class PkgProdSupplier {

    int packageId;

    int productSupplierId;

    public PkgProdSupplier() {
    }

    public PkgProdSupplier(PkgProdSupplier pkgprsup) {
        this.packageId = pkgprsup.getPackageId();
        this.productSupplierId = pkgprsup.getProductSupplierId();
    }

    public PkgProdSupplier(int packageId, int productSupplierId) {
        this.packageId = packageId;
        this.productSupplierId = productSupplierId;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getProductSupplierId() {
        return productSupplierId;
    }

    public void setProductSupplierId(int productSupplierId) {
        this.productSupplierId = productSupplierId;
    }
}
