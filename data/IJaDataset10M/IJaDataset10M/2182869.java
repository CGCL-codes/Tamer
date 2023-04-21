package consciouscode.bonsai.nodes;

import consciouscode.bonsai.actions.ActionException;
import consciouscode.bonsai.components.GenericFrame;
import junit.framework.Assert;

public class TestingFrame extends GenericFrame {

    public TestingFrame() {
    }

    public TestingFrame(WindowManager manager) {
        myWindowManager = manager;
    }

    public WindowManager getWindowManager() {
        return myWindowManager;
    }

    public void setDialogPath(String path) {
        myDialogPath = path;
    }

    public int getDialogValue() {
        return myDialogValue;
    }

    public void setDialogValue(int value) {
        myDialogValue = value;
    }

    public void handleDialog() {
        try {
            TestingDialog dialog = (TestingDialog) myWindowManager.createDialog(myDialogPath, this);
            myWindowManager.showModalDialog(dialog);
            myDialogValue = dialog.getValue();
        } catch (ActionException e) {
            e.printStackTrace(System.out);
            Assert.fail("Caught ActionException. See above.");
        }
    }

    @Override
    protected void doPrepareGui() {
    }

    private WindowManager myWindowManager;

    private String myDialogPath;

    private int myDialogValue;
}
