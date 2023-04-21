package net.sourceforge.freejava.annotation.util;

import java.lang.annotation.Annotation;
import junit.framework.TestCase;
import net.sourceforge.freejava.annotation.util.AnnotationDump;
import org.junit.Test;

@XyzHint("TeST")
public class AnnotationsTest extends TestCase {

    static String annPrefix(Class<? extends Annotation> annotationClass) {
        return annotationClass + " @" + annotationClass.getName();
    }

    @Test
    public void testDumpAnnotationMap() {
        String text = AnnotationDump.dumpAnnotationMap(AnnotationsTest.class);
        assertEquals(annPrefix(XyzHint.class) + "(value=[TeST])\n", text);
    }
}
