package org.doframework.sample.persistence;

import org.doframework.sample.component.*;

public interface ProductPersistence extends BasePersistence {

    Product getById(int productId);

    void insert(Product product);

    void update(Product product);

    boolean delete(Product product);

    Product getByManufacturerNameProductName(String manufacturerName, String productName);

    int countInvoicesWithProductId(int productId);
}
