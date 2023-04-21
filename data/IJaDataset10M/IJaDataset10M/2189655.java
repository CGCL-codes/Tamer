package org.impalaframework.interactive.command;

import org.impalaframework.interactive.command.ShowModulesCommand;
import org.impalaframework.module.definition.SimpleRootModuleDefinition;
import junit.framework.TestCase;

public class ShowModulesCommandTest extends TestCase {

    public void testPrintModuleInfo() {
        ShowModulesCommand commands = new ShowModulesCommand();
        commands.printModuleInfo(null);
        final SimpleRootModuleDefinition simpleRootModuleDefinition = new SimpleRootModuleDefinition("rootproject", "location.xml");
        commands.printModuleInfo(simpleRootModuleDefinition);
    }
}
