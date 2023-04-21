package secondtest;

import java.util.ArrayList;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class MenuTreeView extends ViewPart {

    public static final String ID = "TestUI.menutreeview";

    class TreeObject {

        private String name;

        private TreeParent parent;

        public TreeObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setParent(TreeParent parent) {
            this.parent = parent;
        }

        public TreeParent getParent() {
            return parent;
        }

        public String toString() {
            return getName();
        }
    }

    class TreeParent extends TreeObject {

        private ArrayList children;

        public TreeParent(String name) {
            super(name);
            children = new ArrayList();
        }

        public void addChild(TreeObject child) {
            children.add(child);
            child.setParent(this);
        }

        public void removeChild(TreeObject child) {
            children.remove(child);
            child.setParent(null);
        }

        public TreeObject[] getChildren() {
            return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
        }

        public boolean hasChildren() {
            return children.size() > 0;
        }
    }

    class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider {

        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        public Object getParent(Object child) {
            if (child instanceof TreeObject) {
                return ((TreeObject) child).getParent();
            }
            return null;
        }

        public Object[] getChildren(Object parent) {
            if (parent instanceof TreeParent) {
                return ((TreeParent) parent).getChildren();
            }
            return new Object[0];
        }

        public boolean hasChildren(Object parent) {
            if (parent instanceof TreeParent) return ((TreeParent) parent).hasChildren();
            return false;
        }
    }

    class ViewLabelProvider extends LabelProvider {

        public String getText(Object obj) {
            return obj.toString();
        }

        public Image getImage(Object obj) {
            String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
            if (obj instanceof TreeParent) imageKey = ISharedImages.IMG_ETOOL_HOME_NAV;
            return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
        }
    }

    @Override
    public void createPartControl(Composite parent) {
        TreeViewer tree = new TreeViewer(parent);
        MenuTreeContentProvider provider = new MenuTreeContentProvider();
        tree.setContentProvider(new ViewContentProvider());
        tree.setLabelProvider(new ViewLabelProvider());
        tree.setAutoExpandLevel(2);
        TreeParent root = new TreeParent("root");
        TreeParent[] node1 = new TreeParent[] { new TreeParent("Media Files"), new TreeParent("Encodings"), new TreeParent("Profiles"), new TreeParent("Watchfolder") };
        TreeObject[] node2 = new TreeObject[] { new TreeObject("All Files"), new TreeObject("Imported Files"), new TreeObject("Encoded Files") };
        TreeObject[] node3 = new TreeObject[] { new TreeObject("Running Encodings"), new TreeObject("Completed Encodings") };
        TreeObject[] node4 = new TreeObject[] { new TreeObject("View Profiles"), new TreeObject("Create Profile") };
        TreeObject[] node5 = new TreeObject[] { new TreeObject("View Watchfolder"), new TreeObject("Create Watchfolder") };
        node1[0].addChild(node2[0]);
        node1[0].addChild(node2[1]);
        node1[0].addChild(node2[2]);
        node1[1].addChild(node3[0]);
        node1[1].addChild(node3[1]);
        node1[2].addChild(node4[0]);
        node1[2].addChild(node4[1]);
        node1[3].addChild(node5[0]);
        node1[3].addChild(node5[1]);
        root.addChild(node1[0]);
        root.addChild(node1[1]);
        root.addChild(node1[2]);
        root.addChild(node1[3]);
        tree.setInput(root);
    }

    @Override
    public void setFocus() {
    }
}
