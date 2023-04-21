package com.myres.service;

import java.util.List;
import java.util.Set;
import com.myres.dao.ShopDao;
import com.myres.dao.UserDao;
import com.myres.model.Product;
import com.myres.model.Shop;
import com.myres.model.User;

public class ShopService {

    private ShopDao shopDao;

    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public ShopDao getShopDao() {
        return shopDao;
    }

    public void setShopDao(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    public List<Shop> getShopList() {
        return shopDao.findAll();
    }

    public Set<Shop> getMyShop(int userId) {
        System.out.print("userId=" + userId);
        User u = userDao.get(userId);
        if (u == null) return null;
        return u.getShops();
    }

    public Set<Product> getMyProduct(int shopId) {
        Shop s = shopDao.get(shopId);
        if (s == null) return null;
        return s.getProducts();
    }

    public Shop get(int id) {
        return shopDao.get(id);
    }

    public int save(Shop shop) {
        return shopDao.save(shop);
    }

    public void delete(Shop shop) {
        shopDao.delete(shop);
    }

    public void delete(int id) {
        shopDao.delete(id);
    }

    /**
	 * 
	 * @param status
	 * @param startWith
	 *            keep null if you don't want to use in range function.
	 * @param size
	 *            same as this one.
	 * @return
	 */
    public List<Shop> findByStatusInRange(int status, Integer startWith, Integer size) {
        return shopDao.findByStatusInRange(status, startWith, size);
    }
}
