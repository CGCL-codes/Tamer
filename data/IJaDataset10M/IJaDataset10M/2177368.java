package com.avatal.test.httpunit;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Pattern;
import com.meterware.httpunit.*;
import com.avatal.test.*;

public class GroupsAssignUserTest extends AbstractHttpUnitTest {

    public GroupsAssignUserTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        request = new GetMethodWebRequest(TestProperties.getProperties("application.contextURL") + "/content.jsp?tab=group&submenu=group.assign&body=group.assign.user");
        response = conversation.getResponse(request);
    }

    public void testGroupsAssignUserPageWithTidy() throws Exception {
        outputFile = new File(TestProperties.getProperties("output.file.html") + "\\groups\\groupsAssignUser.html");
        fw = new FileWriter(outputFile);
        fw.write(response.getText());
        fw.close();
    }

    public void testGroupsAssignUserTables() throws Exception {
        WebTable table[] = { response.getTableWithSummary("rootLayout/parent"), response.getTableWithSummary("rootLayout/inner"), response.getTableWithSummary("rootLayout/border"), response.getTableWithSummary("rootLayout/border_top_left"), response.getTableWithSummary("rootLayout/border_top_right"), response.getTableWithSummary("rootLayout/content"), response.getTableWithSummary("groupsAssignUser/parent"), response.getTableWithSummary("groupsAssignUser/search"), response.getTableWithSummary("groupsAssignUser/assign") };
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
        for (int i = 0; i < table.length; i++) {
            rowCount2 = rowCount2 + table[i].getRowCount();
        }
        assertEquals("table count", tableCount, table.length);
        assertEquals("column count", columnCount, endColumnCount);
        assertEquals("row count", rowCount1, rowCount2);
    }

    public void testGroupsAssignUserForms() throws Exception {
        forms = response.getForms();
        assertEquals("form count", 2, forms.length);
        parameters1 = forms[0].getParameterNames();
        assertEquals("Parameteranzahl falsch", 8, parameters1.length);
        assertEquals("falscher Parameter", "lastname", parameters1[0]);
        assertEquals("falscher Parameter", "login", parameters1[1]);
        assertEquals("falscher Parameter", "submenu", parameters1[2]);
        assertEquals("falscher Parameter", "grName", parameters1[3]);
        assertEquals("falscher Parameter", "tab", parameters1[4]);
        assertEquals("falscher Parameter", "grState", parameters1[5]);
        assertEquals("falscher Parameter", "body", parameters1[6]);
        assertEquals("falscher Parameter", "searchTarget", parameters1[7]);
        parameters2 = forms[1].getParameterNames();
        assertEquals("Parameteranzahl falsch", 6, parameters2.length);
        assertEquals("falscher Parameter", "object", parameters2[0]);
        assertEquals("falscher Parameter", "container", parameters2[1]);
        assertEquals("falscher Parameter", "submenu", parameters2[2]);
        assertEquals("falscher Parameter", "tab", parameters2[3]);
        assertEquals("falscher Parameter", "member", parameters2[4]);
        assertEquals("falscher Parameter", "body", parameters2[5]);
    }
}
