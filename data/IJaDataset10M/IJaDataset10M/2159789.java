package org.cyberaide.gridshell2.junit;

import org.cyberaide.gridshell2.CommandletBase;
import org.cyberaide.gridshell2.Context;
import org.cyberaide.gridshell2.util.CliValues;

/** This is a class, that RegistryTests uses for it tests.
 * 
 * It is not possible to use an internal class for this because
 * Class.newInstance() doesn't work with internal classes.
 */
public class TestCommandlet extends CommandletBase {

    @Override
    public String getCommandName() {
        return RegistryTests.randomValue1;
    }

    @Override
    protected String work(CliValues arg0, Context arg1) {
        return RegistryTests.randomValue1;
    }
}
