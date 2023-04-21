package com.cti.product.test;

import java.util.*;
import junit.framework.*;
import org.jlf.dataMap.*;
import org.jlf.dataMap.jdbcMap.*;
import com.cti.product.*;

/**
 * This class tests the DBSchema object create method from the product
 * package.
 *
 * @version: $Revision: 1.1 $
 * @author:  Todd Lauinger
 *
 * @see com.cti.demo.Customer
 */
public class TestPopulateProducts extends TestCase {

    /**
     * Default constructor so the parameterized constructor can
     * be passed through to superclass.
     */
    public TestPopulateProducts(String name) {
        super(name);
    }

    /**
     * Test the create method.
     */
    public void testCreate() {
        JDBCDataMapper dataMapper = null;
        ProductType productTypeProduct = ProductType.findProduct();
        ProductType productTypeCourse = ProductType.findCourse();
        long productProductId, courseProductId;
        Product deleteMeProduct, productProduct, courseProduct;
        Product product = new Product("Object Oriented Analysis and Design Course", productTypeCourse, 2300);
        try {
            dataMapper = (JDBCDataMapper) product.getDefaultDataMapper();
            product.setDetailedDescriptionURL("http://www.catapult-technologies.com/ctiooad.htm");
            product.write(dataMapper);
            courseProductId = product.getId();
            product = new Product("Requirements via Use Case Course", productTypeCourse, 999.99);
            product.setDetailedDescriptionURL("http://www.catapult-technologies.com/ctiusecase.htm");
            product.write(dataMapper);
            product = new Product("Object Oriented Project Management Course", productTypeCourse, 999.99);
            product.setDetailedDescriptionURL("http://www.catapult-technologies.com/ctipm.htm");
            product.write(dataMapper);
            product = new Product("Java Development Foundation Frameworks", productTypeProduct, 499.99);
            product.setDetailedDescriptionURL("http://www.catapult-technologies.com/whitepapers/jlog");
            product.write(dataMapper);
            productProductId = product.getId();
            product = new Product("Delete Me Product", productTypeProduct, 10000.50);
            product.setDetailedDescriptionURL("http://www.catapult-technologies.com/deleteme.html");
            deleteMeProduct = product;
            product.write(dataMapper);
            dataMapper.commitWrites();
        } finally {
            if (dataMapper != null) dataMapper.close();
        }
        this.updateOne(deleteMeProduct);
        this.deleteOneProduct(deleteMeProduct);
        productProduct = Product.findById(productProductId);
        assertNotNull("Couldn't find in DB a Product with ID " + productProductId, productProduct);
        assertEquals("Too many product types!", 1, productProduct.getRelatedObjects(Product.PRODUCT_TYPE_RELATIONSHIP).size());
        assertEquals("Incorrect product type", ProductType.findProduct(), productProduct.getProductType());
        assertEquals("Corrupt product type", ProductType.PRODUCT_ID, productProduct.getProductType().getId());
        courseProduct = Product.findById(courseProductId);
        assertNotNull("Couldn't find in DB a Course Product with ID " + courseProductId, courseProduct);
        assertEquals("Too many product types!", 1, courseProduct.getRelatedObjects(Product.PRODUCT_TYPE_RELATIONSHIP).size());
        assertEquals("Incorrect product type", ProductType.findCourse(), courseProduct.getProductType());
        assertEquals("Corrupt product type", ProductType.COURSE_ID, courseProduct.getProductType().getId());
    }

    /**
     * Test select all.
     */
    public void testFindAll() {
        Vector allProducts = new Product().findAll();
        Enumeration e = allProducts.elements();
        Product prod;
        if (!e.hasMoreElements()) {
            this.fail("Can't find any products in DB!!!");
        }
        while (e.hasMoreElements()) {
            prod = (Product) e.nextElement();
            System.out.println(prod);
        }
    }

    /**
     * Test find one.
     */
    public void testFindOne() {
        Vector allProducts = new Product().findAll();
        Product product;
        if (allProducts.isEmpty()) {
            this.fail("Can't find any Products in DB!!!");
        }
        product = (Product) allProducts.lastElement();
        long id = product.getId();
        Product identicalproduct = Product.findById(id);
        assertEquals(product, identicalproduct);
    }

    /**
     * Test update one Product.
     */
    public void updateOne(Product product) {
        long id = product.getId();
        double newCost = product.getCost() + 10;
        product.setCost(newCost);
        JDBCDataMapper dataMapper = null;
        try {
            dataMapper = (JDBCDataMapper) product.getDefaultDataMapper();
            product.write(dataMapper);
            dataMapper.commitWrites();
        } finally {
            if (dataMapper != null) dataMapper.close();
        }
        Product identicalproduct = Product.findById(id);
        assertEquals(newCost, identicalproduct.getCost(), 0.009);
    }

    /**
     * Test delete one Product.
     */
    public void deleteOneProduct(Product product) {
        long id = product.getId();
        JDBCDataMapper dataMapper = null;
        try {
            dataMapper = (JDBCDataMapper) product.getDefaultDataMapper();
            product.deleteOnWrite();
            product.write(dataMapper);
            dataMapper.commitWrites();
        } finally {
            if (dataMapper != null) dataMapper.close();
        }
        Product deletedproduct = Product.findById(id);
        assertNull(deletedproduct);
    }
}
