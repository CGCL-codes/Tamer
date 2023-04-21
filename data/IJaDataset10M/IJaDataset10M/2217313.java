package whf.test.menu;

import java.util.List;
import whf.framework.entity.Entity;
import whf.framework.ext.entity.MenuItem;
import whf.framework.ext.service.MenuItemServiceImp;
import whf.framework.service.QueryService;
import whf.test.BaseTest;

public class MenuTest extends BaseTest {

    @Override
    public Class getBoClass() {
        return MenuItem.class;
    }

    public void te1stUpdate() throws Exception {
        MenuItem item = MenuItemServiceImp.getMenuItemService().findByPrimaryKey(50);
        log.info(item);
        if ("".equals(item.getHref())) {
            item.setHref("asdfasdf");
        } else {
            item.setHref("");
        }
        MenuItemServiceImp.getMenuItemService().update(item);
    }

    public void testTree() throws Exception {
        QueryService service = MenuItemServiceImp.getMenuItemService();
        List<Entity> list = service.queryWithoutLazy("order by t.parent.id, t.sortOrder, t.name");
        for (Entity o : list) {
            MenuItem m = (MenuItem) o;
            System.out.println(m.getId() + "\t" + m.getName() + "\t" + (m.getParent() == null ? "NULL" : m.getParent().getName()));
        }
    }
}
