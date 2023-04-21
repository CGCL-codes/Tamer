package org.simpleframework.page.translate;

import junit.framework.TestCase;
import org.simpleframework.http.serve.Context;
import org.simpleframework.page.translate.Definition;
import org.simpleframework.page.translate.Page;
import org.simpleframework.page.Workspace;
import simple.FileSystem;
import java.util.List;

public class PageTest extends TestCase {

    private static String EXAMPLE = "/source/Example.jsp";

    private Definition source;

    private Workspace project;

    private Context context;

    private Page page;

    public void setUp() throws Exception {
        context = FileSystem.getContext();
        project = new Workspace(context);
        source = new Definition(project, EXAMPLE);
        page = new Page();
    }

    public void testDefinition() {
        assertEquals("ExamplePage", source.getName());
        assertEquals("source.ExamplePage", source.getTarget());
    }

    public void testLanguage() {
        page.parse("<%@ page language='java' %>");
        page.process(source, null);
        assertEquals("java", source.getLanguage());
        page.parse("<%@ page language='groovy' %>");
        page.process(source, null);
        assertEquals("groovy", source.getLanguage());
        page.parse("<%@ page  language  =\"java\" %>");
        page.process(source, null);
        assertEquals("java", source.getLanguage());
    }

    public void testContentType() {
        page.parse("<%@ page contentType=\"text/xhtml; charset=ISO-8859-1\" %>");
        page.process(source, null);
        assertEquals("ISO-8859-1", source.getCharset());
        assertEquals("text/xhtml", source.getType());
        page.parse("<%@ page CONTENTTYPE='text/html  ;  charset = UTF-8' %>");
        page.process(source, null);
        assertEquals("UTF-8", source.getCharset());
        assertEquals("text/html", source.getType());
    }

    public void testImport() {
        page.parse("<%@ page import='java.util.List, java.util.Map, java.util.HashMap' %>");
        page.process(source, null);
        List list = source.getImports();
        assertEquals(3, list.size());
        assertEquals("java.util.List", list.get(0));
        assertEquals("java.util.Map", list.get(1));
        assertEquals("java.util.HashMap", list.get(2));
    }
}
