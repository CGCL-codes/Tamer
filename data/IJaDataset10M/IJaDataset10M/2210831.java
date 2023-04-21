package org.owasp.jxt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.owasp.jxt.JxtTemplate;
import org.owasp.jxt.servlet.JxtServletBase;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * IntegrationTest
 *
 * @author Jeffrey Ichnowski
 * @version $Revision: 8 $
 */
public class IntegrationTest extends TestCase {

    JxtEngine _engine;

    File _expected;

    IntegrationTest(String name, JxtEngine engine, File expected) {
        super(name);
        _engine = engine;
        _expected = expected;
    }

    @Override
    protected void runTest() throws Exception {
        String input = getName();
        JxtCompilation<JxtTemplate<Object>> compilation = _engine.compileTemplate(input, Object.class);
        Class<? extends JxtTemplate<Object>> compiledClass = compilation.getTemplateClass();
        JxtTemplate<Object> instance = compiledClass.newInstance();
        StringWriter actual = new StringWriter();
        PrintWriter out = new PrintWriter(actual);
        instance.render(null, out);
        out.close();
        Reader expected = new InputStreamReader(new FileInputStream(_expected), "UTF-8");
        try {
            BufferedReader aIn = new BufferedReader(new StringReader(actual.toString()));
            BufferedReader eIn = new BufferedReader(expected);
            for (int lineNo = 1; ; lineNo++) {
                String aLine = aIn.readLine();
                String eLine = eIn.readLine();
                assertEquals("Line " + lineNo, eLine, aLine);
                if (aLine == null || eLine == null) {
                    break;
                }
            }
        } finally {
            expected.close();
        }
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite();
        URL dirUrl = IntegrationTest.class.getClassLoader().getResource("org/owasp/jxt/integration-tests/");
        assert "file".equals(dirUrl.getProtocol()) : "Tests directory is not in 'file' protocol: " + dirUrl;
        File dir = new File(dirUrl.getFile());
        assert dir.isDirectory() : "Tests directory is not a directory: " + dir;
        JxtEngine engine = JxtEngine.builder().webRoot(dir).build();
        List<String> files = new ArrayList<String>();
        for (String name : dir.list()) {
            if (name.endsWith(".jxt")) {
                files.add(name);
            }
        }
        Collections.sort(files);
        for (String name : files) {
            File expected = new File(dir, name.replaceAll("\\.jxt$", ".output"));
            suite.addTest(new IntegrationTest(name, engine, expected));
        }
        return suite;
    }
}
