package net.sourceforge.rules.verifier.drools;

import java.io.File;
import net.sourceforge.rules.verifier.RulesVerifier;
import net.sourceforge.rules.verifier.RulesVerifierConfiguration;
import org.junit.Test;

/**
 * TODO
 * 
 * @version $Revision$ $Date$
 * @author <a href="mailto:rlangbehn@users.sourceforge.net">Rainer Langbehn</a>
 */
public class DroolsRulesVerifierTest {

    /**
	 * Test method for {@link net.sourceforge.rules.verifier.drools.DroolsRulesVerifier#verify(net.sourceforge.rules.verifier.RulesVerifierConfiguration)}.
	 */
    @Test
    public final void testVerify() throws Exception {
        RulesVerifier verifier = new DroolsRulesVerifier();
        RulesVerifierConfiguration config = new RulesVerifierConfiguration();
        File reportsDirectory = new File("target/verifier-reports");
        config.setReportsDirectory(reportsDirectory);
        File rulesDirectory = new File("target/test-classes");
        config.setRulesDirectory(rulesDirectory);
        config.addInclude("**/*.drl");
        config.setVerbose(true);
        verifier.verify(config);
    }
}
