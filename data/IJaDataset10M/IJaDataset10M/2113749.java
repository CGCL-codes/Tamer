package werkzeugkasten.common.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import junit.framework.Assert;
import org.junit.Test;
import werkzeugkasten.common.util.Streams;

public class JarAssemblerTest {

    @Test
    public void testOpenFile() throws Exception {
        JarAssembler assembler = new JarAssembler(new JarConfig() {

            public boolean compress() {
                return true;
            }
        });
        File dest = File.createTempFile("Test", null);
        dest.deleteOnExit();
        try {
            assembler.open(dest);
            assembler.entry("/aaa/bbb/");
            assembler.entry("/aaa/bbb/ccc/ddd/");
            assembler.entry("/aaa/ddd");
            final ClassLoader cl = JarAssemblerTest.class.getClassLoader();
            Opener opener = new Opener() {

                public InputStream open() throws IOException {
                    return cl.getResourceAsStream("werkzeugkasten/common/jar/JarAssemblerTest.data");
                }
            };
            assembler.entry(opener, "ccc/ddd/JarAssemblerTest.das");
            assembler.entry(opener, "ccc/aaa/JarAssemblerTest.das");
            assembler.add(new ManifestModifier() {

                public void modify(Manifest manifest) {
                    Attributes attrs = manifest.getMainAttributes();
                    attrs.putValue("hoge", "fuga");
                }
            });
        } finally {
            assembler.close();
        }
        JarFile jar = new JarFile(dest);
        Assert.assertEquals("fuga", jar.getManifest().getMainAttributes().getValue("hoge"));
        JarEntry dir = jar.getJarEntry("/aaa/bbb/ccc/");
        Assert.assertNotNull(dir);
        Assert.assertTrue(dir.isDirectory());
        dir = jar.getJarEntry("/aaa/ddd/");
        Assert.assertNotNull(dir);
        Assert.assertTrue(dir.isDirectory());
        JarEntry file = jar.getJarEntry("ccc/aaa/JarAssemblerTest.das");
        Assert.assertNotSame(file.getSize(), file.getCompressedSize());
        String s = Streams.readText(jar.getInputStream(file));
        Assert.assertEquals("dddddd", s);
    }
}
