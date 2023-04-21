package org.posper.hibernate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Aaron Luchko <aaron.luchko@oxn.ca>
 *
 */
@Entity
public class ProductLocation extends AbstractIdentifiedHibernateObject<ProductLocation> {

    private static final long serialVersionUID = 1067672240467571831L;

    private Product product;

    private Location location;

    private Double stockSecurity;

    private Double stockMaximum;

    @ManyToOne
    @JoinColumn(nullable = false)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @ManyToOne
    @JoinColumn(nullable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getStockMaximum() {
        return stockMaximum;
    }

    public void setStockMaximum(Double stockMaximum) {
        this.stockMaximum = stockMaximum;
    }

    public Double getStockSecurity() {
        return stockSecurity;
    }

    public void setStockSecurity(Double stockSecurity) {
        this.stockSecurity = stockSecurity;
    }
}
