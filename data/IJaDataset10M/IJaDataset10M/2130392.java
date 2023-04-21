package org.jmesa.worksheet.servlet;

import org.jmesa.core.message.Messages;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.jmesa.web.HttpServletRequestWebContext;
import org.jmesa.web.WebContext;
import org.jmesa.worksheet.UniqueProperty;
import org.jmesa.worksheet.Worksheet;
import org.jmesa.worksheet.WorksheetRow;
import org.jmesa.worksheet.WorksheetUpdater;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @since 2.3
 * @author Jeff Johnston
 */
public class WorksheetServletTest {

    protected static final String ID = "pres";

    @Test
    public void getWorksheet() {
        WorksheetUpdaterTemp servlet = new WorksheetUpdaterTemp();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("id", ID);
        WebContext webContext = new HttpServletRequestWebContext(request);
        Worksheet worksheet = servlet.getAccessToWorksheet(null, webContext);
        WorksheetRow row = new WorksheetRow(new UniqueProperty(null, null));
        worksheet.addRow(row);
        assertNotNull(worksheet);
        assertTrue("There are no rows in the worksheet.", worksheet.getRows().size() == 1);
        Worksheet worksheet2 = servlet.getAccessToWorksheet(null, webContext);
        assertNotNull(worksheet2);
        assertTrue("Did not return the same worksheet.", worksheet == worksheet2);
    }

    private class WorksheetUpdaterTemp extends WorksheetUpdater {

        public Worksheet getAccessToWorksheet(Messages messages, WebContext webContext) {
            return super.getWorksheet(messages, webContext);
        }
    }
}
