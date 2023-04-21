package net.wgen.op.http.filter;

import net.wgen.op.OpTestServices;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import com.mockrunner.servlet.BasicServletTestCaseAdapter;
import com.mockrunner.servlet.ServletTestModule;
import com.mockrunner.mock.web.WebMockObjectFactory;

/**
 * @author Paul Feuer
 * @version $Id: TestParamTriggerFilter.java 8 2007-01-17 15:37:22Z paulfeuer $
 */
public class TestParamTriggerFilter extends BasicServletTestCaseAdapter {

    static {
        BasicConfigurator.configure();
        Logger.getLogger("net.wgen").setLevel(Level.DEBUG);
    }

    public void setUp() {
        WebMockObjectFactory fact = OpTestServices.createNewWebMockObjectFactory(null);
        setWebMockObjectFactory(fact);
        setServletTestModule(new ServletTestModule(fact));
        createFilter(ParamTriggerFilter.class);
    }
}
