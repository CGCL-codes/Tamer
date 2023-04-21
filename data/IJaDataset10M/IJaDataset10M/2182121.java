package com.ail.core.product.listproducts;

import com.ail.core.command.CommandArg;
import com.ail.core.product.ProductDetails;
import java.util.Collection;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2007/10/05 22:47:50 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/product/listproducts/ListProductsArg.java,v $
 * @stereotype arg
 */
public interface ListProductsArg extends CommandArg {

    /**
     * Getter for the productsRet propersty. A collection of ProductDetails representing all the
     * products know to the system is returned.
     * @return Value of productsRet, or null if it is unset
     */
    Collection<ProductDetails> getProductsRet();

    /**
     * Setter for the productsRet property.
     * @see #getProductsRet
     * @param productsRet new value for property.
     */
    void setProductsRet(Collection<ProductDetails> productsRet);
}
