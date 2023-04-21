package com.sivoh.hssftemplates.tags;

import com.meterware.servletunit.InvocationContext;
import com.sivoh.hssftemplates.tags.HssfWorkbookTag;
import com.sivoh.hssftemplates.HssfTemplateTest;
import com.sivoh.hssftemplates.HssfTemplateContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import javax.servlet.ServletException;

/**
 * 
 * @author sivoh
 * @version $REVISION
 */
public class HssfRowTagTest extends HssfBaseTagTest {

    public HssfRowTagTest(String name) {
        super(name);
    }

    public void testCreateRow() throws Exception {
        InvocationContext context = getTestInvocation("tests/row_test1.hssft?row1=1&row2=4&row3=8");
        HssfTemplateContext templateContext = renderWorkbook(context);
        HSSFSheet sheet = templateContext.getWorkbook().getSheetAt(0);
        assertNotNull("Row should exist at explicit index 1", sheet.getRow(1));
        assertNotNull("Row should exist at explicit index 4", sheet.getRow(4));
        assertNotNull("Row should exist at explicit index 8", sheet.getRow(8));
        assertNotNull("Row should exist at contextual index 9", sheet.getRow(9));
    }

    public void testSheetContext() throws Exception {
        InvocationContext context = getTestInvocation("tests/row_test1.hssft");
        HssfWorkbookTag renderTree = getRenderTree(context);
        HssfSheetTag sheetTag = (HssfSheetTag) renderTree.getChildTags().iterator().next();
        HssfRowTag rowTag = (HssfRowTag) sheetTag.getChildTags().iterator().next();
        TestContextTag testTag = new TestContextTag(this);
        rowTag.addHssfTag(testTag);
        renderTree.render(getHssfTemplateContext(context));
        assertTrue("Row tag rendered children", testTag.hasTagBeenRendered());
    }

    public void testDefinesStyle() throws Exception {
        InvocationContext context = getTestInvocation("tests/row_test2.hssft?style=fred");
        HssfTemplateContext templateContext = renderWorkbook(context);
        HSSFCellStyle cellStyle = templateContext.getWorkbook().getSheetAt(0).getRow(0).getCell((short) 0).getCellStyle();
        assertNotNull("Style defined in row is not null", cellStyle);
        assertEquals("Style should have thin top border", HSSFCellStyle.BORDER_THIN, cellStyle.getBorderTop());
    }

    public void childRenderTest(HssfTemplateContext context) throws ServletException {
        if (context.getRow() == null) throw new ServletException("Context has no current row");
    }
}
