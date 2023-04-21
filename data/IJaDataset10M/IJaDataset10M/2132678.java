package org.neuroph.netbeans.ide.project;

import java.util.List;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.queries.VisibilityQuery;
import org.openide.filesystems.FileObject;
import org.openide.loaders.ChangeableDataFilter;
import org.openide.loaders.DataFilter;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

public class NeurophProjectChildFactory extends ChildFactory<FileObject> {

    private NeurophProject project;

    public NeurophProjectChildFactory(NeurophProject project) {
        this.project = project;
    }

    @Override
    protected boolean createKeys(List<FileObject> keys) {
        FileObject projectDir = project.getProjectDirectory();
        keys.add(projectDir.getFileObject(NeurophProject.NEURAL_NETWORKS_DIR));
        keys.add(projectDir.getFileObject(NeurophProject.TRAINING_SETS_DIR));
        keys.add(projectDir.getFileObject(NeurophProject.TEST_SETS_DIR));
        return true;
    }

    @Override
    protected Node createNodeForKey(FileObject key) {
        DataFolder df = DataFolder.findFolder(key);
        DataFilter filter = new MyDataFilter();
        Children children = df.createNodeChildren(filter);
        Lookup lookup = new ProxyLookup(new Lookup[] { df.getNodeDelegate().getLookup(), project.getLookup() });
        FilterNode fn = new FilterNode(df.getNodeDelegate(), children, lookup);
        return fn;
    }

    /**
     * Filter DataObjects that must not be shown (like .svn folders)
     */
    static final class MyDataFilter implements ChangeListener, ChangeableDataFilter {

        private final ChangeSupport changeSupport = new ChangeSupport(this);

        public MyDataFilter() {
            VisibilityQuery.getDefault().addChangeListener(this);
        }

        public boolean acceptDataObject(DataObject obj) {
            FileObject fo = obj.getPrimaryFile();
            return VisibilityQuery.getDefault().isVisible(fo);
        }

        public void stateChanged(ChangeEvent e) {
            changeSupport.fireChange();
        }

        public void addChangeListener(ChangeListener listener) {
            changeSupport.addChangeListener(listener);
        }

        public void removeChangeListener(ChangeListener listener) {
            changeSupport.removeChangeListener(listener);
        }
    }
}
