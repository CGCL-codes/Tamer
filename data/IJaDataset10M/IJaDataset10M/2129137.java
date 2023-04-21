package org.gaea.demo.store;

/**
 * 
 * @author hpicard
 */
public class Boulangerie extends Article {

    private String compagnie;

    @Override
    public String toString() {
        return "Boulangerie: " + super.type;
    }

    public String getCompagnie() {
        return this.compagnie;
    }

    public void setCompagnie(String comp) {
        this.compagnie = comp;
    }
}
