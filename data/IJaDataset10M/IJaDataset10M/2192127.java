package equilibrium.commons.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import equilibrium.commons.report.config.model.ElementBean;
import equilibrium.commons.report.config.model.ElementType;
import equilibrium.commons.report.generator.SubreportData;
import equilibrium.commons.report.generator.contenttable.ContentTableItem;
import equilibrium.commons.report.generator.contenttable.ContentTableListener;
import equilibrium.commons.report.generator.transformer.mock.ReportContextMock;
import junit.framework.TestCase;

public class ContentTableListenerTest extends TestCase {

    private ReportContext context;

    private ContentTableListener listener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = buildReportContext();
        listener = new ContentTableListener();
    }

    public void testParsableTitle() {
        ElementBean element = new ElementBean(ElementType.SUBREPORT, "myRef");
        element.setTocTitle("test ${shouldBeParsed}");
        SubreportData subreportData = new SubreportData(true);
        listener.notifyAfter(element, context, subreportData);
        List<ContentTableItem> contentTableItems = listener.getContentTableItems();
        assertEquals(1, contentTableItems.size());
        ContentTableItem item = contentTableItems.get(0);
        assertEquals("test parsed string", item.getSubreportTitle());
    }

    public void testRegularTitle() {
        ElementBean element = new ElementBean(ElementType.SUBREPORT, "myRef");
        element.setTocTitle("test title");
        SubreportData subreportData = new SubreportData(true);
        listener.notifyAfter(element, context, subreportData);
        List<ContentTableItem> contentTableItems = listener.getContentTableItems();
        assertEquals(1, contentTableItems.size());
        ContentTableItem item = contentTableItems.get(0);
        assertEquals("test title", item.getSubreportTitle());
    }

    public void testInvisibleWhenEmpty() {
        ElementBean element = new ElementBean(ElementType.SUBREPORT, "myRef");
        element.setTocTitle("test title");
        SubreportData subreportData = new SubreportData(false);
        listener.notifyAfter(element, context, subreportData);
        List<ContentTableItem> contentTableItems = listener.getContentTableItems();
        assertEquals(0, contentTableItems.size());
    }

    private ReportContext buildReportContext() {
        ReportContext context = new ReportContextMock();
        Map<String, Object> parserContextExpressions = new HashMap<String, Object>();
        parserContextExpressions.put("shouldBeParsed", "parsed string");
        context.pushToContext(parserContextExpressions);
        return context;
    }
}
