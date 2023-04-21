package salto.test.fwk.mvc.ajax.treeview;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import salto.fwk.mvc.ajax.action.AbstractClientSideTreeViewAction;
import salto.fwk.mvc.ajax.action.Tabs;
import salto.fwk.mvc.ajax.action.TreeHelper;
import salto.fwk.mvc.ajax.model.JavascriptAction;
import salto.fwk.mvc.ajax.util.AjaxUtil;
import salto.fwk.mvc.taglib.treeview.Tree;
import salto.fwk.mvc.taglib.treeview.TreeNode;
import salto.test.fwk.mvc.Constants;
import salto.test.fwk.mvc.business.BoExample;
import salto.test.fwk.mvc.business.Marque;
import salto.test.fwk.mvc.business.Modele;
import salto.test.fwk.mvc.business.Type;

/**
 * Demo of Client side TreeView, i.e. the Tree is build once and no interaction 
 * with servers occurs on node unfold.
 * @author p.mouawad / ubik-ingenierie.com
 *
 */
public class ClientSideTreeAction extends AbstractClientSideTreeViewAction {

    /**
	 * 
	 */
    public ClientSideTreeAction() {
        super();
    }

    /**
	 * @see salto.fwk.mvc.ajax.action.AbstractTreeViewAction#processTreeInit(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionForm)
	 */
    protected ActionForward processTreeInit(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
        showDefaultTree(request);
        AjaxUtil.addInnerJsp(request, response, Tabs.getIdContent(request), Constants.TEST_JSP_PATH + "/treeview/defaultStaticTreeview.jsp");
        AjaxUtil.addResponse(request, new JavascriptAction("Salto.onglet2show"));
        return getRefreshForward();
    }

    /**
	 * @param request
	 */
    private void showDefaultTree(HttpServletRequest request) {
        Map map = new HashMap();
        map.put("id", "0");
        Tree tree = new Tree("Root", null, map);
        TreeNode node = tree.getRootNode();
        BoExample boExample = new BoExample();
        Marque[] marques = boExample.getGarage();
        for (int i = 0; i < marques.length; i++) {
            Marque marque = marques[i];
            map.put("id", String.valueOf(i));
            TreeNode marqueNode = node.addChildNode(marque.getLibelle(), marque, map);
            Type[] types = marque.getTypes();
            for (int j = 0; j < types.length; j++) {
                Type type = types[j];
                map.put("id", String.valueOf(i).concat("-").concat(String.valueOf(j)));
                TreeNode typeNode = marqueNode.addChildNode(type.getLibelle(), type, map);
                Modele[] modeles = type.getModeles();
                for (int k = 0; k < modeles.length; k++) {
                    Modele modele = modeles[k];
                    map.put("id", String.valueOf(i).concat("-").concat(String.valueOf(j)).concat("-").concat(String.valueOf(k)));
                    typeNode.addChildNode(modele.getLibelle(), modele, map);
                }
                for (int k = 0; k < 50; k++) {
                    map.put("id", String.valueOf(i).concat("-").concat(String.valueOf(j)).concat("-").concat(String.valueOf(k)));
                    typeNode.addChildNode("lbl " + k, "lbl " + k, map);
                }
            }
        }
        TreeHelper.storeClientSideTree(request, "theTree", tree, new TestClientSideTreeRenderer());
    }
}
