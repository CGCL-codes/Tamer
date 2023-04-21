package ces.platform.infoplat.ui.website.action;

import java.util.Hashtable;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import ces.coral.log.*;
import ces.platform.infoplat.core.*;
import ces.platform.infoplat.core.base.*;
import ces.platform.infoplat.core.tree.*;
import ces.platform.infoplat.ui.common.defaultvalue.*;
import ces.platform.infoplat.utils.*;
import ces.platform.system.dbaccess.User;

/**
 *  <b>վ���б����-->��ѯվ����Ϣ�б���ʾ</b> <br>
 *
 *
 *@Title      ��Ϣƽ̨
 *@Company    �Ϻ�������Ϣ���޹�˾
 *@version    2.5
 *@author     ���� ֣��ǿ
 *@created    2004��2��11��
 */
public class SiteListAction extends Action {

    Logger log = new Logger(getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String siteChannelPath = request.getParameter("siteChannelPath") == null ? TreeNode.SITECHANNEL_TREE_ROOT_PATH : request.getParameter("siteChannelPath");
        try {
            TreeNode tn = new TreeNode();
            tn.setPath(siteChannelPath);
            TreeNode[] sites = tn.getList();
            if (sites == null) {
                sites = new Site[0];
            }
            request.setAttribute("result", sites);
            LoginUser loginUser = new LoginUser();
            loginUser.setRequest(request);
            int userId = Integer.parseInt(loginUser.getDefaultValue());
            Pepodom pepodom = new Pepodom();
            String[] authority = Function.newStringArray(sites.length, "");
            String[] operates = { Const.OPERATE_ID_COLLECT, Const.OPERATE_ID_NEW, Const.OPERATE_ID_DEL, Const.OPERATE_ID_MODIFY, Const.OPERATE_ID_PREVIEW, Const.OPERATE_ID_INCRE, Const.OPERATE_ID_COMPLETE, Const.OPERATE_ID_RELEASE };
            boolean hasNewAuth = false;
            Hashtable extrAuths = (Hashtable) request.getSession().getAttribute("extrauthority");
            if (userId == User.USER_TOP) {
                hasNewAuth = true;
            }
            for (int i = 0; i < sites.length; i++) {
                pepodom.setLoginProvider(userId);
                int resId = Const.SITE_TYPE_RES + ((Site) sites[i]).getSiteID();
                pepodom.setResID(Integer.toString(resId));
                pepodom.setResTypeID(Const.OPERATE_TYPE_ID);
                for (int j = 0; j < operates.length; j++) {
                    pepodom.setOperateID(operates[j]);
                    boolean hasAuth = pepodom.isDisplay(extrAuths);
                    authority[i] += hasAuth ? "1" : "0";
                }
            }
            request.setAttribute("authority", authority);
            request.setAttribute("hasNewAuth", hasNewAuth ? "1" : "0");
        } catch (Exception ex) {
            log.error("��ȡվ���б����!", ex);
            request.setAttribute(Const.ERROR_MESSAGE_NAME, ex.toString());
            mapping.findForward("error");
        }
        return mapping.findForward("list");
    }
}
