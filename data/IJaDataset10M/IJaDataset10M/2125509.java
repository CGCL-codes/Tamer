package net.sourceforge.freejava.snm;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import net.sourceforge.freejava.io.resource.builtin.URLResource;
import net.sourceforge.freejava.string.StringFeature;
import net.sourceforge.freejava.util.ClassResource;
import net.sourceforge.freejava.util.file.FilePath;
import org.junit.Assert;
import org.junit.Test;

public class JarStuffTest extends Assert {

    static Set<String> commonOutputDirs;

    {
        commonOutputDirs = new HashSet<String>();
        commonOutputDirs.add("test-classes");
        commonOutputDirs.add("test.classes");
        commonOutputDirs.add("classes");
    }

    private String magic = "MaGiC-GoOd..";

    private File projectBase;

    public JarStuffTest() {
        projectBase = EclipseProject.findProjectBase(JarStuffTest.class);
    }

    @Test
    public void testFindProjectBase() throws Exception {
        System.out.println("[projbase] " + projectBase);
    }

    @Test
    public void testGetBaseClasspath() throws Exception {
        File classpath;
        classpath = JarLocations.getBaseClasspath(JarStuffTest.class);
        System.out.println("[outbase] (test class) => " + classpath);
        String name = classpath.getName();
        assertTrue(commonOutputDirs.contains(name));
        classpath = JarLocations.getBaseClasspath(Object.class);
        System.out.println("[outbase] Object.class => " + classpath);
        assertEquals("jar", FilePath.getExtension(classpath.getName()));
    }

    @Test
    public void testGetSrcURL_findMagic() throws IOException {
        URL src = BuildPath.getSrcURL(JarStuffTest.class);
        if (src != null) {
            String code = new URLResource(src).forRead().readTextContents();
            assertEquals(1, StringFeature.count(code, magic));
        }
    }

    @Test
    public void testGetSrcURL_rtjar() throws IOException {
        String classRes = ClassResource.classDataURL(Object.class).toString();
        URL srcurl = BuildPath.getSrcURL(Object.class);
        if (srcurl != null) {
            String srcRes = srcurl.toString();
            assertNotNull("can't find src of Object.class", srcRes);
            String srcExpected = classRes.replace(".class", ".java");
            srcExpected = srcExpected.replace("rt.jar!", "rt-src.jar!");
            assertEquals(srcExpected, srcRes);
        }
    }

    @Test
    public void testGetSrcURLWithName() throws IOException {
    }
}
