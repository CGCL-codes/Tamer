package com.completex.objective.persistency.examples.ex003a.domain;

import com.completex.objective.persistency.examples.ex003a.pos.ProductPO;

/**
 * @author Gennady Krizhevsky
 */
public class Product extends ProductPO {

    public Product() {
    }

    public Product(Long productId) {
        super(productId);
    }
}
