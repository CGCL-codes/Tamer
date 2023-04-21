package com.ateam.webstore.service.impl;

import java.io.Serializable;
import java.util.Collection;
import com.ateam.webstore.dao.VendorDAO;
import com.ateam.webstore.model.Vendor;
import com.ateam.webstore.service.RepositoryService;

/**
 * @author Hendrix Tavarez
 *
 */
public class VendorService implements RepositoryService<Vendor> {

    @Override
    public Vendor store(Vendor vendor) {
        VendorDAO repository = new VendorDAO();
        return repository.save(vendor);
    }

    @Override
    public void remove(Vendor vendor) {
        VendorDAO repository = new VendorDAO();
        repository.delete(vendor);
    }

    @Override
    public Collection<Vendor> getAll() {
        VendorDAO repository = new VendorDAO();
        return repository.getAll();
    }

    @Override
    public Vendor getById(Serializable id) {
        VendorDAO repository = new VendorDAO();
        return repository.get(id);
    }
}
