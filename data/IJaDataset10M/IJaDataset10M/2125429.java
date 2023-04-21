package Views;

/**
 *
 * @author Joshua King
 */
public class ViewLoginUserProxy extends ViewLoginInt {

    private ViewLoginInt viewLogin;

    private String loginMsg = "Please wait while system logs in.";

    public ViewLoginUserProxy() {
        viewLogin = new ViewLoginUserReal();
    }

    @Override
    public void setModelData() {
        ViewMain.getInstance().updateMsg(loginMsg);
        model.setUsername(getUserField());
        model.setPassword(getPasswordField());
    }

    @Override
    public void updateFailure() {
        viewLogin.updateFailure();
    }

    @Override
    public void updateSuccess() {
        viewLogin.updateSuccess();
    }
}
