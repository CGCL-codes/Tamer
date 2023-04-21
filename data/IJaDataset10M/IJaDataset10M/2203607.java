package net.sourceforge.harness.command;

import java.util.Date;
import net.sourceforge.harness.xml.factory.ExecutionFactory;
import net.sourceforge.harness.xml.factory.Factory;
import net.sourceforge.harness.xml.jaxb.Execution;
import net.sourceforge.harness.xml.jaxb.TestCase;
import net.sourceforge.harness.xml.jaxb.TestSuite;
import net.sourceforge.mgl.util.command.AbstractCommand;
import org.apache.log4j.Logger;

/**
 * Command for initializing newly created TestCase instances.
 *
 * <p><b>Usage:</b><code>trivial</code>
 *
 * <p><b>History:</b><br>
 * 20011019, Initial file<br>
 *
 * <p><b>CVS Information:</b><br>
 * <i>
 * $Date: 2002/10/09 13:11:31 $<br>
 * $Revision: 1.9 $<br>
 * $Author: mgl $<br>
 * </i>
 *
 * @author Marcel Schepers (mgl@dds.nl)
 */
public class InitializeTestCaseCommand extends AbstractCommand {

    private static final String CLASSNAME = InitializeTestCaseCommand.class.getName();

    private static final Logger LOGGER = Logger.getLogger(CLASSNAME);

    TestCase testCase = null;

    TestSuite parent = null;

    public InitializeTestCaseCommand(TestCase testCase, TestSuite parent) {
        LOGGER.debug("ctor [IN ]");
        LOGGER.debug("  testCase: " + testCase);
        LOGGER.debug("    name: " + testCase.getName());
        this.testCase = testCase;
        this.parent = parent;
        LOGGER.debug("ctor [OUT]");
    }

    /**
   * Execute the encapsulated command.
   * @return a <code>boolean</code> value (true if command is succesful
   * and can be undone).
   */
    public boolean execute() {
        LOGGER.debug("execute [IN ]");
        Factory factory = ExecutionFactory.getInstance();
        Execution execution = (Execution) factory.createElement();
        this.testCase.setExecution(execution);
        LOGGER.debug("execute [OUT]");
        LOGGER.debug("  return: " + false);
        return false;
    }

    /**
   * Undo the last invocation of execute().
   * @return a <code>boolean</code> value (true if undo was succesful and
   * can be redone.)
   */
    public boolean undo() {
        LOGGER.debug("undo [IN ]");
        LOGGER.debug("undo [OUT]");
        LOGGER.debug("  return: " + false);
        return false;
    }
}
