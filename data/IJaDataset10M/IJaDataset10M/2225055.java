package com.sush.sample.webstore.store.domain.test;

import com.sush.webstore.store.domain.IWebStoreFacade;
import com.sush.webstore.store.domain.facade.WebStorePOJO;
import junit.framework.TestCase;

public class TestWebStorePOJO_Remove extends TestCase {

    private IWebStoreFacade store;

    public TestWebStorePOJO_Remove(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        store = new WebStorePOJO();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testRemoveOrder() {
    }

    public final void testRemoveItem() {
    }

    public final void testRemoveProduct() {
    }

    public final void testRemoveUserAccount() {
    }

    public final void testRemoveCategory() {
    }

    public final void testRemoveCatalog() {
    }
}
