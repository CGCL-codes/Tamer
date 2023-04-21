package org.apache.tapestry.contrib.script;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import junit.framework.TestCase;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.ILocation;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Location;
import org.apache.tapestry.contrib.mocks.MockComponent;
import org.apache.tapestry.resource.ClasspathResourceLocation;
import org.apache.tapestry.util.DefaultResourceResolver;

/**
 * @author rhensle
 */
public class ScriptResolverTest extends TestCase {

    public void testFindScript() {
        IScriptResolver sr = new ScriptResolver(this.getClass().getClassLoader());
        IComponent mc = new MockComponent();
        IResourceResolver rr = new DefaultResourceResolver();
        IResourceLocation rl = new ClasspathResourceLocation(rr, "/pages/test.page");
        mc.getSpecification().setSpecificationLocation(rl);
        ILocation loc = new Location(rl, 1, 2);
        IScriptReference sref = sr.findScript(null, mc, loc);
        assertSame(sref.getComponent(), mc);
        assertSame(sref.getLocation(), loc);
        assertEquals(sref.getResourceLocation().getPath(), "/pages/test.groovy");
    }

    public void testReloadScript() throws IOException {
        IScriptResolver sr = new ScriptResolver(this.getClass().getClassLoader());
        IComponent mc = new MockComponent();
        IResourceResolver rr = new DefaultResourceResolver();
        IResourceLocation rl = new ClasspathResourceLocation(rr, "/pages/test.page");
        mc.getSpecification().setSpecificationLocation(rl);
        ILocation loc = new Location(rl, 1, 2);
        IScriptReference sref = sr.findScript(null, mc, loc);
        assertSame(sref.getComponent(), mc);
        assertSame(sref.getLocation(), loc);
        assertEquals(sref.getResourceLocation().getPath(), "/pages/test.groovy");
        Object first = sref.getObject();
        assertNotNull(first);
        URL url = sref.getResourceLocation().getResourceURL();
        File file = new File(url.getFile());
        FileOutputStream fos = new FileOutputStream(file, true);
        try {
            PrintWriter pw = new PrintWriter(fos);
            pw.println();
            pw.println("// comment");
            pw.close();
        } finally {
            fos.close();
        }
        Object second = sref.getObject();
        assertNotNull(second);
        assertNotSame(first, second);
    }

    public void testResolveNotFoundScript() {
        IScriptResolver sr = new ScriptResolver(this.getClass().getClassLoader());
        IComponent mc = new MockComponent();
        IResourceResolver rr = new DefaultResourceResolver();
        IResourceLocation rl = new ClasspathResourceLocation(rr, "/pages/test.page");
        mc.getSpecification().setSpecificationLocation(rl);
        ILocation loc = new Location(rl, 1, 2);
        try {
            sr.findScript("notfound", mc, loc);
            fail("found the script?");
        } catch (ApplicationRuntimeException are) {
            assertEquals(are.getMessage(), "Script not found at location /pages/notfound.groovy.");
            assertEquals(are.getLocation().getResourceLocation().getPath(), "/pages/test.page");
            assertEquals(are.getLocation().getLineNumber(), 1);
            assertEquals(are.getLocation().getColumnNumber(), 2);
        }
    }

    public void testResolveScript() {
        IScriptResolver sr = new ScriptResolver(this.getClass().getClassLoader());
        IComponent mc = new MockComponent();
        IResourceResolver rr = new DefaultResourceResolver();
        IResourceLocation rl = new ClasspathResourceLocation(rr, "/pages/test.page");
        mc.getSpecification().setSpecificationLocation(rl);
        ILocation loc = new Location(rl, 1, 2);
        IScriptReference sref = sr.findScript(null, mc, loc);
        assertSame(sref.getComponent(), mc);
        assertSame(sref.getLocation(), loc);
        assertEquals(sref.getResourceLocation().getPath(), "/pages/test.groovy");
        Object first = sref.getObject();
        assertNotNull(first);
        Object second = sref.getObject();
        assertNotNull(second);
        assertSame(first, second);
    }

    public void testSyntaxError() {
        IScriptResolver sr = new ScriptResolver(this.getClass().getClassLoader());
        IComponent mc = new MockComponent();
        IResourceResolver rr = new DefaultResourceResolver();
        IResourceLocation rl = new ClasspathResourceLocation(rr, "/pages/test.page");
        mc.getSpecification().setSpecificationLocation(rl);
        ILocation loc = new Location(rl, 1, 2);
        IScriptReference sref = sr.findScript("syntaxerror", mc, loc);
        assertSame(sref.getComponent(), mc);
        assertSame(sref.getLocation(), loc);
        assertEquals(sref.getResourceLocation().getPath(), "/pages/syntaxerror.groovy");
        try {
            sref.getObject();
            fail();
        } catch (ApplicationRuntimeException are) {
            assertEquals(are.getLocation().getResourceLocation().getPath(), "/pages/syntaxerror.groovy");
            assertEquals(are.getLocation().getLineNumber(), 5);
            assertEquals(are.getLocation().getColumnNumber(), 1);
        }
    }
}
