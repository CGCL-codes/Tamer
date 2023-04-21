package tests.com.ivis.xprocess.abbot.testmenuactions;

import org.eclipse.jface.action.IAction;
import tests.com.ivis.xprocess.abbot.suites.GeneralElementCreationSuite;

public class XabbotActionGeneralCreation extends XabbotAction {

    public void run(IAction action) {
        test = GeneralElementCreationSuite.suite();
        super.run(action);
    }
}
