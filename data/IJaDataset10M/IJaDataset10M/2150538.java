package cn.vlabs.clb.ui.frameservice;

import cn.vlabs.clb.CLBException;
import cn.vlabs.clb.CurrentConnection;
import cn.vlabs.clb.api.CLBAppAuth;
import cn.vlabs.clb.api.CLBPasswdInfo;
import cn.vlabs.clb.api.ErrorCode;
import cn.vlabs.clb.domain.folder.FolderManager;
import cn.vlabs.clb.security.AuthModuleFactory;
import cn.vlabs.clb.security.Sessions;
import cn.vlabs.clb.ui.flex.FolderTool;
import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceAction;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.simpleAuth.PasswordInfo;
import cn.vlabs.simpleAuth.Principal;
import cn.vlabs.simpleAuth.UserNameInfo;
import cn.vlabs.simpleAuth.client.AuthenticateManager;
import cn.vlabs.clb.ui.rest.actions.RestAbstractAction;

public class LoginAction implements ServiceAction {

    public Object doAction(RestSession session, Object arg) throws ServiceException {
        AuthenticateManager am = AuthModuleFactory.getAuhthenticator();
        try {
            Principal[] prins = null;
            String username;
            if (arg instanceof CLBAppAuth) {
                CLBAppAuth auth = (CLBAppAuth) arg;
                prins = authUser(am, auth);
                username = auth.getUsername();
            } else if (arg instanceof CLBPasswdInfo) {
                CLBPasswdInfo auth = (CLBPasswdInfo) arg;
                prins = authUser(am, auth);
                username = auth.getUsername();
            } else {
                throw new ServiceException(ErrorCode.BAD_PARAMETER, "�������֤��Ϣ����Ҫ��");
            }
            if (prins != null && prins.length > 0) {
                Sessions sessions = new Sessions();
                sessions.addPrincipals(prins);
                session.setAttribute("sessions", sessions);
                FolderTool tool = FolderTool.create(username);
                sessions.setVOS(tool.getVos());
                sessions.setFolderTool(tool);
                CurrentConnection.setSessions(sessions);
                try {
                    checkHome(username, FolderManager.getInstance());
                } catch (CLBException e) {
                    throw new ServiceException(ErrorCode.INTERNAL_ERROR, "��ʼ���û�Ŀ¼ʧ��");
                }
                return true;
            } else return false;
        } finally {
            AuthModuleFactory.release(am);
        }
    }

    private void checkHome(String currentUser, FolderManager manager) throws CLBException {
        String home = "/user/" + currentUser;
        if (!manager.exist(home)) {
            manager.initUserHome(currentUser);
            manager.initUploadDir("user", currentUser);
        }
    }

    private Principal[] authUser(AuthenticateManager am, CLBPasswdInfo auth) {
        PasswordInfo pinfo = new PasswordInfo(auth.getUsername(), auth.getPassword());
        return am.login(pinfo);
    }

    private Principal[] authUser(AuthenticateManager am, CLBAppAuth auth) {
        String result = am.ValidateAppUser(auth.getAppname(), auth.getPassword());
        if ("true".equalsIgnoreCase(result)) {
            UserNameInfo uinfo = new UserNameInfo();
            uinfo.setUsername(auth.getUsername());
            return am.login(uinfo);
        } else {
            return null;
        }
    }
}
