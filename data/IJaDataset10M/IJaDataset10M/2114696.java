package cn.myapps.core.dynaform.view.action;

import java.util.Collection;
import java.util.Iterator;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.document.ejb.DocumentProcess;
import cn.myapps.core.dynaform.document.ejb.DocumentProcessBean;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewType;
import cn.myapps.core.tree.DocumentTree;
import cn.myapps.core.tree.Node;
import cn.myapps.core.tree.Tree;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.StringUtil;
import cn.myapps.util.cache.MemoryCacheUtil;
import cn.myapps.util.http.ResponseUtil;
import com.opensymphony.webwork.ServletActionContext;

public class ViewRunTimeAction extends ViewAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 970248216937958653L;

    public ViewRunTimeAction() throws ClassNotFoundException {
        super();
    }

    public String getWebUserSessionKey() {
        return Web.SESSION_ATTRIBUTE_FRONT_USER;
    }

    public String doInnerPage() {
        try {
            DocumentProcess process = new DocumentProcessBean(view.getApplicationid());
            String docid = getParams().getParameterAsString("_docid");
            if (!StringUtil.isBlank(docid) && !Web.TREEVIEW_ROOT_NODEID.equals(docid)) {
                Document doc = (Document) process.doView(docid);
                if (doc != null) {
                    setCurrentDocument(doc);
                    MemoryCacheUtil.putToPrivateSpace(doc.getId(), doc, getUser());
                }
            }
            setContent(view);
            if (!"root".equals(docid)) {
                String innerType = (String) this.getParams().getParameter("innerType");
                if (innerType != null && innerType.equals("FORM")) {
                    return "successForm";
                } else if (innerType != null && innerType.equals("VIEW")) {
                    return "successView";
                } else {
                    if (View.TREENODE_HREF_FORM.equals(view.getInnerType()) && !"root".equals(docid)) {
                        return "successForm";
                    } else if (View.TREENODE_HREF_VIEW.equals(view.getInnerType())) {
                        return "successView";
                    } else {
                        return SUCCESS;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            addFieldError("", e.getMessage());
        }
        return "successView";
    }

    public String doSearch() {
        try {
            WebUser user = getUser();
            Document sDoc = getSearchDocument(view);
            DocumentTree tree = new DocumentTree(view, getParams(), user, sDoc);
            tree.search();
            ResponseUtil.setJsonToResponse(ServletActionContext.getResponse(), tree.toSearchJSON());
        } catch (Exception e) {
            e.printStackTrace();
            addFieldError("", e.getMessage());
            return INPUT;
        }
        return SUCCESS;
    }

    public String getChildren() {
        try {
            WebUser user = getUser();
            Document sDoc = getSearchDocument(view);
            ViewType viewType = view.getViewTypeImpl();
            Tree<Document> tree = new DocumentTree(view, getParams(), user, sDoc);
            Collection<Document> children = viewType.getViewDatas(getParams(), user, sDoc).getDatas();
            tree.parse(children);
            Collection<Node> childNodes = tree.getChildNodes();
            for (Iterator<Node> iterator = childNodes.iterator(); iterator.hasNext(); ) {
                Node node = (Node) iterator.next();
                node.addAttr("viewid", get_viewid());
            }
            ResponseUtil.setJsonToResponse(ServletActionContext.getResponse(), tree.toJSON());
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() != null) {
                addFieldError("", e.getMessage());
            } else {
                addFieldError("errorMessage", e.toString());
            }
            return INPUT;
        }
        return NONE;
    }
}
