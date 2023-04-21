package lichen.entities.user;

import lichen.entities.BaseModelTestCase;
import org.hibernate.Session;
import org.testng.annotations.Test;

public class OnlineUserTest extends BaseModelTestCase {

    @Test
    public void test_save_and_query_oneline_user() {
        Session session = this.getSession();
        this.beginTransaction();
        OnlineUser user = new OnlineUser();
        session.save(user);
        this.commit();
        assertNotNull(user.getId());
        this.flushAndClear();
        this.beginTransaction();
        session.delete(user);
        this.commit();
    }
}
