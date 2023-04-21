package com.google.gdt.eclipse.designer.smartgwt.model.widgets;

import org.eclipse.wb.tests.designer.core.DesignerSuiteTests;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * SmartGWT widgets tests.
 * 
 * @author scheglov_ke
 */
public class WidgetsTests extends DesignerSuiteTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("gwt.SmartGWT.model.widgets");
        suite.addTest(createSingleSuite(VersionTest.class));
        suite.addTest(createSingleSuite(CanvasTest.class));
        suite.addTest(createSingleSuite(SliderTest.class));
        suite.addTest(createSingleSuite(HLayoutTest.class));
        suite.addTest(createSingleSuite(VLayoutTest.class));
        suite.addTest(createSingleSuite(TileLayoutTest.class));
        suite.addTest(createSingleSuite(ListGridTest.class));
        suite.addTest(createSingleSuite(DetailViewerTest.class));
        suite.addTest(createSingleSuite(TileGridTest.class));
        suite.addTest(createSingleSuite(ColumnTreeTest.class));
        suite.addTest(createSingleSuite(TreeGridTest.class));
        suite.addTest(createSingleSuite(SectionStackTest.class));
        suite.addTest(createSingleSuite(DynamicFormTest.class));
        suite.addTest(createSingleSuite(FormItemTest.class));
        suite.addTest(createSingleSuite(TabSetTest.class));
        suite.addTest(createSingleSuite(ToolStripTest.class));
        suite.addTest(createSingleSuite(MenuTest.class));
        suite.addTest(createSingleSuite(MenuButtonTest.class));
        suite.addTest(createSingleSuite(MenuBarTest.class));
        suite.addTest(createSingleSuite(WindowTest.class));
        suite.addTest(createSingleSuite(FilterBuilderTest.class));
        suite.addTest(createSingleSuite(DataSourceTest.class));
        suite.addTest(createSingleSuite(ListGridGefTest.class));
        return suite;
    }
}
