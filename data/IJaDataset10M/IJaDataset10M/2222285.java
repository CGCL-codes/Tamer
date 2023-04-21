package com.bluestone.run;

import java.util.List;
import com.bluestone.context.ExecuteContext;
import com.bluestone.context.IContext;
import com.bluestone.context.ProjectContext;
import com.bluestone.context.TestSuiteContext;
import com.bluestone.scripts.Execute;
import com.bluestone.scripts.Property;
import com.bluestone.scripts.TestSuite;
import com.bluestone.util.Util;

public class ExecuteController extends BaseController implements IController {

    @Override
    boolean executeContext(IContext context) {
        boolean flag = false;
        ExecuteContext executeContext = (ExecuteContext) context;
        Execute execute = executeContext.getExecute();
        executeContext.addExecuteLog("run " + execute.getFile() + " start!");
        int failcount = 0;
        List<Property> properties = execute.getProperties();
        if (properties != null) {
            for (Property prop : properties) {
                prop.loadObject(ProjectContext.getInstance(null));
                ProjectContext.getInstance(null).getProject().putProperty(prop);
            }
        }
        TestSuiteContext testSuiteContext = executeContext.getTestSuiteContext();
        TestSuiteController controller = new TestSuiteController();
        flag = controller.execute(testSuiteContext);
        if (properties != null) {
            for (Property prop : properties) {
                ProjectContext.getInstance(null).getProject().removeProperty(prop);
            }
        }
        return flag;
    }
}
