package com.fastaop.aspectloader.discovery;

import java.io.InputStream;
import junit.framework.TestCase;
import com.fastaop.testaspect.TestAspect1;

public class AspectFinderTest extends TestCase {

    public void testFindAspectClasses() throws Exception {
        InputStream class1AsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(TestAspect1.class.getName().replace('.', '/') + ".class");
        InputStream class2AsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(AspectFinderTest.class.getName().replace('.', '/') + ".class");
        final AspectFinder asF = new AspectFinder();
        asF.scan(class1AsStream);
        asF.scan(class2AsStream);
        class1AsStream.close();
        class2AsStream.close();
        assertEquals(1, asF.getAspectClasses().size());
        assertEquals(TestAspect1.class.getName(), asF.getAspectClasses().get(0));
    }
}
