package ces.platform.infoplat.ui.website.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.coral.dbo.DBOperation;
import ces.coral.dbo.DBOperationFactory;
import ces.coral.dbo.ERDBOperationFactory;
import ces.coral.file.CesGlobals;
import ces.coral.log.Logger;
import ces.platform.infoplat.core.Channel;
import ces.platform.infoplat.core.Documents;
import ces.platform.infoplat.core.Site;
import ces.platform.infoplat.core.base.ConfigInfo;
import ces.platform.infoplat.core.base.Const;
import ces.platform.infoplat.core.dao.ChannelDAO;
import ces.platform.infoplat.core.tree.TreeNode;
import ces.platform.infoplat.ui.common.DefaultTreeNodeTitleDecorate;
import ces.platform.infoplat.ui.common.TreeJsCode;
import ces.platform.infoplat.ui.common.defaultvalue.LoginUser;
import ces.platform.infoplat.ui.website.defaultvalue.ChannelPath;
import ces.platform.infoplat.utils.Function;
import ces.platform.system.common.Constant;
import ces.platform.system.common.IdGenerator;
import ces.platform.system.dbaccess.User;

public class ChannelToChannelAction extends Action {

    Logger log = new Logger(this.getClass());

    private static final String SHOW_COPY_JSP = "showCopyJsp";

    private static final String MOVE = "move";

    private static final int SITE_CHANNEL_TREE = TreeNode.SITE_CHANNEL_TREE;

    public ChannelToChannelAction() {
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String operate = (String) request.getParameter("operate");
        if (operate == null || operate.trim().equals("")) {
            operate = SHOW_COPY_JSP;
        }
        try {
            LoginUser lu = new LoginUser();
            lu.setRequest(request);
            int userId = Integer.parseInt(lu.getDefaultValue());
            User user = (User) request.getSession().getAttribute("user");
            String flagSA = (user != null ? user.getFlagSA() : "");
            TreeNode[] tns = TreeNode.getSiteChannelTree();
            if (userId == 1 || flagSA.trim().equals("1")) {
                tns = TreeNode.getSiteChannelTree();
            } else {
                tns = TreeNode.getSiteChannelTree(userId, "1");
            }
            if (tns == null) {
                tns = new TreeNode[0];
            }
            if (SHOW_COPY_JSP.equalsIgnoreCase(operate)) {
                String orgChanPath = (String) request.getParameter("orgChanPath");
                String temp = "";
                for (int i = 0; i < tns.length; i++) {
                    if (tns[i].getPath().indexOf(orgChanPath) > -1) {
                        temp += tns[i].getPath() + ",";
                    }
                }
                String[] disabledPaths = Function.stringToArray(temp);
                String siteChannelTreeCode = getTreeJsCode(SITE_CHANNEL_TREE, TreeNode.getSiteChannelTreeRoot(), tns, new String[0], disabledPaths, user);
                String docTypeTreeCode = getTreeJsCode(TreeNode.DOC_TYPE_TREE, TreeNode.getDocTypeTreeRoot(), TreeNode.getDocTypeTree(), new String[0], new String[0], user);
                request.setAttribute("siteChannelTreeCode", siteChannelTreeCode);
                request.setAttribute("docTypeTreeCode", docTypeTreeCode);
                return mapping.findForward("showCopyJsp");
            } else if (MOVE.equalsIgnoreCase(operate)) {
                String orgChanPath = (String) request.getParameter("orgChanPath");
                String aimChanPath = (String) request.getParameter("aimChanPath");
                if (null == orgChanPath || "".equals(orgChanPath) || null == aimChanPath || "".equals(aimChanPath)) {
                    throw new Exception("�ƶ�ԭƵ������Ŀ��Ƶ��Ϊ�գ�");
                }
                if (aimChanPath.indexOf(orgChanPath) > -1) {
                    throw new Exception("�ƶ�Ŀ��Ƶ��Ϊ�ƶ�Ƶ������Ƶ����");
                }
                int maxLevel = new ChannelDAO().getChannelMaxLevel(orgChanPath);
                if (maxLevel - (orgChanPath.length() - aimChanPath.length()) / 5 > Const.CHANNEL_PATH_MAX_LEVEL) {
                    throw new Exception("�ƶ�Ŀ��Ƶ������̫�Ŀǰϵͳֻ֧�� " + Const.CHANNEL_PATH_MAX_LEVEL + " �㣡");
                }
                int level = aimChanPath.length() / 5;
                int channelId = (int) IdGenerator.getInstance().getId(IdGenerator.GEN_ID_IP_CHANNEL);
                String parentId = aimChanPath.substring((level - 1) * 5, aimChanPath.length());
                String sitePath = aimChanPath.substring(0, 10);
                int siteID = ((Site) TreeNode.getInstance(sitePath)).getSiteID();
                ChannelDAO channelDao = new ChannelDAO();
                Channel orgChannel = channelDao.getInstanceByPath(orgChanPath);
                ChannelPath channelPath = new ChannelPath();
                channelPath.setCPath(aimChanPath);
                String treeId = channelPath.getDefaultValue();
                orgChannel.setPath(aimChanPath + treeId);
                orgChannel.setLevel(level);
                orgChannel.setParentId(parentId);
                orgChannel.setSiteId(siteID);
                List chanlist = getChannelList(orgChannel, orgChanPath);
                List browselist = getBrowseList(orgChannel, orgChanPath);
                List doclist = getDocList(orgChannel, orgChanPath);
                List fieldsetlist = getFieldSetList(orgChannel, orgChanPath);
                changeChannel(orgChannel, orgChanPath, chanlist);
                changeTreeFrame(orgChannel, orgChanPath, aimChanPath, chanlist);
                changeBrowse(orgChannel, orgChanPath, browselist);
                changeDoc(orgChannel, orgChanPath, doclist);
                changeFieldSet(orgChannel, orgChanPath, fieldsetlist);
                request.setAttribute(Const.SUCCESS_MESSAGE_NAME, "Ƶ���ƶ��ɹ�");
                return mapping.findForward("operateSuccess");
            } else {
                throw new Exception("δ֪�Ĳ�������:" + operate + ",����Ƶ���ƶ���صĴ���!");
            }
        } catch (Exception ex) {
            log.error("Ƶ���ƶ�ʧ��!", ex);
            request.setAttribute(Const.ERROR_MESSAGE_NAME, "Ƶ���ƶ�ʧ��!<br>" + ex.getMessage());
            return mapping.findForward("error");
        }
    }

    /**
	 * ������Ľڵ����,���js����
	 * @param treeType ��������:վ��Ƶ�������ĵ�������
	 * @param root ���ĸ�ڵ����,������Ϊ��
	 * @param allTreeNodes �������нڵ���Ϣ,������Ϊ��
	 * @param defaultSelectionPaths Ĭ��ѡ�еĽڵ�path����
	 * @param disabeldPaths ��Ҳ���ѡ�е�path����
	 * @param userId ��ǰ��¼�û�
	 * @return ����js����
	 * @throws java.lang.Exception
	 */
    private String getTreeJsCode(int treeType, TreeNode root, TreeNode[] allTreeNodes, String[] defaultSelectionPaths, String[] disabeldPaths, User user) throws Exception {
        if (root == null || allTreeNodes == null) {
            return "";
        }
        TreeJsCode tree = new TreeJsCode();
        tree.setItemType(TreeJsCode.ITEM_TYPE_RADIO);
        tree.setTreeBehavior(TreeJsCode.TREE_BEHAVIOR_CLASSIC);
        tree.setTreeType(treeType);
        if (treeType == SITE_CHANNEL_TREE) {
            if (user.getUserID() == 1 || user.getFlagSA().equals("1")) {
                tree.setTreeNodeAuthority(null);
            } else {
                tree.setTreeNodeAuthority(null);
            }
        } else {
            tree.setTreeNodeAuthority(null);
        }
        String[] operates = new String[1];
        operates[0] = Const.OPERATE_ID_COLLECT;
        tree.setOperates(operates);
        tree.setUserId(user.getUserID());
        tree.setTreeNodeTitleDecorate(DefaultTreeNodeTitleDecorate.getInstance());
        tree.setRoot(root);
        tree.setRootHyperlink("");
        tree.setRootTarget("");
        tree.setTreeNodeList(allTreeNodes);
        String[] links = Function.newStringArray(allTreeNodes.length, "");
        tree.setHyperlinkList(links);
        String[] targetList = Function.newStringArray(allTreeNodes.length, "");
        tree.setTargetList(targetList);
        tree.setDefualtSelectedTreeNodePathList(defaultSelectionPaths);
        tree.setDisabledTreeNodePathList(disabeldPaths);
        return tree.getCode();
    }

    protected static DBOperation createDBOperation() throws Exception {
        new CesGlobals().setConfigFile(Constant.DB_CONFIGE_FILE);
        DBOperationFactory factory = new ERDBOperationFactory();
        return factory.createDBOperation(ConfigInfo.getInstance().getPoolName());
    }

    private String getParentId(String temp, int level) {
        String parentid = temp.substring((level - 1) * 5, (level - 1) * 5 + 5);
        return parentid;
    }

    private int getLevel(String temp) {
        return temp.length() / 5 - 1;
    }

    private void changeTreeFrame(Channel chan, String orgChanPath, String aimChanPath, List list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            String strArr[] = (String[]) list.get(i);
            int level = 0;
            String parentId = null;
            String temp = null;
            if (!strArr[0].equals(orgChanPath)) {
                temp = strArr[0].substring(orgChanPath.length());
                temp = chan.getPath() + temp;
                level = getLevel(temp);
                parentId = getParentId(temp, level);
            } else {
                temp = chan.getPath();
                level = aimChanPath.length() / 5;
                parentId = aimChanPath.substring((level - 1) * 5, aimChanPath.length());
            }
            String id = temp.substring(level * 5);
            DBOperation dbo = createDBOperation();
            Connection con = dbo.getConnection();
            String sql = "update T_IP_TREE_FRAME" + " set PATH='" + temp + "' ," + " TREE_LEVEL=" + level + " , " + " PARENT_ID='" + parentId + "' ," + " ID='" + id + "'" + " where PATH='" + strArr[0] + "'";
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
            stm.close();
            con.close();
            dbo.close();
        }
    }

    private void changeChannel(Channel chan, String orgChanPath, List list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            String strArr[] = (String[]) list.get(i);
            String temp = null;
            if (!strArr[0].equals(orgChanPath)) {
                temp = strArr[0].substring(orgChanPath.length());
                temp = chan.getPath() + temp;
            } else {
                temp = chan.getPath();
            }
            DBOperation dbo = createDBOperation();
            Connection con = dbo.getConnection();
            String sql = "update T_IP_CHANNEL" + " set CHANNEL_PATH='" + temp + "' " + " where CHANNEL_PATH='" + strArr[0] + "'";
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
            stm.close();
            con.close();
            dbo.close();
        }
    }

    private void changeBrowse(Channel chan, String orgChanPath, List list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            String strArr[] = (String[]) list.get(i);
            String temp = null;
            if (!strArr[0].equals(orgChanPath)) {
                temp = strArr[0].substring(orgChanPath.length());
                temp = chan.getPath() + temp;
            } else {
                temp = chan.getPath();
            }
            DBOperation dbo = createDBOperation();
            Connection con = dbo.getConnection();
            String sql = "update T_IP_BROWSE" + " set CHANNEL_PATH='" + temp + "' " + " where CHANNEL_PATH='" + strArr[0] + "'";
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
            stm.close();
            con.close();
            dbo.close();
        }
    }

    private void changeDoc(Channel chan, String orgChanPath, List list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            String strArr[] = (String[]) list.get(i);
            String temp = null;
            if (!strArr[0].equals(orgChanPath)) {
                temp = strArr[0].substring(orgChanPath.length());
                temp = chan.getPath() + temp;
            } else {
                temp = chan.getPath();
            }
            DBOperation dbo = createDBOperation();
            Connection con = dbo.getConnection();
            String sql = "update T_IP_DOC" + " set CHANNEL_PATH='" + temp + "' " + " where CHANNEL_PATH='" + strArr[0] + "'";
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
            stm.close();
            con.close();
            dbo.close();
        }
    }

    private static List getChannelList(Channel chan, String orgChanPath) throws Exception {
        List list = new ArrayList();
        DBOperation dbo = createDBOperation();
        Connection con = dbo.getConnection();
        String sql = "select * from T_IP_CHANNEL t where t.CHANNEL_PATH like '" + orgChanPath + "%'";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            String strArr[] = new String[2];
            strArr[0] = rs.getString("channel_path");
            list.add(strArr);
        }
        stm.close();
        con.close();
        dbo.close();
        return list;
    }

    private static List getBrowseList(Channel chan, String orgChanPath) throws Exception {
        List list = new ArrayList();
        DBOperation dbo = createDBOperation();
        Connection con = dbo.getConnection();
        String sql = "select * from T_IP_BROWSE t where t.CHANNEL_PATH like '" + orgChanPath + "%'";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            String strArr[] = new String[2];
            strArr[0] = rs.getString("channel_path");
            list.add(strArr);
        }
        stm.close();
        con.close();
        dbo.close();
        return list;
    }

    private static List getDocList(Channel chan, String orgChanPath) throws Exception {
        List list = new ArrayList();
        DBOperation dbo = createDBOperation();
        Connection con = dbo.getConnection();
        String sql = "select * from T_IP_DOC t where t.CHANNEL_PATH like '" + orgChanPath + "%'";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            String strArr[] = new String[2];
            strArr[0] = rs.getString("channel_path");
            list.add(strArr);
        }
        stm.close();
        con.close();
        dbo.close();
        return list;
    }

    private static List getFieldSetList(Channel chan, String orgChanPath) throws Exception {
        List list = new ArrayList();
        DBOperation dbo = createDBOperation();
        Connection con = dbo.getConnection();
        String sql = "select * from T_IP_FIELDSET t where t.PATH like '" + orgChanPath + "%'";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            String strArr[] = new String[2];
            strArr[0] = rs.getString("path");
            list.add(strArr);
        }
        stm.close();
        con.close();
        dbo.close();
        return list;
    }

    private void changeFieldSet(Channel chan, String orgChanPath, List list) throws Exception {
        for (int i = 0; i < list.size(); i++) {
            String strArr[] = (String[]) list.get(i);
            String temp = null;
            if (!strArr[0].equals(orgChanPath)) {
                temp = strArr[0].substring(orgChanPath.length());
                temp = chan.getPath() + temp;
            } else {
                temp = chan.getPath();
            }
            DBOperation dbo = createDBOperation();
            Connection con = dbo.getConnection();
            String sql = "update T_IP_FIELDSET" + " set PATH='" + temp + "' " + " where PATH='" + strArr[0] + "'";
            Statement stm = con.createStatement();
            stm.executeUpdate(sql);
            stm.close();
            con.close();
            dbo.close();
        }
    }
}
