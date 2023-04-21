package internal.generator;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class FileMappingTest {

    @Test
    public void testGetTargetFileName() {
        FileMapping mapping = new FileMapping();
        Map context = new HashMap();
        context.put("basePackagePath", "xx/yy/zz");
        String result = mapping.getTargetFileName("$${basePackagePath}.$bin/", "d:/", context);
        assertEquals("d:/${basePackagePath}/", result);
    }
}
