package jtk.project4.fleet.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import nl.coderight.jazz.form.FormModel;
import jtk.project4.fleet.domain.Vendor;
import jtk.project4.fleet.ibatis.dao.VendorsDao;

public class VendorModel extends FormModel {

    private List<String> types;

    private List<String> terms;

    private List<Vendor> vendors;

    private String term;

    private String type;

    private Vendor vendor;

    public VendorModel() {
        types = new ArrayList<String>();
        types.add("Dealer");
        types.add("Fuel");
        types.add("Insurance");
        types.add("Loan / Lease");
        types.add("Maintenance");
        terms = new ArrayList<String>();
        terms.add("NET 30");
        terms.add("Pre Paid");
        terms.add("Wire X-Fer");
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
        notifyValueChanged("vendors", vendors);
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
        notifyValueChanged("vendor", vendor);
    }

    public Vendor createVendor() {
        Vendor vendor = new Vendor();
        setVendor(vendor);
        return vendor;
    }

    public void insertOrUpdateVendor(Vendor vendor) throws SQLException {
        if (vendor.getVendorID() == null) {
            new VendorsDao().insertVendor(vendor);
            new VendorsDao().insertVendorType(vendor);
            vendors.add(vendor);
            notifyValueInserted("vendors", vendor);
        } else {
            new VendorsDao().updateVendor(vendor);
            new VendorsDao().insertVendorType(vendor);
            notifyValueUpdated("vendors", vendor);
        }
    }

    public void deleteVendor(Vendor vendor) throws SQLException {
        vendors.remove(vendor);
        new VendorsDao().deleteVendor(vendor);
        notifyValueDeleted("vendors", vendor);
    }
}
