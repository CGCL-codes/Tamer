package org.rubato.math.module;

/**
 * The interface for elements in a free module over a product ring.
 * @see org.rubato.math.module.ProductFreeModule
 * 
 * @author Gérard Milmeister
 */
public interface ProductFreeElement extends FreeElement {

    /**
     * Returns the number of factors of the underlying product ring.
     */
    public int getFactorCount();

    /**
     * Returns the underlying product ring.
     */
    public ProductRing getRing();
}
