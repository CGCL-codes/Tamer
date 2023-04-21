package cn.vlabs.duckling.vwb.ui.rsi;

import java.security.Principal;
import java.util.Collection;
import cn.vlabs.duckling.vwb.VWBContext;
import cn.vlabs.duckling.vwb.VWBSession;
import cn.vlabs.duckling.vwb.services.login.LoginAction;
import cn.vlabs.duckling.vwb.services.login.Subject;
import cn.vlabs.duckling.vwb.ui.command.VWBCommand;
import cn.vlabs.duckling.vwb.ui.rsi.api.DCTRsiErrorCode;
import cn.vlabs.duckling.vwb.ui.rsi.api.UserInfo;
import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.ServiceWithInputStream;

/**
 * @date 2010-3-24
 * @author Fred Zhang (fred@cnic.cn)
 */
public class RemoteLoginService extends ServiceWithInputStream {

    public Object doAction(RestSession session, Object message) throws ServiceException {
        try {
            UserInfo user = (UserInfo) message;
            VWBContext vwbcontext = VWBContext.createContext(getRequest(), VWBCommand.LOGIN, null);
            Collection<Principal> prins = vwbcontext.getSite().getUserService().login(user.getUserName(), user.getPassword());
            if (prins == null || prins.size() == 0) {
                throw new Exception("the user(name:" + user.getUserName() + ",password:" + user.getPassword() + ") is invalid");
            }
            Subject subject = LoginAction.convertPrincipal(prins);
            VWBSession vwbsession = VWBSession.findSession(getRequest());
            vwbsession.setSubject(VWBSession.AUTHENTICATED, subject);
            vwbcontext.getSite().getAuthenticationService().login(getRequest());
        } catch (Throwable e) {
            throw new ServiceException(DCTRsiErrorCode.LOGIN_ERROR, e.getMessage());
        }
        return Boolean.TRUE;
    }
}
