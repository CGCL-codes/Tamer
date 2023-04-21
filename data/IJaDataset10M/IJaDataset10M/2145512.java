package action;

import action.AgregaProductTypeAction;
import modelo.ProductType;
import junit.framework.TestCase;

public class AgregaProductTypeActionTests extends TestCase {

    private AgregaProductTypeAction temp;

    protected void setUp() throws Exception {
        temp = new AgregaProductTypeAction();
    }

    public void testaddProduct() {
        ProductType producto = new ProductType();
        producto.setName("CREAM PUFFS");
        this.temp.setNewProduct(producto);
        assertEquals(true, this.temp.addProducType());
    }
}
