package action;

import action.EliminaSecurityAction;
import modelo.Security;
import junit.framework.TestCase;

public class EliminaSecurityActionTests extends TestCase {

    private EliminaSecurityAction temp;

    protected void setUp() throws Exception {
        this.temp = new EliminaSecurityAction();
    }

    public void testGetandSetCreditcard() {
        Security producto = new Security();
        producto.setId(1);
        temp.setSelectedProduct(producto);
        assertEquals(this.temp.getSelectedProduct().getId(), producto.getId());
    }

    public void testdeleteCreditcard() {
        Security producto = new Security();
        producto.setId(1);
        temp.setSelectedProduct(producto);
        temp.setSelectedProduct(producto);
        assertEquals(true, this.temp.deleteSecurity());
    }
}
