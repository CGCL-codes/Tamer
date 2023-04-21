package net.sf.jalita.examples.login;

import net.sf.jalita.ui.automation.FormAutomationSet;

/**
 * Example: Simple Automation, which simulates a simple login ..
 * 
 * @author Daniel Gal�n y Martins
 * @version $Revision: 1.4 $
 */
public class LoginAutomation extends FormAutomationSet {

    public static final int STATE_LOGIN = 1;

    public static final int STATE_MAIN = 2;

    public static final int ACTION_FINISHED = 1;

    public static final int ACTION_LOGIN = 2;

    public static final int ACTION_RESET = 3;

    public static final int ACTION_LOGOUT = 4;

    private LoginForm loginForm;

    private MainForm mainForm;

    @Override
    protected void initAutomationSet() {
        loginForm = new LoginForm(this);
        mainForm = new MainForm(this);
        addForm(STATE_LOGIN, loginForm);
        addForm(STATE_MAIN, mainForm);
        setInitState(STATE_LOGIN);
    }

    private void doActionLogin() {
        showWaitScreen(" processing login..");
        try {
            Thread.sleep(2000);
            if (loginForm.getUsername().equalsIgnoreCase("test") && loginForm.getPassword().equals("test")) {
                loginForm.resetFields();
                loginForm.setLoginWrong(false);
                setState(STATE_MAIN);
                return;
            }
        } catch (InterruptedException e) {
        }
        loginForm.setLoginWrong(true);
        setState(STATE_LOGIN);
    }

    private void doActionReset() {
        loginForm.resetFields();
        setState(STATE_LOGIN);
    }

    private void doActionLogout() {
        setState(STATE_LOGIN);
    }

    @Override
    public void doAction(int action) {
        switch(action) {
            case ACTION_FINISHED:
                setState(STATE_FINISHED);
                break;
            case ACTION_LOGIN:
                doActionLogin();
                break;
            case ACTION_RESET:
                doActionReset();
                break;
            case ACTION_LOGOUT:
                doActionLogout();
                break;
        }
    }

    @Override
    public String toString() {
        return "LoginAutomation";
    }

    @Override
    public void finish() {
    }
}
