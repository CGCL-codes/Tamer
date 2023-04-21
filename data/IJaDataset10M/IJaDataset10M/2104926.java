package net.sourceforge.mile4j.web;

import static org.junit.Assert.assertNotNull;
import java.util.Properties;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;

public class VelocityTest {

    @Test
    public void template() throws Exception {
        Properties oVelocityProps = new Properties();
        oVelocityProps.setProperty("resource.loader", "file");
        oVelocityProps.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        oVelocityProps.setProperty("velocimacro.library", "/net/sourceforge/mile4j/web/templates/macros.vm");
        VelocityEngine oEngine = new VelocityEngine(oVelocityProps);
        Template t = oEngine.getTemplate("/net/sourceforge/mile4j/web/templates/jsp/Model.jsp");
        assertNotNull(t);
    }
}
