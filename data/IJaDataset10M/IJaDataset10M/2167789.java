package com.avatal.test.httpunit;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Pattern;
import com.meterware.httpunit.*;
import com.avatal.test.*;

public class GroupsUpdateTest extends AbstractHttpUnitTest {

    public GroupsUpdateTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        request = new GetMethodWebRequest(TestProperties.getProperties("application.contextURL") + "/content.jsp?tab=group&submenu=group.edit");
        response = conversation.getResponse(request);
        WebForm searchGroupsForm = response.getForms()[0];
        request = searchGroupsForm.getRequest();
        request.setParameter("grName", "");
        response = conversation.getResponse(request);
        request = new GetMethodWebRequest(TestProperties.getProperties("application.contextURL") + "/updateGroup.do?groupId=" + TestProperties.getProperties("groups.id"));
        response = conversation.getResponse(request);
    }

    public void testGroupsUpdatePageWithTidy() throws Exception {
        outputFile = new File(TestProperties.getProperties("output.file.html") + "\\groups\\groupsUpdate.html");
        fw = new FileWriter(outputFile);
        fw.write(response.getText());
        fw.close();
    }

    public void testGroupsUpdateTables() throws Exception {
        WebTable tables[] = { response.getTableWithSummary("rootLayout/parent"), response.getTableWithSummary("rootLayout/inner"), response.getTableWithSummary("rootLayout/border"), response.getTableWithSummary("rootLayout/border_top_left"), response.getTableWithSummary("rootLayout/border_top_right"), response.getTableWithSummary("rootLayout/content"), response.getTableWithSummary("groupsUpdate/data") };
        p = Pattern.compile("<table");
        m = p.matcher(response.getText());
        while (m.find()) {
            tableCount = tableCount + 1;
        }
        p = Pattern.compile("<td");
        m = p.matcher(response.getText());
        while (m.find()) {
            columnCount = columnCount + 1;
        }
        p = Pattern.compile("</td>");
        m = p.matcher(response.getText());
        while (m.find()) {
            endColumnCount = endColumnCount + 1;
        }
        p = Pattern.compile("</tr>");
        m = p.matcher(response.getText());
        while (m.find()) {
            rowCount1 = rowCount1 + 1;
        }
        for (int i = 0; i < tables.length; i++) {
            rowCount2 = rowCount2 + tables[i].getRowCount();
        }
        assertEquals("table count", tableCount, tables.length);
        assertEquals("column count", columnCount, endColumnCount);
        assertEquals("row count", rowCount1, rowCount2);
    }

    public void testGroupsUpdateForms() throws Exception {
        forms = response.getForms();
        assertEquals("form count", 1, forms.length);
        parameters1 = forms[0].getParameterNames();
        assertEquals("Parameteranzahl falsch", 3, parameters1.length);
        assertEquals("falscher Parameter", "description", parameters1[0]);
        assertEquals("falscher Parameter", "grState", parameters1[1]);
        assertEquals("falscher Parameter", "id", parameters1[2]);
    }
}
