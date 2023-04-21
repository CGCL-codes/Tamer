package net.sourceforge.jpp.handler;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class OptionsTest extends HandlerSuite {

    private static final String[][] _parameters = { { "Case1", "Case1Generated", "Case1Expected" }, { "Case2", "Case2Generated", "Case2Expected" } };

    private static final String[] _options = { "-AFactory.template=net/sourceforge/jpp/test/options/Case1.ftl", "-Anet.sourceforge.jpp.annotation.Singleton.template=net/sourceforge/jpp/test/options/Case2.ftl" };

    public OptionsTest(String src, String target, String expected) {
        super(src, target, expected);
    }

    @Parameterized.Parameters
    public static List<String[]> getParameters() {
        return Arrays.asList(_parameters);
    }

    @Override
    public boolean compileClass(String... className) throws Exception {
        return super.compileClass(_options, className);
    }
}
