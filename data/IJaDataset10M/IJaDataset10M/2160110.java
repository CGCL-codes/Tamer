package com.videostore.model.xml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.custommonkey.xmlunit.XMLTestCase;
import org.w3c.dom.Document;
import com.videostore.model.Product;
import com.videostore.model.WareHouse;
import com.videostore.util.TestHelper;

/**
 * @author Enrico Sada
 *
 */
public class WarehouseSerializerTest extends XMLTestCase {

    DateFormat df = new SimpleDateFormat("dd/MM/yy");

    WareHouseSerializer ws;

    public WarehouseSerializerTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        ws = new WareHouseSerializer();
    }

    public void testDeserializeExample() throws Exception {
        Document doc = TestHelper.loadDocument("/videostore/src/test/resources/com/videostore/model/WarehouseExample.xml");
        WareHouse w = ws.deserialize(doc.getDocumentElement());
        assertNotNull(w);
        assertEquals(1, w.getProducts().size());
        assertEquals(true, w.getProducts().containsKey("dvd"));
        Product p = w.getProducts().get("dvd");
        assertNotNull(p);
        assertEquals(44, p.getArticles().size());
    }
}
