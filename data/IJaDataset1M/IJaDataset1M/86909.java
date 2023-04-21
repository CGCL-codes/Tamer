package com.mercury.training.java.mims.dao;

import java.util.Collection;
import com.mercury.training.java.mims.model.Vendor;

public interface ManageVendorDAO {

    public boolean save(Vendor vendor);

    public boolean delete(String vendorId);

    public Collection getAll();

    public Vendor find(String vendorId);
}
