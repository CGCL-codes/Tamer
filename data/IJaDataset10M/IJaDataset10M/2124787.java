package com.ateam.webstore.service.impl;

import java.io.Serializable;
import java.util.Collection;
import com.ateam.webstore.dao.ProductsInWishListDAO;
import com.ateam.webstore.model.ProductsInWishList;
import com.ateam.webstore.service.RepositoryService;

/**
 * @author Hendrix Tavarez
 *
 */
public class ProductsInWishListService implements RepositoryService<ProductsInWishList> {

    @Override
    public ProductsInWishList store(ProductsInWishList productsInWishList) {
        ProductsInWishListDAO repository = new ProductsInWishListDAO();
        return repository.save(productsInWishList);
    }

    @Override
    public void remove(ProductsInWishList productsInWishList) {
        ProductsInWishListDAO repository = new ProductsInWishListDAO();
        repository.delete(productsInWishList);
    }

    @Override
    public Collection<ProductsInWishList> getAll() {
        ProductsInWishListDAO repository = new ProductsInWishListDAO();
        return repository.getAll();
    }

    @Override
    public ProductsInWishList getById(Serializable id) {
        ProductsInWishListDAO repository = new ProductsInWishListDAO();
        return repository.get(id);
    }
}
