package net.sourceforge.mile4j.web;

import net.sourceforge.mile4j.web.FileUtils;
import net.sourceforge.mile4j.web.SetupMojo;
import org.junit.Test;

/**
 * @author Luc Pezet <lpezet@gmail.com>
 *
 */
public class SetupMojoTest {

    @Test
    public void execute() throws Exception {
        SetupMojo oPlugin = new SetupMojo();
        oPlugin.setBaseDir("target/generated");
        oPlugin.setProjectArtifactId("test");
        oPlugin.setProjectGroupId("com.mysite");
        try {
            oPlugin.execute();
            AssertUtils.assertFileExistsAndTemplateFree("target/generated", "src/main/webapp/WEB-INF/web.xml");
            AssertUtils.assertFileExistsAndTemplateFree("target/generated", "src/main/webapp/WEB-INF/spring-servlet.xml");
            AssertUtils.assertFileExistsAndTemplateFree("target/generated", "src/main/webapp/WEB-INF/applicationContext.xml");
            AssertUtils.assertFileExistsAndTemplateFree("target/generated", "src/main/webapp/WEB-INF/tiles-def.xml");
            AssertUtils.assertFileExistsAndTemplateFree("target/generated", "src/main/webapp/WEB-INF/security.xml");
            AssertUtils.assertFileExistsAndTemplateFree("target/generated", "src/main/webapp/META-INF/context.xml");
            AssertUtils.assertFileExistsAndTemplateFree("target/generated", "src/main/java/com/mysite/test/controller/PingController.java");
        } finally {
            FileUtils.deleteDirectory("target/generated");
        }
    }
}
