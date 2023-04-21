package cranix.home.test.action;

import com.opensymphony.xwork2.Action;
import cranix.common.action.SuperStruts2Action;
import cranix.home.test.bc.TestBc;

@SuppressWarnings("serial")
public class FileTestAction extends SuperStruts2Action {

    public String execute() throws Exception {
        TestBc tb = new TestBc(getInput());
        tb.execute();
        return Action.SUCCESS;
    }
}
