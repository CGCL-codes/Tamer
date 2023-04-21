package org.pprun.hjpetstore.dao;

import java.util.List;
import org.pprun.hjpetstore.domain.Product;
import org.pprun.hjpetstore.persistence.jaxb.ProductSummary;
import org.springframework.dao.DataAccessException;

/**
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
public interface ProductDao {

    List<Product> getProductListByCategory(String categoryName, int page, int max) throws DataAccessException;

    List<Product> searchProductList(String keywords, int page, int max) throws DataAccessException;

    Product getProduct(String productNumber) throws DataAccessException;

    List<ProductSummary> searchProductLightweightList(String keywords, int page, int max) throws DataAccessException;
}
