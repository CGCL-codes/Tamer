package my.stuff.joodin.examples;

import joodin.impl.application.util.ApplicationWrapper;
import org.jowidgets.examples.common.workbench.demo1.WorkbenchDemo1;
import org.jowidgets.workbench.impl.WorkbenchRunner;

@SuppressWarnings("serial")
public class TestApplication extends ApplicationWrapper {

    public void applicationInit() {
        new WorkbenchRunner().run(new WorkbenchDemo1());
        this.setTheme("reindeer");
    }
}
