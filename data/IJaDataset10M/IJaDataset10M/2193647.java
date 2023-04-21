package cn.vlabs.duckling.vwb.services.auth.permissions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Introduction Here.
 * @date Mar 10, 2010
 * @author xiejj@cnic.cn
 */
public class PagePermissionTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testEditPermission() {
        PagePermission edit = PagePermission.EDIT;
        PagePermission view = new PagePermission("*", PagePermission.VIEW_ACTION);
        assertTrue("edit imply view", edit.implies(view));
        assertFalse("edit imply view", view.implies(edit));
        PagePermission view1 = new PagePermission("1", PagePermission.VIEW_ACTION);
        assertTrue("edit imply view", edit.implies(view1));
        assertFalse("edit imply view", view1.implies(edit));
        PagePermission edit1 = new PagePermission("*", PagePermission.EDIT_ACTION);
        assertTrue("edit imply edit", edit.implies(edit1));
        assertTrue("edit imply edit", edit1.implies(edit));
        PagePermission edit2 = new PagePermission("1", PagePermission.EDIT_ACTION);
        assertTrue("edit imply specify edit", edit.implies(edit2));
        assertFalse("specify edit imply edit", edit2.implies(edit));
    }

    @Test
    public void testViewPermission() {
        PagePermission view = PagePermission.VIEW;
        PagePermission view1 = new PagePermission("*", PagePermission.VIEW_ACTION);
        assertTrue("view imply view", view.implies(view1));
        assertTrue("view imply view", view1.implies(view));
        PagePermission view2 = new PagePermission("1", PagePermission.VIEW_ACTION);
        assertTrue("view imply view", view.implies(view2));
        assertFalse("view imply view", view2.implies(view));
        PagePermission edit = new PagePermission("*", PagePermission.EDIT_ACTION);
        assertFalse("view imply edit", view.implies(edit));
        PagePermission edit1 = new PagePermission("1", PagePermission.EDIT_ACTION);
        assertFalse("view imply edit", view.implies(edit1));
    }
}
