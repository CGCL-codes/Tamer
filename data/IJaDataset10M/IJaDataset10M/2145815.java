package org.doframework.sample.persistence;

import org.doframework.sample.component.*;
import java.util.*;

public class MockManufacturerPersistence implements ManufacturerPersistence {

    Map<Integer, Manufacturer> manufacturerIdToManufacturer = new HashMap<Integer, Manufacturer>();

    static int nextId = 0;

    public Manufacturer getById(int id) {
        return manufacturerIdToManufacturer.get(id);
    }

    public int countProductsWithManufacturerId(int manufacturerId) {
        return 0;
    }

    /**
     * Delete the manufacturer
     *
     * @param manufacturer to delete
     *
     * @return true if the manufacturer was deleted
     */
    public boolean delete(Manufacturer manufacturer) {
        return (manufacturerIdToManufacturer.remove(manufacturer.getId()) != null);
    }

    /**
     * for a key other than the PK, iterate to find the value
     * @param pk
     * @return
     */
    public Manufacturer getByName(String pk) {
        for (Manufacturer m : manufacturerIdToManufacturer.values()) {
            if (m.getName().equals(pk)) {
                return m;
            }
        }
        return null;
    }

    public int getNextId() {
        return nextId++;
    }

    public void insert(Manufacturer manufacturer) {
        manufacturerIdToManufacturer.put(manufacturer.getId(), manufacturer);
    }

    public void update(Manufacturer manufacturer) {
        manufacturerIdToManufacturer.put(manufacturer.getId(), manufacturer);
    }
}
