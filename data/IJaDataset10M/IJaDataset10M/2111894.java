package biz.xsoftware.vm.server;

import java.rmi.RemoteException;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import biz.xsoftware.scm.api.NodeInfo;
import biz.xsoftware.scm.api.ProjectTreeModel;
import biz.xsoftware.scm.api.ProjectTreeService;
import biz.xsoftware.vm.server.project.map.ProjectMap;
import biz.xsoftware.vm.server.projtree.VMasterTreeModelImpl;

/**
 *  FILL IN JAVADOC HERE
 *
 * @author $Author: deanhiller $
 * @version $Revision: 1.2 $ $Date: 2004/07/24 23:38:29 $
 * @since $ProductVersion$
 */
public class ProjectTreeServiceImpl implements ProjectTreeService {

    /**
	 * Logger for non-static methods and constructors to be used for
	 * this class.
	 */
    private final Logger log = Logger.getLogger(getClass().getName());

    private static ProjectTreeServiceImpl singleInstance;

    private VMasterTreeModelImpl model = new VMasterTreeModelImpl();

    private ProjectTreeServiceImpl() {
    }

    public static synchronized ProjectTreeServiceImpl instance() {
        if (singleInstance == null) singleInstance = new ProjectTreeServiceImpl();
        return singleInstance;
    }

    public ProjectTreeModel getTreeModel() {
        System.err.println("ASDFSDAF");
        return model;
    }

    public void setTreeModel(TreeModel m) {
        model = (VMasterTreeModelImpl) m;
    }

    public void changeNodeProperties(TreeNode node, NodeInfo nodeInfo) throws RemoteException {
        System.err.println("ASDFSDAF");
    }

    public void createNode(TreeNode parent, NodeInfo childInfo) throws RemoteException {
        System.err.println("ASDFSDAF");
    }

    public String createProject(String projName, Vector files) {
        return null;
    }

    public ProjectMap getProjectMap(String moduleId) {
        return null;
    }

    public Vector createView(String project, String loadline, String tree, String node) {
        return null;
    }

    public void shutDown() {
    }

    public void branchView(String string, Object object) {
    }
}
