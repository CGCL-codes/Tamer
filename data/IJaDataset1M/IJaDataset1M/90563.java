package com.sush.sample.webstore.store.domain.test.nonJUnit;

import java.util.Date;
import com.sush.webstore.store.domain.ICatalog;
import com.sush.webstore.store.domain.ICategory;
import com.sush.webstore.store.domain.IImage;
import com.sush.webstore.store.domain.IImages;
import com.sush.webstore.store.domain.IItem;
import com.sush.webstore.store.domain.IProduct;
import com.sush.webstore.store.domain.IWebStoreFacade;
import com.sush.webstore.store.domain.beans.Catalog;
import com.sush.webstore.store.domain.beans.Image;
import com.sush.webstore.store.domain.beans.UserAccount;
import com.sush.webstore.store.domain.facade.WebStorePOJO;

public class TestCatalog {

    public static void main(String[] args) {
        TestCatalog.createAndStoreCatalog();
    }

    public static void createAndStoreCatalog() {
        Catalog cat1 = TestCatalog.createCatalog("name1", "description1");
        ICatalog cat2 = TestCatalog.createCatalog("name2", "description2");
        ICategory catgry1 = TestCategory.createCategory("name1", "description1");
        ICategory catgry2 = TestCategory.createCategory("name2", "description2");
        ICategory catgry3 = TestCategory.createCategory("name3", "description3");
        ICategory catgry4 = TestCategory.createCategory("name4", "description4");
        IImage img1 = new Image();
        img1.setName("name1");
        img1.setPath("path1");
        IImage img2 = new Image();
        img2.setName("name2");
        img2.setPath("path2");
        IImage img3 = new Image();
        img3.setName("name3");
        img3.setPath("path3");
        IImage img4 = new Image();
        img4.setName("name4");
        img4.setPath("path4");
        IProduct p1 = TestProduct.createProduct("name1", "desc1");
        IProduct p2 = TestProduct.createProduct("name2", "desc2");
        IProduct p3 = TestProduct.createProduct("name3", "desc3");
        IProduct p4 = TestProduct.createProduct("name4", "desc4");
        IProduct p5 = TestProduct.createProduct("name5", "desc5");
        IItem i1 = TestItem.createItem("name1", "desc1", 1.00f, 1);
        IItem i2 = TestItem.createItem("name2", "desc2", 2.00f, 2);
        IItem i3 = TestItem.createItem("name3", "desc3", 3.00f, 3);
        IItem i4 = TestItem.createItem("name4", "desc4", 4.00f, 4);
        IItem i5 = TestItem.createItem("name5", "desc5", 5.00f, 5);
        IItem i6 = TestItem.createItem("name6", "desc6", 6.00f, 6);
        IImages images1 = cat1.getIImages();
        images1.addImage(img1);
        IImages images2 = cat2.getIImages();
        images2.addImage(img2);
        IWebStoreFacade store = new WebStorePOJO();
        store.addCatalog((Catalog) cat1);
        store.addCatalog(cat2);
        store.addCategory(cat1, catgry1);
        store.addCategory(cat2, catgry3);
        store.addSubCategory(catgry1, catgry2);
        store.addSubCategory(catgry1, catgry4);
        store.addProduct(catgry3, p5);
        store.addProduct(catgry2, p1);
        store.addProduct(catgry2, p2);
        store.addProduct(catgry4, p3);
        store.addProduct(catgry4, p4);
        store.addItem(p1, i3);
        store.addItem(p2, i4);
        store.addItem(p3, i6);
        store.addItem(p4, i5);
        store.addItem(p5, i1);
        store.addItem(p5, i2);
        UserAccount account = TestUser.createUser("fName", "lName", new Date(), "email", "phNo", "userName", "password");
        store.addUserAccount(account);
    }

    public static Catalog createCatalog(String name, String description) {
        Catalog cat = new Catalog();
        cat.setName(name);
        cat.setDescription(description);
        return cat;
    }

    public static void printCatalog(Catalog cat) {
        System.out.println("Catalog..." + cat.getId());
        System.out.println(cat.getDescription());
        System.out.println(cat.getName());
    }
}
