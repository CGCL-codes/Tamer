package name.angoca.db2sa;

import name.angoca.db2sa.core.graph.model.EndingNodeTest;
import name.angoca.db2sa.core.graph.model.GraphNodeTest;
import name.angoca.db2sa.core.graph.model.GraphTest;
import name.angoca.db2sa.core.graph.model.LicenseNodeTest;
import name.angoca.db2sa.core.graph.model.StartingNodeTest;
import name.angoca.db2sa.core.lexical.model.TokenTest;
import name.angoca.db2sa.core.syntactic.model.GraphAnswerTest;
import name.angoca.db2sa.interfaze.model.ReturnOptionsTest;
import name.angoca.db2sa.ui.impl.system.SystemOutputWriterTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * This is the suite of unit test of all the project.<br/>
 * <b>Control Version</b><br />
 * <ul>
 * <li>1.0.0 Class creation.</li>
 * </ul>
 * 
 * @author Andres Gomez Casanova <a
 *         href="mailto:a n g o c a at y a h o o dot c o m">(AngocA)</a>
 * @version 1.0.0 2009-11-14
 */
@RunWith(Suite.class)
@SuiteClasses({ StartingNodeTest.class, EndingNodeTest.class, GraphNodeTest.class, LicenseNodeTest.class, GraphTest.class, GraphAnswerTest.class, TokenTest.class, ReturnOptionsTest.class, SystemOutputWriterTest.class })
public final class UnitTests {

    /**
     * Default constructor.
     */
    private UnitTests() {
    }
}
