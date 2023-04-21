package cs;

import com.puppycrawl.tools.checkstyle.BaseCheckTestCase;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;

/**
 * 
 * @author Arnaud Roques
 * 
 */
public class DeclareVariableInItsBlocTest extends BaseCheckTestCase {

    public void testDefault() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(DeclareVariableInItsBloc.class);
        final String[] expected = { "6: Declare the variable in the bloc that uses it.", "14: Declare the variable in the bloc that uses it." };
        verify(checkConfig, getPath("testinputs/InputDeclareVariableInItsBloc.java"), expected);
    }

    public void testDefault2() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(DeclareVariableInItsBloc.class);
        final String[] expected = { "6: Declare the variable in the bloc that uses it.", "14: Declare the variable in the bloc that uses it." };
        verify(checkConfig, getPath("testinputs/InputDeclareVariableInItsBloc2.java"), expected);
    }

    public void testDefault3() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(DeclareVariableInItsBloc.class);
        final String[] expected = { "7: Declare the variable in the bloc that uses it.", "27: Declare the variable in the bloc that uses it.", "42: Declare the variable in the bloc that uses it." };
        verify(checkConfig, getPath("testinputs/InputDeclareVariableInItsBloc3.java"), expected);
    }

    public void testDefault4() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(DeclareVariableInItsBloc.class);
        final String[] expected = {};
        verify(checkConfig, getPath("testinputs/InputDeclareVariableInItsBloc4.java"), expected);
    }
}
