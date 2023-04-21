package net.beeger.osmorc.frameworkintegration.util;

import org.junit.Test;
import static org.junit.Assert.*;
import com.intellij.testFramework.LightVirtualFile;
import static org.hamcrest.core.IsEqual.*;

public class FileUtilTest {

    @Test
    public void testGetNameWithoutJarSuffix() {
        assertThat(FileUtil.getNameWithoutJarSuffix(new LightVirtualFile("test.jar")), equalTo("test"));
        assertThat(FileUtil.getNameWithoutJarSuffix(new LightVirtualFile("test.bla.jar")), equalTo("test.bla"));
        assertThat(FileUtil.getNameWithoutJarSuffix(new LightVirtualFile("test")), equalTo("test"));
    }

    public void testGetNameWithoutTailVirtualFile() {
        assertThat(FileUtil.getNameWithoutTail(new LightVirtualFile("test.bla.jar"), ".bla.jar"), equalTo("test"));
    }
}
