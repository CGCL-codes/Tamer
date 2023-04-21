package simpleorm.simplewebapp.eg.simple;

import simpleorm.simplewebapp.eg.WAllTests;
import simpleorm.simplewebapp.core.WPageStructure;
import simpleorm.simplewebapp.core.WButton;
import simpleorm.simplewebapp.context.WPageContextTest;
import java.util.Map;
import java.util.LinkedHashMap;

public class WListBeanTest {

    public static void main(String[] args) throws Exception {
        WListBeanTest.unfiltered();
        WListBeanTest.filtered();
    }

    static void unfiltered() throws Exception {
        Map<String, String> fields = new LinkedHashMap();
        fields.put(WButton.BUTTONS_NAME, "Search");
        WManualListPage page = WAllTests.MENUS.simple.manualList.newPage();
        page.setPageContext(new WPageContextTest(page, fields));
        WPageStructure struct = page.getPageStructure();
        WAllTests.assertEquals("!Name", page.translate("name"));
        WAllTests.assertEquals("?xyzzx", page.translate("xyzzx"));
        struct.doMain();
        struct.doListRow();
        struct.doListRow();
        WAllTests.assertEquals("two", page.getField("id").getText());
        WAllTests.assertEquals("/dummyCtx/swb/Simple/WManualCrudPage.swb?id=two", page.getField("id").getAnchorHRef());
        struct.doFinalize();
    }

    static void filtered() throws Exception {
        Map<String, String> fields = new LinkedHashMap();
        fields.put("nameWord", "row");
        fields.put(WButton.BUTTONS_NAME, "Search");
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
