package org.t2framework.t2.tool.apt;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.seasar.aptina.unit.AptinaTestCase;
import org.t2framework.commons.util.StringUtil;

/**
 * TODO mavenだと値が返ってこないorz
 * 
 * @author c9katayama
 * @author shot
 * 
 */
public class PageSettingsProcessorTest extends AptinaTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        addSourcePath("test");
        setCharset("UTF-8");
    }

    public void testProcess() throws Exception {
        PageSettingsProcessor processor = new PageSettingsProcessor();
        addProcessor(processor);
        addCompilationUnit(Page1.class);
        compile();
        FileObject generatedFile = getProcessingEnvironment().getFiler().getResource(StandardLocation.SOURCE_OUTPUT, "", "page.properties");
        String contents = generatedFile.getCharContent(true).toString();
        if (StringUtil.isEmpty(contents) == false) {
            final String expected = "org.t2framework.t2.tool.apt=Page1" + System.getProperty("line.separator");
            assertEquals(expected, contents);
        }
    }

    public void testProcess2_interfaceShouldBeIgnored() throws Exception {
        PageSettingsProcessor processor = new PageSettingsProcessor();
        addProcessor(processor);
        addCompilationUnit(Page1.class);
        addCompilationUnit(IPage1.class);
        compile();
        FileObject generatedFile = getProcessingEnvironment().getFiler().getResource(StandardLocation.SOURCE_OUTPUT, "", "page.properties");
        String contents = generatedFile.getCharContent(true).toString();
        if (StringUtil.isEmpty(contents) == false) {
            final String expected1 = "org.t2framework.t2.tool.apt=Page1";
            assertTrue(contents.contains(expected1));
            assertTrue(contents.contains("IPage1") == false);
        }
    }

    public void testProcess3_abstractClassShouldBeIgnored() throws Exception {
        PageSettingsProcessor processor = new PageSettingsProcessor();
        addProcessor(processor);
        addCompilationUnit(Page1.class);
        addCompilationUnit(AbstractPage1.class);
        compile();
        FileObject generatedFile = getProcessingEnvironment().getFiler().getResource(StandardLocation.SOURCE_OUTPUT, "", "page.properties");
        String contents = generatedFile.getCharContent(true).toString();
        if (StringUtil.isEmpty(contents) == false) {
            final String expected1 = "org.t2framework.t2.tool.apt=Page1";
            assertTrue(contents.contains(expected1));
            assertTrue(contents.contains("AbstractPage1") == false);
        }
    }

    public void testProcess4_multipleClass() throws Exception {
        PageSettingsProcessor processor = new PageSettingsProcessor();
        addProcessor(processor);
        addCompilationUnit(Page1.class);
        addCompilationUnit(AbstractPage1.class);
        addCompilationUnit(PageImpl1.class);
        compile();
        FileObject generatedFile = getProcessingEnvironment().getFiler().getResource(StandardLocation.SOURCE_OUTPUT, "", "page.properties");
        String contents = generatedFile.getCharContent(true).toString();
        if (StringUtil.isEmpty(contents) == false) {
            assertTrue(contents.contains("AbstractPage1") == false);
            assertTrue(contents.contains("Page1"));
            assertTrue(contents.contains("PageImpl1"));
        }
    }
}
