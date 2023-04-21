package ca.concordia.comp5541.search;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * @author team 3
 * helper class to do a search inside a collection
 * search comes in to flavours: a basic which take as parameter a string
 * And an advanced search
 *
 */
public class CollectionsSearch extends GenericSearch {

    private Collection<DataBean> target;

    private void setTarget(Collection<DataBean> target) {
        this.target = target;
    }

    /**
	 * constructor
	 * prepare the target
	 */
    public CollectionsSearch(Collection<DataBean> target) {
        setTarget(target);
    }

    /**
	 * basic search
	 * @param A string
	 * @return a collection of results "rows in the database that match the search criteria"
	 */
    @Override
    public Collection<DataBean> basicSearch(String param) {
        Iterator<DataBean> itr = target.iterator();
        while (itr.hasNext()) {
            DataBean test = itr.next();
            if ((test.getAssetID().toLowerCase().matches(param)) || (test.getBarCode().toLowerCase().matches(param)) || (test.getCategory().toLowerCase().matches(param)) || (test.getLegacyCode().toLowerCase().matches(param)) || (test.getManufacturer().toLowerCase().matches(param)) || (test.getModel().toLowerCase().matches(param)) || (test.getOwner().toLowerCase().matches(param))) {
                resultSet.add(test);
            }
        }
        return resultSet;
    }

    /**
	 * This method perform an advanced search: a more refined search
	 * @param a string
	 * @return a collection of results
	 */
    @Override
    public Collection<DataBean> advancedSearch(String param) {
        return resultSet;
    }
}

class DataBean implements Serializable {

    private String AssetID;

    private String BarCode;

    private int REQ;

    private int PON;

    private String LegacyCode;

    private String Owner;

    private Date DatePurchased;

    private Date WarrantyExpiration;

    private int LocationID;

    private String Manufacturer;

    private String Status;

    private String Category;

    private String Model;

    public String getAssetID() {
        return AssetID;
    }

    public void setAssetID(final String assetID) {
        AssetID = assetID;
    }

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(final String barCode) {
        BarCode = barCode;
    }

    public int getREQ() {
        return REQ;
    }

    public void setREQ(final int req) {
        REQ = req;
    }

    public int getPON() {
        return PON;
    }

    public void setPON(final int pon) {
        PON = pon;
    }

    public String getLegacyCode() {
        return LegacyCode;
    }

    public void setLegacyCode(final String legacyCode) {
        LegacyCode = legacyCode;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(final String owner) {
        Owner = owner;
    }

    public Date getDatePurchased() {
        return DatePurchased;
    }

    public void setDatePurchased(final Date datePurchased) {
        DatePurchased = datePurchased;
    }

    public Date getWarrantyExpiration() {
        return WarrantyExpiration;
    }

    public void setWarrantyExpiration(final Date warrantyExpiration) {
        WarrantyExpiration = warrantyExpiration;
    }

    public int getLocationID() {
        return LocationID;
    }

    public void setLocationID(final int locationID) {
        LocationID = locationID;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(final String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(final String status) {
        Status = status;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(final String category) {
        Category = category;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(final String model) {
        Model = model;
    }
}
