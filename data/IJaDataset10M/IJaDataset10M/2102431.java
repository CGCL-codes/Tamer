package org.jcvi.common.core.assembly.ace.consed;

import java.io.File;
import org.jcvi.common.core.assembly.ace.consed.ConsedUtil;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dkatzel
 *
 *
 */
public class TestConsedUtilGetNextAceVersion {

    @Test
    public void version1ShouldMakeVersion2() {
        File version1 = new File("consed.ace.1");
        assertEquals("consed.ace.2", ConsedUtil.generateNextAceVersionNameFor(version1));
    }

    @Test
    public void version2ShouldMakeVersion3() {
        File version2 = new File("consed.ace.2");
        assertEquals("consed.ace.3", ConsedUtil.generateNextAceVersionNameFor(version2));
    }

    @Test
    public void twoDigitVersion() {
        File version28 = new File("consed.ace.28");
        assertEquals("consed.ace.29", ConsedUtil.generateNextAceVersionNameFor(version28));
    }

    @Test(expected = IllegalArgumentException.class)
    public void noVersionShouldThrowIllegalArgumentException() {
        File noVersion = new File("consed.ace");
        ConsedUtil.generateNextAceVersionNameFor(noVersion);
    }

    @Test
    public void doubleSuffix() {
        File doubleSuffix = new File("consed.ace.1.ace.5");
        assertEquals("consed.ace.1.ace.6", ConsedUtil.generateNextAceVersionNameFor(doubleSuffix));
    }

    @Test
    public void noPrefix() {
        File noPrefix = new File("ace.5");
        assertEquals("ace.6", ConsedUtil.generateNextAceVersionNameFor(noPrefix));
    }
}
