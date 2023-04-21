package org.javalite.activeweb.freemarker;

import org.javalite.activeweb.RequestSpec;
import org.junit.Before;
import org.junit.Test;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.StringWriter;
import static org.javalite.common.Collections.map;

/**
 * @author Igor Polevoy
 */
public class DebugTagSpec extends RequestSpec {

    FreeMarkerTemplateManager manager = new FreeMarkerTemplateManager();

    StringWriter sw = new StringWriter();

    @Before
    public void before() throws IOException, ServletException, IllegalAccessException, InstantiationException {
        super.before();
        manager.setTemplateLocation("src/test/views");
    }

    @Test
    public void shouldPrintDebugInformationForMap() {
        sw = new StringWriter();
        manager.merge(map("context_path", "/bookstore", "activeweb", map("controller", "simple", "restful", false)), "/debug/debug", sw);
        a(sw.toString()).shouldBeEqual("{restful=false, controller=simple}");
    }
}
