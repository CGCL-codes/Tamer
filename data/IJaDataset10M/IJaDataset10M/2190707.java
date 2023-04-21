package redora.junit;

import redora.api.fetch.Mode;
import redora.api.fetch.Page;
import redora.api.fetch.Scope;
import redora.exceptions.RedoraException;
import redora.test.rdo.model.JUnitChild;
import redora.test.rdo.service.JUnitChildService;
import redora.test.rdo.service.ServiceFactory;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static redora.junit.AbstractDBTest.*;
import static redora.util.JUnitUtil.assertRedoraPersist;

/**
 * PageTest will check if page for retrieve objects work well....
 */
public class PageTest {

    static JUnitChildService jUnitChildService;

    @BeforeClass
    public static void makeTables() throws RedoraException {
        makeTestTables();
        jUnitChildService = ServiceFactory.jUnitChildService();
    }

    @AfterClass
    public static void dropTables() throws RedoraException {
        dropTestTables();
        jUnitChildService.close();
    }

    @Test
    public void testPage() throws RedoraException {
        for (int i = 0; i < 50; i++) {
            JUnitChild child = new JUnitChild();
            child.setName("name" + i);
            assertRedoraPersist(jUnitChildService.persist(child), null);
        }
        List<JUnitChild> listForAll = jUnitChildService.findAll(Page.ALL_TABLE);
        Page page = new Page(Scope.Table, Mode.Page, 10);
        List<JUnitChild> listWithPaging = jUnitChildService.findAll(page);
        assertEquals("The page's resultCount should equal total size of objects when first retrieve objects", page.resultCount(), listForAll.size());
        assertEquals("The retrieved list size should equal page size when first retrieve objects", page.pageSize(), listWithPaging.size());
        assertEquals("When first retrieve by page,it should retrieve the first page's first object", listWithPaging.get(0).getName(), listForAll.get(page.position() * page.pageSize()).getName());
        listWithPaging = jUnitChildService.findAll(page.next());
        assertEquals("It should retrieve the next page's first object", listWithPaging.get(0).getName(), listForAll.get(page.position() * page.pageSize()).getName());
        listWithPaging = jUnitChildService.findAll(page.last());
        assertEquals("It should retrieve the last page's first object", listWithPaging.get(0).getName(), listForAll.get(page.position() * page.pageSize()).getName());
        listWithPaging = jUnitChildService.findAll(page.previous());
        assertEquals("It should retrieve the previous page's first object", listWithPaging.get(0).getName(), listForAll.get(page.position() * page.pageSize()).getName());
        listWithPaging = jUnitChildService.findAll(page.goTo(4));
        assertEquals("It should retrieve the appointed page's first object", listWithPaging.get(0).getName(), listForAll.get(page.position() * page.pageSize()).getName());
        listWithPaging = jUnitChildService.findAll(page.first());
        assertEquals("It should retrieve the first page's first object", listWithPaging.get(0).getName(), listForAll.get(page.position() * page.pageSize()).getName());
    }

    @Test
    public void testScroll() {
    }
}
