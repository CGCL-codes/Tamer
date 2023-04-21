package net.sf.revolver.sample.service.auth.login;

import javax.servlet.http.HttpServletRequest;
import net.sf.bulletlib.authentication.core.LoginCheck;
import net.sf.bulletlib.authentication.core.LoginUser;
import net.sf.bulletlib.core.convert.Convert4String;
import net.sf.bulletlib.core.logging.Logging;
import net.sf.revolver.core.BusinessLogicException;
import net.sf.revolver.core.Result;
import net.sf.revolver.core.RvContext;
import net.sf.revolver.s2.RvDoS2;

/**
 * Login処理ビジネスロジック処理Doクラス.
 *
 * @author bose999
 *
 */
public class LoginDo extends RvDoS2 {

    /**
     * ログインチェック.
     */
    public LoginCheck loginCheck;

    /**
     * Login処理.
     *
     * @param rvContext RvContext
     * @return Result
     * @throws BusinessLogicException BusinessLogicException
     */
    @Override
    protected Result doLogic(RvContext rvContext) throws BusinessLogicException {
        LoginUser loginUser = isAuth(rvContext);
        if (loginUser == null) {
            rvContext.setResultValue("loginUser", null);
            addErrorMessage(rvContext, "viewValues.auth", "loginCtrl.auth01");
            return Result.FAILURE;
        }
        loginInfoLogger(loginUser);
        setSessionLoginUser(rvContext, loginUser);
        return Result.SUCCESS;
    }

    /**
     * 認証処理.
     *
     * @param rvContext RvContext
     * @return LoginUser
     */
    protected LoginUser isAuth(RvContext rvContext) {
        HttpServletRequest request = rvContext.getAttributeValue("request");
        String remoteAddr = request.getRemoteAddr();
        String name = (String) rvContext.getInValue("name");
        String password = (String) rvContext.getInValue("password");
        return loginCheck.isAuth(name, password, remoteAddr);
    }

    /**
     * 認証情報ログ出力処理.
     *
     * @param loginUser LoginUser
     */
    protected void loginInfoLogger(LoginUser loginUser) {
        String[] roles = loginUser.roles;
        String rolesString = Convert4String.strings2String(roles, ", ");
        Logging.debug(this.getClass(), "RVS999999", loginUser.loginId, "loginId:" + loginUser.loginId);
        Logging.debug(this.getClass(), "RVS999999", loginUser.loginId, "roles:" + rolesString);
        Logging.debug(this.getClass(), "RVS999999", loginUser.loginId, "locale:" + loginUser.locale.toString());
        Logging.debug(this.getClass(), "RVS999999", loginUser.loginId, "remoteAddr:" + loginUser.remoteAddr);
    }
}
