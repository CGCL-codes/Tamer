package test.canyon.support;

import static org.junit.Assert.*;
import java.io.IOException;
import java.net.URL;
import net.dataforte.canyon.support.ClassUtils;
import org.junit.Test;

public class ClassUtilsTest {

    @Test
    public void testGetFilesInFolders() throws IOException {
        URL[] filesInFolders = ClassUtils.getResources("META-INF");
        for (URL url : filesInFolders) {
            System.out.println(url.toString());
        }
    }
}
