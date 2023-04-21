package gndzh.som.summer.functree;

import gndzh.som.summer.schema.SmFuncregister;
import gndzh.som.summer.schema.SmFuncregisterDAO;
import gndzh.som.summer.schema.SmUser;
import gndzh.som.summer.util.BaseHibernateDAO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;

/**
 * ����json.jstree�������ݼ��Ϲ���.
 * @author gan
 *
 */
public class JSFuncTreeFactory implements TreeFactory {

    private SmFuncregisterDAO funcdao = new SmFuncregisterDAO();

    /**
	 * ��ݴ�����û���Ϣ,�����û���ɫ����ɫ���Ʊ�,��λ
	 * �����û����ܲ鿴���Ĺ��ܽ��ļ���.
	 */
    public List buildTree(Map<String, Object> params) {
        Session session = new BaseHibernateDAO().getSession();
        SmUser user = (SmUser) params.get("user");
        List result = session.createSQLQuery("select fun.* " + "  from sm_user u, sm_user_role ur, sm_role_powerctrl p,sm_funcregister fun " + " where u.pk_user = ur.userid " + "   and ur.pk_role = p.pk_role " + "   and ur.orgcode=p.orgcode " + "   and p.resourceid=fun.func_code " + "   and u.logincode='" + user.getLogincode() + "'" + "   and fun.func_level=0 ").addEntity(SmFuncregister.class).list();
        ArrayList<JSFuncTreeNode> roots = new ArrayList<JSFuncTreeNode>();
        for (Object obj : result) {
            JSFuncTreeNode rootnode = new JSFuncTreeNode((SmFuncregister) obj);
            rootnode.initialize();
            addChildren(rootnode);
            roots.add(rootnode);
        }
        return roots;
    }

    /**
	 * �ݹ���������ӽ�㺯��������ĳ������µ�
	 * �����ӽ��,�������ݼ���
	 * @param func
	 */
    private void addChildren(JSFuncTreeNode func) {
        try {
            List _func = funcdao.findByPkParent(func.getSmFuncPK());
            for (Object obj : _func) {
                JSFuncTreeNode child = new JSFuncTreeNode((SmFuncregister) obj);
                child.initialize();
                func.addChild(child);
                addChildren(child);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
