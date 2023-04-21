package net.sf.docbook_utils.maven.plugin.tidy;

import java.io.StringWriter;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.Test;

public class TidyRunnerTest {

    @Test
    public void testConstructor() {
        Properties props = new Properties();
        props.setProperty("indent", "true");
        props.setProperty("wrap", "120");
        TidyRunner runner = new TidyRunner(null, props);
        StringWriter writer = new StringWriter();
        runner.showConfiguration(writer);
        assertOutputContains(writer.getBuffer(), "indent", "Indent", "true");
        assertOutputContains(writer.getBuffer(), "wrap", "Integer", "120");
    }

    private void assertOutputContains(StringBuffer strBuf, String propertyKey, String propertyType, String propertyValue) {
        Pattern pattern = Pattern.compile(".*" + propertyKey + "\\s*" + propertyType + "\\s*" + propertyValue + ".*", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(strBuf);
        Assert.assertTrue("Expected property [" + propertyKey + "|" + propertyType + "|" + propertyValue + "]", matcher.matches());
    }
}
