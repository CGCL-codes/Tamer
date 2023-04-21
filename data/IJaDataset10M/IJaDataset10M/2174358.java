package net.zero.smarttrace.ui.views;

import java.util.List;
import net.zero.smarttrace.core.dao.ObjectsInTimeEntityQuerier;
import net.zero.smarttrace.core.data.EReferenceType;
import net.zero.smarttrace.core.data.EThread;
import net.zero.smarttrace.core.data.events.EEvent;
import net.zero.smarttrace.core.data.values.EObjectReference;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class Summary extends ViewPart {

    public static final String VIEW_ID = "net.zero.smarttrace.ui.views.Summary";

    private Viewer viewer;

    private TreeNode[] roots = new TreeNode[] { new TreeNode("Threads"), new TreeNode("Classes"), new TreeNode("Exceptions") };

    public Summary() {
    }

    @Override
    public void createPartControl(Composite parent) {
        viewer = ViewerFactory.FACTORY.crearVisorSummary(parent);
        loadEmptyTree();
    }

    @Override
    public void setFocus() {
    }

    public Viewer getViewer() {
        return viewer;
    }

    public void loadTree(EEvent event) {
        if (event == null) {
            loadEmptyTree();
            return;
        }
        ObjectsInTimeEntityQuerier dataQuerier = new ObjectsInTimeEntityQuerier();
        List<EThread> lThreads = dataQuerier.getThreads(0l, event.getNanosFromStart());
        roots[0].setChildren(toTreeNode(lThreads));
        List<EReferenceType> lClasses = dataQuerier.getReferenceTypes(0l, event.getNanosFromStart());
        roots[1].setChildren(toTreeNode(lClasses));
        int i = 0;
        for (EReferenceType refType : lClasses) {
            TreeNode[] childs = new TreeNode[] { new TreeNode("Static Fields"), new TreeNode("Static Methods"), new TreeNode("Objects") };
            roots[1].getChildren()[i++].setChildren(childs);
            childs[0].setChildren(toTreeNode(dataQuerier.getStaticFields(0l, event.getNanosFromStart(), refType)));
            childs[1].setChildren(toTreeNode(dataQuerier.getStaticMethods(0l, event.getNanosFromStart(), refType)));
            List<EObjectReference> lObjects = dataQuerier.getObjects(0l, event.getNanosFromStart(), refType);
            childs[2].setChildren(toTreeNode(lObjects));
            int j = 0;
            for (EObjectReference objRef : lObjects) {
                TreeNode[] objChilds = new TreeNode[] { new TreeNode("Object Fields"), new TreeNode("Object Methods") };
                childs[2].getChildren()[j++].setChildren(objChilds);
                objChilds[0].setChildren(toTreeNode(dataQuerier.getObjectFields(0l, event.getNanosFromStart(), objRef)));
                objChilds[1].setChildren(toTreeNode(dataQuerier.getObjectMethods(0l, event.getNanosFromStart(), objRef)));
            }
        }
        viewer.setInput(roots);
    }

    public void loadEmptyTree() {
        roots[0].setChildren(null);
        roots[1].setChildren(null);
        roots[2].setChildren(null);
        viewer.setInput(roots);
    }

    private TreeNode[] toTreeNode(List<?> list) {
        TreeNode[] ret = new TreeNode[list.size()];
        int i = 0;
        for (Object obj : list) ret[i++] = new TreeNode(obj);
        return ret;
    }
}
