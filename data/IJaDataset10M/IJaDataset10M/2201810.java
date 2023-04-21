package com.javampire.util.mapping;

import com.javampire.util.io.IOUtil;
import junit.framework.TestCase;
import java.io.File;
import java.io.IOException;

/**
 * TODO: document this.
 *
 * @author <a href="mailto:cnagy@ecircle.de">Csaba Nagy</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2007/04/10 10:21:59 $
 */
public class TestIdOrder extends TestCase {

    public void testConstructor() {
        int[] ids = { 2, 0, 1, 3 };
        IdOrder idOrder = new IdOrder(ids);
        idOrder.dump("test");
        ids = new int[] { 2, 0, 1, 2 };
        try {
            idOrder = new IdOrder(ids);
            fail("IllegalStateException expected");
        } catch (IllegalStateException exception) {
        }
    }

    public void testSaveLoad() throws IOException {
        int[] ids = { 2, 0, 1, 3 };
        IdOrder sampleOrder = new IdOrder(ids);
        sampleOrder.dump("test");
        File file = new File(IOUtil.getTempDir(), "test_id_order_test_save_load.bin");
        file.delete();
        IOUtil.save(sampleOrder, file);
        IdOrder result = IOUtil.load(file, IdOrder.class);
        result.dump("test");
        file.delete();
    }
}
