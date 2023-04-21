package org.gaea.demo.store;

import java.util.Date;

/**
 * 
 * @author hpicard
 */
public class Epicerie extends Article {

    private Date livraison;

    private Date expiration;

    @Override
    public String toString() {
        return "Epicerie: " + super.type;
    }

    public void setExpiration(Date date) {
        this.expiration = date;
    }

    public Date getExpiration() {
        return this.expiration;
    }

    public void setLivraison(Date date) {
        this.livraison = date;
    }

    public Date getLivraison() {
        return this.livraison;
    }
}
