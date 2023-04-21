package Test.ThietBiDao;

import static org.junit.Assert.*;
import hcmus.am.client.view.ThietBiView;
import hcmus.am.dao.ThietBiDao;
import org.junit.Test;

public class selectThietBiView {

    @Test
    public void testSelect() {
        ThietBiView[] lst = ThietBiDao.selectThietBiView(3);
        assertTrue(lst.length > 1);
    }
}
