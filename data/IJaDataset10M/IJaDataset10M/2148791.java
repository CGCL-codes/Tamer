package springapp.repository;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import springapp.domain.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml" })
public class JdbcProductRepositoryTests extends AbstractTransactionalJUnit4SpringContextTests {

    private ProductRepository productRepository;

    @BeforeTransaction
    public void onSetUpInTransaction() throws Exception {
        super.deleteFromTables(new String[] { "products" });
        super.executeSqlScript("file:db/hsqldb/load_data.sql", true);
        productRepository = applicationContext.getBean(ProductRepository.class);
    }

    @Test
    public void testGetProductList() {
        List<Product> products = productRepository.getProductList();
        assertEquals("Wrong number of products?", 3, products.size());
    }

    @Test
    public void testSaveProduct() {
        List<Product> products = productRepository.getProductList();
        for (Product p : products) {
            p.setPrice(200.12);
            productRepository.saveProduct(p);
        }
        List<Product> updatedProducts = productRepository.getProductList();
        for (Product p : updatedProducts) {
            assertEquals("Wrong price of product?", new Double(200.12), p.getPrice());
        }
    }
}
