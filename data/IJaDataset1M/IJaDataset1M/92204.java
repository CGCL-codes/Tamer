package com.avatal.test.httpunit;

import java.io.File;
import java.io.FileWriter;
import java.util.regex.Pattern;
import com.meterware.httpunit.*;
import com.avatal.test.*;

public class CoursesUpdateNewsTest extends AbstractHttpUnitTest {

    public CoursesUpdateNewsTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        request = new GetMethodWebRequest(TestProperties.getProperties("application.contextURL") + "/content.jsp?tab=course_management&submenu=course_management.edit&body=course_management.edit");
        response = conversation.getResponse(request);
        WebForm searchCoursesForm = response.getForms()[0];
        request = searchCoursesForm.getRequest();
        request.setParameter("keywords", "");
        response = conversation.getResponse(request);
        request = new GetMethodWebRequest(TestProperties.getProperties("application.contextURL") + "/content.jsp?tab=course_management&submenu=course_management.edit&body=course_management.edit.adminscorm&courseId=1");
        response = conversation.getResponse(request);
        request = new GetMethodWebRequest(TestProperties.getProperties("application.contextURL") + "/content.jsp?tab=course_management&submenu=course_management.edit&body=course_management.edit.news");
        response = conversation.getResponse(request);
        request = new GetMethodWebRequest(TestProperties.getProperties("application.contextURL") + "/updateNews.do?NewsId=3");
        response = conversation.getResponse(request);
    }

    public void testCoursesUpdateNewsPageWithTidy() throws Exception {
        outputFile = new File(TestProperties.getProperties("output.file.html") + "\\courses\\coursesUpdateNews.html");
        fw = new FileWriter(outputFile);
        fw.write(response.getText());
        fw.close();
    }

    public void testCoursesUpdateNewsTables() throws Exception {
        WebTable tables[] = { response.getTableWithSummary("rootLayout/parent"), response.getTableWithSummary("rootLayout/inner"), response.getTableWithSummary("rootLayout/border"), response.getTableWithSummary("rootLayout/border_top_left"), response.getTableWithSummary("rootLayout/border_top_right"), response.getTableWithSummary("rootLayout/content"), response.getTableWithSummary("coursesUpdateNews/data") };
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

    public void testCoursesUpdateNewsForms() throws Exception {
        forms = response.getForms();
        assertEquals("form count", 1, forms.length);
        parameters1 = forms[0].getParameterNames();
        assertEquals("Parameteranzahl falsch", 6, parameters1.length);
        assertEquals("falscher Parameter", "validFrom", parameters1[0]);
        assertEquals("falscher Parameter", "value", parameters1[1]);
        assertEquals("falscher Parameter", "newsType", parameters1[2]);
        assertEquals("falscher Parameter", "validTo", parameters1[3]);
        assertEquals("falscher Parameter", "title", parameters1[4]);
        assertEquals("falscher Parameter", "id", parameters1[5]);
    }
}
