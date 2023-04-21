package org.slasoi.businessManager.productSLA.productDiscovery;

import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slasoi.businessManager.common.model.EmServiceSpecification;
import org.slasoi.businessManager.common.model.EmSpProducts;
import org.slasoi.businessManager.common.model.EmSpProductsOffer;
import org.slasoi.businessManager.common.model.EmSpServices;
import org.slasoi.businessManager.common.service.ProductManager;
import org.slasoi.businessManager.productSLA.productDiscovery.impl.ProductDiscoveryImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit test for simple App.
 */
public class ProductsCatalogTest extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ProductsCatalogTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ProductsCatalogTest.class);
    }

    private static String[] files = new String[] { "classpath:META-INF/spring/bm-productdiscovery-context.xml" };

    final ApplicationContext context = new ClassPathXmlApplicationContext(files);

    final ProductManager productService = (ProductManager) context.getBean("productService");

    /**
     * Test get parameter list countries.
     */
    public void testGetProducts() {
        try {
            System.out.println("ProductDiscovery, Test method getProducts");
            ProductDiscoveryImpl discoveryImpl = new ProductDiscoveryImpl();
            String[] filters = { "Mobile", "Audio", "Multimedia" };
            List<EmSpProducts> lProducts = discoveryImpl.getProducts("1", filters);
            for (EmSpProducts product : lProducts) {
                System.out.println("Product: " + product.getTxProductname() + " - Product Offers: " + product.getEmSpProductsOffers().size());
                for (EmSpProductsOffer offer : product.getEmSpProductsOffers()) {
                    System.out.println("  - Offer: " + offer.getTxName() + " Num Services: " + offer.getEmSpServiceses().size());
                    for (EmSpServices service : offer.getEmSpServiceses()) {
                        System.out.println("    - Service : " + service.getTxServicename() + " Num Categories: " + service.getEmServiceSpecifications().size());
                        for (EmServiceSpecification sp : service.getEmServiceSpecifications()) System.out.println("       - Category: " + sp.getTxName());
                    }
                }
                System.out.println("****************************************************");
            }
            System.out.println("Response: " + lProducts.size());
            for (EmSpProducts product : lProducts) {
                System.out.println(product.getTxProductname());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
