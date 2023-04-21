package org.richfaces.tarkus.fmk.business;

import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.richfaces.tarkus.fmk.persistence.model.MenuBean;
import org.unitils.dbunit.annotation.DataSet;
import org.unitils.spring.annotation.SpringBeanByName;

public class MenuBeanTest extends AbstractFmkTest {

    @SpringBeanByName
    private IMenuService menuService;

    @Test
    @DataSet
    public void testMenuList() throws Exception {
        List<MenuBean> menus = menuService.getRootsMenu();
        Assert.assertNotNull(menus);
    }
}
