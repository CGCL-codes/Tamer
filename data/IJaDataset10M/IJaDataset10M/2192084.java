package org.castafiore.shoppingmall.checkout;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import org.castafiore.catalogue.Product;

@Entity
public class OrderEntry extends BookEntry<Product> {

    private BigDecimal quantity = BigDecimal.ZERO;

    private String productCode;

    @Override
    public List<Product> getDetails() {
        throw new UnsupportedOperationException("this is not supported for performance issues");
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
