package au.gov.nla.aons.mvc.actions.repository;

import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import au.gov.nla.aons.mvc.util.RepositoryScanTree;
import au.gov.nla.aons.repository.crawl.RepositoryScan;

public class CreateTreeAction implements Action {

    private RepositoryScanTreeDao treeDao;

    public Event execute(RequestContext context) throws Exception {
        RepositoryScan repositoryScan = (RepositoryScan) context.getFlowScope().get("repositoryScan");
        RepositoryScanTree tree = new RepositoryScanTree();
        tree.setRepositoryScanId(repositoryScan.getId());
        treeDao.readRootAndChildren(tree);
        context.getFlowScope().put("tree", tree);
        return new Event(this, "success");
    }

    public RepositoryScanTreeDao getTreeDao() {
        return treeDao;
    }

    public void setTreeDao(RepositoryScanTreeDao treeDao) {
        this.treeDao = treeDao;
    }
}
