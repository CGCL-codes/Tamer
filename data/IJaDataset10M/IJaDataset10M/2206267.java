package cn.vlabs.clb.ui.flex.tree;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import cn.ac.ntarl.umt.api.ServiceException;
import cn.ac.ntarl.umt.api.simpleAuth.VOTreeService;
import cn.ac.ntarl.umt.api.vo.VOInfo;
import cn.ac.ntarl.umt.tree.AbstractNode;
import cn.ac.ntarl.umt.tree.GroupTree;
import cn.ac.ntarl.umt.tree.Node;
import cn.vlabs.clb.CurrentConnection;
import cn.vlabs.clb.security.AuthModuleFactory;
import cn.vlabs.clb.ui.flex.FlexAction;
import cn.vlabs.clb.ui.flex.PathDecoder;

/**
 * MyEclipse Struts Creation date: 10-14-2008
 * 
 * XDoclet definition:
 * 
 * @struts.action validate="true"
 */
public class ListGroupTreeAction extends FlexAction {

    @Override
    protected String exec(ActionForm form, HttpServletRequest request) {
        try {
            GroupTree tree = getGroupTree();
            if (tree == null) return fail("�޷���ѯ�û�����Ϣ��");
            String currentPath = request.getParameter("currentPath");
            if (currentPath == null) return fail("�Ƿ���������Ϣ��currrentPath��nodeName������һ����Ч��");
            currentPath = PathDecoder.decode(currentPath);
            if (currentPath.startsWith("/user")) {
                return dumpAllVO(request, tree);
            } else if (currentPath.startsWith("/vo")) {
                return dumpGroup(tree, this.getAssignedGroup(request, currentPath), CurrentConnection.getSessions().getVOS());
            }
            return fail("·��������/user ����/vo��ʼ��");
        } catch (ServiceException e) {
            return fail(e.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
            return fail("�����ڲ��������");
        }
    }

    private String getAssignedGroup(HttpServletRequest request, String path) {
        VOInfo[] vos = CurrentConnection.getSessions().getVOS();
        if (vos == null) return null;
        for (VOInfo vo : vos) {
            String vohome = "/vo/" + vo.getName();
            if (path.startsWith(vohome)) return vo.getAssignedGroup();
        }
        return null;
    }

    private String dumpAllVO(HttpServletRequest request, GroupTree tree) {
        VOInfo[] vos = CurrentConnection.getSessions().getVOS();
        XMLDumper dumper = new XMLDumper(vos);
        if (vos != null) {
            for (VOInfo vo : vos) {
                Node node = tree.getNodeByName(vo.getAssignedGroup(), AbstractNode.NODE_GROUP);
                if (node != null) node.accept(null, dumper);
            }
        }
        dumper.finish();
        return success(dumper.getResult());
    }

    private String dumpGroup(GroupTree tree, String group, VOInfo[] vos) {
        Node node = tree.getNodeByName(group, AbstractNode.NODE_GROUP);
        if (node == null) return fail("�޷��ҵ��û���Ϣ");
        XMLDumper dumper = new XMLDumper(vos);
        node.accept(null, dumper);
        dumper.finish();
        return success(dumper.getResult());
    }

    private GroupTree getGroupTree() throws ServiceException {
        VOTreeService treesrv = AuthModuleFactory.getVOTreeService();
        GroupTree tree = treesrv.getTree("VO");
        return tree;
    }
}
