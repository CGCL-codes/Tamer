package bean;

public class Package {

    int packageId;

    String pkgName;

    String pkgStartDate;

    String pkgEndDate;

    String pkgDesc;

    String pkgBasePrice;

    String pkgAgencyCommission;

    public Package() {
    }

    public Package(Package pkg) {
        this.packageId = pkg.getPackageId();
        this.pkgName = pkg.getPkgName();
        this.pkgStartDate = pkg.getPkgStartDate();
        this.pkgEndDate = pkg.getPkgEndDate();
        this.pkgDesc = pkg.getPkgDesc();
        this.pkgBasePrice = pkg.getPkgBasePrice();
        this.pkgAgencyCommission = pkg.getPkgAgencyCommission();
    }

    public Package(int packageId, String pkgName, String pkgStartDate, String pkgEndDate, String pkgDesc, String pkgBasePrice, String pkgAgencyCommission) {
        this.packageId = packageId;
        this.pkgName = pkgName;
        this.pkgStartDate = pkgStartDate;
        this.pkgEndDate = pkgEndDate;
        this.pkgDesc = pkgDesc;
        this.pkgBasePrice = pkgBasePrice;
        this.pkgAgencyCommission = pkgAgencyCommission;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPkgStartDate() {
        return pkgStartDate;
    }

    public void setPkgStartDate(String pkgStartDate) {
        this.pkgStartDate = pkgStartDate;
    }

    public String getPkgEndDate() {
        return pkgEndDate;
    }

    public void setPkgEndDate(String pkgEndDate) {
        this.pkgEndDate = pkgEndDate;
    }

    public String getPkgDesc() {
        return pkgDesc;
    }

    public void setPkgDesc(String pkgDesc) {
        this.pkgDesc = pkgDesc;
    }

    public String getPkgBasePrice() {
        return pkgBasePrice;
    }

    public void setPkgBasePrice(String pkgBasePrice) {
        this.pkgBasePrice = pkgBasePrice;
    }

    public String getPkgAgencyCommission() {
        return pkgAgencyCommission;
    }

    public void setPkgAgencyCommission(String pkgAgencyCommission) {
        this.pkgAgencyCommission = pkgAgencyCommission;
    }
}
