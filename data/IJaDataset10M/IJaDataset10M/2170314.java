package simpleorm.simpleweb.eg.simple;

import simpleorm.simpleweb.eg.WAllTests;
import simpleorm.simpleweb.core.WPageStructure;
import simpleorm.simpleweb.context.WPageContextTest;
import java.util.Map;
import java.util.LinkedHashMap;

public class WListBeanTest {

    public static void main(String[] args) throws Exception {
        WListBeanTest.unsubmitted();
        WListBeanTest.submitted();
    }

    static void unsubmitted() throws Exception {
        Map<String, String> fields = new LinkedHashMap();
        WManualListPage page = WAllTests.MENUS.simple.manualList.newPage();
        page.setPageContext(new WPageContextTest(page, fields));
        WPageStructure struct = page.getPageStructure();
        struct.doMain();
        struct.doListRow();
        struct.doListRow();
        WAllTests.assertEquals("two", page.getField("id").getText());
        WAllTests.assertEquals("/dummyCtx/swb/Simple/WManualCrudPage.swb?id=two", page.getField("id").getAnchorHRef());
        struct.doFinalize();
    }

    static void submitted() throws Exception {
        Map<String, String> fields = new LinkedHashMap();
        fields.put("nameWord", "row");
        WManualListPage page = WAllTests.MENUS.simple.manualList.newPage();
        page.setPageContext(new WPageContextTest(page, fields));
        WPageStructure struct = page.getPageStructure();
        struct.doMain();
        struct.doListRow();
        struct.doListRow();
        WAllTests.assertEquals("three", page.getField("id").getText());
        struct.doFinalize();
    }
}
