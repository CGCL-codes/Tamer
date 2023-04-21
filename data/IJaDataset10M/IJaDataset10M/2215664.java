package com.scs.service.ejb;

import java.io.Serializable;

public class ProductPK implements Serializable {

    public String productId;

    public ProductPK(String productId) {
        this.productId = productId;
    }

    public ProductPK() {
    }

    public boolean equals(Object o) {
        return true;
    }

    public int hashCode() {
        return 0;
    }
}
