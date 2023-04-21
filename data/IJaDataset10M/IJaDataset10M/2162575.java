package it.webscience.kpeople.service.cross;

import it.webscience.kpeople.service.datatypes.User;
import org.junit.Test;

public class GlobalSearchServiceTest {

    @Test
    public final void testFind() throws Exception {
        GlobalSearchService ms = new GlobalSearchService();
        User u = new User();
        u.setHpmUserId("dellanna@kpeople.webscience.it");
        ms.find("exec", u);
    }
}
