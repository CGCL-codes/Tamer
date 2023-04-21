package com.cci.bmc.dao;

import java.util.List;
import com.cci.bmc.domain.Vendor;

public interface VendorDao {

    public Vendor get(Long id);

    public Vendor get(String name);

    public Vendor getByMacPrefix(String macAddressPrefix);

    public List<Vendor> list();

    public void remove(Vendor vendor);

    public void save(Vendor vendor);
}
