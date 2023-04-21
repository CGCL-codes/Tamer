package org.vh.warehouse.management.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.vh.database.InfoProvider;
import org.vh.warehouse.management.CatalogEntry;
import org.vh.warehouse.management.Category;
import org.vh.warehouse.management.Shop;

/**
*
* @jmx.mbean
*             persistPolicy = "OnUpdate"
*             persistence-manager = "org.jboss.mx.persistence.DelegatingPersistenceManager"
*             persistName = "WarehouseMgt"
*             description = "Managing for wares, categories, shops, ..."
*             name = "vh.warehouse.admin:service=WarehouseMgt"
* 
* @jboss.service servicefile="jboss"
* @jboss.depends object-name="jboss.mq:service=InvocationLayer,type=JVM"
* @jboss.xmbean
* 
*/
public class WarehouseMgt extends ServiceMBeanSupport {

    Logger log = Logger.getLogger(WarehouseMgt.class.getName());

    public WarehouseMgt() {
        super();
    }

    protected void startService() throws Exception {
        super.startService();
        log.info("Starting service WarehouseMgt ...");
    }

    protected void stopService() throws Exception {
        super.stopService();
        log.info("Stopping service WarehouseMgt ...");
    }

    /**
     * @jmx.managed-operation
     *   description = "used to add a shop"
     * @jmx.managed-parameter
     *   name= "name"
     *   description = "name of the shop to add"
     *   type="java.lang.String"
     */
    public void addShop(String name) {
        Shop shop = new Shop();
        shop.setName(name);
        try {
            InfoProvider ip = new InfoProvider();
            ip.insertShop(shop);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not add shop: " + name);
        }
    }

    /**
     * @jmx.managed-operation
     *   description = "used to add a shop"
     * @jmx.managed-parameter
     *   name= "name"
     *   description = "name of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "street"
     *   description = "street of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "city"
     *   description = "city of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "zipCode"
     *   description = "zipCode of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "country"
     *   description = "country of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "phoneNr"
     *   description = "phoneNr of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "faxNr"
     *   description = "faxNr of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "mailAdress"
     *   description = "mailAdress of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "homepage"
     *   description = "homepage of the shop to add"
     *   type="java.lang.String"
     */
    public void addShop(String name, String street, String city, String zipCode, String country, String phoneNr, String faxNr, String mailAdress, String homepage) {
        Shop shop = new Shop();
        shop.setName(name);
        shop.getContact().getAddress().setCity(city);
        shop.getContact().getAddress().setCountry(country);
        shop.getContact().getAddress().setStreet(street);
        shop.getContact().getAddress().setZipCode(zipCode);
        shop.getContact().setEmailAdress(mailAdress);
        shop.getContact().setFaxNumber(faxNr);
        shop.getContact().setPhoneNumber(phoneNr);
        shop.getContact().setHomepage(homepage);
        try {
            InfoProvider ip = new InfoProvider();
            ip.insertShop(shop);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not add shop: " + name);
        }
    }

    /**
     * @jmx.managed-operation
     *   description = "used to remove a shop"
     * @jmx.managed-parameter
     *   name= "shopId"
     *   description = "shopId of the shop to remove"
     *   type="java.lang.String"
     */
    public void removeShop(String shopId) {
        if (shopId == null || shopId.length() < 1) return;
        Shop shop = new Shop();
        shop.setShopId(shopId);
        try {
            InfoProvider ip = new InfoProvider();
            ip.deleteShop(shop);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not delete shop with id: " + shopId);
        }
    }

    /**
     * @jmx.managed-operation
     *   description = "used to update a shop"
     * @jmx.managed-parameter
     *   name= "shopId"
     *   description = "shopId of the shop to update"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "name"
     *   description = "name of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "street"
     *   description = "street of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "city"
     *   description = "city of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "zipCode"
     *   description = "zipCode of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "country"
     *   description = "country of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "phoneNr"
     *   description = "phoneNr of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "faxNr"
     *   description = "faxNr of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "mailAdress"
     *   description = "mailAdress of the shop to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "homepage"
     *   description = "homepage of the shop to add"
     *   type="java.lang.String"
     */
    public void updateShop(String shopId, String name, String street, String city, String zipCode, String country, String phoneNr, String faxNr, String mailAdress, String homepage) {
        Shop shop = new Shop();
        shop.setShopId(shopId);
        shop.setName(name);
        shop.getContact().getAddress().setCity(city);
        shop.getContact().getAddress().setCountry(country);
        shop.getContact().getAddress().setStreet(street);
        shop.getContact().getAddress().setZipCode(zipCode);
        shop.getContact().setEmailAdress(mailAdress);
        shop.getContact().setFaxNumber(faxNr);
        shop.getContact().setPhoneNumber(phoneNr);
        shop.getContact().setHomepage(homepage);
        try {
            InfoProvider ip = new InfoProvider();
            ip.updateShop(shop);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not update shop: " + name);
            log.error(sqle.getMessage());
        }
    }

    /**
     * @jmx.managed-operation
     *   description = "used to add a category"
     * @jmx.managed-parameter
     *   name= "name"
     *   description = "name of the category to add"
     *   type="java.lang.String"
     */
    public void addCategory(String name) {
        Category cat = new Category();
        cat.setName(name);
        try {
            InfoProvider ip = new InfoProvider();
            ip.insertCategory(cat);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not add category: " + name);
        }
    }

    /**
     * @jmx.managed-operation
     *   description = "used to remove a shop"
     * @jmx.managed-parameter
     *   name= "categoryId"
     *   description = "categoryId of the category to remove"
     *   type="java.lang.String"
     */
    public void removeCategory(String categoryId) {
        if (categoryId == null || categoryId.length() < 1) return;
        Category cat = new Category();
        cat.setCategoryId(categoryId);
        try {
            InfoProvider ip = new InfoProvider();
            ip.deleteCategory(cat);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not delete category with id: " + categoryId);
        }
    }

    /**
     * @jmx.managed-operation
     *   description = "used to update a category"
     * @jmx.managed-parameter
     *   name= "categoryId"
     *   description = "categoryId of the category to update"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "name"
     *   description = "name of the category to update"
     *   type="java.lang.String"
     */
    public void updateCategory(String categoryId, String name) {
        Category cat = new Category();
        cat.setCategoryId(categoryId);
        cat.setName(name);
        try {
            InfoProvider ip = new InfoProvider();
            ip.updateCategory(cat);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not update category: " + name);
        }
    }

    /**
     * @jmx.managed-operation
     *   description = "used to add a CatalogEntry"
     * @jmx.managed-parameter
     *   name= "name"
     *   description = "name of the CatalogEntry to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "manufacturer"
     *   description = "manufacturer of the CatalogEntry to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "ean"
     *   description = "ean of the CatalogEntry to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "price"
     *   description = "price of the CatalogEntry to add"
     *   type="double"
     * @jmx.managed-parameter
     *   name= "amount"
     *   description = "amount of the CatalogEntry to add"
     *   type="double"
     * @jmx.managed-parameter
     *   name= "measure"
     *   description = "measure of the CatalogEntry to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "shopId"
     *   description = "shopId of the CatalogEntry to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "categoryId"
     *   description = "categoryId of the CatalogEntry to add"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "imagePath"
     *   description = "imagePath for the image to add to CatalogEntry"
     *   type="java.lang.String"
     */
    public void addCatalogEntry(String name, String manufacturer, String ean, double price, double amount, String measure, String shopId, String categoryId, String imagePath) {
        CatalogEntry catEnt = new CatalogEntry();
        Shop shop = new Shop();
        Category cat = new Category();
        shop.setShopId(shopId);
        cat.setCategoryId(categoryId);
        catEnt.setName(name);
        catEnt.setManufacturer(manufacturer);
        catEnt.setEan(ean);
        catEnt.setPrice(price);
        catEnt.setAmount(amount);
        catEnt.setMeasure(measure);
        catEnt.setImage(null);
        catEnt.setShop(shop);
        catEnt.setCategory(cat);
        try {
            InputStream is = new FileInputStream(imagePath);
            catEnt.setImage(ImageIO.read(is));
        } catch (IOException ioe) {
            log.error("Could not add image data to catalog entry! Maybe the file is missing!");
        } catch (Exception e) {
            log.error("Could not add image data to catalog entry!");
        }
        try {
            InfoProvider ip = new InfoProvider();
            ip.insertCatalogEntry(catEnt);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not add catalog entry: " + name);
            log.error(sqle.getMessage());
        }
    }

    /**
     * @jmx.managed-operation
     *   description = "used to remove a CatalogEntry"
     * @jmx.managed-parameter
     *   name= "uid"
     *   description = "uid of the category to remove"
     *   type="java.lang.String"
     */
    public void removeCatalogEntry(String uid) {
        if (uid == null || uid.length() < 1) return;
        CatalogEntry catEnt = new CatalogEntry();
        catEnt.setUid(uid);
        try {
            InfoProvider ip = new InfoProvider();
            ip.deleteCatalogEntry(catEnt);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not delete catalog entry with uid: " + uid);
            log.error(sqle.getMessage());
        }
    }

    /**
     * @jmx.managed-operation
     *   description = "used to update a CatalogEntry"
     * @jmx.managed-parameter
     *   name= "uid"
     *   description = "uid of the CatalogEntry to update"
     *   type="java.lang.String"
     * @jmx.managed-parameter
     *   name= "name"
     *   description = "name of the CatalogEntry to update"
     *   type="java.lang.String"
     */
    public void updateCatalogEntry(String uid, String name, String manufacturer, String ean, double price, double amount, String measure, String shopId, String categoryId) {
        CatalogEntry catEnt = new CatalogEntry();
        Shop shop = new Shop();
        Category cat = new Category();
        shop.setShopId(shopId);
        cat.setCategoryId(categoryId);
        catEnt.setUid(uid);
        catEnt.setName(name);
        catEnt.setManufacturer(manufacturer);
        catEnt.setEan(ean);
        catEnt.setPrice(price);
        catEnt.setAmount(amount);
        catEnt.setMeasure(measure);
        catEnt.setImage(null);
        catEnt.setShop(shop);
        catEnt.setCategory(cat);
        try {
            InfoProvider ip = new InfoProvider();
            ip.updateCatalogEntry(catEnt);
            ip.close();
        } catch (SQLException sqle) {
            log.error("Could not update catalog entry: " + name);
            log.error(sqle.getMessage());
        } catch (IOException ioe) {
            log.error("Could not update catalog entry: " + name);
            log.error(ioe);
        }
    }
}
