package es.devel.opentrats.dao;

import es.devel.opentrats.dao.exception.ProductDaoException;
import es.devel.opentrats.model.DTO.ProductStockDTO;
import es.devel.opentrats.model.Product;
import java.util.Collection;

/**
 *
 * @author Fran Serrano
 */
public interface IProductDao {

    Collection<String> getAllBrands() throws ProductDaoException;

    Collection<String> getAllProductBrands(String filterArg) throws ProductDaoException;

    Collection<String> getAllProductCategories(String filterArg) throws ProductDaoException;

    Collection<String> getAllCategories() throws ProductDaoException;

    Product load(Integer idProductArg) throws ProductDaoException;

    void save(Product productArg) throws ProductDaoException;

    void delete(Product productArg) throws ProductDaoException;

    Collection<Product> findByNameOrBrand(String nameArg, String brandArg) throws ProductDaoException;

    Collection<Product> findByNameAndBrand(String nameArg, String brandArg) throws ProductDaoException;

    Collection<ProductStockDTO> findDTOByNameAndBrand(String nameArg, String brandArg, String delegationArg) throws ProductDaoException;

    Collection<Product> findByBrand(String brandArg) throws ProductDaoException;
}
