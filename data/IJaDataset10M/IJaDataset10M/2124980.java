package org.helianto.core.filter;

import static org.junit.Assert.assertEquals;
import java.util.Date;
import org.helianto.core.UserGroup;
import org.helianto.core.filter.form.CompositeIdentityForm;
import org.helianto.core.filter.form.UserRequestForm;
import org.helianto.core.test.UserGroupTestSupport;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class UserRequestFilterAdapterTests {

    String OB = "order by alias.internalNumber ";

    String C1 = "alias.userGroup.id = 1 ";

    String C2 = "AND alias.internalNumber = 100 ";

    String C3 = "lower(alias.principal) like '%test@domain%' ";

    String C4 = "(alias.nextCheckDate >= '1969-12-24 23:59:59' AND alias.nextCheckDate < '1969-12-31 21:00:01' ) ";

    String C5 = "alias.tempPassword = 'ABCD' ";

    @Test
    public void empty() {
        assertEquals(OB, filter.createCriteriaAsString());
    }

    @Test
    public void select() {
        ((CompositeIdentityForm) form).setUserGroup(userGroup);
        form.setInternalNumber(100);
        assertEquals(C1 + C2, filter.createCriteriaAsString());
    }

    @Test
    public void docName() {
        ((CompositeIdentityForm) form).setPrincipal("TEST@domain");
        assertEquals(C3 + OB, filter.createCriteriaAsString());
    }

    @Test
    public void nextCheckDate() {
        ((CompositeIdentityForm) form).setNextCheckDate(new Date(1000L));
        assertEquals(C4 + OB, filter.createCriteriaAsString());
    }

    @Test
    public void tempPassword() {
        ((CompositeIdentityForm) form).setTempPassword("ABCD");
        assertEquals(C5 + OB, filter.createCriteriaAsString());
    }

    private UserRequestForm form;

    private UserRequestFormFilterAdapter filter;

    private UserGroup userGroup;

    @Before
    public void setUp() {
        userGroup = UserGroupTestSupport.createUserGroup(1);
        form = new CompositeIdentityForm("");
        filter = new UserRequestFormFilterAdapter(form);
        form.reset();
    }
}
