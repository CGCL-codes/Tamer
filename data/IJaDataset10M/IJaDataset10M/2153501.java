package net.sf.stump.eclipse.template;

import junit.framework.TestCase;
import net.sf.stump.eclipse.metadata.WicketClass;
import net.sf.stump.eclipse.template.WicketClassContextType;
import net.sf.stump.eclipse.template.WicketClassTemplateContext;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateBuffer;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateException;

/**
 * @author Joni Suominen
 */
public class WicketClassContextTypeTest extends TestCase {

    private String testJavaTemplatePattern = "package ${packageName}; public class ${classShortName}";

    private TemplateContext context;

    @Override
    protected void setUp() throws Exception {
        WicketClass clazz = new WicketClass("com.example", "MyPanel");
        context = new WicketClassTemplateContext(new WicketClassContextType(), clazz);
    }

    public void testEvaluateJava() throws BadLocationException, TemplateException {
        Template template = new Template("test", "", WicketClassContextType.ID, testJavaTemplatePattern, true);
        TemplateBuffer buffer = context.evaluate(template);
        assertEquals(testJavaTemplatePattern.replace("${packageName}", "com.example").replace("${classShortName}", "MyPanel"), buffer.getString());
    }
}
