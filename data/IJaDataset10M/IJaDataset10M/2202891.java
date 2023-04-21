package net.sf.just4logng;

import java.io.IOException;
import net.sf.just4log.transform.Config;
import net.sf.just4logng.util.JUnitUtil;
import org.junit.Test;

public class JustLog2Test {

    @Test
    public void testClasses() throws IOException {
        String before = "package net.sf.just4logng;\n" + "\n" + "import org.apache.commons.logging.Log;\n" + "import org.apache.commons.logging.LogFactory;\n" + "\n" + "public class SimpleClass {\n" + "	private static Log logger = LogFactory.getLog(SimpleClass.class);\n" + "	public int sum(int a, int b) {\n" + "		if ((a>0) && (b>0)) {\n" + "			int result = a+b;\n" + "			logger.debug(\"Sum(\"+a+\",\"+b+\")=\"+result);\n" + "			return result;\n" + "		}\n" + "		return 0;\n" + "	}\n" + "}";
        String after = "package net.sf.just4logng;\n" + "\n" + "import org.apache.commons.logging.Log;\n" + "import org.apache.commons.logging.LogFactory;\n" + "\n" + "public class SimpleClass {\n" + "	private static Log logger = LogFactory.getLog(SimpleClass.class);\n" + "	public int sum(int a, int b) {\n" + "		if ((a>0) && (b>0)) {\n" + "			int result = a+b;\n" + "			if (logger.isDebugEnabled()) {" + "				logger.debug(\"Sum(\"+a+\",\"+b+\")=\"+result);\n" + "			}" + "			return result;\n" + "		}\n" + "		return 0;\n" + "	}\n" + "}";
        JUnitUtil.check(new Config(), before, after);
    }
}
