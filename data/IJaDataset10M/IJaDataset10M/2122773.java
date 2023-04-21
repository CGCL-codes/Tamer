package plugin.pretokens;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import plugin.pretokens.parser.PreSpellParser;
import plugin.pretokens.writer.PreSpellWriter;

public class PreSpellRoundRobin extends AbstractBasicRoundRobin {

    public static void main(String args[]) {
        TestRunner.run(PreSpellRoundRobin.class);
    }

    /**
	 * @return Test
	 */
    public static Test suite() {
        return new TestSuite(PreSpellRoundRobin.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TokenRegistration.register(new PreSpellParser());
        TokenRegistration.register(new PreSpellWriter());
    }

    @Override
    public String getBaseString() {
        return "SPELL";
    }

    @Override
    public boolean isTypeAllowed() {
        return false;
    }
}
