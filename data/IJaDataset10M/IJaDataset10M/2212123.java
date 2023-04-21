package ecom.test.ejb;

import java.util.List;
import com.bm.testsuite.BaseSessionBeanFixture;
import com.bm.testsuite.dataloader.CSVInitialDataSet;
import com.bm.testsuite.dataloader.EntityInitialDataSet;
import ecom.beans.AccountBean;
import ecom.beans.BrandBean;
import ecom.beans.CartBean;
import ecom.beans.ClientBean;
import ecom.beans.EcomAdminBean;
import ecom.beans.ModelBean;
import ecom.beans.ProductBean;
import ecom.beans.ProductStoreBean;

public class AdminBeanTest extends BaseSessionBeanFixture<EcomAdminBean> {

    private static final Class<?>[] usedBeans = { ProductBean.class, ProductStoreBean.class, AccountBean.class, ClientBean.class, ModelBean.class, BrandBean.class };

    /**
	 * Constructor.
	 */
    public AdminBeanTest() {
        super(EcomAdminBean.class, usedBeans);
    }

    public void testLoadProductStore() {
        final EcomAdminBean toTest = this.getBeanToTest();
        assertNotNull(toTest);
        ProductStoreBean productstore = null;
        try {
            toTest.begin();
            productstore = toTest.createProductStore("Magasin Paris", "32 rue des saints-p�res", "75005", "Paris", "France", 0);
            toTest.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(productstore.getRef());
        ProductStoreBean p1 = toTest.findProductStoreById(productstore.getRef());
        assertNotNull(p1);
        System.out.println(p1.getName());
        List back = toTest.listAllProductsStore();
        assertNotNull(back);
        assertEquals(4, back.size());
        assertEquals(((ProductStoreBean) (back.get(0))).getRef(), 1);
        System.out.println(((ProductStoreBean) (back.get(0))).getName() + " " + ((ProductStoreBean) (back.get(0))).getAddress());
    }

    public void testLoadAccount() {
        final EcomAdminBean toTest = this.getBeanToTest();
        assertNotNull(toTest);
        try {
            toTest.createAccount(100);
        } catch (Exception e) {
        }
        List accounts = toTest.listAllAccounts();
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
    }

    public void testLoadClient() {
        final EcomAdminBean toTest = this.getBeanToTest();
        assertNotNull(toTest);
        ClientBean client = null;
        try {
            client = toTest.createClient("cheng", "PENG", "exopeth@gmail.com", "123456", "rue des peuplier", "92100", "Boulogne-Billancourt", "FRANCE", 100);
        } catch (Exception e) {
        }
        ClientBean c1 = toTest.findClientById(client.getUid());
        assertNotNull(c1);
        List back = toTest.listAllClients();
        assertNotNull(back);
        assertEquals(1, back.size());
        assertEquals(((ClientBean) (back.get(0))).getUid(), 1);
        System.out.println(((ClientBean) (back.get(0))).getFirstName() + " " + ((ClientBean) (back.get(0))).getLastName());
    }

    public void testLoadProduct() {
        final EcomAdminBean toTest = this.getBeanToTest();
        assertNotNull(toTest);
        ProductBean product = null;
        BrandBean brand = null;
        ModelBean model = null;
        ProductStoreBean productStore = null;
        try {
            brand = toTest.createBrand("BMW", "none");
            model = toTest.createModel("4x4", 1000, "none", "none", brand.getId());
            productStore = toTest.createProductStore("123", "123", "123", "123", "123", 1000);
            product = toTest.createProduct(model.getId(), productStore.getRef());
            List ppp = toTest.listAllProducts();
        } catch (Exception e) {
        }
        ProductBean p1 = toTest.findProductById(product.getRef());
        assertNotNull(p1);
        List back = toTest.listAllProducts();
        assertNotNull(back);
        assertEquals(1, back.size());
        assertEquals(((ProductBean) (back.get(0))).getRef(), 1);
    }
}
