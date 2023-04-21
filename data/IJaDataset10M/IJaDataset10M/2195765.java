package dms.test;

import dms.core.logic.CustomizableEntityManager;
import dms.core.logic.CustomizableEntityManagerImpl;
import dms.core.user.logic.User;
import dms.core.user.logic.UserModel;
import dms.util.AppConstants;
import dms.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.io.Serializable;
import java.util.Date;

public class TestCustomEntities {

    private static final String TEST_FIELD_NAME = "email";

    private static final String TEST_VALUE = "test@test.com";

    public static void main(String[] args) {
        HibernateUtil.getInstance().getCurrentSession();
        CustomizableEntityManager userEntityManager = new CustomizableEntityManagerImpl(UserModel.class);
        boolean isFieldAdded = userEntityManager.addCustomField(TEST_FIELD_NAME);
        if (isFieldAdded) {
            Session session = HibernateUtil.getInstance().getCurrentSession();
            Transaction tx = session.beginTransaction();
            try {
                User user = new UserModel();
                user.setName("user Name 1");
                user.setStatus(AppConstants.STATUS_ACTIVE);
                user.setCrtdate(new Date());
                user.setValueOfCustomField(TEST_FIELD_NAME, TEST_VALUE);
                Serializable id = session.save(user);
                tx.commit();
                user = (User) session.get(UserModel.class, id);
                Object value = user.getValueOfCustomField(TEST_FIELD_NAME);
                System.out.println("value = " + value);
            } catch (Exception e) {
                tx.rollback();
                System.out.println("e = " + e);
            }
        } else {
            System.out.println("Failure: field not added");
        }
    }
}
